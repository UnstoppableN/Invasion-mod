package invmod.common.entity.ai;

//NOOB HAUS: Done

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.Path;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingMoveToEntity extends EntityAIBase
{
  private EntityIMFlying theEntity;

  public EntityAIFlyingMoveToEntity(EntityIMFlying entity)
  {
    this.theEntity = entity;
  }

  public boolean shouldExecute()
  {
    return (this.theEntity.getAIGoal() == Goal.GOTO_ENTITY) && (this.theEntity.getAttackTarget() != null);
  }

  public void startExecuting()
  {
    INavigationFlying nav = this.theEntity.getNavigatorNew();
    Entity target = this.theEntity.getAttackTarget();
    if (target != nav.getTargetEntity())
    {
      nav.clearPath();
      nav.setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
      Path path = nav.getPathToEntity(target, 0.0F);
      if (path.getCurrentPathLength() > 2.0D * this.theEntity.getDistanceToEntity(target))
      {
        nav.setMovementType(INavigationFlying.MoveType.MIXED);
      }
      nav.autoPathToEntity(target);
    }
  }

  public void updateTask()
  {
  }
}