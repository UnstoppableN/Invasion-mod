package invmod.client.render;

import invmod.common.entity.EntityIMBird;
import invmod.common.util.MathUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class RenderGiantBird extends RenderIMLiving {
	private static final ResourceLocation texture = new ResourceLocation("invmod:textures/vulture.png");
	private ModelVulture modelBird;

	public RenderGiantBird() {
		super(new ModelVulture(), 0.4F);
		this.modelBird = ((ModelVulture) this.mainModel);
	}

	public void renderGiantBird(EntityIMBird entityBird, double renderX, double renderY, double renderZ, float interpYaw, float partialTick) {
		if (entityBird.hasFlyingDebug()) {
			renderNavigationVector(entityBird, renderX, renderY, renderZ);
		}

		float roll = MathUtil.interpRotationDeg(entityBird.getPrevRotationRoll(), entityBird.getRotationRoll(), partialTick);
		float headYaw = MathUtil.interpRotationDeg(entityBird.getPrevRotationYawHeadIM(), entityBird.getRotationYawHeadIM(), partialTick);
		float headPitch = MathUtil.interpRotationDeg(entityBird.getPrevRotationPitchHead(), entityBird.getRotationPitchHead(), partialTick);

		this.modelBird.resetSkeleton();
		this.modelBird.setFlyingAnimations(entityBird.getWingAnimationState(), entityBird.getLegAnimationState(), entityBird.getBeakAnimationState(), roll, headYaw, headPitch, partialTick);

		super.doRenderLiving(entityBird, renderX, renderY, renderZ, interpYaw, partialTick);
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderGiantBird((EntityIMBird) entity, d, d1, d2, f, f1);
	}

	protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.modelBird.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLiving);
		super.renderModel(par1EntityLiving, par2, par3, par4, par5, par6, par7);
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