package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Path;
import invmod.common.util.IPosition;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIWanderIM extends EntityAIBase
{
  private static final int MIN_HORIZONTAL_PATH = 1;
  private static final int MAX_HORIZONTAL_PATH = 6;
  private static final int MAX_VERTICAL_PATH = 4;
  private EntityIMLiving theEntity;
  private IPosition movePosition;

  public EntityAIWanderIM(EntityIMLiving entity)
  {
    this.theEntity = entity;
    setMutexBits(1);
  }

  public boolean shouldExecute()
  {
    if (this.theEntity.getRNG().nextInt(120) == 0)
    {
      int x = this.theEntity.getXCoord() + this.theEntity.getRNG().nextInt(13) - 6;
      int z = this.theEntity.getZCoord() + this.theEntity.getRNG().nextInt(13) - 6;
      Path path = this.theEntity.getNavigatorNew().getPathTowardsXZ(x, z, 1, 6, 4);
      if (path != null)
      {
        this.theEntity.getNavigatorNew().setPath(path, this.theEntity.getMoveSpeedStat());
        return true;
      }
    }

    return false;
  }

  public boolean continueExecuting()
  {
    return (!this.theEntity.getNavigatorNew().noPath()) && (this.theEntity.getNavigatorNew().getStuckTime() < 40);
  }
}