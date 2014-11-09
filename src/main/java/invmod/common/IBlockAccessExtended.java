package invmod.common;

import net.minecraft.world.IBlockAccess;

public abstract interface IBlockAccessExtended extends IBlockAccess
{
  public abstract int getLayeredData(int paramInt1, int paramInt2, int paramInt3);

  public abstract void setData(int paramInt1, int paramInt2, int paramInt3, Integer paramInteger);
}