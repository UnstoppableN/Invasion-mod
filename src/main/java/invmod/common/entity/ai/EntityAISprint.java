package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public class EntityAISprint extends EntityAIBase
{
  private EntityIMLiving theEntity;
  private int updateTimer;
  private int timer;
  private boolean isExecuting;
  private boolean isSprinting;
  private boolean isInWindup;
  private int missingTarget;
  private double lastX;
  private double lastY;
  private double lastZ;

  public EntityAISprint(EntityIMLiving entity)
  {
    this.theEntity = entity;
    this.updateTimer = 0;
    this.timer = 0;
    this.isExecuting = true;
    this.isSprinting = false;
    this.isInWindup = false;
    this.missingTarget = 0;
  }

  public boolean shouldExecute()
  {
    if (--this.updateTimer <= 0)
    {
      this.updateTimer = 20;
      if (((this.theEntity.getAttackTarget() != null) && (this.theEntity.canEntityBeSeen(this.theEntity.getAttackTarget()))) || (this.isSprinting))
      {
        return true;
      }

      this.isExecuting = false;
      return false;
    }

    return this.isExecuting;
  }

  public void startExecuting()
  {
    this.isExecuting = true;
    this.timer = 60;
  }

  public void updateTask()
  {
    if (this.isSprinting)
    {
      Entity target = this.theEntity.getAttackTarget();
      if ((!this.theEntity.isSprinting()) || (target == null) || ((this.missingTarget > 0) && (++this.missingTarget > 20)))
      {
        endSprint();
        return;
      }

      double dX = target.posX - this.theEntity.posX;
      double dZ = target.posZ - this.theEntity.posZ;
      double dAngle = (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D - this.theEntity.rotationYaw) % 360.0D;
      if (dAngle > 60.0D)
      {
        this.theEntity.setTurnRate(2.0F);
        this.missingTarget = 1;
      }

      if (this.theEntity.getDistanceSq(this.lastX, this.lastY, this.lastZ) < 0.0009D)
      {
        crash();
        return;
      }

      this.lastX = this.theEntity.posX;
      this.lastY = this.theEntity.posY;
      this.lastZ = this.theEntity.posZ;
    }

    if (--this.timer <= 0)
    {
      if (!this.isInWindup)
      {
        if (!this.isSprinting)
        {
          startSprint();
        }
        else
        {
          endSprint();
        }
      }
      else
      {
        sprint();
      }
    }
  }

  protected void startSprint()
  {
    Entity target = this.theEntity.getAttackTarget();
    if ((target == null) || (target.boundingBox.minY - this.theEntity.posY >= 1.0D)) {
      return;
    }
    double dX = target.posX - this.theEntity.posX;
    double dZ = target.posZ - this.theEntity.posZ;
    double dAngle = (Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D - 90.0D - this.theEntity.rotationYaw) % 360.0D;
    if (dAngle < 10.0D)
    {
      this.isInWindup = true;
      this.timer = 20;

      this.theEntity.setMoveSpeedStat(0.0F);
    }
    else
    {
      this.timer = 10;
    }
  }

  protected void sprint()
  {
    this.isInWindup = false;
    this.isSprinting = true;
    this.missingTarget = 0;
    this.timer = 35;

    this.theEntity.resetMoveSpeed();
    this.theEntity.setMoveSpeedStat(this.theEntity.getMoveSpeedStat() * 2.3F);
    this.theEntity.setSprinting(true);
    this.theEntity.setTurnRate(4.9F);
    this.theEntity.attackTime = 0;
  }

  protected void endSprint()
  {
    this.isSprinting = false;
    this.timer = 180;
    this.theEntity.resetMoveSpeed();
    this.theEntity.setTurnRate(30.0F);
    this.theEntity.setSprinting(false);
  }

  protected void crash()
  {
    this.theEntity.stunEntity(40);
    this.theEntity.attackEntityFrom(DamageSource.generic, 5.0F);
    this.theEntity.worldObj.playSoundAtEntity(this.theEntity, "random.explode", 1.0F, 0.6F);
    endSprint();
  }
}