package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.Distance;
import net.minecraft.world.IBlockAccess;

public class NavigatorEngy extends NavigatorIM
{
  private final EntityIMPigEngy pigEntity;

  public NavigatorEngy(EntityIMPigEngy entity, IPathSource pathSource)
  {
    super(entity, pathSource);
    this.pigEntity = entity;
    setNoMaintainPos();
  }

  protected Path createPath(EntityIMLiving entity, int x, int y, int z, float targetRadius)
  {
    IBlockAccess terrainCache = getChunkCache(entity.getXCoord(), entity.getYCoord(), entity.getZCoord(), x, y, z, 16.0F);
    INexusAccess nexus = this.pigEntity.getNexus();
    if (nexus != null)
    {
      IBlockAccessExtended terrainCacheExt = nexus.getAttackerAI().wrapEntityData(terrainCache);

      nexus.getAttackerAI().addScaffoldDataTo(terrainCacheExt);
      terrainCache = terrainCacheExt;
    }
    float maxSearchRange = 12.0F + (float)Distance.distanceBetween(entity, x, y, z);
    if (this.pathSource.canPathfindNice(IPathSource.PathPriority.HIGH, maxSearchRange, this.pathSource.getSearchDepth(), this.pathSource.getQuickFailDepth())) {
      return this.pathSource.createPath(entity, x, y, z, targetRadius, maxSearchRange, terrainCache);
    }
    return null;
  }

  protected boolean handlePathAction()
  {
    if (!this.actionCleared)
    {
      resetStatus();
      if (getLastActionResult() != 0) {
        return false;
      }
      return true;
    }

    if ((this.activeNode.action == PathAction.LADDER_UP_PX) || (this.activeNode.action == PathAction.LADDER_UP_NX) || (this.activeNode.action == PathAction.LADDER_UP_PZ) || (this.activeNode.action == PathAction.LADDER_UP_NZ))
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildLadder(this.activeNode, this))
        return setDoingTaskAndHold();
    }
    else if (this.activeNode.action == PathAction.BRIDGE)
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildBridge(this.activeNode, this))
        return setDoingTaskAndHold();
    }
    else if (this.activeNode.action == PathAction.SCAFFOLD_UP)
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildScaffoldLayer(this.activeNode, this))
        return setDoingTaskAndHoldOnPoint();
    }
    else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_PX)
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 0, 1, this))
        return setDoingTaskAndHold();
    }
    else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_NX)
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 1, 1, this))
        return setDoingTaskAndHold();
    }
    else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_PZ)
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 2, 1, this))
        return setDoingTaskAndHold();
    }
    else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_NZ)
    {
      if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode, 3, 1, this)) {
        return setDoingTaskAndHold();
      }
    }
    this.nodeActionFinished = true;
    return true;
  }
}