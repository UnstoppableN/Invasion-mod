package com.whammich.invasion.items;

import com.whammich.invasion.InvasionMod;
import com.whammich.invasion.Reference;
import invmod.Invasion;
import invmod.common.entity.EntityIMWolf;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

public class ItemStrangeBone extends Item {

    public ItemStrangeBone() {
        super();

        setUnlocalizedName(Reference.PREFIX + ".bone.strange");
        setTextureName(Reference.PREFIX + ":strangeBone");
        setCreativeTab(InvasionMod.tabInvasion);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase targetEntity) {
        if ((!targetEntity.worldObj.isRemote) && ((targetEntity instanceof EntityWolf)) && (!(targetEntity instanceof EntityIMWolf))) {
            EntityWolf wolf = (EntityWolf) targetEntity;

            if (wolf.isTamed()) {
                TileEntityNexus nexus = null;
                int x = MathHelper.floor_double(wolf.posX);
                int y = MathHelper.floor_double(wolf.posY);
                int z = MathHelper.floor_double(wolf.posZ);

                for (int i = -7; i < 8; i++) {
                    for (int j = -4; j < 5; j++) {
                        for (int k = -7; k < 8; k++) {
                            if (wolf.worldObj.getBlock(x + i, y + j, z + k) == Invasion.blockNexus) {
                                nexus = (TileEntityNexus) wolf.worldObj.getTileEntity(x + i, y + j, z + k);
                                break;
                            }
                        }
                    }
                }

                if (nexus != null) {
                    EntityIMWolf newWolf = new EntityIMWolf(wolf, nexus);
                    wolf.worldObj.spawnEntityInWorld(newWolf);
                    wolf.setDead();
                    itemStack.stackSize -= 1;
                } else {
                    player.addChatMessage(new ChatComponentText("The wolf doesn't like this strange bone."));
                }
            }

            return true;
        }

        return false;
    }
}