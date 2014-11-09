package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.IPosition;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class TerrainBuilder
  implements ITerrainBuild
{
  private static final float LADDER_COST = 25.0F;
  private static final float PLANKS_COST = 45.0F;
  private static final float COBBLE_COST = 65.0F;
  private EntityIMLiving theEntity;
  private ITerrainModify modifier;
  private float buildRate;

  public TerrainBuilder(EntityIMLiving entity, ITerrainModify modifier, float buildRate)
  {
    this.theEntity = entity;
    this.modifier = modifier;
    this.buildRate = buildRate;
  }

  public void setBuildRate(float buildRate)
  {
    this.buildRate = buildRate;
  }

  public float getBuildRate()
  {
    return this.buildRate;
  }

  public boolean askBuildScaffoldLayer(IPosition pos, INotifyTask asker)
  {
    if (this.modifier.isReadyForTask(asker))
    {
      Scaffold scaffold = this.theEntity.getNexus().getAttackerAI().getScaffoldAt(pos);
      if (scaffold != null)
      {
        int height = pos.getYCoord() - scaffold.getYCoord();
        int xOffset = invmod.common.util.CoordsInt.offsetAdjX[scaffold.getOrientation()];
        int zOffset = invmod.common.util.CoordsInt.offsetAdjZ[scaffold.getOrientation()];
        Block block = this.theEntity.worldObj.getBlock(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset);
        List modList = new ArrayList();

        if (height == 1)
        {
          if (!block.isNormalCube()) {
            modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset, Blocks.planks, (int)(45.0F / this.buildRate)));
          }
          block = this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord());
          if (block == Blocks.air) {
            modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.ladder, (int)(25.0F / this.buildRate)));
          }
        }
        block = this.theEntity.worldObj.getBlock(pos.getXCoord() + xOffset, pos.getYCoord(), pos.getZCoord() + zOffset);
        if (!block.isNormalCube()) {
          modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord(), pos.getZCoord() + zOffset, Blocks.planks, (int)(45.0F / this.buildRate)));
        }
        block = this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
        if (block != Blocks.ladder) {
          modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), Blocks.ladder, (int)(25.0F / this.buildRate)));
        }

        if (scaffold.isLayerPlatform(height))
        {
          for (int i = 0; i < 8; i++)
          {
            if ((invmod.common.util.CoordsInt.offsetRing1X[i] != xOffset) || (invmod.common.util.CoordsInt.offsetRing1Z[i] != zOffset))
            {
            	block = this.theEntity.worldObj.getBlock(pos.getXCoord() + invmod.common.util.CoordsInt.offsetRing1X[i], pos.getYCoord(), pos.getZCoord() + invmod.common.util.CoordsInt.offsetRing1Z[i]);
              if (!block.isNormalCube())
                modList.add(new ModifyBlockEntry(pos.getXCoord() + invmod.common.util.CoordsInt.offsetRing1X[i], pos.getYCoord(), pos.getZCoord() + invmod.common.util.CoordsInt.offsetRing1Z[i], Blocks.planks, (int)(45.0F / this.buildRate)));
            }
          }
        }
        if (modList.size() > 0)
          return this.modifier.requestTask((ModifyBlockEntry[])modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
      }
    }
    return false;
  }

  public boolean askBuildLadderTower(IPosition pos, int orientation, int layersToBuild, INotifyTask asker)
  {
    if (this.modifier.isReadyForTask(asker))
    {
      int xOffset = orientation == 1 ? -1 : orientation == 0 ? 1 : 0;
      int zOffset = orientation == 3 ? -1 : orientation == 2 ? 1 : 0;
      List modList = new ArrayList();

      Block block = this.theEntity.worldObj.getBlock(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset);
      if (!block.isNormalCube()) {
        modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset, Blocks.planks, (int)(45.0F / this.buildRate)));
      }
      block = this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord());
      if (block == Blocks.air) {
        modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.ladder, (int)(25.0F / this.buildRate)));
      }
      for (int i = 0; i < layersToBuild; i++)
      {
    	  block = this.theEntity.worldObj.getBlock(pos.getXCoord() + xOffset, pos.getYCoord() + i, pos.getZCoord() + zOffset);
        if (!block.isNormalCube()) {
          modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord() + i, pos.getZCoord() + zOffset, Blocks.planks, (int)(45.0F / this.buildRate)));
        }
        block = this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() + i, pos.getZCoord());
        if (block != Blocks.ladder) {
          modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() + i, pos.getZCoord(), Blocks.ladder, (int)(25.0F / this.buildRate)));
        }
      }
      if (modList.size() > 0)
        return this.modifier.requestTask((ModifyBlockEntry[])modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
    }
    return false;
  }

  public boolean askBuildLadder(IPosition pos, INotifyTask asker)
  {
    if (this.modifier.isReadyForTask(asker))
    {
      List modList = new ArrayList();
      Block block = this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
      if (block != Blocks.ladder)
      {
        if (EntityIMPigEngy.canPlaceLadderAt(this.theEntity.worldObj, pos.getXCoord(), pos.getYCoord(), pos.getZCoord()))
          modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), Blocks.ladder, (int)(25.0F / this.buildRate)));
        else {
          return false;
        }
      }

      block = this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() - 2, pos.getZCoord());
      if ((block !=Blocks.air) && (block.getMaterial().isSolid()))
      {
        if (EntityIMPigEngy.canPlaceLadderAt(this.theEntity.worldObj, pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord())) {
          modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.ladder, (int)(25.0F / this.buildRate)));
        }
      }
      if (modList.size() > 0)
        return this.modifier.requestTask((ModifyBlockEntry[])modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
    }
    return false;
  }

  public boolean askBuildBridge(IPosition pos, INotifyTask asker)
  {
    if (this.modifier.isReadyForTask(asker))
    {
      List modList = new ArrayList();
      if (this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord()) == Blocks.air)
      {
        if ((this.theEntity.avoidsBlock(this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() - 2, pos.getZCoord()))) || (this.theEntity.avoidsBlock(this.theEntity.worldObj.getBlock(pos.getXCoord(), pos.getYCoord() - 3, pos.getZCoord()))))
        {
          modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.cobblestone, (int)(65.0F / this.buildRate)));
        }
        else
        {
          modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.planks, (int)(45.0F / this.buildRate)));
        }

        if (modList.size() > 0)
          return this.modifier.requestTask((ModifyBlockEntry[])modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
      }
    }
    return false;
  }
}