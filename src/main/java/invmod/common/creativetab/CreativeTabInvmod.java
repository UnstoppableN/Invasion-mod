package invmod.common.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.mod_Invasion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class CreativeTabInvmod extends CreativeTabs{

	public CreativeTabInvmod() {
		super("invasionTab");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
	return Item.getItemFromBlock(mod_Invasion.blockNexus);
	}
	
}
