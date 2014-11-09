package invmod.common.nexus;

public abstract interface ISpawnerAccess
{
  public abstract boolean attemptSpawn(EntityConstruct paramEntityConstruct, int paramInt1, int paramInt2);

  public abstract int getNumberOfPointsInRange(int paramInt1, int paramInt2, SpawnType paramSpawnType);

  public abstract void sendSpawnAlert(String paramString);

  public abstract void noSpawnPointNotice();
}