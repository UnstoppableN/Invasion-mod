package invmod.common.entity;

import invmod.common.INotifyTask;

public abstract interface ITerrainModify
{
  public abstract boolean isReadyForTask(INotifyTask paramINotifyTask);

  public abstract boolean requestTask(ModifyBlockEntry[] paramArrayOfModifyBlockEntry, INotifyTask paramINotifyTask1, INotifyTask paramINotifyTask2);

  public abstract ModifyBlockEntry getLastBlockModified();
}