package invmod.common;

import invmod.Invasion;
import invmod.common.nexus.*;

import java.util.Random;

public class Tester {
    Random rand;

    public Tester() {
        this.rand = new Random();
    }

    public void doWaveBuilderTest(float difficulty, float tierLevel, int lengthSeconds) {
        Invasion.log("Doing wave builder test. Difficulty: " + difficulty + ", tier: " + tierLevel + ", length: " + lengthSeconds + " seconds");
        Invasion.log("Generating dummy nexus and fake spawn points...");
        DummyNexus nexus = new DummyNexus();
        SpawnPointContainer spawnPoints = new SpawnPointContainer();
        for (int i = -170; i < -100; i += 3) {
            spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
        }
        for (int i = 90; i < 180; i += 3) {
            spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
        }
        Invasion.log("Setting radius to 45");
        IMWaveSpawner spawner = new IMWaveSpawner(nexus, 45);
        spawner.giveSpawnPoints(spawnPoints);
        spawner.debugMode(true);
        spawner.setSpawnMode(false);

        IMWaveBuilder waveBuilder = new IMWaveBuilder();
        Wave wave = waveBuilder.generateWave(difficulty, tierLevel, lengthSeconds);

        int successfulSpawns = 0;
        int definedSpawns = 0;
        try {
            spawner.beginNextWave(wave);
            Invasion.log("Starting wave.Wave duration: " + spawner.getWaveDuration());
            while (!spawner.isWaveComplete()) {
                spawner.spawn(100);
            }
            Invasion.log("Wave finished spawning. Wave rest time: " + spawner.getWaveRestTime());
            successfulSpawns += spawner.getSuccessfulSpawnsThisWave();
            definedSpawns += spawner.getTotalDefinedMobsThisWave();
        } catch (WaveSpawnerException e) {
            Invasion.log(e.getMessage());
        } catch (Exception e) {
            Invasion.log(e.getMessage());
            e.printStackTrace();
        }

        Invasion.log("Successful spawns for wave: " + spawner.getSuccessfulSpawnsThisWave());
        Invasion.log("Test finished. Total successful spawns: " + successfulSpawns + "  Total defined spawns: " + definedSpawns);
    }

    public void doWaveSpawnerTest(int startWave, int endWave) {
        Invasion.log("Doing wave spawner test. Start wave: " + startWave + "  End wave: " + endWave);
        Invasion.log("Generating dummy nexus and fake spawn points...");
        DummyNexus nexus = new DummyNexus();
        SpawnPointContainer spawnPoints = new SpawnPointContainer();
        for (int i = -170; i < -100; i += 3) {
            spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
        }
        for (int i = 90; i < 180; i += 3) {
            spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
        }
        Invasion.log("Setting radius to 45");
        IMWaveSpawner spawner = new IMWaveSpawner(nexus, 45);
        spawner.giveSpawnPoints(spawnPoints);
        spawner.debugMode(true);
        spawner.setSpawnMode(false);

        int successfulSpawns = 0;
        int definedSpawns = 0;
        for (; startWave <= endWave; startWave++) {
            try {
                spawner.beginNextWave(startWave);
                Invasion.log("Starting wave " + startWave + ". Wave duration: " + spawner.getWaveDuration());
                while (!spawner.isWaveComplete()) {
                    spawner.spawn(100);
                }
                Invasion.log("Wave finished spawning. Wave rest time: " + spawner.getWaveRestTime());
                successfulSpawns += spawner.getSuccessfulSpawnsThisWave();
                definedSpawns += spawner.getTotalDefinedMobsThisWave();
            } catch (WaveSpawnerException e) {
                Invasion.log(e.getMessage());
            }
        }

        Invasion.log("Successful spawns last wave: " + spawner.getSuccessfulSpawnsThisWave());
        Invasion.log("Test finished. Total successful spawns: " + successfulSpawns + "  Total defined spawns: " + definedSpawns);
    }

    public void doSpawnPointSelectionTest() {
        Invasion.log("Doing SpawnPointContainer test");
        Invasion.log("Filling with spawn points...");
        SpawnPointContainer spawnPoints = new SpawnPointContainer();
        for (int i = -180; i < 180; i += this.rand.nextInt(3)) {
            spawnPoints.addSpawnPointXZ(new SpawnPoint(i, 0, 0, i, SpawnType.HUMANOID));
        }
        Invasion.log(spawnPoints.getNumberOfSpawnPoints(SpawnType.HUMANOID) + " random points in container");

        Invasion.log("Cycling through ranges... format: min <= x < max");
        for (int i = -180; i < 180; i += 25) {
            int i2 = i + 40;
            if (i2 >= 180)
                i2 -= 360;
            Invasion.log(i + " to " + i2);
            for (int j = 0; j < 4; j++) {
                SpawnPoint point = spawnPoints.getRandomSpawnPoint(SpawnType.HUMANOID, i, i2);
                if (point != null) {
                    Invasion.log(point.toString());
                }
            }
        }
        Invasion.log("Beginning random stress test");

        int count = 0;
        int count2 = 0;
        for (int i = 0; i < 1105000; i++) {
            int r = this.rand.nextInt(361) - 180;
            int r2 = this.rand.nextInt(361) - 180;
            for (int j = 0; j < 17; j++) {
                count++;
                SpawnPoint point = spawnPoints.getRandomSpawnPoint(SpawnType.HUMANOID, r, r2);
                if (point != null) {
                    if (r < r2) {
                        if ((point.getAngle() < r) || (point.getAngle() >= r2)) {
                            count2++;
                            Invasion.log(point.toString() + " with specified: " + r + ", " + r2);
                        }

                    } else if ((point.getAngle() >= r) && (point.getAngle() < r2)) {
                        count2++;
                        Invasion.log(point.toString() + " with specified: " + r + ", " + r2);
                    }
                }
            }

        }

        Invasion.log("Tested " + count + " random spawn point retrievals. " + count2 + " results out of bounds.");

        Invasion.log("Finished test.");
    }
}