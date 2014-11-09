package invmod.common.nexus;

public class WaveBuilderNormal
  implements IWaveSource
{
  public Wave getWave()
  {
    int difficulty = 0;
    int lengthSeconds = 0;

    float basicMobsPerSecond = 0.12F * difficulty;
    int numberOfGroups = 7;
    int numberOfBigGroups = 1;
    float proportionInGroups = 0.5F;
    int mobsPerGroup = Math.round(proportionInGroups * basicMobsPerSecond * lengthSeconds / (numberOfGroups + numberOfBigGroups * 2));
    int mobsPerBigGroup = mobsPerGroup * 2;
    int remainingMobs = (int)(basicMobsPerSecond * lengthSeconds) - mobsPerGroup * numberOfGroups - mobsPerBigGroup * numberOfBigGroups;
    int mobsPerSteady = Math.round(0.7F * remainingMobs / numberOfGroups);
    int extraMobsForFinale = Math.round(0.3F * remainingMobs);
    int extraMobsForCleanup = (int)(basicMobsPerSecond * lengthSeconds * 0.2F);
    float timeForGroups = 0.5F;
    int groupTimeInterval = (int)(lengthSeconds * 1000 * timeForGroups / (numberOfGroups + numberOfBigGroups * 3));
    int steadyTimeInterval = (int)(lengthSeconds * 1000 * (1.0F - timeForGroups) / numberOfGroups);

    return null;
  }
}