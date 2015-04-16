package invmod.common.nexus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.Invasion;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class BlockNexus extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon sideOn;

    @SideOnly(Side.CLIENT)
    private IIcon sideOff;

    @SideOnly(Side.CLIENT)
    private IIcon topOn;

    @SideOnly(Side.CLIENT)
    private IIcon topOff;

    @SideOnly(Side.CLIENT)
    private IIcon botTexture;

    public BlockNexus() {
        super(Material.rock);
        this.setResistance(6000000.0F);
        this.setHardness(3.0F);
        this.setStepSound(Blocks.glass.stepSound);
        this.setBlockName("blockNexus");
        this.setCreativeTab(Invasion.tabInvmod);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.sideOn = iconRegister.registerIcon("invmod:nexusSideOn");
        this.sideOff = iconRegister.registerIcon("invmod:nexusSideOff");
        this.topOn = iconRegister.registerIcon("invmod:nexusTopOn");
        this.topOff = iconRegister.registerIcon("invmod:nexusTopOff");
        this.botTexture = iconRegister.registerIcon("obsidian");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if ((meta & 0x4) == 0) {
            if (side == 1) {
                return this.topOff;
            }
            return side != 0 ? this.sideOff : this.botTexture;
        }

        if (side == 1) {
            return this.topOn;
        }
        return side != 0 ? this.sideOn : this.botTexture;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {

        Item item = null;
        ItemStack equippedItem = entityPlayer.getCurrentEquippedItem();
        if (equippedItem != null) {
            item = equippedItem.getItem();
        }

        if (world.isRemote) {
            return true;

        }
        if ((item != Invasion.itemProbe) && ((!Invasion.isDebug()) || (item != Invasion.itemDebugWand))) {
            TileEntityNexus tileEntityNexus = (TileEntityNexus) world.getTileEntity(x, y, z);
            if (tileEntityNexus != null) {
                Invasion.setNexusClicked(tileEntityNexus);
                entityPlayer.openGui(Invasion.getLoadedInstance(), Invasion.getGuiIdNexus(), world, x, y, z);

            }
            return true;
        }

        return false;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z);
        int numberOfParticles;
        if ((meta & 0x4) == 0)
            numberOfParticles = 0;
        else {
            numberOfParticles = 6;
        }

        for (int i = 0; i < numberOfParticles; i++) {
            double y1 = y + random.nextFloat();
            double y2 = (random.nextFloat() - 0.5D) * 0.5D;

            int direction = random.nextInt(2) * 2 - 1;
            double x2;
            double x1;
            double z1;
            double z2;
            if (random.nextInt(2) == 0) {
                z1 = z + 0.5D + 0.25D * direction;
                z2 = random.nextFloat() * 2.0F * direction;

                x1 = x + random.nextFloat();
                x2 = (random.nextFloat() - 0.5D) * 0.5D;
            } else {
                x1 = x + 0.5D + 0.25D * direction;
                x2 = random.nextFloat() * 2.0F * direction;
                z1 = z + random.nextFloat();
                z2 = (random.nextFloat() - 0.5D) * 0.5D;
            }

            world.spawnParticle("portal", x1, y1, z1, x2, y2, z2);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityNexus(world);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {

        TileEntityNexus tile = (TileEntityNexus) world.getTileEntity(x, y, z);

        if (tile.isActive()) {
            return -1.0F;
        } else {
            return ForgeHooks.blockStrength(this, player, world, x, y, z);
        }

    }
}