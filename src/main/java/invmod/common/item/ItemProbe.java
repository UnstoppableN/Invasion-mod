package invmod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.entity.EntityIMLiving;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemProbe extends ItemIM
{

  @SideOnly(Side.CLIENT)
  private IIcon iconAdjuster;

  @SideOnly(Side.CLIENT)
  private IIcon iconProbe;
  public static final String[] probeNames = { "nexusAdjuster", "materialProbe" };

  public ItemProbe()
  {
    super();
    setHasSubtypes(true);
    this.setMaxDamage(0);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerIcons(IIconRegister par1IconRegister)
  {
    this.iconAdjuster = par1IconRegister.registerIcon("invmod:adjuster");
    this.iconProbe = par1IconRegister.registerIcon("invmod:probe");
  }
  
  @Override
  public boolean isFull3D()
  {
    return true;
  }
  
  @Override
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
  {
    return itemstack;
  }

  @Override
  public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
  {

    if (world.isRemote) {
      return false;
    }
    Block block = world.getBlock(x, y, z);
    EntityPlayerMP player = (EntityPlayerMP)entityplayer;
    if (block == mod_Invasion.blockNexus)
    {

      TileEntityNexus nexus = (TileEntityNexus)world.getTileEntity(x, y, z);
      int newRange = nexus.getSpawnRadius();
      
      //check if the player wants to increase or decrease the range
      if(entityplayer.isSneaking()){
    	  
    	  newRange -= 8;
    	  if (newRange < 32)
    	  {
    		  newRange = 128;
    	  }
    	  
      }else{
    	  
    	  newRange += 8;
    	  if (newRange > 128)
    	  {
    		  newRange = 32;
    	  }
    	  
      }
      
      nexus.setSpawnRadius(newRange);
      mod_Invasion.sendMessageToPlayer(player, "Nexus range changed to: " + nexus.getSpawnRadius());
      return true;
    }
    if (itemstack.getItemDamage() == 1)
    {
    	
      float blockStrength = EntityIMLiving.getBlockStrength(x, y, z, block, world);
      mod_Invasion.sendMessageToPlayer(player, "Block strength: " + (int)((blockStrength + 0.005D) * 100.0D) / 100.0D);
      return true;
    }
    return false;
  }
  @Override
  public String getUnlocalizedName(ItemStack itemstack)
  {
    if (itemstack.getItemDamage() < probeNames.length) {
      return probeNames[itemstack.getItemDamage()];
    }
    return "";
  }
  @Override
  public IIcon getIconFromDamage(int i)
  {
    if (i == 1) {
      return this.iconProbe;
    }
    return this.iconAdjuster;
  }
  @Override
  public int getItemEnchantability()
  {
    return 14;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubItems(Item item, CreativeTabs tab, List dest)
  {
    dest.add(new ItemStack(item, 1, 0));
    dest.add(new ItemStack(item, 1, 1));
  }
}