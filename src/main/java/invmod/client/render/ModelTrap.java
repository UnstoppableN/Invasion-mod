package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTrap extends ModelBase
{
  ModelRenderer Core;
  ModelRenderer CoreFire;
  ModelRenderer Clasp1a;
  ModelRenderer Clasp1b;
  ModelRenderer Clasp2b;
  ModelRenderer Clasp2a;
  ModelRenderer Clasp3a;
  ModelRenderer Clasp3b;
  ModelRenderer Clasp4a;
  ModelRenderer Clasp4b;
  ModelRenderer Base;
  ModelRenderer BaseS1;
  ModelRenderer BaseS2;

  public ModelTrap()
  {
    this.textureWidth = 32;
    this.textureHeight = 32;
    this.Core = new ModelRenderer(this, 0, 13);
    this.Core.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4);
    this.Core.setRotationPoint(-2.0F, -2.0F, -2.0F);
    this.Core.setTextureSize(32, 32);
    this.Core.mirror = true;
    this.CoreFire = new ModelRenderer(this, 5, 7);
    this.CoreFire.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4);
    this.CoreFire.setRotationPoint(-2.0F, -2.0F, -2.0F);
    this.CoreFire.setTextureSize(32, 32);
    this.CoreFire.mirror = true;
    setRotation(this.Core, 0.0F, 0.0F, 0.0F);
    this.Clasp1a = new ModelRenderer(this, 0, 0);
    this.Clasp1a.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1);
    this.Clasp1a.setRotationPoint(-1.0F, -2.0F, 2.0F);
    this.Clasp1a.setTextureSize(32, 32);
    this.Clasp1a.mirror = true;
    setRotation(this.Clasp1a, 0.0F, 0.0F, 0.0F);
    this.Clasp1b = new ModelRenderer(this, 0, 7);
    this.Clasp1b.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
    this.Clasp1b.setRotationPoint(-1.0F, -1.0F, 3.0F);
    this.Clasp1b.setTextureSize(32, 32);
    this.Clasp1b.mirror = true;
    setRotation(this.Clasp1b, 0.0F, 0.0F, 0.0F);
    this.Clasp2b = new ModelRenderer(this, 0, 19);
    this.Clasp2b.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
    this.Clasp2b.setRotationPoint(3.0F, -1.0F, -1.0F);
    this.Clasp2b.setTextureSize(32, 32);
    this.Clasp2b.mirror = true;
    setRotation(this.Clasp2b, 0.0F, 0.0F, 0.0F);
    this.Clasp2a = new ModelRenderer(this, 0, 3);
    this.Clasp2a.addBox(0.0F, 0.0F, 0.0F, 1, 2, 2);
    this.Clasp2a.setRotationPoint(2.0F, -2.0F, -1.0F);
    this.Clasp2a.setTextureSize(32, 32);
    this.Clasp2a.mirror = true;
    setRotation(this.Clasp2a, 0.0F, 0.0F, 0.0F);
    this.Clasp3a = new ModelRenderer(this, 0, 0);
    this.Clasp3a.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1);
    this.Clasp3a.setRotationPoint(-1.0F, -2.0F, -3.0F);
    this.Clasp3a.setTextureSize(32, 32);
    this.Clasp3a.mirror = true;
    setRotation(this.Clasp3a, 0.0F, 0.0F, 0.0F);
    this.Clasp3b = new ModelRenderer(this, 0, 7);
    this.Clasp3b.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
    this.Clasp3b.setRotationPoint(-1.0F, -1.0F, -5.0F);
    this.Clasp3b.setTextureSize(32, 32);
    this.Clasp3b.mirror = true;
    setRotation(this.Clasp3b, 0.0F, 0.0F, 0.0F);
    this.Clasp4a = new ModelRenderer(this, 0, 3);
    this.Clasp4a.addBox(0.0F, 0.0F, 0.0F, 1, 2, 2);
    this.Clasp4a.setRotationPoint(-3.0F, -2.0F, -1.0F);
    this.Clasp4a.setTextureSize(32, 32);
    this.Clasp4a.mirror = true;
    setRotation(this.Clasp4a, 0.0F, 0.0F, 0.0F);
    this.Clasp4b = new ModelRenderer(this, 0, 19);
    this.Clasp4b.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
    this.Clasp4b.setRotationPoint(-5.0F, -1.0F, -1.0F);
    this.Clasp4b.setTextureSize(32, 32);
    this.Clasp4b.mirror = true;
    setRotation(this.Clasp4b, 0.0F, 0.0F, 0.0F);
    this.Base = new ModelRenderer(this, 0, 23);
    this.Base.addBox(0.0F, 0.0F, 0.0F, 4, 1, 2);
    this.Base.setRotationPoint(-2.0F, -1.0F, -1.0F);
    this.Base.setTextureSize(32, 32);
    this.Base.mirror = true;
    setRotation(this.Base, 0.0F, 0.0F, 0.0F);
    this.BaseS1 = new ModelRenderer(this, 0, 27);
    this.BaseS1.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1);
    this.BaseS1.setRotationPoint(-1.0F, -1.0F, 1.0F);
    this.BaseS1.setTextureSize(32, 32);
    this.BaseS1.mirror = true;
    setRotation(this.BaseS1, 0.0F, 0.0F, 0.0F);
    this.BaseS2 = new ModelRenderer(this, 0, 27);
    this.BaseS2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1);
    this.BaseS2.setRotationPoint(-1.0F, -1.0F, -2.0F);
    this.BaseS2.setTextureSize(32, 32);
    this.BaseS2.mirror = true;
    setRotation(this.BaseS2, 0.0F, 0.0F, 0.0F);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean isEmpty, int type)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);

    if (!isEmpty)
    {
      if (type == 1)
        this.Core.render(f5);
      else if (type == 2) {
        this.CoreFire.render(f5);
      }
    }
    this.Clasp1a.render(f5);
    this.Clasp1b.render(f5);
    this.Clasp2b.render(f5);
    this.Clasp2a.render(f5);
    this.Clasp3a.render(f5);
    this.Clasp3b.render(f5);
    this.Clasp4a.render(f5);
    this.Clasp4b.render(f5);
    this.Base.render(f5);
    this.BaseS1.render(f5);
    this.BaseS2.render(f5);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    render(entity, f, f1, f2, f3, f4, f5, false, 0);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }
}