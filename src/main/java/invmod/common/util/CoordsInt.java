package invmod.common.util;

public class CoordsInt
  implements IPosition
{
  public static final int[] offsetAdjX = { 1, -1, 0, 0 };
  public static final int[] offsetAdjZ = { 0, 0, 1, -1 };

  public static final int[] offsetAdj2X = { 2, 2, -1, -1, 1, 0, 0, 1 };
  public static final int[] offsetAdj2Z = { 0, 1, 1, 0, 2, 2, -1, -1 };

  public static final int[] offsetRing1X = { 1, 0, -1, -1, -1, 0, 1, 1 };
  public static final int[] offsetRing1Z = { 1, 1, 1, 0, -1, -1, -1, 0 };
  private int x;
  private int y;
  private int z;

  public CoordsInt(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int getXCoord()
  {
    return this.x;
  }

  public int getYCoord()
  {
    return this.y;
  }

  public int getZCoord()
  {
    return this.z;
  }
}