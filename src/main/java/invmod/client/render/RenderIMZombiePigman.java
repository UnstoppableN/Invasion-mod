package invmod.client.render;

import invmod.common.entity.EntityIMZombiePigman;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderIMZombiePigman extends RenderBiped {
	private static final ResourceLocation t_T1 = new ResourceLocation("invmod:textures/pigzombie64x32.png");
	private static final ResourceLocation t_T3 = new ResourceLocation("invmod:textures/zombiePigmanT3.png");
	protected ModelBiped modelBiped;
	protected ModelBigBiped modelBigBiped;

	public RenderIMZombiePigman(ModelBiped model, float par2) {
		this(model, par2, 1.0F);
	}

	public RenderIMZombiePigman(ModelBiped model, float par2, float par3) {
		super(model, par2);
		this.modelBiped = model;
		this.modelBigBiped = new ModelBigBiped();
	}
	@Override
	public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
		if ((entity instanceof EntityIMZombiePigman)) {
			if (((EntityIMZombiePigman) entity).isBigRenderTempHack()) {
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
		float f = ((EntityIMZombiePigman) par1EntityLiving).scaleAmount();
		GL11.glScalef(f, (2.0F + f) / 3.0F, f);
	}
	@Override
	protected void renderEquippedItems(EntityLivingBase entity, float par2) {
		if(((EntityIMZombiePigman)entity).getTier()!=3){
		super.renderEquippedItems(entity, par2);
		}
	}

	  
	    
	protected ResourceLocation getTexture(EntityIMZombiePigman entity) {
		switch (entity.getTextureId()) {
		case 0:
			return t_T1;
		case 2:
			return t_T3;
			
		}
		return t_T1;
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return getTexture((EntityIMZombiePigman) entity);
	}
}