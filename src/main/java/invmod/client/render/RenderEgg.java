package invmod.client.render;

import invmod.common.entity.EntityIMEgg;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderEgg extends Render {
	private static final ResourceLocation texture = new ResourceLocation("invmod:textures/spideregg.png");
	private ModelEgg modelEgg;

	public RenderEgg() {
		this.modelEgg = new ModelEgg();
	}

	public void renderEgg(EntityIMEgg entityEgg, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glEnable(32826);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);

		bindEntityTexture(entityEgg);
		this.modelEgg.render(entityEgg, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderEgg((EntityIMEgg) entity, d, d1, d2, f, f1);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
}