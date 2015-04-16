package invmod.common.entity;

import invmod.Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.nexus.SpawnPoint;
import invmod.common.nexus.SpawnType;
import invmod.common.nexus.TileEntityNexus;
import invmod.common.util.ComparatorDistanceFrom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityIMWolf extends EntityWolf {
    private static final int META_BOUND = 30;
    private INexusAccess nexus;
    private int nexusX;
    private int nexusY;
    private int nexusZ;
    private int updateTimer;
    private boolean loadedFromNBT;
    private float maxHealth;

    public EntityIMWolf(World world) {
        this(world, null);
    }

    public EntityIMWolf(EntityWolf wolf, INexusAccess nexus) {
        this(wolf.worldObj, nexus);
        this.loadedFromNBT = false;
        setPositionAndRotation(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, wolf.rotationPitch);
        this.dataWatcher.updateObject(16, Byte.valueOf(wolf.getDataWatcher().getWatchableObjectByte(16)));
        this.dataWatcher.updateObject(17, wolf.getDataWatcher().getWatchableObjectString(17));
        this.dataWatcher.updateObject(18, Float.valueOf(wolf.getDataWatcher().getWatchableObjectFloat(18)));
        this.aiSit.setSitting(isSitting());
    }

    public EntityIMWolf(World world, INexusAccess nexus) {
        super(world);
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, IMob.class, 0, true));
        setEntityHealth(getMaxHealth());
        this.dataWatcher.addObject(30, Byte.valueOf((byte) 0));
        this.nexus = nexus;
        if (nexus != null) {
            this.nexusX = nexus.getXCoord();
            this.nexusY = nexus.getYCoord();
            this.nexusZ = nexus.getZCoord();
            this.dataWatcher.updateObject(30, Byte.valueOf((byte) 1));
        }
    }

    @Override

    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (this.loadedFromNBT) {
            this.loadedFromNBT = false;
            checkNexus();
        }

        if ((!this.worldObj.isRemote) && (this.updateTimer++ > 40))
            checkNexus();
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity) {
        int damage = isTamed() ? 4 : 2;
        if ((par1Entity instanceof IMob))
            damage *= 2;
        boolean success = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (success) {
            heal(4.0F);
        }
        return success;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }
    }

    @Override
    public int getCollarColor() {
        return this.dataWatcher.getWatchableObjectByte(30) == 1 ? 10 : 1;
    }

    @Override
    protected String getHurtSound() {
        if ((getAttackTarget() instanceof IMob)) {
            return "mob.wolf.growl";
        }
        return "mob.wolf.hurt";
    }

    @Override
    protected void onDeathUpdate() {
        this.deathTime += 1;
        if (this.deathTime == 120) {
            int i;
            if ((!this.worldObj.isRemote) && ((this.recentlyHit > 0) || (isPlayer())) && (!isChild())) {
                for (i = getExperiencePoints(this.attackingPlayer); i > 0; ) {
                    int k = EntityXPOrb.getXPSplit(i);
                    i -= k;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
                }
            }

            setDead();
            for (int j = 0; j < 20; j++) {
                double d = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle("explode", this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d, d1, d2);
            }
        }
    }


    @Override
    public void setDead() {
        this.isDead = true;
        if (this.nexus != null) {
            if (this.nexus.getMode() != 0) {
                respawnAtNexus();
            } else {
                super.setDead();
            }
        }

    }

    public void setEntityHealth(float par1) {
        this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(par1, 0.0F, getMaxHealth())));
    }

    public boolean respawnAtNexus() {
        if ((!this.worldObj.isRemote) && (this.dataWatcher.getWatchableObjectByte(30) == 1) && (this.nexus != null)) {
            EntityIMWolf wolfRecreation = new EntityIMWolf(this, this.nexus);

            int x = this.nexus.getXCoord();
            int y = this.nexus.getYCoord();
            int z = this.nexus.getZCoord();
            List spawnPoints = new ArrayList();
            setRotation(0.0F, 0.0F);
            for (int vertical = 0; vertical < 3; vertical = vertical > 0 ? vertical * -1 : vertical * -1 + 1) {
                for (int i = -4; i < 5; i++) {
                    for (int j = -4; j < 5; j++) {
                        wolfRecreation.setPosition(x + i + 0.5F, y + vertical, z + j + 0.5F);
                        if (wolfRecreation.getCanSpawnHere())
                            spawnPoints.add(new SpawnPoint(x + i, y + vertical, z + i, 0, SpawnType.WOLF));
                    }
                }
            }
            Collections.sort(spawnPoints, new ComparatorDistanceFrom(x, y, z));

            if (spawnPoints.size() > 0) {
                SpawnPoint point = (SpawnPoint) spawnPoints.get(spawnPoints.size() / 2);
                wolfRecreation.setPosition(point.getXCoord() + 0.5D, point.getYCoord(), point.getZCoord() + 0.5D);
                wolfRecreation.heal(60.0F);
                this.worldObj.spawnEntityInWorld(wolfRecreation);
                return true;
            }
        }
        Invasion.log("No respawn spot for wolf");
        return false;
    }

    @Override
    public boolean getCanSpawnHere() {
        return (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox));
    }

