package invmod.client.render;

import invmod.common.entity.EntityIMBird;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class RenderB extends RenderLiving {
	private ModelBird modelBird;
	private static final ResourceLocation texture = new ResourceLocation("invmod:textures/bird_tx1.png");

	public RenderB() {
		super(new ModelBird(), 0.4F);
		this.modelBird = ((ModelBird) this.mainModel);
	}

	public void renderBz(EntityIMBird entityBird, double renderX, double renderY, double renderZ, float interpYaw, float partialTick) {
		if (entityBird.hasFlyingDebug()) {
			renderNavigationVector(entityBird, renderX, renderY, renderZ);
		}

		float flapProgress = entityBird.getWingAnimationState().getCurrentAnimationTimeInterp(partialTick);
		this.modelBird.setFlyingAnimations(flapProgress, entityBird.getLegSweepProgress(), entityBird.getRotationRoll());
		super.doRender(entityBird, renderX, renderY, renderZ, interpYaw, partialTick);
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderBz((EntityIMBird) entity, d, d1, d2, f, f1);
	}

	private void renderNavigationVector(EntityIMBird entityBird, double entityRenderOffsetX, double entityRenderOffsetY, double entityRenderOffsetZ) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glPushMatrix();

		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);

		Vec3 target = entityBird.getFlyTarget();
		double drawWidth = 0.1D;

		tessellator.startDrawing(5);
		tessellator.setColorRGBA_F(1.0F, 0.0F, 0.0F, 1.0F);
		for (int j = 0; j < 5; j++) {
			double xOffset = drawWidth;
			double zOffset = drawWidth;
			if ((j == 1) || (j == 2)) {
				xOffset += drawWidth * 2.0D;
			}
			if ((j == 2) || (j == 3)) {
				zOffset += drawWidth * 2.0D;
			}
			tessellator.addVertex(entityRenderOffsetX - entityBird.width / 2.0F + xOffset, entityRenderOffsetY + entityBird.height / 2.0F, entityRenderOffsetZ - entityBird.width / 2.0F + zOffset);
			tessellator.addVertex(target.xCoord + xOffset - RenderManager.renderPosX, target.yCoord - RenderManager.renderPosY, target.zCoord + zOffset - RenderManager.renderPosZ);
		}
		tessellator.draw();

		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);

		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
}