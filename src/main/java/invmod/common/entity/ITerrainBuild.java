package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.util.IPosition;

public abstract interface ITerrainBuild
{
  public abstract boolean askBuildScaffoldLayer(IPosition paramIPosition, INotifyTask paramINotifyTask);

  public abstract boolean askBuildLadderTower(IPosition paramIPosition, int paramInt1, int paramInt2, INotifyTask paramINotifyTask);

  public abstract boolean askBuildLadder(IPosition paramIPosition, INotifyTask paramINotifyTask);

  public abstract boolean askBuildBridge(IPosition paramIPosition, INotifyTask paramINotifyTask);
}