package invmod.common.nexus;

import invmod.common.mod_Invasion;
import invmod.common.util.ISelect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WaveEntry
{
  private int timeBegin;
  private int timeEnd;
  private int amount;
  private int granularity;
  private int amountQueued;
  private int elapsed;
  private int toNextSpawn;
  private int minAngle;
  private int maxAngle;
  private int minPointsInRange;
  private int nextAlert;
  private ISelect<IEntityIMPattern> mobPool;
  private List<EntityConstruct> spawnList;
  private Map<Integer, String> alerts;

  public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool)
  {
    this(timeBegin, timeEnd, amount, granularity, mobPool, -180, 180, 1);
  }

  public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool, int angleRange, int minPointsInRange)
  {
    this(timeBegin, timeEnd, amount, granularity, mobPool, 0, 0, minPointsInRange);
    this.minAngle = (new Random().nextInt(360) - 180);
    this.maxAngle = (this.minAngle + angleRange);
    while (this.maxAngle > 180)
      this.maxAngle -= 360;
  }

  public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool, int minAngle, int maxAngle, int minPointsInRange)
  {
    this.spawnList = new ArrayList();
    this.alerts = new HashMap();
    this.timeBegin = timeBegin;
    this.timeEnd = timeEnd;
    this.amount = amount;
    this.granularity = granularity;
    this.mobPool = mobPool;
    this.minAngle = minAngle;
    this.maxAngle = maxAngle;
    this.minPointsInRange = minPointsInRange;
    this.amountQueued = 0;
    this.elapsed = 0;
    this.toNextSpawn = 0;
    this.nextAlert = 2147483647;
  }

  public int doNextSpawns(int elapsedMillis, ISpawnerAccess spawner)
  {
    this.toNextSpawn -= elapsedMillis;
    if (this.nextAlert <= this.elapsed - this.toNextSpawn)
    {
      sendNextAlert(spawner);
    }

    if (this.toNextSpawn <= 0)
    {
      this.elapsed += this.granularity;
      this.toNextSpawn += this.granularity;
      if (this.toNextSpawn < 0)
      {
        this.elapsed -= this.toNextSpawn;
        this.toNextSpawn = 0;
      }

      int amountToSpawn = Math.round(this.amount * this.elapsed / (this.timeEnd - this.timeBegin)) - this.amountQueued;
      if (amountToSpawn > 0)
      {
        if (amountToSpawn + this.amountQueued > this.amount) {
          amountToSpawn = this.amount - this.amountQueued;
        }
        while (amountToSpawn > 0)
        {
          IEntityIMPattern pattern = (IEntityIMPattern)this.mobPool.selectNext();
          if (pattern != null)
          {
            EntityConstruct mobConstruct = pattern.generateEntityConstruct(this.minAngle, this.maxAngle);
            if (mobConstruct != null)
            {
              amountToSpawn--;
              this.amountQueued += 1;
              this.spawnList.add(mobConstruct);
            }
          }
          else
          {
            mod_Invasion.log("A selection pool in wave entry " + toString() + " returned empty");
            mod_Invasion.log("Pool: " + this.mobPool.toString());
          }
        }
      }
    }

    if (this.spawnList.size() > 0)
    {
      int numberOfSpawns = 0;
      if (spawner.getNumberOfPointsInRange(this.minAngle, this.maxAngle, SpawnType.HUMANOID) >= this.minPointsInRange)
      {
        for (int i = this.spawnList.size() - 1; i >= 0; i--)
        {
          if (spawner.attemptSpawn((EntityConstruct)this.spawnList.get(i), this.minAngle, this.maxAngle))
          {
            numberOfSpawns++;
            this.spawnList.remove(i);
          }
        }
      }
      else
      {
        reviseSpawnAngles(spawner);
      }
      return numberOfSpawns;
    }
    return 0;
  }

  public void resetToBeginning()
  {
    this.elapsed = 0;
    this.amountQueued = 0;
    this.mobPool.reset();
  }

  public void setToTime(int millis)
  {
    this.elapsed = millis;
  }

  public int getTimeBegin()
  {
    return this.timeBegin;
  }

  public int getTimeEnd()
  {
    return this.timeEnd;
  }

  public int getAmount()
  {
    return this.amount;
  }

  public int getGranularity()
  {
    return this.granularity;
  }

  public void addAlert(String message, int timeElapsed)
  {
    this.alerts.put(Integer.valueOf(timeElapsed), message);
    if (timeElapsed < this.nextAlert)
      this.nextAlert = timeElapsed;
  }

  @Override
  public String toString()
  {
    return "WaveEntry@" + Integer.toHexString(hashCode()) + "#time=" + this.timeBegin + "-" + this.timeEnd + "#amount=" + this.amount;
  }

  private void sendNextAlert(ISpawnerAccess spawner)
  {
    String message = (String)this.alerts.remove(Integer.valueOf(this.nextAlert));
    if (message != null) {
      spawner.sendSpawnAlert(message);
    }
    this.nextAlert = 2147483647;
    if (this.alerts.size() > 0)
    {
      for (Integer key : this.alerts.keySet())
      {
        if (key.intValue() < this.nextAlert)
          this.nextAlert = key.intValue();
      }
    }
  }

  private void reviseSpawnAngles(ISpawnerAccess spawner)
  {
    int angleRange = this.maxAngle - this.minAngle;
    while (angleRange < 0)
      angleRange += 360;
    if (angleRange == 0) {
      angleRange = 360;
    }
    List validAngles = new ArrayList();

    for (int angle = -180; angle < 180; angle += angleRange)
    {
      int nextAngle = angle + angleRange;
      if (nextAngle >= 180)
        nextAngle -= 360;
      if (spawner.getNumberOfPointsInRange(angle, nextAngle, SpawnType.HUMANOID) >= this.minPointsInRange) {
        validAngles.add(Integer.valueOf(angle));
      }
    }
    if (validAngles.size() > 0)
    {
      this.minAngle = ((Integer)validAngles.get(new Random().nextInt(validAngles.size()))).intValue();
      this.maxAngle = (this.minAngle + angleRange);
      while (this.maxAngle >= 180) {
        this.maxAngle -= 360;
      }
    }

    if (this.minPointsInRange > 1)
    {
      mod_Invasion.log("Can't find a direction with enough spawn points: " + this.minPointsInRange + ". Lowering requirement.");

      this.minPointsInRange = 1;
    }
    else if (this.maxAngle - this.minAngle < 360)
    {
      mod_Invasion.log("Can't find a direction with enough spawn points: " + this.minPointsInRange + ". Switching to 360 degree mode for this entry");

      this.minAngle = -180;
      this.maxAngle = 180;
    }
    else
    {
      mod_Invasion.log("Wave entry cannot find a single spawn point");
      spawner.noSpawnPointNotice();
    }
  }
}