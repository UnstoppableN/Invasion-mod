package invmod.common.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.Invasion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabInvmod extends CreativeTabs {

    public CreativeTabInvmod() {
        super("invasionTab");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return Item.getItemFromBlock(Invasion.blockNexus);
    }

}
