package com.whammich.invasion.items;

import com.whammich.invasion.InvasionMod;
import com.whammich.invasion.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemMaterials extends Item {

    public static String[] names = {
            "catalyst.mixture.unstable",
            "catalyst.mixture.stable",
            "catalyst.nexus.unstable",
            "catalyst.nexus.stable",
            "catalyst.strong",
            "agent.damping.weak",
            "agent.damping.strong",
            "remnants.small",
            "flux.rift",
            "crystal.phase"
    };
    public IIcon[] icon = new IIcon[names.length];

    public ItemMaterials() {
        super();

        setUnlocalizedName(Reference.PREFIX + ".material");
        setCreativeTab(InvasionMod.tabInvasion);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ri) {
        this.icon[0] = ri.registerIcon(Reference.PREFIX + ":catalystMixture_unstable");
        this.icon[1] = ri.registerIcon(Reference.PREFIX + ":catalystMixture_stable");
        this.icon[2] = ri.registerIcon(Reference.PREFIX + ":nexusCatalyst_unstable");
        this.icon[3] = ri.registerIcon(Reference.PREFIX + ":nexusCatalyst_stable");
        this.icon[4] = ri.registerIcon(Reference.PREFIX + ":catalyst_weak");
        this.icon[5] = ri.registerIcon(Reference.PREFIX + ":dampingAgent_weak");
        this.icon[6] = ri.registerIcon(Reference.PREFIX + ":dampingAgent_strong");
        this.icon[7] = ri.registerIcon(Reference.PREFIX + ":smallRemnants");
        this.icon[8] = ri.registerIcon(Reference.PREFIX + ":riftFlux");
        this.icon[9] = ri.registerIcon(Reference.PREFIX + ":phaseCrystal");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}
