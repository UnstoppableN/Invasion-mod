package invmod.client.render;

import invmod.common.entity.EntityIMSpider;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderSpiderIM extends RenderLiving {
	private static final ResourceLocation t_eyes = new ResourceLocation("textures/entity/spider_eyes.png");
	private static final ResourceLocation t_spider = new ResourceLocation("textures/entity/spider/spider.png");
	private static final ResourceLocation t_jumping = new ResourceLocation("invmod:textures/spiderT2.png");
	private static final ResourceLocation t_mother = new ResourceLocation("invmod:textures/spiderT2b.png");

	public RenderSpiderIM() {
		super(new ModelSpider(), 1.0F);
		setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntityIMSpider entityspider) {
		return 180.0F;
	}

	protected int setSpiderEyeBrightness(EntityIMSpider entityspider, int i, float f) {
		if (i != 0) {
			return -1;
		}

		bindTexture(t_eyes);
		float f1 = 1.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3008);
		GL11.glBlendFunc(1, 1);

		if (entityspider.isInvisible()) {
			GL11.glDepthMask(false);
		} else {
			GL11.glDepthMask(true);
		}

		char c0 = 61680;
		int j = c0 % 65536;
		int k = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
		return 1;
	}

	protected void scaleSpider(EntityIMSpider entityspider, float f) {
		float f1 = entityspider.spiderScaleAmount();
		this.shadowSize = f1;
		GL11.glScalef(f1, f1, f1);
	}

	protected void preRenderCallback(EntityLivingBase entityliving, float f) {
		scaleSpider((EntityIMSpider) entityliving, f);
	}

	protected float getDeathMaxRotation(EntityLivingBase entityliving) {
		return setSpiderDeathMaxRotation((EntityIMSpider) entityliving);
	}

	protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f) {
		return setSpiderEyeBrightness((EntityIMSpider) entityliving, i, f);
	}

	protected ResourceLocation getTexture(EntityIMSpider entity) {
		switch (entity.getTextureId()) {
		case 0:
			return t_spider;
		case 1:
			return t_jumping;
		case 2:
			return t_mother;
		}
		return t_spider;
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return getTexture((EntityIMSpider) entity);
	}
}