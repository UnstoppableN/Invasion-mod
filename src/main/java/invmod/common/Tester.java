package invmod.common;

import invmod.common.nexus.DummyNexus;
import invmod.common.nexus.IMWaveBuilder;
import invmod.common.nexus.IMWaveSpawner;
import invmod.common.nexus.SpawnPoint;
import invmod.common.nexus.SpawnPointContainer;
import invmod.common.nexus.SpawnType;
import invmod.common.nexus.Wave;
import invmod.common.nexus.WaveSpawnerException;
import java.util.Random;

public class Tester
{
  Random rand;

  public Tester()
  {
    this.rand = new Random();
  }

  public void doWaveBuilderTest(float difficulty, float tierLevel, int lengthSeconds)
  {
    mod_Invasion.log("Doing wave builder test. Difficulty: " + difficulty + ", tier: " + tierLevel + ", length: " + lengthSeconds + " seconds");
    mod_Invasion.log("Generating dummy nexus and fake spawn points...");
    DummyNexus nexus = new DummyNexus();
    SpawnPointContainer spawnPoints = new SpawnPointContainer();
    for (int i = -170; i < -100; i += 3) {
      spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
    }
    for (int i = 90; i < 180; i += 3) {
      spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
    }
    mod_Invasion.log("Setting radius to 45");
    IMWaveSpawner spawner = new IMWaveSpawner(nexus, 45);
    spawner.giveSpawnPoints(spawnPoints);
    spawner.debugMode(true);
    spawner.setSpawnMode(false);

    IMWaveBuilder waveBuilder = new IMWaveBuilder();
    Wave wave = waveBuilder.generateWave(difficulty, tierLevel, lengthSeconds);

    int successfulSpawns = 0;
    int definedSpawns = 0;
    try
    {
      spawner.beginNextWave(wave);
      mod_Invasion.log("Starting wave.Wave duration: " + spawner.getWaveDuration());
      while (!spawner.isWaveComplete())
      {
        spawner.spawn(100);
      }
      mod_Invasion.log("Wave finished spawning. Wave rest time: " + spawner.getWaveRestTime());
      successfulSpawns += spawner.getSuccessfulSpawnsThisWave();
      definedSpawns += spawner.getTotalDefinedMobsThisWave();
    }
    catch (WaveSpawnerException e)
    {
      mod_Invasion.log(e.getMessage());
    }
    catch (Exception e)
    {
      mod_Invasion.log(e.getMessage());
      e.printStackTrace();
    }

    mod_Invasion.log("Successful spawns for wave: " + spawner.getSuccessfulSpawnsThisWave());
    mod_Invasion.log("Test finished. Total successful spawns: " + successfulSpawns + "  Total defined spawns: " + definedSpawns);
  }

  public void doWaveSpawnerTest(int startWave, int endWave)
  {
    mod_Invasion.log("Doing wave spawner test. Start wave: " + startWave + "  End wave: " + endWave);
    mod_Invasion.log("Generating dummy nexus and fake spawn points...");
    DummyNexus nexus = new DummyNexus();
    SpawnPointContainer spawnPoints = new SpawnPointContainer();
    for (int i = -170; i < -100; i += 3) {
      spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
    }
    for (int i = 90; i < 180; i += 3) {
      spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
    }
    mod_Invasion.log("Setting radius to 45");
    IMWaveSpawner spawner = new IMWaveSpawner(nexus, 45);
    spawner.giveSpawnPoints(spawnPoints);
    spawner.debugMode(true);
    spawner.setSpawnMode(false);

    int successfulSpawns = 0;
    int definedSpawns = 0;
    for (; startWave <= endWave; startWave++)
    {
      try
      {
        spawner.beginNextWave(startWave);
        mod_Invasion.log("Starting wave " + startWave + ". Wave duration: " + spawner.getWaveDuration());
        while (!spawner.isWaveComplete())
        {
          spawner.spawn(100);
        }
        mod_Invasion.log("Wave finished spawning. Wave rest time: " + spawner.getWaveRestTime());
        successfulSpawns += spawner.getSuccessfulSpawnsThisWave();
        definedSpawns += spawner.getTotalDefinedMobsThisWave();
      }
      catch (WaveSpawnerException e)
      {
        mod_Invasion.log(e.getMessage());
      }
    }

    mod_Invasion.log("Successful spawns last wave: " + spawner.getSuccessfulSpawnsThisWave());
    mod_Invasion.log("Test finished. Total successful spawns: " + successfulSpawns + "  Total defined spawns: " + definedSpawns);
  }

  public void doSpawnPointSelectionTest()
  {
    mod_Invasion.log("Doing SpawnPointContainer test");
    mod_Invasion.log("Filling with spawn points...");
    SpawnPointContainer spawnPoints = new SpawnPointContainer();
    for (int i = -180; i < 180; i += this.rand.nextInt(3)) {
      spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
    }
    mod_Invasion.log(spawnPoints.getNumberOfSpawnPoints(SpawnType.HUMANOID) + " random points in container");

    mod_Invasion.log("Cycling through ranges... format: min <= x < max");
    for (int i = -180; i < 180; i += 25)
    {
      int i2 = i + 40;
      if (i2 >= 180)
        i2 -= 360;
      mod_Invasion.log(i + " to " + i2);
      for (int j = 0; j < 4; j++)
      {
        SpawnPoint point = spawnPoints.getRandomSpawnPoint(SpawnType.HUMANOID, i, i2);
        if (point != null) {
          mod_Invasion.log(point.toString());
        }
      }
    }
    mod_Invasion.log("Beginning random stress test");

    int count = 0;
    int count2 = 0;
    for (int i = 0; i < 1105000; i++)
    {
      int r = this.rand.nextInt(361) - 180;
      int r2 = this.rand.nextInt(361) - 180;
      for (int j = 0; j < 17; j++)
      {
        count++;
        SpawnPoint point = spawnPoints.getRandomSpawnPoint(SpawnType.HUMANOID, r, r2);
        if (point != null)
        {
          if (r < r2)
          {
            if ((point.getAngle() < r) || (point.getAngle() >= r2))
            {
              count2++;
              mod_Invasion.log(point.toString() + " with specified: " + r + ", " + r2);
            }

          }
          else if ((point.getAngle() >= r) && (point.getAngle() < r2))
          {
            count2++;
            mod_Invasion.log(point.toString() + " with specified: " + r + ", " + r2);
          }
        }
      }

    }

    mod_Invasion.log("Tested " + count + " random spawn point retrievals. " + count2 + " results out of bounds.");

    mod_Invasion.log("Finished test.");
  }
}