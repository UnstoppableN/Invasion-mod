package com.whammich.invasion.items;

import com.whammich.invasion.InvasionMod;
import com.whammich.invasion.Reference;
import invmod.Invasion;
import invmod.common.entity.*;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWandDebug extends Item {

    private TileEntityNexus nexus;

    public ItemWandDebug() {
        super();

        setUnlocalizedName(Reference.PREFIX + ".wand.debug");
        setTextureName(Reference.PREFIX + ":debugWand");
        setCreativeTab(InvasionMod.tabInvasion);
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return false;
        }
        Block block = world.getBlock(x, y, z);
        if (block == Invasion.blockNexus) {
            this.nexus = ((TileEntityNexus) world.getTileEntity(x, y, z));
            return true;
        }

        EntityIMBird bird = new EntityIMGiantBird(world);
        bird.setPosition(x, y + 1, z);

        EntityZombie zombie2 = new EntityZombie(world);
        zombie2.setPosition(x, y + 1, z);

        EntityWolf wolf = new EntityWolf(world);
        wolf.setPosition(x, y + 1, z);
        world.spawnEntityInWorld(wolf);

        Entity entity1 = new EntityIMPigEngy(world);
        entity1.setPosition(x, y + 1, z);

        EntityIMZombie zombie = new EntityIMZombie(world, this.nexus);
        zombie.setTexture(0);
        zombie.setFlavour(0);
        zombie.setTier(1);

        zombie.setPosition(x, y + 1, z);

        if (this.nexus != null) {
            Entity entity = new EntityIMPigEngy(world, this.nexus);
            entity.setPosition(x, y + 1, z);

            zombie = new EntityIMZombie(world, this.nexus);
            zombie.setTexture(0);
            zombie.setFlavour(0);
            zombie.setTier(2);
            zombie.setPosition(x, y + 1, z);

            Entity thrower = new EntityIMThrower(world, this.nexus);
            thrower.setPosition(x, y + 1, z);

            EntityIMCreeper creep = new EntityIMCreeper(world, this.nexus);
            creep.setPosition(x, y + 1, z);

            EntityIMSpider spider = new EntityIMSpider(world, this.nexus);

            spider.setTexture(0);
            spider.setFlavour(0);
            spider.setTier(2);

            spider.setPosition(x, y + 1, z);

            EntityIMSkeleton skeleton = new EntityIMSkeleton(world, this.nexus);
            skeleton.setPosition(x, y + 1, z);
        }

        EntityIMSpider entity = new EntityIMSpider(world, this.nexus);

        entity.setTexture(0);
        entity.setFlavour(1);
        entity.setTier(2);

        entity.setPosition(x, y + 1, z);

        EntityIMCreeper creep = new EntityIMCreeper(world);
        creep.setPosition(150.5D, 64.0D, 271.5D);

        return true;
    }
}