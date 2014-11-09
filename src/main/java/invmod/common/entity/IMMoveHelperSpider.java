package invmod.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class IMMoveHelperSpider extends IMMoveHelper
{
  public IMMoveHelperSpider(EntityIMLiving par1EntityLiving)
  {
    super(par1EntityLiving);
  }

  protected int getClimbFace(double x, double y, double z)
  {
    int mobX = MathHelper.floor_double(x - this.a.width / 2.0F);
    int mobY = MathHelper.floor_double(y);
    int mobZ = MathHelper.floor_double(z - this.a.width / 2.0F);

    int index = 0;
    Path path = this.a.getNavigatorNew().getPath();
    if ((path != null) && (!path.isFinished()))
    {
      PathNode currentPoint = path.getPathPointFromIndex(path.getCurrentPathIndex());
      int pathLength = path.getCurrentPathLength();
      for (int i = path.getCurrentPathIndex(); i < pathLength; i++)
      {
        PathNode point = path.getPathPointFromIndex(i);
        if (point.xCoord > currentPoint.xCoord)
        {
          break;
        }
        if (point.xCoord < currentPoint.xCoord)
        {
          index = 2;
          break;
        }
        if (point.zCoord > currentPoint.zCoord)
        {
          index = 4;
          break;
        }
        if (point.zCoord < currentPoint.zCoord)
        {
          index = 6;
          break;
        }
      }

    }

    for (int count = 0; count < 8; count++)
    {
    	//set bool to true, donno why
      if (this.a.worldObj.isBlockNormalCubeDefault(mobX + invmod.common.util.CoordsInt.offsetAdj2X[index], mobY, mobZ + invmod.common.util.CoordsInt.offsetAdj2Z[index],true)) {
        return index / 2;
      }
      index++; if (index > 7) {
        index = 0;
      }
    }
    return -1;
  }
}