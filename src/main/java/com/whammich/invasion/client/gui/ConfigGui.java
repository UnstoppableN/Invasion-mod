package com.whammich.invasion.client.gui;

import com.whammich.invasion.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

import static com.whammich.invasion.ConfigHandler.config;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), Reference.MODID, false, false, StatCollector.translateToLocal("gui.invasion.config.title"));
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.add(new ConfigElement<ConfigCategory>(config.getCategory("Crafting".toLowerCase())));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory("Drops".toLowerCase())));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory("Mob Tweaks".toLowerCase())));

        return list;
    }
}
