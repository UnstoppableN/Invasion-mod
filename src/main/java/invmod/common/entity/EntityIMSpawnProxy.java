package invmod.common.entity;

import invmod.common.mod_Invasion;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityIMSpawnProxy extends EntityLiving
{
  public EntityIMSpawnProxy(World world)
  {
    super(world);
  }

  @Override
  public void onEntityUpdate()
  {
    if (this.worldObj != null)
    {
      Entity[] entities = mod_Invasion.getNightMobSpawns1(this.worldObj);
      for (Entity entity : entities)
      {
        entity.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        this.worldObj.spawnEntityInWorld(entity);
      }
    }
    setDead();
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound)
  {
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound)
  {
  }

  public float getBlockPathWeight(int i, int j, int k)
  {
    return 0.5F - this.worldObj.getLightBrightness(i, j, k);
  }

  protected boolean darkEnoughToSpawn()
  {
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.boundingBox.minY);
    int k = MathHelper.floor_double(this.posZ);
    if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32))
    {
      return false;
    }
    int l = this.worldObj.getBlockLightValue(i, j, k);
    if (this.worldObj.isThundering())
    {
      int i1 = this.worldObj.skylightSubtracted;
      this.worldObj.skylightSubtracted = 10;
      l = this.worldObj.getBlockLightValue(i, j, k);
      this.worldObj.skylightSubtracted = i1;
    }
    return l <= this.rand.nextInt(8);
  }

  @Override
  public boolean getCanSpawnHere()
  {
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.boundingBox.minY);
    int k = MathHelper.floor_double(this.posZ);
    return (darkEnoughToSpawn()) && (super.getCanSpawnHere()) && (getBlockPathWeight(i, j, k) >= 0.0F);
  }
}