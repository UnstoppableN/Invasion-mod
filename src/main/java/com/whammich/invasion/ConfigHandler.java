package com.whammich.invasion;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        String category;

        category = "Crafting";
        config.addCustomCategoryComment(category, "All settings pertaining to Crafting");

        category = "Drops";
        config.addCustomCategoryComment(category, "All settings pertaining to Drops");

        category = "Mob Tweaks";
        config.addCustomCategoryComment(category, "All settings pertaining to Mobs");

        config.save();
    }
}