//  @Override
//  public boolean interact(EntityPlayer player)
//  {
//    ItemStack itemstack = player.inventory.getCurrentItem();
//    if (itemstack != null)
//    {
////      if ((itemstack.getItem() == Items.bone) && (player.getDisplayName().equalsIgnoreCase(((EntityPlayerMP)getOwner()).getDisplayName())) && (this.dataWatcher.getWatchableObjectByte(30) == 1))
////      {
////        this.dataWatcher.updateObject(30, Byte.valueOf((byte)0));
////        this.nexus = null;
////
////        itemstack.stackSize -= 1;
////        if (itemstack.stackSize <= 0)
////          player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
////        return false;
////      }
//      if ((itemstack.getItem() == mod_Invasion.itemStrangeBone) && (player.getDisplayName().equalsIgnoreCase(((EntityPlayerMP)getOwner()).getDisplayName())))
//      {
//        INexusAccess newNexus = findNexus();
//        if ((newNexus != null) && (newNexus != this.nexus))
//        {
//          this.nexus = newNexus;
//          this.dataWatcher.updateObject(30, Byte.valueOf((byte)1));
//          this.nexusX = this.nexus.getXCoord();
//          this.nexusY = this.nexus.getYCoord();
//          this.nexusZ = this.nexus.getZCoord();
//
//          itemstack.stackSize -= 1;
//          if (itemstack.stackSize <= 0) {
//            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
//          }
//          this.maxHealth = 25.0F;
//          setEntityHealth(25.0F);
//        }
//        return true;
//      }
//    }
//    return super.interact(player);
//  }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        if (this.nexus != null) {
            nbttagcompound.setInteger("nexusX", this.nexus.getXCoord());
            nbttagcompound.setInteger("nexusY", this.nexus.getYCoord());
            nbttagcompound.setInteger("nexusZ", this.nexus.getZCoord());
        }
        nbttagcompound.setByte("nexusBound", this.dataWatcher.getWatchableObjectByte(30));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.nexusX = nbttagcompound.getInteger("nexusX");
        this.nexusY = nbttagcompound.getInteger("nexusY");
        this.nexusZ = nbttagcompound.getInteger("nexusZ");
        this.dataWatcher.updateObject(30, Byte.valueOf(nbttagcompound.getByte("nexusBound")));
        this.loadedFromNBT = true;
    }

    @Override
    public void setAngry(boolean par1) {
    }

    private void checkNexus() {
        if ((this.worldObj != null) && (this.dataWatcher.getWatchableObjectByte(30) == 1)) {
            if (this.worldObj.getBlock(this.nexusX, this.nexusY, this.nexusZ) == Invasion.blockNexus) {
                this.nexus = ((TileEntityNexus) this.worldObj.getTileEntity(this.nexusX, this.nexusY, this.nexusZ));
            }
            if (this.nexus == null)
                this.dataWatcher.updateObject(30, Byte.valueOf((byte) 0));
        }
    }

    private INexusAccess findNexus() {
        TileEntityNexus nexus = null;
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);
        for (int i = -7; i < 8; i++) {
            for (int j = -4; j < 5; j++) {
                for (int k = -7; k < 8; k++) {
                    if (this.worldObj.getBlock(x + i, y + j, z + k) == Invasion.blockNexus) {
                        nexus = (TileEntityNexus) this.worldObj.getTileEntity(x + i, y + j, z + k);
                        break;
                    }
                }
            }
        }

        return nexus;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float par2float) {

        return super.attackEntityFrom(damageSource, par2float);
    }

}