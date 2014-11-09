package invmod.common.util;

import java.util.Comparator;

public class ComparatorDistanceFrom
  implements Comparator<IPosition>
{
  private double x;
  private double y;
  private double z;

  public ComparatorDistanceFrom(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int compare(IPosition pos1, IPosition pos2)
  {
    double d1 = (this.x - pos1.getXCoord()) * (this.x - pos1.getXCoord()) + (this.y - pos1.getYCoord()) * (this.y - pos1.getYCoord()) + (this.z - pos1.getZCoord()) * (this.z - pos1.getZCoord());
    double d2 = (this.x - pos2.getXCoord()) * (this.x - pos2.getXCoord()) + (this.y - pos2.getYCoord()) * (this.y - pos2.getYCoord()) + (this.z - pos2.getZCoord()) * (this.z - pos2.getZCoord());
    if (d1 > d2)
      return -1;
    if (d1 < d2) {
      return 1;
    }
    return 0;
  }
}