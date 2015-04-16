package com.whammich.invasion;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;

    public static boolean enableLogging;

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

        category = "General";
        config.addCustomCategoryComment(category, "Miscellaneous settings");
        enableLogging = config.getBoolean("enableLogging", category, true, "Enables logging additional information to the console.");

        category = "Mob Tweaks";
        config.addCustomCategoryComment(category, "All settings pertaining to Mobs");

        config.save();
    }
}
