package invmod.client.render;

import invmod.common.entity.EntityIMBoulder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBoulder extends Render {
	private static final ResourceLocation texture = new ResourceLocation("invmod:textures/boulder.png");
	private ModelBoulder modelBoulder;

	public RenderBoulder() {
		this.modelBoulder = new ModelBoulder();
	}

	public void renderBoulder(EntityIMBoulder entityBoulder, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glEnable(32826);
		GL11.glScalef(2.2F, 2.2F, 2.2F);
		bindEntityTexture(entityBoulder);
		float spin = entityBoulder.getFlightTime() % 20 / 20.0F;
		this.modelBoulder.render(entityBoulder, spin, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderBoulder((EntityIMBoulder) entity, d, d1, d2, f, f1);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
}