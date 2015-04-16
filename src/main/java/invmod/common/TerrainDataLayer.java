package invmod.common;

import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class TerrainDataLayer
        implements IBlockAccessExtended {
    public static final int EXT_DATA_SCAFFOLD_METAPOSITION = 16384;
    private IBlockAccess world;
    private IntHashMap dataLayer;

    public TerrainDataLayer(IBlockAccess world) {
        this.world = world;
        this.dataLayer = new IntHashMap();
    }

    public void setData(int x, int y, int z, Integer data) {
        this.dataLayer.addKey(PathNode.makeHash(x, y, z, PathAction.NONE), data);
    }

    public int getLayeredData(int x, int y, int z) {
        int key = PathNode.makeHash(x, y, z, PathAction.NONE);
        if (this.dataLayer.containsItem(key)) {
            return ((Integer) this.dataLayer.lookup(key)).intValue();
        }
        return 0;
    }

    public void setAllData(IntHashMap data) {
        this.dataLayer = data;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return this.world.getBlock(x, y, z);
    }

    public TileEntity getBlockTileEntity(int x, int y, int z) {
        return this.world.getTileEntity(x, y, z);
    }

    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int meta) {
        return this.world.getLightBrightnessForSkyBlocks(x, y, z, meta);
    }

    //useless?
//  public float getBrightness(int x, int y, int z, int meta)
//  {
//    return this.world.getBrightness(x, y, z, meta);
//  }
//
//  public float getLightBrightness(int x, int y, int z)
//  {
//    return this.world.getLightBrightness(x, y, z);
//  }

    public int getBlockMetadata(int x, int y, int z) {
        return this.world.getBlockMetadata(x, y, z);
    }

    public Material getBlockMaterial(int x, int y, int z) {
        return this.world.getBlock(x, y, z).getMaterial();
    }

    public boolean isBlockOpaqueCube(int x, int y, int z) {
        return this.world.getBlock(x, y, z).isOpaqueCube();
    }

    public boolean isBlockNormalCube(int x, int y, int z) {
        return this.world.getBlock(x, y, z).isNormalCube();
    }

    public boolean isAirBlock(int x, int y, int z) {
        return this.world.isAirBlock(x, y, z);
    }

    public BiomeGenBase getBiomeGenForCoords(int i, int j) {
        return this.world.getBiomeGenForCoords(i, j);
    }

    public int getHeight() {
        return this.world.getHeight();
    }

    public boolean extendedLevelsInChunkCache() {
        return this.world.extendedLevelsInChunkCache();
    }

    public boolean doesBlockHaveSolidTopSurface(int x, int y, int z) {
        return this.world.getBlock(x, y, z).getMaterial().isSolid();
    }


    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
        return this.world.isBlockProvidingPowerTo(var1, var2, var3, var4);
    }

    public boolean isBlockSolidOnSide(int x, int y, int z, net.minecraftforge.common.util.ForgeDirection side, boolean _default) {
        return this.world.isSideSolid(x, y, z, side, _default);
    }


    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return this.world.getTileEntity(x, y, z);
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, net.minecraftforge.common.util.ForgeDirection side, boolean _default) {
        return this.world.getBlock(x, y, z).getMaterial().isSolid();
    }
}