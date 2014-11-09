package invmod.common.entity;

import invmod.common.IPathfindable;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Scaffold
  implements IPathfindable, IPosition
{
  private static final int MIN_SCAFFOLD_HEIGHT = 4;
  private int xCoord;
  private int yCoord;
  private int zCoord;
  private int targetHeight;
  private int orientation;
  private int[] platforms;
  private IPathfindable pathfindBase;
  private INexusAccess nexus;
  private float latestPercentCompleted;
  private float latestPercentIntact;
  private float initialCompletion;

  public Scaffold(INexusAccess nexus)
  {
    this.nexus = nexus;
    this.initialCompletion = 0.01F;
    calcPlatforms();
  }

  public Scaffold(int x, int y, int z, int height, INexusAccess nexus)
  {
    this.xCoord = x;
    this.yCoord = y;
    this.zCoord = z;
    this.targetHeight = height;
    this.latestPercentCompleted = 0.0F;
    this.latestPercentIntact = 0.0F;
    this.initialCompletion = 0.01F;
    this.nexus = nexus;
    calcPlatforms();
  }

  public void setPosition(int x, int y, int z)
  {
    this.xCoord = x;
    this.yCoord = y;
    this.zCoord = z;
  }

  public void setInitialIntegrity()
  {
    this.initialCompletion = evaluateIntegrity();
    if (this.initialCompletion == 0.0F)
      this.initialCompletion = 0.01F;
  }

  public void setOrientation(int i)
  {
    this.orientation = i;
  }

  public int getOrientation()
  {
    return this.orientation;
  }

  public void setHeight(int height)
  {
    this.targetHeight = height;
    calcPlatforms();
  }

  public int getTargetHeight()
  {
    return this.targetHeight;
  }

  public void forceStatusUpdate()
  {
    this.latestPercentIntact = ((evaluateIntegrity() - this.initialCompletion) / (1.0F - this.initialCompletion));
    if (this.latestPercentIntact > this.latestPercentCompleted)
      this.latestPercentCompleted = this.latestPercentIntact;
  }

  public float getPercentIntactCached()
  {
    return this.latestPercentIntact;
  }

  public float getPercentCompletedCached()
  {
    return this.latestPercentCompleted;
  }

  public int getXCoord()
  {
    return this.xCoord;
  }

  public int getYCoord()
  {
    return this.yCoord;
  }

  public int getZCoord()
  {
    return this.zCoord;
  }

  public INexusAccess getNexus()
  {
    return this.nexus;
  }

  public void setPathfindBase(IPathfindable base)
  {
    this.pathfindBase = base;
  }

  public boolean isLayerPlatform(int height)
  {
    if (height == this.targetHeight - 1) {
      return true;
    }
    if (this.platforms != null)
    {
      for (int i : this.platforms)
      {
        if (i == height)
          return true;
      }
    }
    return false;
  }

  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    this.xCoord = nbttagcompound.getInteger("xCoord");
    this.yCoord = nbttagcompound.getInteger("yCoord");
    this.zCoord = nbttagcompound.getInteger("zCoord");
    this.targetHeight = nbttagcompound.getInteger("targetHeight");
    this.orientation = nbttagcompound.getInteger("orientation");
    this.initialCompletion = nbttagcompound.getFloat("initialCompletion");
    this.latestPercentCompleted = nbttagcompound.getFloat("latestPercentCompleted");
    calcPlatforms();
  }

  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setInteger("xCoord", this.xCoord);
    nbttagcompound.setInteger("yCoord", this.yCoord);
    nbttagcompound.setInteger("zCoord", this.zCoord);
    nbttagcompound.setInteger("targetHeight", this.targetHeight);
    nbttagcompound.setInteger("orientation", this.orientation);
    nbttagcompound.setFloat("initialCompletion", this.initialCompletion);
    nbttagcompound.setFloat("latestPercentCompleted", this.latestPercentCompleted);
  }

  private void calcPlatforms()
  {
    int spanningPlatforms = this.targetHeight < 16 ? this.targetHeight / 4 - 1 : this.targetHeight / 5 - 1;
    if (spanningPlatforms > 0)
    {
      int avgSpace = this.targetHeight / (spanningPlatforms + 1);
      int remainder = this.targetHeight % (spanningPlatforms + 1) - 1;
      this.platforms = new int[spanningPlatforms];
      for (int i = 0; i < spanningPlatforms; i++) {
        this.platforms[i] = (avgSpace * (i + 1) - 1);
      }

      int i = spanningPlatforms - 1;
      while (remainder > 0)
      {
        this.platforms[i] += 1;
        i--; if (i < 0)
        {
          i = spanningPlatforms - 1;
          remainder--;
        }
        remainder--;
      }
    }
    else
    {
      this.platforms = new int[0];
    }
  }

  private float evaluateIntegrity()
  {
    if (this.nexus != null)
    {
      int existingMainSectionBlocks = 0;
      int existingMainLadderBlocks = 0;
      int existingPlatformBlocks = 0;
      World world = this.nexus.getWorld();
      for (int i = 0; i < this.targetHeight; i++)
      {
    	  //set bool true, donno why
        if (world.isBlockNormalCubeDefault(this.xCoord + invmod.common.util.CoordsInt.offsetAdjX[this.orientation], this.yCoord + i, this.zCoord + invmod.common.util.CoordsInt.offsetAdjZ[this.orientation],true)) {
          existingMainSectionBlocks++;
        }
        if (world.getBlock(this.xCoord, this.yCoord + i, this.zCoord) == Blocks.ladder) {
          existingMainLadderBlocks++;
        }
        if (isLayerPlatform(i))
        {
          for (int j = 0; j < 8; j++)
          {
        	//set bool true, donno why
            if (world.isBlockNormalCubeDefault(this.xCoord + invmod.common.util.CoordsInt.offsetRing1X[j], this.yCoord + i, this.zCoord + invmod.common.util.CoordsInt.offsetRing1Z[j],true)) {
              existingPlatformBlocks++;
            }
          }
        }
      }

      float mainSectionPercent = this.targetHeight > 0 ? existingMainSectionBlocks / this.targetHeight : 0.0F;
      float ladderPercent = this.targetHeight > 0 ? existingMainLadderBlocks / this.targetHeight : 0.0F;

      return 0.7F * (0.7F * mainSectionPercent + 0.3F * ladderPercent) + 0.3F * (existingPlatformBlocks / ((this.platforms.length + 1) * 8));
    }
    return 0.0F;
  }

  public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap)
  {
    float materialMultiplier = terrainMap.getBlock(node.xCoord, node.yCoord, node.zCoord).getMaterial().isSolid() ? 2.2F : 1.0F;
    if (node.action == PathAction.SCAFFOLD_UP)
    {
      if (prevNode.action != PathAction.SCAFFOLD_UP) {
        materialMultiplier *= 3.4F;
      }
      return prevNode.distanceTo(node) * 0.85F * materialMultiplier;
    }
    if (node.action == PathAction.BRIDGE)
    {
      if (prevNode.action == PathAction.SCAFFOLD_UP) {
        materialMultiplier = 0.0F;
      }
      return prevNode.distanceTo(node) * 1.1F * materialMultiplier;
    }
    if ((node.action == PathAction.LADDER_UP_NX) || (node.action == PathAction.LADDER_UP_NZ) || (node.action == PathAction.LADDER_UP_PX) || (node.action == PathAction.LADDER_UP_PZ))
    {
      return prevNode.distanceTo(node) * 1.5F * materialMultiplier;
    }

    if (this.pathfindBase != null) {
      return this.pathfindBase.getBlockPathCost(prevNode, node, terrainMap);
    }
    return prevNode.distanceTo(node);
  }

  public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder)
  {
    if (this.pathfindBase != null) {
      this.pathfindBase.getPathOptionsFromNode(terrainMap, currentNode, pathFinder);
    }
    Block block = terrainMap.getBlock(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord);
    if ((currentNode.getPrevious() != null) && (currentNode.getPrevious().action == PathAction.SCAFFOLD_UP) && (!avoidsBlock(block)))
    {
      pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.SCAFFOLD_UP);
      return;
    }
    int minDistance;
    if (this.nexus != null)
    {
      List scaffolds = this.nexus.getAttackerAI().getScaffolds();
      minDistance = this.nexus.getAttackerAI().getMinDistanceBetweenScaffolds();
      for (int sl = scaffolds.size()-1; sl >= 0; sl--)
      {
        Scaffold scaffold = (Scaffold) scaffolds.get(sl);
        if (Distance.distanceBetween(scaffold, currentNode.xCoord, currentNode.yCoord, currentNode.zCoord) < minDistance) {
          return;
        }
      }
    }

    if ((block == Blocks.air) && (terrainMap.getBlock(currentNode.xCoord, currentNode.yCoord - 2, currentNode.zCoord).getMaterial().isSolid()))
    {
      boolean flag = false;
      for (int i = 1; i < 4; i++)
      {
        if (terrainMap.getBlock(currentNode.xCoord, currentNode.yCoord + i, currentNode.zCoord) != Blocks.air)
        {
          flag = true;
          break;
        }
      }

      if (!flag)
        pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.SCAFFOLD_UP);
    }
  }

  private boolean avoidsBlock(Block block)
  {
    if ((block == Blocks.fire) || (block == Blocks.bedrock) || (block == Blocks.wooden_door) || (block == Blocks.water) || (block == Blocks.flowing_water) || (block == Blocks.lava) || (block == Blocks.flowing_water))
    {
      return true;
    }

    return false;
  }
}
