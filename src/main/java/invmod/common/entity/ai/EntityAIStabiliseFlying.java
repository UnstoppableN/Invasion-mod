package invmod.common.entity.ai;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStabiliseFlying extends EntityAIBase
{
  private static int INITIAL_STABILISE_TIME = 50;
  private EntityIMFlying theEntity;
  private int time;
  private int stabiliseTime;

  public EntityAIStabiliseFlying(EntityIMFlying entity, int stabiliseTime)
  {
    this.theEntity = entity;
    this.time = 0;
    this.stabiliseTime = stabiliseTime;
  }

  public boolean shouldExecute()
  {
    return this.theEntity.getAIGoal() == Goal.STABILISE;
  }

  public boolean continueExecuting()
  {
    if (this.time >= this.stabiliseTime)
    {
      this.theEntity.transitionAIGoal(Goal.NONE);
      this.theEntity.getNavigatorNew().setPitchBias(0.0F, 0.0F);
      return false;
    }
    return true;
  }

  public void startExecuting()
  {
    this.time = 0;
    INavigationFlying nav = this.theEntity.getNavigatorNew();
    nav.clearPath();
    nav.setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
    nav.setPitchBias(20.0F, 0.5F);
  }

  public void updateTask()
  {
    this.time += 1;
    if (this.time == INITIAL_STABILISE_TIME)
    {
      this.theEntity.getNavigatorNew().setPitchBias(0.0F, 0.0F);
    }
  }
}