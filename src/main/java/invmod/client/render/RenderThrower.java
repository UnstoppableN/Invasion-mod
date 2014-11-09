package invmod.client.render;

import invmod.common.entity.EntityIMSpider;
import invmod.common.entity.EntityIMThrower;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderThrower extends RenderLiving {
	private static final ResourceLocation texture_T1 = new ResourceLocation("invmod:textures/throwerT1.png");
	private static final ResourceLocation texture_T2 = new ResourceLocation("invmod:textures/throwerT2.png");
	
	public RenderThrower(ModelBase modelbase, float f) {
		super(modelbase, f);
	}

	protected void preRenderScale(EntityIMThrower entity, float f) {
		GL11.glScalef(2.4F, 2.8F, 2.4F);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase entityliving, float f) {
		preRenderScale((EntityIMThrower) entityliving, f);
	}
	
	protected ResourceLocation getTexture(EntityIMThrower entity) {
		switch (entity.getTextureId()) {
		case 1:
			return texture_T1;
		case 2:
			return texture_T2;

		}
		return texture_T1;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return getTexture((EntityIMThrower) entity);
	}
	
}