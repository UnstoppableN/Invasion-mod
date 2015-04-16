package com.whammich.invasion.client.gui;

import com.whammich.invasion.register.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabInvasion extends CreativeTabs {

    public CreativeTabInvasion(String tabLabel) {
        super(tabLabel);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ItemRegistry.materials, 1, 4);
    }

    @Override
    public Item getTabIconItem() {
        return new Item();
    }
}
