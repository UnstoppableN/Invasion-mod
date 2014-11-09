package invmod.common.entity;

import invmod.common.IPathfindable;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.IBlockAccess;

public class PathfinderIM
{
  private static PathfinderIM pathfinder = new PathfinderIM();
  private IBlockAccess worldMap;
  private NodeContainer path;
  private IntHashMap pointMap;
  private PathNode[] pathOptions;
  private PathNode finalTarget;
  private float targetRadius;
  private int pathsIndex;
  private float searchRange;
  private int nodeLimit;
  private int nodesOpened;

  public static synchronized Path createPath(IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float targetRadius, float maxSearchRange, IBlockAccess iblockaccess, int searchDepth, int quickFailDepth)
  {
    return pathfinder.createEntityPathTo(entity, x, y, z, x2, y2, z2, targetRadius, maxSearchRange, iblockaccess, searchDepth, quickFailDepth);
  }

  public PathfinderIM()
  {
    this.path = new NodeContainer();
    this.pointMap = new IntHashMap();
    this.pathOptions = new PathNode[32];
  }

  public Path createEntityPathTo(IPathfindable entity, int x, int y, int z, int x2, int y2, int z2, float targetRadius, float maxSearchRange, IBlockAccess iblockaccess, int searchDepth, int quickFailDepth)
  {
    this.worldMap = iblockaccess;
    this.nodeLimit = searchDepth;
    this.nodesOpened = 1;
    this.searchRange = maxSearchRange;
    this.path.clearPath();
    this.pointMap.clearMap();
    PathNode start = openPoint(x, y, z);
    PathNode target = openPoint(x2, y2, z2);
    this.finalTarget = target;
    this.targetRadius = targetRadius;
    Path pathentity = addToPath(entity, start, target);

    return pathentity;
  }

  private Path addToPath(IPathfindable entity, PathNode start, PathNode target)
  {
    start.totalPathDistance = 0.0F;
    start.distanceToNext = start.distanceTo(target);
    start.distanceToTarget = start.distanceToNext;
    this.path.clearPath();
    this.path.addPoint(start);
    PathNode previousPoint = start;

    int loops = 0;

    long elapsed = 0L;
    while (!this.path.isPathEmpty())
    {
      if (this.nodesOpened > this.nodeLimit)
      {
        return createEntityPath(start, previousPoint);
      }
      PathNode examiningPoint = this.path.dequeue();
      float distanceToTarget = examiningPoint.distanceTo(target);
      if (distanceToTarget < this.targetRadius + 0.1F)
      {
        return createEntityPath(start, examiningPoint);
      }
      if (distanceToTarget < previousPoint.distanceTo(target))
      {
        previousPoint = examiningPoint;
      }
      examiningPoint.isFirst = true;

      int i = findPathOptions(entity, examiningPoint, target);

      int j = 0;
      while (j < i)
      {
        PathNode newPoint = this.pathOptions[j];

        float actualCost = examiningPoint.totalPathDistance + entity.getBlockPathCost(examiningPoint, newPoint, this.worldMap);

        if ((!newPoint.isAssigned()) || (actualCost < newPoint.totalPathDistance))
        {
          newPoint.setPrevious(examiningPoint);
          newPoint.totalPathDistance = actualCost;
          newPoint.distanceToNext = estimateDistance(newPoint, target);

          if (newPoint.isAssigned())
          {
            this.path.changeDistance(newPoint, newPoint.totalPathDistance + newPoint.distanceToNext);
          }
          else
          {
            newPoint.distanceToTarget = (newPoint.totalPathDistance + newPoint.distanceToNext);
            this.path.addPoint(newPoint);
          }
        }
        j++;
      }
    }

    if (previousPoint == start) {
      return null;
    }
    return createEntityPath(start, previousPoint);
  }

  public void addNode(int x, int y, int z, PathAction action)
  {
    PathNode node = openPoint(x, y, z, action);
    if ((node != null) && (!node.isFirst) && (node.distanceTo(this.finalTarget) < this.searchRange))
      this.pathOptions[(this.pathsIndex++)] = node;
  }

  private float estimateDistance(PathNode start, PathNode target)
  {
    return Math.abs(target.xCoord - start.xCoord) + Math.abs(target.yCoord - start.yCoord) + Math.abs(target.zCoord - start.zCoord) * 1.01F;
  }

  protected PathNode openPoint(int x, int y, int z)
  {
    return openPoint(x, y, z, PathAction.NONE);
  }

  protected PathNode openPoint(int x, int y, int z, PathAction action)
  {
    int hash = PathNode.makeHash(x, y, z, action);
    PathNode pathpoint = (PathNode)this.pointMap.lookup(hash);
    if (pathpoint == null)
    {
      pathpoint = new PathNode(x, y, z, action);
      this.pointMap.addKey(hash, pathpoint);
      this.nodesOpened += 1;
    }

    return pathpoint;
  }

  private int findPathOptions(IPathfindable entity, PathNode pathpoint, PathNode target)
  {
    this.pathsIndex = 0;
    entity.getPathOptionsFromNode(this.worldMap, pathpoint, this);
    return this.pathsIndex;
  }

  private Path createEntityPath(PathNode pathpoint, PathNode pathpoint1)
  {
    int i = 1;
    for (PathNode pathpoint2 = pathpoint1; pathpoint2.getPrevious() != null; pathpoint2 = pathpoint2.getPrevious())
    {
      i++;
    }

    PathNode[] apathpoint = new PathNode[i];
    PathNode pathpoint3 = pathpoint1;
    for (apathpoint[(--i)] = pathpoint3; pathpoint3.getPrevious() != null; apathpoint[(--i)] = pathpoint3)
    {
      pathpoint3 = pathpoint3.getPrevious();
    }

    return new Path(apathpoint, this.finalTarget);
  }
}