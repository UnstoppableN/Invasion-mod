package invmod.common.util;

import invmod.common.nexus.BlockNexus;
import invmod.common.nexus.TileEntityNexus;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class IMWailaProvider implements IWailaDataProvider {

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new IMWailaProvider(), BlockNexus.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntityNexus teNexus = (TileEntityNexus) accessor.getTileEntity();
        if (teNexus != null) {

            currenttip.add(String.format(StatCollector.translateToLocal("waila.invasion.status"), teNexus.isActive()));
            if (teNexus.isActive()) {
                int waveNumber = teNexus.getCurrentWave();
                currenttip.add(String.format(StatCollector.translateToLocal("waila.invasion.wavenumber"), waveNumber));
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return null;
    }

}
