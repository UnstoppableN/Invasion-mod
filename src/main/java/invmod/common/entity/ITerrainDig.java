package invmod.common.entity;

import invmod.common.INotifyTask;

public abstract interface ITerrainDig
{
  public abstract boolean askRemoveBlock(int paramInt1, int paramInt2, int paramInt3, INotifyTask paramINotifyTask, float paramFloat);

  public abstract boolean askClearPosition(int paramInt1, int paramInt2, int paramInt3, INotifyTask paramINotifyTask, float paramFloat);
}