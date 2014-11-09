package invmod.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderIMSkeleton extends RenderBiped
{
  private static final ResourceLocation texture = new ResourceLocation("textures/entity/skeleton/skeleton.png");

  public RenderIMSkeleton(ModelBiped model, float shadowSize)
  {
    super(model, shadowSize);
  }

  public RenderIMSkeleton(ModelBiped model, float shadowSize, float par3)
  {
    super(model, shadowSize, par3);
  }

  protected ResourceLocation getEntityTexture(Entity entity)
  {
    return texture;
  }
}