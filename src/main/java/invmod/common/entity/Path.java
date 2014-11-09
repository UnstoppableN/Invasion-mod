package invmod.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class Path
{
  protected final PathNode[] points;
  private PathNode intendedTarget;
  private int pathLength;
  private int pathIndex;
  private float totalCost;

  public Path(PathNode[] apathpoint)
  {
    this.points = apathpoint;
    this.pathLength = apathpoint.length;
    if (apathpoint.length > 0)
    {
      this.intendedTarget = apathpoint[(apathpoint.length - 1)];
    }
  }

  public Path(PathNode[] apathpoint, PathNode intendedTarget)
  {
    this.points = apathpoint;
    this.pathLength = apathpoint.length;
    this.intendedTarget = intendedTarget;
  }

  public float getTotalPathCost()
  {
    return this.points[(this.pathLength - 1)].totalPathDistance;
  }

  public void incrementPathIndex()
  {
    this.pathIndex += 1;
  }

  public boolean isFinished()
  {
    return this.pathIndex >= this.points.length;
  }

  public PathNode getFinalPathPoint()
  {
    if (this.pathLength > 0)
    {
      return this.points[(this.pathLength - 1)];
    }

    return null;
  }

  public PathNode getPathPointFromIndex(int par1)
  {
    return this.points[par1];
  }

  public int getCurrentPathLength()
  {
    return this.pathLength;
  }

  public void setCurrentPathLength(int par1)
  {
    this.pathLength = par1;
  }

  public int getCurrentPathIndex()
  {
    return this.pathIndex;
  }

  public void setCurrentPathIndex(int par1)
  {
    this.pathIndex = par1;
  }

  public PathNode getIntendedTarget()
  {
    return this.intendedTarget;
  }

  public Vec3 getPositionAtIndex(Entity entity, int index)
  {
    double d = this.points[index].xCoord + (int)(entity.width + 1.0F) * 0.5D;
    double d1 = this.points[index].yCoord;
    double d2 = this.points[index].zCoord + (int)(entity.width + 1.0F) * 0.5D;
    return Vec3.createVectorHelper(d, d1, d2);
  }

  public Vec3 getCurrentNodeVec3d(Entity entity)
  {
    return getPositionAtIndex(entity, this.pathIndex);
  }

  public Vec3 destination()
  {
    return Vec3.createVectorHelper(this.points[(this.points.length - 1)].xCoord, this.points[(this.points.length - 1)].yCoord, this.points[(this.points.length - 1)].zCoord);
  }

  public boolean equalsPath(Path par1PathEntity)
  {
    if (par1PathEntity == null)
    {
      return false;
    }

    if (par1PathEntity.points.length != this.points.length)
    {
      return false;
    }

    for (int i = 0; i < this.points.length; i++)
    {
      if ((this.points[i].xCoord != par1PathEntity.points[i].xCoord) || (this.points[i].yCoord != par1PathEntity.points[i].yCoord) || (this.points[i].zCoord != par1PathEntity.points[i].zCoord))
      {
        return false;
      }
    }

    return true;
  }

  public boolean isDestinationSame(Vec3 par1Vec3D)
  {
    PathNode pathpoint = getFinalPathPoint();

    if (pathpoint == null)
    {
      return false;
    }

    return (pathpoint.xCoord == (int)par1Vec3D.xCoord) && (pathpoint.zCoord == (int)par1Vec3D.zCoord);
  }
}