package invmod.common;

import invmod.common.entity.PathNode;
import invmod.common.entity.PathfinderIM;
import net.minecraft.world.IBlockAccess;

public abstract interface IPathfindable
{
  public abstract float getBlockPathCost(PathNode paramPathNode1, PathNode paramPathNode2, IBlockAccess paramIBlockAccess);

  public abstract void getPathOptionsFromNode(IBlockAccess paramIBlockAccess, PathNode paramPathNode, PathfinderIM paramPathfinderIM);
}