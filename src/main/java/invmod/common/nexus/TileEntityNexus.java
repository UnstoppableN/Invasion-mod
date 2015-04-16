package invmod.common.nexus;

import cpw.mods.fml.common.FMLCommonHandler;
import invmod.Invasion;
import invmod.common.entity.EntityIMBolt;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMWolf;
import invmod.common.entity.ai.AttackerAI;
import invmod.common.util.ComparatorEntityDistance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.*;

public class TileEntityNexus extends TileEntity implements INexusAccess, IInventory {
    private static final long BIND_EXPIRE_TIME = 300000L;
    private IMWaveSpawner waveSpawner;
    private IMWaveBuilder waveBuilder;
    private ItemStack[] nexusItemStacks;
    private AxisAlignedBB boundingBoxToRadius;
    private HashMap<String, Long> boundPlayers;
    private List<EntityIMLiving> mobList;
    private AttackerAI attackerAI;
    private int activationTimer;
    private int currentWave;
    private int spawnRadius;
    private int nexusLevel;
    private int nexusKills;
    private int generation;
    private int cookTime;
    private int maxHp;
    private int hp;
    private int lastHp;
    private int mode;
    private int powerLevel;
    private int lastPowerLevel;
    private int powerLevelTimer;
    private int mobsLeftInWave;
    private int lastMobsLeftInWave;
    private int mobsToKillInWave;
    private int nextAttackTime;
    private int daysToAttack;
    private long lastWorldTime;
    private int zapTimer;
    private int errorState;
    private int tickCount;
    private int cleanupTimer;
    private long spawnerElapsedRestore;
    private long timer;
    private long waveDelayTimer;
    private long waveDelay;
    private boolean continuousAttack;
    private boolean mobsSorted;
    private boolean resumedFromNBT;
    private boolean activated;

    public TileEntityNexus() {
        this(null);
    }

    public TileEntityNexus(World world) {
        this.worldObj = world;
        this.spawnRadius = 52;
        this.waveSpawner = new IMWaveSpawner(this, this.spawnRadius);
        this.waveBuilder = new IMWaveBuilder();
        this.nexusItemStacks = new ItemStack[2];
        this.boundingBoxToRadius = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
        this.boundingBoxToRadius.setBounds(this.xCoord - (this.spawnRadius + 10), this.yCoord - (this.spawnRadius + 40), this.zCoord - (this.spawnRadius + 10), this.xCoord + (this.spawnRadius + 10), this.yCoord + (this.spawnRadius + 40), this.zCoord + (this.spawnRadius + 10));
        this.boundPlayers = new HashMap();
        this.mobList = new ArrayList();
        this.attackerAI = new AttackerAI(this);
        this.activationTimer = 0;
        this.cookTime = 0;
        this.currentWave = 0;
        this.nexusLevel = 1;
        this.nexusKills = 0;
        this.generation = 0;
        this.maxHp = (this.hp = this.lastHp = 100);
        this.mode = 0;
        this.powerLevelTimer = 0;
        this.powerLevel = 0;
        this.lastPowerLevel = 0;
        this.mobsLeftInWave = 0;
        this.nextAttackTime = 0;
        this.daysToAttack = 0;
        this.lastWorldTime = 0L;
        this.errorState = 0;
        this.tickCount = 0;
        this.timer = 0L;
        this.zapTimer = 0;
        this.cleanupTimer = 0;
        this.waveDelayTimer = -1L;
        this.waveDelay = 0L;
        this.continuousAttack = false;
        this.mobsSorted = false;
        this.resumedFromNBT = false;
        this.activated = false;
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }

        updateStatus();

        updateAI();

        if ((this.mode == 1) || (this.mode == 2) || (this.mode == 3)) {
            if (this.resumedFromNBT) {
                this.boundingBoxToRadius.setBounds(this.xCoord - (this.spawnRadius + 10), 0.0D, this.zCoord - (this.spawnRadius + 10), this.xCoord + (this.spawnRadius + 10), 127.0D, this.zCoord + (this.spawnRadius + 10));
                if ((this.mode == 2) && (this.continuousAttack)) {
                    if (resumeSpawnerContinuous()) {
                        this.mobsLeftInWave = (this.lastMobsLeftInWave += acquireEntities());
                        Invasion.log("mobsLeftInWave: " + this.mobsLeftInWave);
                        Invasion.log("mobsToKillInWave: " + this.mobsToKillInWave);
                    }
                } else {
                    resumeSpawnerInvasion();
                    acquireEntities();
                }
                this.attackerAI.onResume();

                this.resumedFromNBT = false;
            }
            try {
                this.tickCount += 1;
                if (this.tickCount == 60) {
                    this.tickCount -= 60;
                    bindPlayers();
                    updateMobList();
                }

                if ((this.mode == 1) || (this.mode == 3))
                    doInvasion(50);
                else if (this.mode == 2)
                    doContinuous(50);
            } catch (WaveSpawnerException e) {
                Invasion.log(e.getMessage());
                e.printStackTrace();
                stop();
            }
        }

