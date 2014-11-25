package invmod.client.render;

import invmod.common.entity.EntityIMWolf;
import invmod.common.entity.EntityIMZombie;
import invmod.common.entity.EntityIMZombiePigman;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderIMWolf extends RenderWolf {
	public RenderIMWolf() {
        super(new ModelWolf(),new ModelWolf(), 1.0F);
	}

	private static final ResourceLocation wolf = new ResourceLocation("invmod:textures/wolf_tame_nexus.png");

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
		float f = 1.3F;
		GL11.glScalef(f, (2.0F + f) / 3.0F, f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return wolf;
	}
}