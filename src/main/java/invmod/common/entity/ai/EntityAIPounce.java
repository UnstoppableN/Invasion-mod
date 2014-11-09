package invmod.common.entity.ai;

import invmod.common.entity.EntityIMSpider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAIPounce extends EntityAIBase
{
  private EntityIMSpider theEntity;
  private boolean isPouncing;
  private int pounceTimer;
  private int cooldown;
  private float minPower;
  private float maxPower;

  public EntityAIPounce(EntityIMSpider entity, float minPower, float maxPower, int cooldown)
  {
    this.theEntity = entity;
    this.isPouncing = false;
    this.minPower = minPower;
    this.maxPower = maxPower;
    this.cooldown = cooldown;
  }

  public boolean shouldExecute()
  {
    EntityLivingBase target = this.theEntity.getAttackTarget();
    if ((--this.pounceTimer <= 0) && (target != null) && (this.theEntity.canEntityBeSeen(target)) && (this.theEntity.onGround))
    {
      return true;
    }

    return false;
  }

  public boolean continueExecuting()
  {
    return this.isPouncing;
  }

  public void startExecuting()
  {
    EntityLivingBase target = this.theEntity.getAttackTarget();
    if (pounce(target.posX, target.posY, target.posZ))
    {
      this.theEntity.setAirborneTime(0);
      this.isPouncing = true;
      this.theEntity.getNavigatorNew().haltForTick();
    }
    else
    {
      this.isPouncing = false;
    }
  }

  public void updateTask()
  {
    this.theEntity.getNavigatorNew().haltForTick();
    int airborneTime = this.theEntity.getAirborneTime();
    if ((airborneTime > 20) && (this.theEntity.onGround))
    {
      this.isPouncing = false;
      this.pounceTimer = this.cooldown;
      this.theEntity.setAirborneTime(0);
      this.theEntity.getNavigatorNew().clearPath();
    }
    else
    {
      this.theEntity.setAirborneTime(airborneTime + 1);
    }
  }

  protected boolean pounce(double x, double y, double z)
  {
    double dX = x - this.theEntity.posX;
    double dY = y - this.theEntity.posY;
    double dZ = z - this.theEntity.posZ;
    double dXZ = MathHelper.sqrt_double(dX * dX + dZ * dZ);
    double a = Math.atan(dY / dXZ);
    if ((a > -0.7853981633974483D) && (a < 0.7853981633974483D))
    {
      double rratio = (1.0D - Math.tan(a)) * (1.0D / Math.cos(a));
      double r = dXZ / rratio;
      double v = 1.0D / Math.sqrt(1.0F / this.theEntity.getGravity() / r);
      if ((v > this.minPower) && (v < this.maxPower))
      {
        double distance = MathHelper.sqrt_double(2.0D * (dXZ * dXZ));
        this.theEntity.motionX = (v * dX / distance);
        this.theEntity.motionY = (v * dXZ / distance);
        this.theEntity.motionZ = (v * dZ / distance);
        return true;
      }
    }
    return false;
  }
}