package invmod.common.nexus;

import java.util.List;

public class Wave
{
  private List<WaveEntry> entries;
  private int elapsed;
  private int waveTotalTime;
  private int waveBreakTime;

  public Wave(int waveTotalTime, int waveBreakTime, List<WaveEntry> entries)
  {
    this.entries = entries;
    this.waveTotalTime = waveTotalTime;
    this.waveBreakTime = waveBreakTime;
    this.elapsed = 0;
  }

  public void addEntry(WaveEntry entry)
  {
    this.entries.add(entry);
  }

  public int doNextSpawns(int elapsedMillis, ISpawnerAccess spawner)
  {
    int numberOfSpawns = 0;
    this.elapsed += elapsedMillis;
    for (WaveEntry entry : this.entries)
    {
      if ((this.elapsed >= entry.getTimeBegin()) && (this.elapsed < entry.getTimeEnd()))
      {
        numberOfSpawns += entry.doNextSpawns(elapsedMillis, spawner);
      }
    }
    return numberOfSpawns;
  }

  public int getTimeInWave()
  {
    return this.elapsed;
  }

  public int getWaveTotalTime()
  {
    return this.waveTotalTime;
  }

  public int getWaveBreakTime()
  {
    return this.waveBreakTime;
  }

  public boolean isComplete()
  {
    return this.elapsed > this.waveTotalTime;
  }

  public void resetWave()
  {
    this.elapsed = 0;
    for (WaveEntry entry : this.entries)
    {
      entry.resetToBeginning();
    }
  }

  public void setWaveToTime(int millis)
  {
    this.elapsed = millis;
  }

  public int getTotalMobAmount()
  {
    int total = 0;
    for (WaveEntry entry : this.entries)
    {
      total += entry.getAmount();
    }
    return total;
  }
}