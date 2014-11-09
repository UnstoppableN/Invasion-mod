package invmod.client.render;

import invmod.common.entity.EntityIMBurrower;
import invmod.common.util.PosRotate3D;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderBurrower extends Render {
	private static final ResourceLocation texture = new ResourceLocation("invmod:textures/burrower.png");
	private ModelBurrower2 modelBurrower;

	public RenderBurrower() {
		this.modelBurrower = new ModelBurrower2(16);
	}

	public void renderBurrower(EntityIMBurrower entityBurrower, double x, double y, double z, float yaw, float partialTick) {
		PosRotate3D[] pos = entityBurrower.getSegments3D();
		PosRotate3D[] lastPos = entityBurrower.getSegments3DLastTick();
		PosRotate3D[] renderPos = new PosRotate3D[17];
		renderPos[0] = new PosRotate3D();
		renderPos[0].setPosX(x * -7.269999980926514D);
		renderPos[0].setPosY(y * -7.269999980926514D);
		renderPos[0].setPosZ(z * 7.269999980926514D);
		renderPos[0].setRotX(entityBurrower.getPrevRotX() + partialTick * (entityBurrower.getRotX() - entityBurrower.getPrevRotX()));
		renderPos[0].setRotY(entityBurrower.getPrevRotY() + partialTick * (entityBurrower.getRotY() - entityBurrower.getPrevRotY()));
		renderPos[0].setRotZ(entityBurrower.getPrevRotZ() + partialTick * (entityBurrower.getRotZ() - entityBurrower.getPrevRotZ()));

		for (int i = 0; i < 16; i++) {
			renderPos[(i + 1)] = new PosRotate3D();
			renderPos[(i + 1)].setPosX((lastPos[i].getPosX() + partialTick * (pos[i].getPosX() - lastPos[i].getPosX()) - RenderManager.renderPosX) * -7.269999980926514D);
			renderPos[(i + 1)].setPosY((lastPos[i].getPosY() + partialTick * (pos[i].getPosY() - lastPos[i].getPosY()) - RenderManager.renderPosY) * -7.269999980926514D);
			renderPos[(i + 1)].setPosZ((lastPos[i].getPosZ() + partialTick * (pos[i].getPosZ() - lastPos[i].getPosZ()) - RenderManager.renderPosZ) * 7.269999980926514D);
			renderPos[(i + 1)].setRotX(lastPos[i].getRotX() + partialTick * (pos[i].getRotX() - lastPos[i].getRotX()));
			renderPos[(i + 1)].setRotY(lastPos[i].getRotY() + partialTick * (pos[i].getRotY() - lastPos[i].getRotY()));
			renderPos[(i + 1)].setRotZ(lastPos[i].getRotZ() + partialTick * (pos[i].getRotZ() - lastPos[i].getRotZ()));
		}

		GL11.glPushMatrix();
		GL11.glEnable(32826);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glScalef(2.2F, 2.2F, 2.2F);
		bindEntityTexture(entityBurrower);
		this.modelBurrower.render(entityBurrower, partialTick, renderPos, 0.0625F);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float yaw, float partialTick) {
		renderBurrower((EntityIMBurrower) entity, d, d1, d2, yaw, partialTick);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return texture;
	}
}