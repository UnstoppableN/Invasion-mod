package invmod.common.entity;

import net.minecraft.block.Block;
import invmod.common.util.IPosition;

public class ModifyBlockEntry implements IPosition
{
  private int xCoord;
  private int yCoord;
  private int zCoord;
  private Block oldBlock;
  private Block newBlock;
  private int newBlockMeta;
  private int cost;

  public ModifyBlockEntry(int x, int y, int z, Block block)
  {
    this(x, y, z, block, 0, 0, null);
  }

  public ModifyBlockEntry(int x, int y, int z, Block planks, int cost)
  {
    this(x, y, z, planks, cost, 0, null);
  }

  public ModifyBlockEntry(int x, int y, int z, Block block, int cost, int newBlockMeta, Block oldBlock)
  {
    this.xCoord = x;
    this.yCoord = y;
    this.zCoord = z;
    this.newBlock= block;
    this.cost = cost;
    this.newBlockMeta = newBlockMeta;
    this.oldBlock = oldBlock;
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

  public Block getNewBlock()
  {
    return this.newBlock;
  }

  public int getNewBlockMeta()
  {
    return this.newBlockMeta;
  }

  public int getCost()
  {
    return this.cost;
  }

  public Block getOldBlock()
  {
    return this.oldBlock;
  }

  public void setOldBlock(Block block)
  {
    this.oldBlock = block;
  }
}