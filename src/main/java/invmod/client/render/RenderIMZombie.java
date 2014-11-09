package invmod.client.render;

import invmod.common.entity.EntityIMZombie;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderIMZombie extends RenderLiving {
	private static final ResourceLocation t_old = new ResourceLocation("invmod:textures/zombie_old.png");
	private static final ResourceLocation t_T1a = new ResourceLocation("invmod:textures/zombieT1a.png");
	private static final ResourceLocation t_pig = new ResourceLocation("invmod:textures/pigzombie64x32.png");
	private static final ResourceLocation t_T2 = new ResourceLocation("invmod:textures/zombieT2.png");
	private static final ResourceLocation t_T2a = new ResourceLocation("invmod:textures/zombieT2a.png");
	private static final ResourceLocation t_T3 = new ResourceLocation("invmod:textures/zombieT3.png");
	private static final ResourceLocation t_tar = new ResourceLocation("invmod:textures/zombietar.png");
	protected ModelBiped modelBiped;
	protected ModelBigBiped modelBigBiped;

	public RenderIMZombie(ModelBiped model, float par2) {
		this(model, par2, 1.0F);
	}

	public RenderIMZombie(ModelBiped model, float par2, float par3) {
		super(model, par2);
		this.modelBiped = model;
		this.modelBigBiped = new ModelBigBiped();
	}
	@Override
	public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
		if ((entity instanceof EntityIMZombie)) {
			if (((EntityIMZombie) entity).isBigRenderTempHack()) {
				this.mainModel = this.modelBigBiped;
				this.modelBigBiped.setSneaking(entity.isSneaking());
			} else {
				this.mainModel = this.modelBiped;
			}
			super.doRender(entity, par2, par4, par6, par8, par9);
		}
	}
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
		float f = ((EntityIMZombie) par1EntityLiving).scaleAmount();
		GL11.glScalef(f, (2.0F + f) / 3.0F, f);
	}
	@Override
	protected void renderEquippedItems(EntityLivingBase entity, float par2) {
		super.renderEquippedItems(entity, par2);
		ItemStack itemstack = entity.getHeldItem();

		if (itemstack != null) {
			GL11.glPushMatrix();
			if (((EntityIMZombie) entity).isBigRenderTempHack())
				this.modelBigBiped.itemArmPostRender(0.0625F);
			else {
				this.modelBiped.bipedRightArm.postRender(0.0625F);
			}
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
				float f = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f, -f, f);
			} else if (itemstack.getItem() == Items.bow) {
				float f1 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, f1);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (itemstack.getItem().isFull3D()) {
				float f2 = 0.625F;
				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f2, -f2, f2);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				float f3 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f3, f3, f3);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(entity, itemstack, 0);

			if (itemstack.getItem().requiresMultipleRenderPasses()) {
				for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++) {
					this.renderManager.itemRenderer.renderItem(entity, itemstack, x);
				}
			}

			GL11.glPopMatrix();
		}
	}

	protected ResourceLocation getTexture(EntityIMZombie entity) {
		switch (entity.getTextureId()) {
		case 0:
			return t_old;
		case 1:
			return t_T1a;
		case 2:
			return t_T2;
		case 3:
			return t_pig;
		case 4:
			return t_T2a;
		case 5:
			return t_tar;
		case 6:
			return t_T3;
		}
		return t_old;
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return getTexture((EntityIMZombie) entity);
	}
}