        if (this.cleanupTimer++ > 40) {
            this.cleanupTimer = 0;
            if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) != Invasion.blockNexus) {
                Invasion.setInvasionEnded(this);
                stop();
                invalidate();
                Invasion.log("Stranded nexus entity trying to delete itself...");
            }
        }
    }

    public void emergencyStop() {
        Invasion.log("Nexus manually stopped by command");
        stop();
        killAllMobs();
    }

    public void debugStatus() {
        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Current Time: " + this.worldObj.getWorldTime());
        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Time to next: " + this.nextAttackTime);
        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Days to attack: " + this.daysToAttack);
        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Mobs left: " + this.mobsLeftInWave);
        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Mode: " + this.mode);
    }

    public void debugStartInvaion(int startWave) {
        Invasion.tryGetInvasionPermission(this);
        startInvasion(startWave);
        this.activated = true;
    }

    public void createBolt(int x, int y, int z, int t) {
        EntityIMBolt bolt = new EntityIMBolt(this.worldObj, this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, x + 0.5D, y + 0.5D, z + 0.5D, t, 1);
        this.worldObj.spawnEntityInWorld(bolt);
    }

    public boolean setSpawnRadius(int radius) {
        if ((!this.waveSpawner.isActive()) && (radius > 8)) {
            this.spawnRadius = radius;
            this.waveSpawner.setRadius(radius);
            this.boundingBoxToRadius.setBounds(this.xCoord - (this.spawnRadius + 10), 0.0D, this.zCoord - (this.spawnRadius + 10), this.xCoord + (this.spawnRadius + 10), 127.0D, this.zCoord + (this.spawnRadius + 10));
            return true;
        }

        return false;
    }

    @Override
    public void attackNexus(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.hp = 0;
            if (this.mode == 1) {
                theEnd();
            }
        }
        while (this.hp + 5 <= this.lastHp) {
            Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus at " + (this.lastHp - 5) + " hp");
            this.lastHp -= 5;
        }
    }

    @Override
    public void registerMobDied() {
        this.nexusKills += 1;
        this.mobsLeftInWave -= 1;
        if (this.mobsLeftInWave <= 0) {
            if (this.lastMobsLeftInWave > 0) {
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus rift stable again!");
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Unleashing tapped energy...");
                this.lastMobsLeftInWave = this.mobsLeftInWave;
            }
            return;
        }
        while (this.mobsLeftInWave + this.mobsToKillInWave * 0.1F <= this.lastMobsLeftInWave) {
            Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus rift stabilised to " + (100 - (int) (100 * this.mobsLeftInWave / this.mobsToKillInWave)) + "%");
            this.lastMobsLeftInWave = ((int) (this.lastMobsLeftInWave - this.mobsToKillInWave * 0.1F));
        }
    }

    public void registerMobClose() {
    }

    @Override
    public boolean isActivating() {
        return (this.activationTimer > 0) && (this.activationTimer < 400);
    }

    @Override
    public int getMode() {
        return this.mode;
    }

    protected void setMode(int i) {
        this.mode = i;
        if (this.mode == 0)
            setActive(false);
        else
            setActive(true);
    }

    @Override
    public int getActivationTimer() {
        return this.activationTimer;
    }

    protected void setActivationTimer(int i) {
        this.activationTimer = i;
    }

    @Override
    public int getSpawnRadius() {
        return this.spawnRadius;
    }

    @Override
    public int getNexusKills() {
        return this.nexusKills;
    }

    protected void setNexusKills(int i) {
        this.nexusKills = i;
    }

    @Override
    public int getGeneration() {
        return this.generation;
    }

    protected void setGeneration(int i) {
        this.generation = i;
    }

    @Override
    public int getNexusLevel() {
        return this.nexusLevel;
    }

    protected void setNexusLevel(int i) {
        this.nexusLevel = i;
    }

    public int getPowerLevel() {
        return this.powerLevel;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    protected void setCookTime(int i) {
        this.cookTime = i;
    }

    public int getNexusID() {
        return -1;
    }

    @Override
    public int getXCoord() {
        return this.xCoord;
    }

    @Override
    public int getYCoord() {
        return this.yCoord;
    }

    @Override
    public int getZCoord() {
        return this.zCoord;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override

    public List<EntityIMLiving> getMobList() {
        return this.mobList;
    }

    public int getActivationProgressScaled(int i) {
        return this.activationTimer * i / 400;
    }

    public int getGenerationProgressScaled(int i) {
        return this.generation * i / 3000;
    }

    public int getCookProgressScaled(int i) {
        return this.cookTime * i / 1200;
    }

    public int getNexusPowerLevel() {
        return this.powerLevel;
    }

    protected void setNexusPowerLevel(int i) {
        this.powerLevel = i;
    }

    public int getCurrentWave() {
        return this.currentWave;
    }

    @Override
    public int getSizeInventory() {
        return this.nexusItemStacks.length;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    //needed?
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.nexusItemStacks[i] = itemstack;
        if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.nexusItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.nexusItemStacks[i] != null) {
            if (this.nexusItemStacks[i].stackSize <= j) {
                ItemStack itemstack = this.nexusItemStacks[i];
                this.nexusItemStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = this.nexusItemStacks[i].splitStack(j);
            if (this.nexusItemStacks[i].stackSize == 0) {
                this.nexusItemStacks[i] = null;
            }
            return itemstack1;
        }

        return null;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        Invasion.log("Restoring TileEntityNexus from NBT");
        super.readFromNBT(nbttagcompound);
        //added 0 to gettaglist, because it asked an int
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 0);
        this.nexusItemStacks = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if ((byte0 >= 0) && (byte0 < this.nexusItemStacks.length)) {
                this.nexusItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }

        }
        //added 0 to gettaglist, because it asked an int
        nbttaglist = nbttagcompound.getTagList("boundPlayers", 0);
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            this.boundPlayers.put(((NBTTagCompound) nbttaglist.getCompoundTagAt(i)).getString("name"), Long.valueOf(System.currentTimeMillis()));
            Invasion.log("Added bound player: " + ((NBTTagCompound) nbttaglist.getCompoundTagAt(i)).getString("name"));
        }

        this.activationTimer = nbttagcompound.getShort("activationTimer");
        this.mode = nbttagcompound.getInteger("mode");
        this.currentWave = nbttagcompound.getShort("currentWave");
        this.spawnRadius = nbttagcompound.getShort("spawnRadius");
        this.nexusLevel = nbttagcompound.getShort("nexusLevel");
        this.hp = nbttagcompound.getShort("hp");
        this.nexusKills = nbttagcompound.getInteger("nexusKills");
        this.generation = nbttagcompound.getShort("generation");
        this.powerLevel = nbttagcompound.getInteger("powerLevel");
        this.lastPowerLevel = nbttagcompound.getInteger("lastPowerLevel");
        this.nextAttackTime = nbttagcompound.getInteger("nextAttackTime");
        this.daysToAttack = nbttagcompound.getInteger("daysToAttack");
        this.continuousAttack = nbttagcompound.getBoolean("continuousAttack");
        this.activated = nbttagcompound.getBoolean("activated");

        this.boundingBoxToRadius.setBounds(this.xCoord - (this.spawnRadius + 10), this.yCoord - (this.spawnRadius + 40), this.zCoord - (this.spawnRadius + 10), this.xCoord + (this.spawnRadius + 10), this.yCoord + (this.spawnRadius + 40), this.zCoord + (this.spawnRadius + 10));

        Invasion.log("activationTimer = " + this.activationTimer);
        Invasion.log("mode = " + this.mode);
        Invasion.log("currentWave = " + this.currentWave);
        Invasion.log("spawnRadius = " + this.spawnRadius);
        Invasion.log("nexusLevel = " + this.nexusLevel);
        Invasion.log("hp = " + this.hp);
        Invasion.log("nexusKills = " + this.nexusKills);
        Invasion.log("powerLevel = " + this.powerLevel);
        Invasion.log("lastPowerLevel = " + this.lastPowerLevel);
        Invasion.log("nextAttackTime = " + this.nextAttackTime);

        this.waveSpawner.setRadius(this.spawnRadius);
        if ((this.mode == 1) || (this.mode == 3) || ((this.mode == 2) && (this.continuousAttack))) {
            Invasion.log("Nexus is active; flagging for restore");
            this.resumedFromNBT = true;
            this.spawnerElapsedRestore = nbttagcompound.getLong("spawnerElapsed");
            Invasion.log("spawnerElapsed = " + this.spawnerElapsedRestore);
        }

        this.attackerAI.readFromNBT(nbttagcompound);
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        if (this.mode != 0) {
            Invasion.setNexusUnloaded(this);
        }
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("activationTimer", (short) this.activationTimer);
        nbttagcompound.setShort("currentWave", (short) this.currentWave);
        nbttagcompound.setShort("spawnRadius", (short) this.spawnRadius);
        nbttagcompound.setShort("nexusLevel", (short) this.nexusLevel);
        nbttagcompound.setShort("hp", (short) this.hp);
        nbttagcompound.setInteger("nexusKills", this.nexusKills);
        nbttagcompound.setShort("generation", (short) this.generation);
        nbttagcompound.setLong("spawnerElapsed", this.waveSpawner.getElapsedTime());
        nbttagcompound.setInteger("mode", this.mode);
        nbttagcompound.setInteger("powerLevel", this.powerLevel);
        nbttagcompound.setInteger("lastPowerLevel", this.lastPowerLevel);
        nbttagcompound.setInteger("nextAttackTime", this.nextAttackTime);
        nbttagcompound.setInteger("daysToAttack", this.daysToAttack);
        nbttagcompound.setBoolean("continuousAttack", this.continuousAttack);
        nbttagcompound.setBoolean("activated", this.activated);


        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.nexusItemStacks.length; i++) {
            if (this.nexusItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.nexusItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);

        NBTTagList nbttaglist2 = new NBTTagList();
        for (Map.Entry entry : this.boundPlayers.entrySet()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setString("name", (String) entry.getKey());
            nbttaglist2.appendTag(nbttagcompound1);
        }
        nbttagcompound.setTag("boundPlayers", nbttaglist2);

        this.attackerAI.writeToNBT(nbttagcompound);
    }

    public void askForRespawn(EntityIMLiving entity) {
        Invasion.log("Stuck entity asking for respawn: " + entity.toString() + "  " + entity.posX + ", " + entity.posY + ", " + entity.posZ);
        this.waveSpawner.askForRespawn(entity);
    }

    public AttackerAI getAttackerAI() {
        return this.attackerAI;
    }

    protected void setWave(int wave) {
        this.currentWave = wave;
    }

    private void startInvasion(int startWave) {
        this.boundingBoxToRadius.setBounds(this.xCoord - (this.spawnRadius + 10), this.yCoord - (this.spawnRadius + 40), this.zCoord - (this.spawnRadius + 10), this.xCoord + (this.spawnRadius + 10), this.yCoord + (this.spawnRadius + 40), this.zCoord + (this.spawnRadius + 10));
        if ((this.mode == 2) && (this.continuousAttack)) {
            Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Can't activate nexus when already under attack!");
            return;
        }

        if ((this.mode == 0) || (this.mode == 2)) {
            if (this.waveSpawner.isReady()) {
                try {
                    this.currentWave = startWave;
                    this.waveSpawner.beginNextWave(this.currentWave);
                    if (this.mode == 0)
                        setMode(1);
                    else {
                        setMode(3);
                    }
                    bindPlayers();
                    this.hp = this.maxHp;
                    this.lastHp = this.maxHp;
                    this.waveDelayTimer = -1L;
                    this.timer = System.currentTimeMillis();
                    Invasion.sendMessageToPlayers(this.getBoundPlayers(), "The first wave is coming soon!");
                    playSoundForBoundPlayers("invmod:rumble");
                } catch (WaveSpawnerException e) {
                    stop();
                    Invasion.log(e.getMessage());
                    Invasion.sendMessageToPlayers(this.getBoundPlayers(), e.getMessage());
                }
            } else {
                Invasion.log("Wave spawner not in ready state");
            }
        } else {
            Invasion.log("Tried to activate nexus while already active");
        }
    }

    private void startContinuousPlay() {
        this.boundingBoxToRadius.setBounds(this.xCoord - (this.spawnRadius + 10), 0.0D, this.zCoord - (this.spawnRadius + 10), this.xCoord + (this.spawnRadius + 10), 127.0D, this.zCoord + (this.spawnRadius + 10));
        if ((this.mode == 4) && (this.waveSpawner.isReady()) && (Invasion.tryGetInvasionPermission(this))) {
            setMode(2);
            this.hp = this.maxHp;
            this.lastHp = this.maxHp;
            this.lastPowerLevel = this.powerLevel;
            this.lastWorldTime = this.worldObj.getWorldTime();
            this.nextAttackTime = ((int) (this.lastWorldTime / 24000L * 24000L) + 14000);
            if ((this.lastWorldTime % 24000L > 12000L) && (this.lastWorldTime % 24000L < 16000L)) {
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), "The night looms around the nexus...");
            } else {
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus activated and stable");
            }
        } else {
            Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Couldn't activate nexus");
        }
    }

    private void doInvasion(int elapsed) throws WaveSpawnerException {
        if (this.waveSpawner.isActive()) {
            if (this.hp <= 0) {
                theEnd();
            } else {
                generateFlux(1);
                if (this.waveSpawner.isWaveComplete()) {
                    if (this.waveDelayTimer == -1L) {
                        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Wave " + this.currentWave + " almost complete!");
                        playSoundForBoundPlayers("invmod:chime1");
                        this.waveDelayTimer = 0L;
                        this.waveDelay = this.waveSpawner.getWaveRestTime();
                    } else {
                        this.waveDelayTimer += elapsed;
                        if (this.waveDelayTimer > this.waveDelay) {
                            this.currentWave += 1;
                            Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Wave " + this.currentWave + " about to begin");
                            this.waveSpawner.beginNextWave(this.currentWave);
                            this.waveDelayTimer = -1L;
                            playSoundForBoundPlayers("invmod:rumble1");
                            if (this.currentWave > this.nexusLevel) {
                                this.nexusLevel = this.currentWave;
                            }
                        }
                    }
                } else {
                    this.waveSpawner.spawn(elapsed);
                }
            }
        }
    }

    private void playSoundForBoundPlayers(String sound) {
        HashMap<String, Long> boundPlayers = this.getBoundPlayers();
        if (boundPlayers != null) {
            for (Map.Entry entry : boundPlayers.entrySet()) {
                try {
                    //should have getPlayerForUsername at the end
                    EntityPlayerMP player = (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152612_a((String) entry.getKey());
                    if (player != null) {
                        player.getEntityWorld().playSoundAtEntity((Entity) player, sound, 1, 1);
                    }
                } catch (Exception name) {
                    System.out.println("Problem while trying to play sound at player.");
                }
            }
        }
    }

    private void doContinuous(int elapsed) {
        this.powerLevelTimer += elapsed;
        if (this.powerLevelTimer > 2200) {
            this.powerLevelTimer -= 2200;
            generateFlux(5 + (int) (5 * this.powerLevel / 1550.0F));
            if ((this.nexusItemStacks[0] == null) || (this.nexusItemStacks[0].getItem() != Invasion.itemDampingAgent)) {
                this.powerLevel += 1;
            }
        }

        if ((this.nexusItemStacks[0] != null) && (this.nexusItemStacks[0].getItem() == Invasion.itemStrongDampingAgent)) {
            if ((this.powerLevel >= 0) && (!this.continuousAttack)) {
                this.powerLevel -= 1;
                if (this.powerLevel < 0) {
                    stop();
                }
            }
        }

        if (!this.continuousAttack) {
            long currentTime = this.worldObj.getWorldTime();
            int timeOfDay = (int) (this.lastWorldTime % 24000L);
            if ((timeOfDay < 12000) && (currentTime % 24000L >= 12000L) && (currentTime + 12000L > this.nextAttackTime)) {
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), "The night looms around the nexus...");
            }
            if (this.lastWorldTime > currentTime) {
                this.nextAttackTime = ((int) (this.nextAttackTime - (this.lastWorldTime - currentTime)));
            }
            this.lastWorldTime = currentTime;

            if (this.lastWorldTime >= this.nextAttackTime) {
                float difficulty = 1.0F + this.powerLevel / 4500;
                float tierLevel = 1.0F + this.powerLevel / 4500;
                int timeSeconds = 240;
                try {
                    Wave wave = this.waveBuilder.generateWave(difficulty, tierLevel, timeSeconds);
                    this.mobsLeftInWave = (this.lastMobsLeftInWave = this.mobsToKillInWave = (int) (wave.getTotalMobAmount() * 0.8F));
                    this.waveSpawner.beginNextWave(wave);
                    this.continuousAttack = true;
                    int days = Invasion.getMinContinuousModeDays() + this.worldObj.rand.nextInt(1 + Invasion.getMaxContinuousModeDays() - Invasion.getMinContinuousModeDays());
                    this.nextAttackTime = ((int) (currentTime / 24000L * 24000L) + 14000 + days * 24000);
                    this.hp = (this.lastHp = 100);
                    this.zapTimer = 0;
                    this.waveDelayTimer = -1L;
                    Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Forces are destabilising the nexus!");
                    playSoundForBoundPlayers("invmod:rumble");
                } catch (WaveSpawnerException e) {
                    Invasion.log(e.getMessage());
                    e.printStackTrace();
                    stop();
                }

            }

        } else if (this.hp <= 0) {
            this.continuousAttack = false;
            continuousNexusHurt();
        } else if (this.waveSpawner.isWaveComplete()) {

            if (this.waveDelayTimer == -1L) {
                this.waveDelayTimer = 0L;
                this.waveDelay = this.waveSpawner.getWaveRestTime();
            } else {

                this.waveDelayTimer += elapsed;
                if ((this.waveDelayTimer > this.waveDelay) && (this.zapTimer < -200)) {
                    this.waveDelayTimer = -1L;
                    this.continuousAttack = false;
                    this.waveSpawner.stop();
                    this.hp = 100;
                    this.lastHp = 100;
                    this.lastPowerLevel = this.powerLevel;
                }
            }

            this.zapTimer -= 1;
            if (this.mobsLeftInWave <= 0) {
                if ((this.zapTimer <= 0) && (zapEnemy(1))) {
                    zapEnemy(0);
                    this.zapTimer = 23;
                }
            }
        } else {
            try {
                this.waveSpawner.spawn(elapsed);
            } catch (WaveSpawnerException e) {
                Invasion.log(e.getMessage());
                e.printStackTrace();
                stop();
            }
        }
    }

    private void updateStatus() {
        if (this.nexusItemStacks[0] != null) {
            if ((this.nexusItemStacks[0].getItem() == Invasion.itemIMTrap) && (this.nexusItemStacks[0].getItemDamage() == 0)) {
                if (this.cookTime < 1200) {
                    if (this.mode == 0)
                        this.cookTime += 1;
                    else {
                        this.cookTime += 9;
                    }
                }
                if (this.cookTime >= 1200) {
                    if (this.nexusItemStacks[1] == null) {
                        this.nexusItemStacks[1] = new ItemStack(Invasion.itemIMTrap, 1, 1);
                        if (--this.nexusItemStacks[0].stackSize <= 0)
                            this.nexusItemStacks[0] = null;
                        this.cookTime = 0;
                    } else if ((this.nexusItemStacks[1].getItem() == Invasion.itemIMTrap) && (this.nexusItemStacks[1].getItemDamage() == 1)) {
                        this.nexusItemStacks[1].stackSize += 1;
                        if (--this.nexusItemStacks[0].stackSize <= 0)
                            this.nexusItemStacks[0] = null;
                        this.cookTime = 0;
                    }
                }
            } else if ((this.nexusItemStacks[0].getItem() == Invasion.itemRiftFlux) && (this.nexusItemStacks[0].getItemDamage() == 1)) {
                if ((this.cookTime < 1200) && (this.nexusLevel >= 10)) {
                    this.cookTime += 5;
                }

                if (this.cookTime >= 1200) {
                    if (this.nexusItemStacks[1] == null) {
                        this.nexusItemStacks[1] = new ItemStack(Invasion.itemStrongCatalyst, 1);
                        if (--this.nexusItemStacks[0].stackSize <= 0)
                            this.nexusItemStacks[0] = null;
                        this.cookTime = 0;
                    }
                }
            }
        } else {
            this.cookTime = 0;
        }

        if (this.activationTimer >= 400) {
            this.activationTimer = 0;
            if ((Invasion.tryGetInvasionPermission(this)) && (this.nexusItemStacks[0] != null)) {
                if (this.nexusItemStacks[0].getItem() == Invasion.itemNexusCatalyst) {
                    this.nexusItemStacks[0].stackSize -= 1;
                    if (this.nexusItemStacks[0].stackSize == 0)
                        this.nexusItemStacks[0] = null;
                    this.activated = true;
                    startInvasion(1);
                } else if (this.nexusItemStacks[0].getItem() == Invasion.itemStrongCatalyst) {
                    this.nexusItemStacks[0].stackSize -= 1;
                    if (this.nexusItemStacks[0].stackSize == 0)
                        this.nexusItemStacks[0] = null;
                    this.activated = true;
                    startInvasion(10);
                } else if (this.nexusItemStacks[0].getItem() == Invasion.itemStableNexusCatalyst) {
                    this.nexusItemStacks[0].stackSize -= 1;
                    if (this.nexusItemStacks[0].stackSize == 0)
                        this.nexusItemStacks[0] = null;
                    this.activated = true;
                    startContinuousPlay();
                }
            }

        } else if ((this.mode == 0) || (this.mode == 4)) {
            if (this.nexusItemStacks[0] != null) {
                if ((this.nexusItemStacks[0].getItem() == Invasion.itemNexusCatalyst) || (this.nexusItemStacks[0].getItem() == Invasion.itemStrongCatalyst)) {
                    this.activationTimer += 1;
                    this.mode = 0;
                } else if (this.nexusItemStacks[0].getItem() == Invasion.itemStableNexusCatalyst) {
                    this.activationTimer += 1;
                    this.mode = 4;
                }
            } else {
                this.activationTimer = 0;
            }
        } else if (this.mode == 2) {
            if (this.nexusItemStacks[0] != null) {
                if ((this.nexusItemStacks[0].getItem() == Invasion.itemNexusCatalyst) || (this.nexusItemStacks[0].getItem() == Invasion.itemStrongCatalyst)) {
                    this.activationTimer += 1;
                }
            } else
                this.activationTimer = 0;
        }
    }

    private void generateFlux(int increment) {
        this.generation += increment;
        if (this.generation >= 3000) {
            if (this.nexusItemStacks[1] == null) {
                this.nexusItemStacks[1] = new ItemStack(Invasion.itemRiftFlux, 1);
                this.generation -= 3000;
            } else if (this.nexusItemStacks[1].getItem() == Invasion.itemRiftFlux) {
                this.nexusItemStacks[1].stackSize += 1;
                this.generation -= 3000;
            }
        }
    }

    private void stop() {
        if (this.mode == 3) {
            setMode(2);
            int days = Invasion.getMinContinuousModeDays() + this.worldObj.rand.nextInt(1 + Invasion.getMaxContinuousModeDays() - Invasion.getMinContinuousModeDays());
            this.nextAttackTime = ((int) (this.worldObj.getWorldTime() / 24000L * 24000L) + 14000 + days * 24000);
        } else {
            setMode(0);
        }

        this.waveSpawner.stop();
        Invasion.setInvasionEnded(this);
        this.activationTimer = 0;
        this.currentWave = 0;
        this.errorState = 0;
        this.activated = false;
    }

    private void bindPlayers() {
        List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBoxToRadius);
        for (EntityPlayer entityPlayer : players) {
            long time = System.currentTimeMillis();
            if (!this.boundPlayers.containsKey(entityPlayer.getDisplayName())) {
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), entityPlayer.getDisplayName() + (entityPlayer.getDisplayName().toLowerCase().endsWith("s") ? "'" : "'s") + " life is now bound to the nexus");
            } else if (time - ((Long) this.boundPlayers.get(entityPlayer.getDisplayName())).longValue() > 300000L) {
                Invasion.sendMessageToPlayers(this.getBoundPlayers(), entityPlayer.getDisplayName() + (entityPlayer.getDisplayName().toLowerCase().endsWith("s") ? "'" : "'s") + " life is now bound to the nexus");
            }
            this.boundPlayers.put(entityPlayer.getDisplayName(), Long.valueOf(time));
        }
    }

    private void updateMobList() {
        this.mobList = this.worldObj.getEntitiesWithinAABB(EntityIMLiving.class, this.boundingBoxToRadius);
        this.mobsSorted = false;
    }

    private int acquireEntities() {
        AxisAlignedBB bb = this.boundingBoxToRadius.expand(10.0D, 128.0D, 10.0D);

        List<EntityIMLiving> entities = this.worldObj.getEntitiesWithinAABB(EntityIMLiving.class, bb);
        for (EntityIMLiving entity : entities) {
            entity.acquiredByNexus(this);
        }
        Invasion.log("Acquired " + entities.size() + " entities after state restore");
        return entities.size();
    }

    private void theEnd() {
        if (!this.worldObj.isRemote) {
            Invasion.sendMessageToPlayers(this.boundPlayers, "The nexus is destroyed!");
            stop();
            long time = System.currentTimeMillis();
            for (Map.Entry entry : this.boundPlayers.entrySet()) {
                if (time - ((Long) entry.getValue()).longValue() < 300000L) {
                    EntityPlayer player = this.worldObj.getPlayerEntityByName((String) entry.getKey());
                    if (player != null) {
                        player.attackEntityFrom(DamageSource.magic, 500.0F);
                        playSoundForBoundPlayers("random.explode");

                    }

                }

            }

            this.boundPlayers.clear();
            killAllMobs();
        }
    }

    private void continuousNexusHurt() {
        Invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus severely damaged!");
        playSoundForBoundPlayers("random.explode");
        killAllMobs();
        this.waveSpawner.stop();
        this.powerLevel = ((int) ((this.powerLevel - (this.powerLevel - this.lastPowerLevel)) * 0.7F));
        this.lastPowerLevel = this.powerLevel;
        if (this.powerLevel < 0) {
            this.powerLevel = 0;
            stop();
        }
    }

    private void killAllMobs() {
        //monsters
        List<EntityIMLiving> mobs = this.worldObj.getEntitiesWithinAABB(EntityIMLiving.class, this.boundingBoxToRadius);
        for (EntityIMLiving mob : mobs) {
            mob.attackEntityFrom(DamageSource.magic, 500.0F);
        }


        //wolves
        List<EntityIMWolf> wolves = this.worldObj.getEntitiesWithinAABB(EntityIMWolf.class, this.boundingBoxToRadius);
        for (EntityIMWolf wolf : wolves) {
            wolf.attackEntityFrom(DamageSource.magic, 500.0F);
        }
    }

    private boolean zapEnemy(int sfx) {
        if (this.mobList.size() > 0) {
            if (!this.mobsSorted) {
                Collections.sort(this.mobList, new ComparatorEntityDistance(this.xCoord, this.yCoord, this.zCoord));
            }
            EntityIMLiving mob = (EntityIMLiving) this.mobList.remove(this.mobList.size() - 1);
            mob.attackEntityFrom(DamageSource.magic, 500.0F);
            EntityIMBolt bolt = new EntityIMBolt(this.worldObj, this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, mob.posX, mob.posY, mob.posZ, 15, sfx);
            this.worldObj.spawnEntityInWorld(bolt);
            return true;
        }

        return false;
    }

    private boolean resumeSpawnerContinuous() {
        try {
            Invasion.tryGetInvasionPermission(this);
            float difficulty = 1.0F + this.powerLevel / 4500;
            float tierLevel = 1.0F + this.powerLevel / 4500;
            int timeSeconds = 240;
            Wave wave = this.waveBuilder.generateWave(difficulty, tierLevel, timeSeconds);
            this.mobsToKillInWave = ((int) (wave.getTotalMobAmount() * 0.8F));
            Invasion.log("Original mobs to kill: " + this.mobsToKillInWave);
            this.mobsLeftInWave = (this.lastMobsLeftInWave = this.mobsToKillInWave - this.waveSpawner.resumeFromState(wave, this.spawnerElapsedRestore, this.spawnRadius));
            return true;
        } catch (WaveSpawnerException e) {
            float tierLevel;
            Invasion.log("Error resuming spawner:" + e.getMessage());
            this.waveSpawner.stop();
            return false;
        } finally {
            Invasion.setInvasionEnded(this);
        }
    }

    private boolean resumeSpawnerInvasion() {
        try {
            Invasion.tryGetInvasionPermission(this);
            this.waveSpawner.resumeFromState(this.currentWave, this.spawnerElapsedRestore, this.spawnRadius);
            return true;
        } catch (WaveSpawnerException e) {
            Invasion.log("Error resuming spawner:" + e.getMessage());
            this.waveSpawner.stop();
            return false;
        } finally {
            Invasion.setInvasionEnded(this);
        }
    }

    private void playSoundTo() {
    }

    private void updateAI() {
        this.attackerAI.update();
    }

    @Override
    public String getInventoryName() {
        return "Nexus";
    }

    @Override
    public boolean hasCustomInventoryName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {

    }

    @Override
    public HashMap<String, Long> getBoundPlayers() {
        return this.boundPlayers;
    }

    public boolean isActive() {
        return this.activated;
    }

    private void setActive(boolean flag) {
        if (this.worldObj != null) {
            int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            if (flag) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, (meta & 0x4) == 0 ? meta + 4 : meta, 3);
            } else {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, (meta & 0x4) == 4 ? meta - 4 : meta, 3);
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }
}