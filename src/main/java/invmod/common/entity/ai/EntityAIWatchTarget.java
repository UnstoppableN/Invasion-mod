package invmod.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityLookHelper;

public class EntityAIWatchTarget extends EntityAIBase
{
  private EntityLiving theEntity;

  public EntityAIWatchTarget(EntityLiving entity)
  {
    this.theEntity = entity;
  }

  public boolean shouldExecute()
  {
    return this.theEntity.getAttackTarget() != null;
  }

  public void updateTask()
  {
    this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 2.0F, 2.0F);
  }
}