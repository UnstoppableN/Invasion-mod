package invmod.common.entity;

import invmod.common.util.IPosition;
import net.minecraft.util.MathHelper;

public class PathNode
  implements IPosition
{
  public final int xCoord;
  public final int yCoord;
  public final int zCoord;
  public final PathAction action;
  private final int hash;
  int index;
  float totalPathDistance;
  float distanceToNext;
  float distanceToTarget;
  private PathNode previous;
  public boolean isFirst;

  public PathNode(int i, int j, int k)
  {
    this(i, j, k, PathAction.NONE);
  }

  public PathNode(int i, int j, int k, PathAction pathAction)
  {
    this.index = -1;
    this.isFirst = false;
    this.xCoord = i;
    this.yCoord = j;
    this.zCoord = k;
    this.action = pathAction;
    this.hash = makeHash(i, j, k, this.action);
  }

  public static int makeHash(int x, int y, int z, PathAction action)
  {
    return y & 0xFF | (x & 0xFF) << 8 | (z & 0xFF) << 16 | (action.ordinal() & 0xFF) << 24;
  }

  public float distanceTo(PathNode pathpoint)
  {
    float f = pathpoint.xCoord - this.xCoord;
    float f1 = pathpoint.yCoord - this.yCoord;
    float f2 = pathpoint.zCoord - this.zCoord;
    return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
  }

  public float distanceTo(float x, float y, float z)
  {
    float f = x - this.xCoord;
    float f1 = y - this.yCoord;
    float f2 = z - this.zCoord;
    return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
  }

  public boolean equals(Object obj)
  {
    if ((obj instanceof PathNode))
    {
      PathNode node = (PathNode)obj;
      return (this.hash == node.hash) && (this.xCoord == node.xCoord) && (this.yCoord == node.yCoord) && (this.zCoord == node.zCoord) && (node.action == this.action);
    }

    return false;
  }

  public boolean equals(int x, int y, int z)
  {
    return (this.xCoord == x) && (this.yCoord == y) && (this.zCoord == z);
  }

  public int hashCode()
  {
    return this.hash;
  }

  public boolean isAssigned()
  {
    return this.index >= 0;
  }

  public int getXCoord()
  {
    return this.xCoord;
  }

  public int getYCoord()
  {
    return this.yCoord;
  }

  public int getZCoord()
  {
    return this.zCoord;
  }

  public String toString()
  {
    return this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ", " + this.action;
  }

public PathNode getPrevious() {
	return previous;
}

public void setPrevious(PathNode previous) {
	this.previous = previous;
}
}