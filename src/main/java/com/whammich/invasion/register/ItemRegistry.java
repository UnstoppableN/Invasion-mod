package com.whammich.invasion.register;

import com.whammich.invasion.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemRegistry {

    public static Item materials;
    public static Item debugWand;
    public static Item hammerEngineer;
    public static Item swordInfused;
    public static Item probe;
    public static Item bowSearing;
    public static Item strangeBone;
    public static Item trap;

    public static void registerItems() {

        materials = new ItemMaterials();
        GameRegistry.registerItem(materials, "ItemMaterials");

        debugWand = new ItemWandDebug();
        GameRegistry.registerItem(debugWand, "ItemWandDebug");

        hammerEngineer = new ItemHammerEngineer();
        GameRegistry.registerItem(hammerEngineer, "ItemHammerEngineer");

        swordInfused = new ItemSwordInfused();
        GameRegistry.registerItem(swordInfused, "ItemSwordInfused");

        probe = new ItemProbe();
        GameRegistry.registerItem(probe, "ItemProbe");

        bowSearing = new ItemBowSearing();
        GameRegistry.registerItem(bowSearing, "ItemBowSearing");

        strangeBone = new ItemStrangeBone();
        GameRegistry.registerItem(strangeBone, "ItemStrangeBone");

        trap = new ItemTrap();
        GameRegistry.registerItem(trap, "ItemTrap");
    }
}
