package invmod.common.util;

import invmod.common.nexus.BlockNexus;
import invmod.common.nexus.TileEntityNexus;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class IMWailaProvider implements IWailaDataProvider{
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) { return null; }

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip,IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntityNexus teNexus = (TileEntityNexus)accessor.getTileEntity();
		if (teNexus != null){
				if(teNexus.isActive()){
					int waveNumber=teNexus.getCurrentWave();
					currenttip.add(StatCollector.translateToLocal("invmod.waila.status.active"));
					currenttip.add(StatCollector.translateToLocal("invmod.waila.wavenumber")+waveNumber);
					
				}else{
					currenttip.add(StatCollector.translateToLocal("invmod.waila.status.deactivated"));
					
				}
		}
		
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
		return currenttip;
	}
	
	public static void callbackRegister(IWailaRegistrar registrar){
		registrar.registerBodyProvider(new IMWailaProvider(), BlockNexus.class);
	}

}
