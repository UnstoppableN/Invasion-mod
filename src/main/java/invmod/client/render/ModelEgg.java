package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEgg extends ModelBase
{
  ModelRenderer base;
  ModelRenderer l3s4;
  ModelRenderer l3s2;
  ModelRenderer l3s3;
  ModelRenderer l3s1;
  ModelRenderer top;
  ModelRenderer l4s1;
  ModelRenderer l4s4;
  ModelRenderer l2s3;
  ModelRenderer l2s4;
  ModelRenderer l2s1;
  ModelRenderer l4s2;
  ModelRenderer l4s3;
  ModelRenderer l2s2;
  ModelRenderer l1s4;
  ModelRenderer l1s1;
  ModelRenderer l1s2;
  ModelRenderer l1s3;

  public ModelEgg()
  {
    this.textureWidth = 64;
    this.textureHeight = 32;

    top = new ModelRenderer(this, 0, 8);
    top.addBox(2F, -11F, 2F, 5, 1, 5);
    top.setRotationPoint(0F, 0F, 0F);
    top.setTextureSize(64, 32);
    top.mirror = true;
    setRotation(top, 0F, 0F, 0F);
    l4s4 = new ModelRenderer(this, 28, 23);
    l4s4.addBox(1F, -10F, 2F, 1, 3, 6);
    l4s4.setRotationPoint(0F, 0F, 0F);
    l4s4.setTextureSize(64, 32);
    l4s4.mirror = true;
    setRotation(l4s4, 0F, 0F, 0F);
    l4s3 = new ModelRenderer(this, 0, 24);
    l4s3.addBox(1F, -10F, 1F, 6, 3, 1);
    l4s3.setRotationPoint(0F, 0F, 0F);
    l4s3.setTextureSize(64, 32);
    l4s3.mirror = true;
    setRotation(l4s3, 0F, 0F, 0F);
    l4s2 = new ModelRenderer(this, 28, 23);
    l4s2.addBox(7F, -10F, 1F, 1, 3, 6);
    l4s2.setRotationPoint(0F, 0F, 0F);
    l4s2.setTextureSize(64, 32);
    l4s2.mirror = true;
    setRotation(l4s2, 0F, 0F, 0F);
    l4s1 = new ModelRenderer(this, 0, 24);
    l4s1.addBox(2F, -10F, 7F, 6, 3, 1);
    l4s1.setRotationPoint(0F, 0F, 0F);
    l4s1.setTextureSize(64, 32);
    l4s1.mirror = true;
    setRotation(l4s1, 0F, 0F, 0F);
    l3s4 = new ModelRenderer(this, 10, 22);
    l3s4.addBox(0F, -7F, 1F, 1, 2, 8);
    l3s4.setRotationPoint(0F, 0F, 0F);
    l3s4.setTextureSize(64, 32);
    l3s4.mirror = true;
    setRotation(l3s4, 0F, 0F, 0F);
    l3s3 = new ModelRenderer(this, 0, 21);
    l3s3.addBox(0F, -7F, 0F, 8, 2, 1);
    l3s3.setRotationPoint(0F, 0F, 0F);
    l3s3.setTextureSize(64, 32);
    l3s3.mirror = true;
    setRotation(l3s3, 0F, 0F, 0F);
    l3s2 = new ModelRenderer(this, 10, 22);
    l3s2.addBox(8F, -7F, 0F, 1, 2, 8);
    l3s2.setRotationPoint(0F, 0F, 0F);
    l3s2.setTextureSize(64, 32);
    l3s2.mirror = true;
    setRotation(l3s2, 0F, 0F, 0F);
    l3s1 = new ModelRenderer(this, 0, 21);
    l3s1.addBox(1F, -7F, 8F, 8, 2, 1);
    l3s1.setRotationPoint(0F, 0F, 0F);
    l3s1.setTextureSize(64, 32);
    l3s1.mirror = true;
    setRotation(l3s1, 0F, 0F, 0F);
    l2s4 = new ModelRenderer(this, 20, 10);
    l2s4.addBox(-1F, -5F, 0F, 1, 4, 9);
    l2s4.setRotationPoint(0F, 0F, 0F);
    l2s4.setTextureSize(64, 32);
    l2s4.mirror = true;
    setRotation(l2s4, 0F, 0F, 0F);
    l2s3 = new ModelRenderer(this, 0, 16);
    l2s3.addBox(0F, -5F, -1F, 9, 4, 1);
    l2s3.setRotationPoint(0F, 0F, 0F);
    l2s3.setTextureSize(64, 32);
    l2s3.mirror = true;
    setRotation(l2s3, 0F, 0F, 0F);
    l2s2 = new ModelRenderer(this, 20, 10);
    l2s2.addBox(9F, -5F, 0F, 1, 4, 9);
    l2s2.setRotationPoint(0F, 0F, 0F);
    l2s2.setTextureSize(64, 32);
    l2s2.mirror = true;
    setRotation(l2s2, 0F, 0F, 0F);
    l2s1 = new ModelRenderer(this, 0, 16);
    l2s1.addBox(0F, -5F, 9F, 9, 4, 1);
    l2s1.setRotationPoint(0F, 0F, 0F);
    l2s1.setTextureSize(64, 32);
    l2s1.mirror = true;
    setRotation(l2s1, 0F, 0F, 0F);
    l1s4 = new ModelRenderer(this, 28, 0);
    l1s4.addBox(0F, -1F, 1F, 1, 1, 8);
    l1s4.setRotationPoint(0F, 0F, 0F);
    l1s4.setTextureSize(64, 32);
    l1s4.mirror = true;
    setRotation(l1s4, 0F, 0F, 0F);
    l1s3 = new ModelRenderer(this, 0, 14);
    l1s3.addBox(0F, -1F, 0F, 8, 1, 1);
    l1s3.setRotationPoint(0F, 0F, 0F);
    l1s3.setTextureSize(64, 32);
    l1s3.mirror = true;
    setRotation(l1s3, 0F, 0F, 0F);
    l1s2 = new ModelRenderer(this, 28, 0);
    l1s2.addBox(8F, -1F, 0F, 1, 1, 8);
    l1s2.setRotationPoint(0F, 0F, 0F);
    l1s2.setTextureSize(64, 32);
    l1s2.mirror = true;
    setRotation(l1s2, 0F, 0F, 0F);
    l1s1 = new ModelRenderer(this, 0, 14);
    l1s1.addBox(0F, 0F, 0F, 8, 1, 1);
    l1s1.setRotationPoint(1F, -1F, 8F);
    l1s1.setTextureSize(64, 32);
    l1s1.mirror = true;
    setRotation(l1s1, 0F, 0F, 0F);
    base = new ModelRenderer(this, 0, 0);
    base.addBox(1F, 0F, 1F, 7, 1, 7);
    base.setRotationPoint(0F, 0F, 0F);
    base.setTextureSize(64, 32);
    base.mirror = true;
    setRotation(base, 0F, 0F, 0F);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    this.base.render(f5);
    this.l3s4.render(f5);
    this.l3s2.render(f5);
    this.l3s3.render(f5);
    this.l3s1.render(f5);
    this.top.render(f5);
    this.l4s1.render(f5);
    this.l4s4.render(f5);
    this.l2s3.render(f5);
    this.l2s4.render(f5);
    this.l2s1.render(f5);
    this.l4s2.render(f5);
    this.l4s3.render(f5);
    this.l2s2.render(f5);
    this.l1s4.render(f5);
    this.l1s1.render(f5);
    this.l1s2.render(f5);
    this.l1s3.render(f5);
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