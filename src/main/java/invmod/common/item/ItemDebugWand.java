package invmod.common.item;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMGiantBird;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.EntityIMSkeleton;
import invmod.common.entity.EntityIMSpider;
import invmod.common.entity.EntityIMThrower;
import invmod.common.entity.EntityIMZombie;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDebugWand extends ItemIM
{
  private TileEntityNexus nexus;

  public ItemDebugWand()
  {
    super();
    this.setMaxDamage(0);
    this.setUnlocalizedName("debugWand");
  }

  @Override
  public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
  {
    if (world.isRemote) {
      return false;
    }
    Block block = world.getBlock(x, y, z);
    if (block == mod_Invasion.blockNexus)
    {
      this.nexus = ((TileEntityNexus)world.getTileEntity(x, y, z));
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

    if (this.nexus != null)
    {
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

  public boolean hitEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase targetEntity)
  {
    if ((targetEntity instanceof EntityWolf))
    {
      EntityWolf wolf = (EntityWolf)targetEntity;

      if (player != null) {
        wolf.func_152115_b(player.getDisplayName());
      }
      return true;
    }
    return false;
  }

  public void addCreativeItems(ArrayList itemList)
  {
  }
}