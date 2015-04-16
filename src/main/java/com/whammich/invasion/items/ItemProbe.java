package com.whammich.invasion.items;

import com.whammich.invasion.InvasionMod;
import com.whammich.invasion.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.Invasion;
import invmod.common.entity.EntityIMLiving;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemProbe extends Item {

    public static String[] names = { "adjuster.nexus", "material" };
    public IIcon[] icons = new IIcon[names.length];

    public ItemProbe() {
        super();
        setUnlocalizedName(Reference.PREFIX + ".probe");
        setCreativeTab(InvasionMod.tabInvasion);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons[0] = par1IconRegister.registerIcon(Reference.PREFIX + ":adjuster");
        this.icons[1] = par1IconRegister.registerIcon(Reference.PREFIX + ":probe");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        if (world.isRemote) {
            return false;
        }
        Block block = world.getBlock(x, y, z);
        EntityPlayerMP player = (EntityPlayerMP) entityplayer;
        if (block == Invasion.blockNexus) {

            TileEntityNexus nexus = (TileEntityNexus) world.getTileEntity(x, y, z);
            int newRange = nexus.getSpawnRadius();

            //check if the player wants to increase or decrease the range
            if (entityplayer.isSneaking()) {

                newRange -= 8;
                if (newRange < 32) {
                    newRange = 128;
                }

            } else {

                newRange += 8;
                if (newRange > 128) {
                    newRange = 32;
                }

            }

            nexus.setSpawnRadius(newRange);
            Invasion.sendMessageToPlayer(player, "Nexus range changed to: " + nexus.getSpawnRadius());
            return true;
        }
        if (itemstack.getItemDamage() == 1) {

            float blockStrength = EntityIMLiving.getBlockStrength(x, y, z, block, world);
            Invasion.sendMessageToPlayer(player, "Block strength: " + (int) ((blockStrength + 0.005D) * 100.0D) / 100.0D);
            return true;
        }
        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getItemEnchantability() {
        return 14;
    }
}