package com.whammich.invasion;

import com.whammich.invasion.client.gui.CreativeTabInvasion;
import com.whammich.invasion.proxies.CommonProxy;
import com.whammich.invasion.register.ItemRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;

import java.io.File;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPEND, guiFactory = Reference.GUIFACTORY)
public class InvasionMod {

    @SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)
    public static CommonProxy proxy;

    public static CreativeTabs tabInvasion = new CreativeTabInvasion(Reference.PREFIX + ".creativeTab");

    @Mod.Instance
    public static InvasionMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(new File(event.getModConfigurationDirectory() + "/InvasionMod.cfg"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        ItemRegistry.registerItems();

        FMLInterModComms.sendMessage("Waila", "register", "invmod.common.util.IMWailaProvider.callbackRegister");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
