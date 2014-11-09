package invmod.common.item;

import invmod.common.mod_Invasion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemIM extends Item
{
  public ItemIM()
  {
    super();
    this.setCreativeTab(mod_Invasion.tabInvmod);
    this.setMaxStackSize(1);
  }

  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister par1IconRegister)
  {
    this.itemIcon = par1IconRegister.registerIcon("invmod:" + getUnlocalizedName().substring(5));
  }
}