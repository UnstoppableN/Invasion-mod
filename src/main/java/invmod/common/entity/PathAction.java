package invmod.common.entity;

public enum PathAction
{
  NONE, LADDER_UP, BRIDGE, SWIM, DIG, LADDER_UP_PX, LADDER_UP_NX, LADDER_UP_PZ, LADDER_UP_NZ, LADDER_TOWER_UP_PX, 
  LADDER_TOWER_UP_NX, LADDER_TOWER_UP_PZ, LADDER_TOWER_UP_NZ, SCAFFOLD_UP;

  public static final PathAction[] ladderTowerIndexOrient = { LADDER_TOWER_UP_PX, LADDER_TOWER_UP_NX, LADDER_TOWER_UP_PZ, LADDER_TOWER_UP_NZ };
  public static final PathAction[] ladderIndexOrient = { LADDER_UP_PX, LADDER_UP_NX, LADDER_UP_PZ, LADDER_UP_NZ };
}