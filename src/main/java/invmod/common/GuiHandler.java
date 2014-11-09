package invmod.common;

import cpw.mods.fml.common.network.IGuiHandler;
import invmod.common.nexus.ContainerNexus;
import invmod.common.nexus.GuiNexus;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler
  implements IGuiHandler
{
	@Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
  {
    if (id == mod_Invasion.getGuiIdNexus())
    {
      TileEntityNexus nexus = (TileEntityNexus)world.getTileEntity(x, y, z);
      if (nexus != null) {
        return new GuiNexus(player.inventory, nexus);
      }
    }
    return null;
  }
	@Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
  {
    if (id == mod_Invasion.getGuiIdNexus())
    {
      TileEntityNexus nexus = (TileEntityNexus)world.getTileEntity(x, y, z);
      if (nexus != null) {
        return new ContainerNexus(player.inventory, nexus);
      }
    }
    return null;
  }
}