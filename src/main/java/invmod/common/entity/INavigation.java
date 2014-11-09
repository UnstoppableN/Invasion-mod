package invmod.common.entity;

import invmod.common.INotifyTask;
import net.minecraft.entity.Entity;

public abstract interface INavigation extends INotifyTask
{
  public abstract PathAction getCurrentWorkingAction();

  public abstract void setSpeed(float paramFloat);

  public abstract Path getPathToXYZ(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat);

  public abstract boolean tryMoveToXYZ(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

  public abstract Path getPathTowardsXZ(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean tryMoveTowardsXZ(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, int paramInt3, float paramFloat);

  public abstract Path getPathToEntity(Entity paramEntity, float paramFloat);

  public abstract boolean tryMoveToEntity(Entity paramEntity, float paramFloat1, float paramFloat2);

  public abstract void autoPathToEntity(Entity paramEntity);

  public abstract boolean setPath(Path paramPath, float paramFloat);

  public abstract boolean isWaitingForTask();

  public abstract Path getPath();

  public abstract void onUpdateNavigation();

  public abstract int getLastActionResult();

  public abstract boolean noPath();

  public abstract int getStuckTime();

  public abstract float getLastPathDistanceToTarget();

  public abstract void clearPath();

  public abstract void haltForTick();

  public abstract Entity getTargetEntity();

  public abstract String getStatus();
}