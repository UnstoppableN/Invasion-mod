package com.whammich.invasion.items;

import com.whammich.invasion.InvasionMod;
import com.whammich.invasion.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ItemBowSearing extends ItemBow {

    public static final String[] bowPullIconNameArray = { "sbowc1", "sbowc2", "sbowc3" };
    @SideOnly(Side.CLIENT)
    private IIcon iconCharge1;
    @SideOnly(Side.CLIENT)
    private IIcon iconCharge2;
    @SideOnly(Side.CLIENT)
    private IIcon iconCharge3;
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public ItemBowSearing() {
        super();
        setUnlocalizedName(Reference.PREFIX + ".bow.searing");
        setCreativeTab(InvasionMod.tabInvasion);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
        int var6 = getMaxItemUseDuration(par1ItemStack) - par4;

        ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, var6);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        var6 = event.charge;

        boolean var5 = (par3EntityPlayer.capabilities.isCreativeMode) || (EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0);

        if ((var5) || (par3EntityPlayer.inventory.hasItem(Items.arrow))) {
            float f = var6 / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            boolean special = false;

            if (f < 0.1D) {
                return;
            }
            if (f >= 3.8F) {
                special = true;
                f = 1.0F;
            } else if (f > 1.0F) {
                f = 1.0F;
            }

            if (!special) {
                EntityArrow var8 = new EntityArrow(par2World, par3EntityPlayer, f * 2.0F);
                if (f == 1.0F) {
                    var8.setIsCritical(true);
                }

                int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

                if (var9 > 0) {
                    var8.setDamage(var8.getDamage() + var9 * 0.5D + 0.5D);
                }

                int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

                if (var10 > 0) {
                    var8.setKnockbackStrength(var10);
                }

                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0) {
                    var8.setFire(100);
                }

                if (var5) {
                    var8.canBePickedUp = 2;
                } else {
                    par3EntityPlayer.inventory.consumeInventoryItem(Items.arrow);
                }

                if (!par2World.isRemote) {
                    par2World.spawnEntityInWorld(var8);
                }
            } else {
                EntityArrow var8 = new EntityArrow(par2World, par3EntityPlayer, f * 2.0F);

                var8.setFire(100);
                var8.setDamage((var8.getDamage() + 1 * 0.5D + 0.5D) * 3 / 2 + 1);
                if (!par2World.isRemote) {
                    par2World.spawnEntityInWorld(var8);
                }
            }

            par1ItemStack.damageItem(1, par3EntityPlayer);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
        }
    }


    @Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        return itemstack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        ArrowNockEvent event = new ArrowNockEvent(entityPlayer, itemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return event.result;
        }

        if ((entityPlayer.capabilities.isCreativeMode) || (entityPlayer.inventory.hasItem(Items.arrow)))
            entityPlayer.setItemInUse(itemStack, getMaxItemUseDuration(itemStack));

        return itemStack;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(Reference.PREFIX + ":searingBow");
        this.iconArray = new IIcon[(bowPullIconNameArray.length + 1)];
        this.iconArray[0] = iconRegister.registerIcon(Reference.PREFIX + ":searingBow");
        for (int i = 1; i < this.iconArray.length; ++i)
            this.iconArray[i] = iconRegister.registerIcon(Reference.PREFIX + ":" + bowPullIconNameArray[i - 1]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (player.getItemInUse() == null) return this.itemIcon;
        int Pulling = stack.getMaxItemUseDuration() - useRemaining;
        float f = Pulling / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f >= 3.8F) {
            return iconArray[3];
        } else if (Pulling > 17) {
            return iconArray[2];
        } else if (Pulling > 0) {
            return iconArray[1];
        }
        return iconArray[0];
    }


}