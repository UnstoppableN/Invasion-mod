package com.whammich.invasion.proxies;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;

import java.io.File;

public class CommonProxy {
    public void preloadTexture(String texture) {
    }

    public int addTextureOverride(String fileToOverride, String fileToAdd) {
        return 0;
    }

    public void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
    }

    public void broadcastToAll(String message) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
    }

    public void printGuiMessage(String message) {
    }

    public void registerEntityRenderers() {
    }

    public void loadAnimations() {
    }

    public File getFile(String fileName) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile(fileName);
    }
}