package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStoop extends EntityAIBase
{
  private EntityIMLiving theEntity;
  private int updateTimer;
  private boolean stopStoop;

  public EntityAIStoop(EntityIMLiving entity)
  {
    this.theEntity = entity;
    this.stopStoop = true;
  }

  public boolean shouldExecute()
  {
    if (--this.updateTimer <= 0)
    {
      this.updateTimer = 10;
      if (this.theEntity.worldObj.getBlock(this.theEntity.getXCoord(), this.theEntity.getYCoord() + 2, this.theEntity.getZCoord()).getMaterial().blocksMovement()) {
        return true;
      }
    }
    return false;
  }

  public boolean continueExecuting()
  {
    return !this.stopStoop;
  }

  public void startExecuting()
  {
    this.theEntity.setSneaking(true);
    this.stopStoop = false;
  }

  public void updateTask()
  {
    if (--this.updateTimer <= 0)
    {
      this.updateTimer = 10;
      if (!this.theEntity.worldObj.getBlock(this.theEntity.getXCoord(), this.theEntity.getYCoord() + 2, this.theEntity.getZCoord()).getMaterial().blocksMovement())
      {
        this.theEntity.setSneaking(false);
        this.stopStoop = true;
      }
    }
  }
}