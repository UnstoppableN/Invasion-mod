package invmod.common.entity;

import invmod.common.IPathfindable;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;

public abstract interface IPathSource
{
  public abstract Path createPath(IPathfindable paramIPathfindable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, float paramFloat1, float paramFloat2, IBlockAccess paramIBlockAccess);

  public abstract Path createPath(EntityIMLiving paramEntityIMLiving, Entity paramEntity, float paramFloat1, float paramFloat2, IBlockAccess paramIBlockAccess);

  public abstract Path createPath(EntityIMLiving paramEntityIMLiving, int paramInt1, int paramInt2, int paramInt3, float paramFloat1, float paramFloat2, IBlockAccess paramIBlockAccess);

  public abstract void createPath(IPathResult paramIPathResult, IPathfindable paramIPathfindable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, float paramFloat, IBlockAccess paramIBlockAccess);

  public abstract void createPath(IPathResult paramIPathResult, EntityIMLiving paramEntityIMLiving, Entity paramEntity, float paramFloat, IBlockAccess paramIBlockAccess);

  public abstract void createPath(IPathResult paramIPathResult, EntityIMLiving paramEntityIMLiving, int paramInt1, int paramInt2, int paramInt3, float paramFloat, IBlockAccess paramIBlockAccess);

  public abstract int getSearchDepth();

  public abstract int getQuickFailDepth();

  public abstract void setSearchDepth(int paramInt);

  public abstract void setQuickFailDepth(int paramInt);

  public abstract boolean canPathfindNice(PathPriority paramPathPriority, float paramFloat, int paramInt1, int paramInt2);

  public static enum PathPriority
  {
    LOW, MEDIUM, HIGH;
  }
}