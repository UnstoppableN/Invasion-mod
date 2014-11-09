package invmod.common.entity;

import net.minecraft.entity.Entity;

public abstract interface INavigationFlying extends INavigation
{
  public abstract void setMovementType(MoveType paramMoveType);

  public abstract void setLandingPath();

  public abstract void setCirclingPath(Entity paramEntity, float paramFloat1, float paramFloat2);

  public abstract void setCirclingPath(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

  public abstract float getDistanceToCirclingRadius();

  public abstract boolean isCircling();

  public abstract void setFlySpeed(float paramFloat);

  public abstract void setPitchBias(float paramFloat1, float paramFloat2);

  public abstract void enableDirectTarget(boolean paramBoolean);

  public static enum MoveType
  {
    PREFER_WALKING, MIXED, PREFER_FLYING;
  }
}