package invmod.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.mod_Invasion;
import invmod.common.entity.EntityIMTrap;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemTrap extends ItemIM {

	@SideOnly(Side.CLIENT)
	private IIcon emptyIcon;

	@SideOnly(Side.CLIENT)
	private IIcon riftIcon;

	@SideOnly(Side.CLIENT)
	private IIcon flameIcon;
	public static final String[] trapNames = { "emptyTrap", "riftTrap", "flameTrap", "XYZ Trap" };

	public ItemTrap() {
		super();
		this.setMaxStackSize(64);
		setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName("trap");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.emptyIcon = par1IconRegister.registerIcon("invmod:trapEmpty");
		this.riftIcon = par1IconRegister.registerIcon("invmod:trapPurple");
		this.flameIcon = par1IconRegister.registerIcon("invmod:trapRed");
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
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() < trapNames.length) {
			return trapNames[itemstack.getItemDamage()];
		}
		return "";
	}

	@Override
	public IIcon getIconFromDamage(int i) {
		if (i == 1)
			return this.riftIcon;
		if (i == 2) {
			return this.flameIcon;
		}
		return this.emptyIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List dest) {
		dest.add(new ItemStack(item, 1, 0));
		dest.add(new ItemStack(item, 1, 1));
		dest.add(new ItemStack(item, 1, 2));
	}
}