package invmod.common.entity;

import invmod.Invasion;
import invmod.common.IBlockAccessExtended;
import invmod.common.INotifyTask;
import invmod.common.entity.ai.*;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class EntityIMZombie extends EntityIMMob implements ICanDig {
    private static final int META_CHANGED = 29;
    private static final int META_TIER = 30;
    private static final int META_TEXTURE = 31;
    private static final int META_FLAVOUR = 28;
    private static final int META_SWINGING = 27;
    private TerrainModifier terrainModifier;
    private TerrainDigger terrainDigger;
    private byte metaChanged;
    private int tier;
    private int flavour;
    private ItemStack defaultHeldItem;
    private Item itemDrop;
    private float dropChance;
    private int swingTimer;

    public EntityIMZombie(World world) {
        this(world, null);

    }

    public EntityIMZombie(World world, INexusAccess nexus) {
        super(world, nexus);
        this.terrainModifier = new TerrainModifier(this, 2.0F);
        this.terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0F);
        this.dropChance = 0.0F;
        if (world.isRemote) {
            this.metaChanged = 1;
        } else {
            this.metaChanged = 0;
        }


        this.flavour = 0;
        this.tier = 1;

        DataWatcher dataWatcher = getDataWatcher();
        dataWatcher.addObject(29, Byte.valueOf(this.metaChanged));
        dataWatcher.addObject(30, Integer.valueOf(this.tier));
        dataWatcher.addObject(31, Integer.valueOf(0));
        dataWatcher.addObject(28, Integer.valueOf(this.flavour));
        dataWatcher.addObject(27, Byte.valueOf((byte) 0));

        setAttributes(this.tier, this.flavour);
        this.floatsInWater = true;
        setAI();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.metaChanged != getDataWatcher().getWatchableObjectByte(29)) {
            DataWatcher data = getDataWatcher();
            this.metaChanged = data.getWatchableObjectByte(29);
            setTexture(data.getWatchableObjectInt(31));

            if (this.tier != data.getWatchableObjectInt(30))
                setTier(data.getWatchableObjectInt(30));
            if (this.flavour != data.getWatchableObjectInt(28)) {
                setFlavour(data.getWatchableObjectInt(28));
            }
        }
        if ((!this.worldObj.isRemote) && (this.flammability >= 20) && (isBurning()))
            doFireball();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateAnimation();
        updateSound();
    }

    @Override
    public void onPathSet() {
        this.terrainModifier.cancelTask();
    }

    protected void setAI() {
        //added entityaiswimming and increased all other tasksordernumers with 1
        this.tasks = new EntityAITasks(this.worldObj.theProfiler);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIKillEntity(this, EntityPlayer.class, 40));
        this.tasks.addTask(1, new EntityAIKillEntity(this, EntityPlayerMP.class, 40));
        this.tasks.addTask(2, new EntityAIAttackNexus(this));
        this.tasks.addTask(3, new EntityAIWaitForEngy(this, 4.0F, true));
        this.tasks.addTask(4, new EntityAIKillEntity(this, EntityLiving.class, 40));
        this.tasks.addTask(5, new EntityAIGoToNexus(this));
        this.tasks.addTask(6, new EntityAIWanderIM(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));


        this.targetTasks = new EntityAITasks(this.worldObj.theProfiler);
        this.targetTasks.addTask(0, new EntityAITargetRetaliate(this, EntityLiving.class, (float) Invasion.getNightMobSightRange()));
        this.targetTasks.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, (float) Invasion.getNightMobSightRange(), true));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));

        if (this.tier == 3) {
            this.tasks.addTask(4, new EntityAIStoop(this));
            this.tasks.addTask(3, new EntityAISprint(this));
        } else {
            //track players from sensing them
            this.targetTasks.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, (float) Invasion.getNightMobSenseRange(), false));
            this.targetTasks.addTask(3, new EntityAITargetOnNoNexusPath(this, EntityIMPigEngy.class, 3.5F));
        }
    }

    public void setTexture(int textureId) {
        getDataWatcher().updateObject(31, Integer.valueOf(textureId));
    }

    public void setFlavour(int flavour) {
        getDataWatcher().updateObject(28, Integer.valueOf(flavour));
        this.flavour = flavour;
        setAttributes(this.tier, flavour);
    }

    public int getTextureId() {
        return getDataWatcher().getWatchableObjectInt(31);
    }

    @Override
    public String toString() {
        return "IMZombie-T" + this.tier;
    }

    @Override
    public IBlockAccess getTerrain() {
        return this.worldObj;
    }

    @Override
    public ItemStack getHeldItem() {
        return this.defaultHeldItem;

    }

    @Override
    public boolean avoidsBlock(Block block) {
        if ((this.isImmuneToFire) && ((block == Blocks.fire) || (block == Blocks.flowing_lava) || (block == Blocks.lava))) {
            return false;
        }
        return super.avoidsBlock(block);
    }

    @Override
    public float getBlockRemovalCost(int x, int y, int z) {
        return getBlockStrength(x, y, z) * 20.0F;
    }

    @Override
    public boolean canClearBlock(int x, int y, int z) {
        Block block = this.worldObj.getBlock(x, y, z);
        return (block == Blocks.air) || (isBlockDestructible(this.worldObj, x, y, z, block));

    }

    @Override
    protected boolean onPathBlocked(Path path, INotifyTask notifee) {
        if ((!path.isFinished()) && ((isNexusBound()) || (getAttackTarget() != null))) {

            if ((path.getFinalPathPoint().distanceTo(path.getIntendedTarget()) > 2.2D) && (path.getCurrentPathIndex() + 2 >= path.getCurrentPathLength() / 2)) {

                return false;
            }
            PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());

            if (this.terrainDigger.askClearPosition(node.xCoord, node.yCoord, node.zCoord, notifee, 1.0F)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBigRenderTempHack() {
        return this.tier == 3;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return (this.tier == 3) && (isSprinting()) ? chargeAttack(entity) : super.attackEntityAsMob(entity);
    }

    @Override
    public boolean canBePushed() {
        return this.tier != 3;
    }

    @Override
    public void knockBack(Entity par1Entity, float par2, double par3, double par5) {
        if (this.tier == 3) {
            return;
        }
        this.isAirBorne = true;
        float f = MathHelper.sqrt_double(par3 * par3 + par5 * par5);
        float f1 = 0.4F;
        this.motionX /= 2.0D;
        this.motionY /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= par3 / f * f1;
        this.motionY += f1;
        this.motionZ -= par5 / f * f1;

        if (this.motionY > 0.4000000059604645D) {
            this.motionY = 0.4000000059604645D;
        }
    }

    @Override
    public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        if ((this.tier == 2) && (this.flavour == 2) && (node.action == PathAction.SWIM)) {
            float multiplier = 1.0F;
            if ((terrainMap instanceof IBlockAccessExtended)) {
                int mobDensity = ((IBlockAccessExtended) terrainMap).getLayeredData(node.xCoord, node.yCoord, node.zCoord) & 0x7;
                multiplier += mobDensity * 3;
            }

            if ((node.yCoord > prevNode.yCoord) && (getCollide(terrainMap, node.xCoord, node.yCoord, node.zCoord) == 2)) {
                multiplier += 2.0F;
            }

            return prevNode.distanceTo(node) * 1.2F * multiplier;
        }

        return super.getBlockPathCost(prevNode, node, terrainMap);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return (this.tier == 2) && (this.flavour == 2);
    }

    @Override
    public boolean isBlockDestructible(IBlockAccess terrainMap, int x, int y, int z, Block block) {
        if (getDestructiveness() == 0) {
            return false;
        }

        IPosition pos = getCurrentTargetPos();
        int dY = pos.getYCoord() - y;
        boolean isTooSteep = false;
        if (dY > 0) {
            dY += 8;
            int dX = pos.getXCoord() - x;
            int dZ = pos.getZCoord() - z;
            double dXZ = Math.sqrt(dX * dX + dZ * dZ) + 1.E-005D;
            isTooSteep = dY / dXZ > 2.144D;
        }

        return (!isTooSteep) && (super.isBlockDestructible(terrainMap, x, y, z, block));
    }

    @Override
    public void onFollowingEntity(Entity entity) {
        if (entity == null) {
            setDestructiveness(1);
        } else if (((entity instanceof EntityIMPigEngy)) || ((entity instanceof EntityIMCreeper))) {
            setDestructiveness(0);
        } else {
            setDestructiveness(1);
        }
    }

    public float scaleAmount() {
        if (this.tier == 2)
            return 1.12F;
        if (this.tier == 3) {
            return 1.21F;
        }
        return 1.0F;
    }

    @Override
    public String getSpecies() {
        return "Zombie";
    }

    @Override
    public int getTier() {
        return this.tier < 3 ? 2 : 3;
    }

    public void setTier(int tier) {
        this.tier = tier;
        getDataWatcher().updateObject(30, Integer.valueOf(tier));
        setAttributes(tier, this.flavour);
        setAI();

        if (getDataWatcher().getWatchableObjectInt(31) == 0) {
            if (tier == 1) {
                int r = this.rand.nextInt(2);
                if (r == 0)
                    setTexture(0);
                else if (r == 1)
                    setTexture(1);
            } else if (tier == 2) {
                if (this.flavour == 2) {
                    setTexture(5);
                } else if (this.flavour == 3) {
                    setTexture(3);
                } else {
                    int r = this.rand.nextInt(2);
                    if (r == 0)
                        setTexture(2);
                    else if (r == 1)
                        setTexture(4);
                }
            } else if (tier == 3) {
                setTexture(6);
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("tier", this.tier);
        nbttagcompound.setInteger("flavour", this.flavour);
        nbttagcompound.setInteger("textureId", this.dataWatcher.getWatchableObjectInt(31));
        super.writeEntityToNBT(nbttagcompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        setTexture(nbttagcompound.getInteger("textureId"));
        this.flavour = nbttagcompound.getInteger("flavour");
        this.tier = nbttagcompound.getInteger("tier");
        if (this.tier == 0) {
            this.tier = 1;
        }
        setFlavour(this.flavour);
        setTier(this.tier);
        //super.readEntityFromNBT(nbttagcompound);
    }

    @Override
    protected void sunlightDamageTick() {
        if ((this.tier == 2) && (this.flavour == 2))
            damageEntity(DamageSource.generic, 3.0F);
        else
            setFire(8);
    }

    protected void updateAnimation() {
        updateAnimation(false);
    }

    public void updateAnimation(boolean override) {
        if ((!this.worldObj.isRemote) && ((this.terrainModifier.isBusy()) || override)) {
            setSwinging(true);
        }
        int swingSpeed = getSwingSpeed();
        if (isSwinging()) {
            this.swingTimer += 1;
            if (this.swingTimer >= swingSpeed) {
                this.swingTimer = 0;
                setSwinging(false);
            }
        } else {
            this.swingTimer = 0;
        }
        this.swingProgress = (float) ((float) this.swingTimer / (float) swingSpeed);
    }

    protected boolean isSwinging() {
        return getDataWatcher().getWatchableObjectByte(27) != 0;
    }

    protected void setSwinging(boolean flag) {
        //this.isSwingInProgress=flag;
        getDataWatcher().updateObject(27, Byte.valueOf((byte) (flag == true ? 1 : 0)));
    }

    protected void updateSound() {
        if (this.terrainModifier.isBusy()) {
            if (--this.throttled2 <= 0) {
                this.worldObj.playSoundAtEntity(this, "invmod:scrape", 0.85F, 1.0F / (this.rand.nextFloat() * 0.5F + 1.0F));
                this.throttled2 = (45 + this.rand.nextInt(20));
            }
        }
    }

    protected int getSwingSpeed() {
        return 10;
    }

    protected boolean chargeAttack(Entity entity) {
        int knockback = 4;
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength + 3);
        entity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F, 0.4D, MathHelper.cos(this.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F);
        setSprinting(false);
        this.worldObj.playSoundAtEntity(entity, "damage.fallbig", 1.0F, 1.0F);
        return true;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.terrainModifier.onUpdate();
    }

    protected ITerrainDig getTerrainDig() {
        return this.terrainDigger;
    }

    @Override
    protected String getLivingSound() {
        if (this.tier == 3) {
            return this.rand.nextInt(3) == 0 ? "invmod:bigzombie1" : null;
        }

        return "mob.zombie.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.zombie.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }

    @Override
    protected Item getDropItem() {
        return Items.rotten_flesh;
    }

    @Override
    protected void dropFewItems(boolean flag, int bonus) {
        super.dropFewItems(flag, bonus);
        if (this.rand.nextFloat() < 0.35F) {
            dropItem(Items.rotten_flesh, 1);
        }

        if ((this.itemDrop != null) && (this.rand.nextFloat() < this.dropChance)) {
            entityDropItem(new ItemStack(this.itemDrop, 1, 0), 0.0F);
        }
    }

    private void setAttributes(int tier, int flavour) {
        if (tier == 1) {
            this.tier = 1;
            if (flavour == 0) {
                setName("Zombie");
                setGender(1);
                setBaseMoveSpeedStat(0.19F);
                this.attackStrength = 4;
                this.selfDamage = 3;
                this.maxSelfDamage = 6;
                this.maxDestructiveness = 2;
                this.flammability = 3;
                setDestructiveness(2);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            } else if (flavour == 1) {
                setName("Zombie");
                setGender(1);
                setBaseMoveSpeedStat(0.19F);
                this.attackStrength = 6;
                this.selfDamage = 3;
                this.maxSelfDamage = 6;
                this.maxDestructiveness = 0;
                this.flammability = 3;
                this.defaultHeldItem = new ItemStack(Items.wooden_sword, 1);
                this.itemDrop = Items.wooden_sword;
                this.dropChance = 0.2F;
                setDestructiveness(0);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            }
        } else if (tier == 2) {
            this.tier = 2;
            if (flavour == 0) {
                setName("Zombie");
                setGender(1);
                setBaseMoveSpeedStat(0.19F);
                this.attackStrength = 7;
                this.selfDamage = 4;
                this.maxSelfDamage = 12;
                this.maxDestructiveness = 2;
                this.flammability = 4;
                this.itemDrop = Items.iron_chestplate;
                this.dropChance = 0.25F;
                setDestructiveness(2);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            } else if (flavour == 1) {
                setName("Zombie");
                setGender(1);
                setBaseMoveSpeedStat(0.19F);
                this.attackStrength = 10;
                this.selfDamage = 3;
                this.maxSelfDamage = 9;
                this.maxDestructiveness = 0;
                this.itemDrop = Items.iron_sword;
                this.dropChance = 0.25F;
                this.defaultHeldItem = new ItemStack(Items.iron_sword, 1);
                setDestructiveness(0);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            } else if (flavour == 2) {
                setName("Tar Zombie");
                setGender(1);
                setBaseMoveSpeedStat(0.19F);
                this.attackStrength = 5;
                this.selfDamage = 3;
                this.maxSelfDamage = 9;
                this.maxDestructiveness = 2;
                this.flammability = 30;
                this.floatsInWater = false;
                setDestructiveness(2);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            } else if (flavour == 3) {
                setName("Zombie Pigman");
                setGender(1);
                setBaseMoveSpeedStat(0.25F);
                this.attackStrength = 8;
                this.maxDestructiveness = 2;
                this.isImmuneToFire = true;
                this.defaultHeldItem = new ItemStack(Items.golden_sword, 1);
                setDestructiveness(2);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            }
        } else if (tier == 3) {
            this.tier = 3;
            if (flavour == 0) {
                setName("Zombie Brute");
                setGender(1);
                setBaseMoveSpeedStat(0.17F);
                this.attackStrength = 18;
                this.selfDamage = 4;
                this.maxSelfDamage = 20;
                this.maxDestructiveness = 2;
                this.flammability = 4;
                this.dropChance = 0.0F;
                setDestructiveness(2);
                setMaxHealthAndHealth(Invasion.getMobHealth(this));
            }
        }
    }

    private void doFireball() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);
        int ii;
        for (ii = -1; ii < 2; ii++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if ((this.worldObj.getBlock(x + ii, y + j, z + k) == Blocks.air) || (this.worldObj.getBlock(x + ii, y + j, z + k).getMaterial().getCanBurn())) {
                        this.worldObj.setBlock(x + ii, y + j, z + k, Blocks.fire);
                    }
                }
            }
        }

        List entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.5D, 1.5D, 1.5D));
        for (int el = entities.size() - 1; el >= 0; el--) {
            Entity entity = (Entity) entities.get(el);
            entity.setFire(8);
        }
        attackEntityFrom(DamageSource.inFire, 500.0F);
    }

    @Override
    public void onBlockRemoved(int paramInt1, int paramInt2, int paramInt3,
                               Block block) {
        // TODO Auto-generated method stub

    }

}
