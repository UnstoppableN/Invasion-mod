package invmod.common;

import net.minecraft.entity.Entity;

public abstract interface SparrowAPI
{
  public abstract boolean isStupidToAttack();

  public abstract boolean doNotVaporize();

  public abstract boolean isPredator();

  public abstract boolean isHostile();

  public abstract boolean isPeaceful();

  public abstract boolean isPrey();

  public abstract boolean isNeutral();

  public abstract boolean isUnkillable();

  public abstract boolean isThreatTo(Entity paramEntity);

  public abstract boolean isFriendOf(Entity paramEntity);

  public abstract boolean isNPC();

  public abstract int isPet();

  public abstract Entity getPetOwner();

  public abstract String getName();

  public abstract Entity getAttackingTarget();

  public abstract float getSize();

  public abstract String getSpecies();

  public abstract int getTier();

  public abstract int getGender();

  public abstract String customStringAndResponse(String paramString);

  public abstract String getSimplyID();
}