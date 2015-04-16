package com.whammich.invasion.items;

import com.whammich.invasion.InvasionMod;
import com.whammich.invasion.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.entity.EntityIMTrap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemTrap extends Item {

    public static String[] names = { "empty", "rift", "flame" };
    public IIcon[] icons = new IIcon[names.length];

    public ItemTrap() {
        super();
        setUnlocalizedName(Reference.PREFIX + ".trap");
        setCreativeTab(InvasionMod.tabInvasion);
        setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons[0] = par1IconRegister.registerIcon(Reference.PREFIX + ":trapEmpty");
        this.icons[1] = par1IconRegister.registerIcon(Reference.PREFIX + ":trapPurple");
        this.icons[2] = par1IconRegister.registerIcon(Reference.PREFIX + ":trapRed");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        return itemstack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return false;
        }
        if (side == 1) {
            EntityIMTrap trap;
            if (itemstack.getItemDamage() == 1) {
                trap = new EntityIMTrap(world, x + 0.5D, y + 1.0D, z + 0.5D, 1);
            } else {
                if (itemstack.getItemDamage() == 2)
                    trap = new EntityIMTrap(world, x + 0.5D, y + 1.0D, z + 0.5D, 2);
                else
                    return false;
            }
            if ((trap.isValidPlacement()) && (world.getEntitiesWithinAABB(EntityIMTrap.class, trap.boundingBox).size() == 0)) {
                world.spawnEntityInWorld(trap);

                // players in creative mode won't lose the item
                if (!entityplayer.capabilities.isCreativeMode) {
                    itemstack.stackSize -= 1;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}