package com.whammich.invasion.client.render;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderIMWolf extends RenderWolf {
    private static final ResourceLocation wolf = new ResourceLocation("invmod:textures/wolf_tame_nexus.png");

    public RenderIMWolf() {
        super(new ModelWolf(), new ModelWolf(), 1.0F);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
        float f = 1.3F;
        GL11.glScalef(f, (2.0F + f) / 3.0F, f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return wolf;
    }
}