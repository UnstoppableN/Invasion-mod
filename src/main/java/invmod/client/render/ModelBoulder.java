package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBoulder extends ModelBase
{
  ModelRenderer boulder;

  public ModelBoulder()
  {
    this.boulder = new ModelRenderer(this, 0, 0);
    this.boulder.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
    this.boulder.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.boulder.rotateAngleX = 0.0F;
    this.boulder.rotateAngleY = 0.0F;
    this.boulder.rotateAngleZ = 0.0F;
    this.boulder.mirror = false;
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    this.boulder.render(f5);
  }

  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    this.boulder.rotateAngleX = f;
    this.boulder.rotateAngleY = f1;
    this.boulder.rotateAngleZ = f2;
  }
}