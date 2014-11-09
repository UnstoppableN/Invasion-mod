package invmod.common.nexus;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot
{
  public SlotOutput(IInventory iinventory, int i, int j, int k)
  {
    super(iinventory, i, j, k);
  }

  public boolean isItemValid(ItemStack itemstack)
  {
    return false;
  }
}