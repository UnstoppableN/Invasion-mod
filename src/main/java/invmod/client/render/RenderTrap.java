package invmod.client.render;

import invmod.common.entity.EntityIMTrap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderTrap extends Render {
	private static final ResourceLocation texture = new ResourceLocation("invmod:textures/trap.png");
	private ModelTrap modelTrap;

	public RenderTrap(ModelTrap model) {
		this.modelTrap = model;
	}

	public void renderTrap(EntityIMTrap entityTrap, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);

		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(32826);
		GL11.glScalef(1.3F, 1.3F, 1.3F);
		bindEntityTexture(entityTrap);
		this.modelTrap.render(entityTrap, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entityTrap.isEmpty(), entityTrap.getTrapType());
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderTrap((EntityIMTrap) entity, d, d1, d2, f, f1);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
}