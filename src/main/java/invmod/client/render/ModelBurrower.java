package invmod.client.render;

import invmod.common.util.PosRotate3D;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBurrower extends ModelBase
{
  ModelRenderer head;
  ModelRenderer seg1;
  ModelRenderer seg2;
  ModelRenderer seg3;

  public ModelBurrower()
  {
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.head = new ModelRenderer(this, 0, 0);
    this.head.addBox(-2.0F, -2.5F, -2.5F, 4, 5, 5);
    this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.head.setTextureSize(64, 32);
    this.head.mirror = true;
    setRotation(this.head, 0.0F, 0.0F, 0.0F);
    this.seg1 = new ModelRenderer(this, 0, 0);
    this.seg1.addBox(-2.0F, -2.5F, -2.5F, 4, 5, 5);
    this.seg1.setRotationPoint(-4.0F, 0.0F, 0.0F);
    this.seg1.setTextureSize(64, 32);
    this.seg1.mirror = true;
    setRotation(this.seg1, 0.0F, 0.0F, 0.0F);
    this.seg2 = new ModelRenderer(this, 0, 0);
    this.seg2.addBox(-2.0F, -2.5F, -2.5F, 4, 5, 5);
    this.seg2.setRotationPoint(-8.0F, 0.0F, 0.0F);
    this.seg2.setTextureSize(64, 32);
    this.seg2.mirror = true;
    setRotation(this.seg2, 0.0F, 0.0F, 0.0F);
    this.seg3 = new ModelRenderer(this, 0, 0);
    this.seg3.addBox(-2.0F, -2.5F, -2.5F, 4, 5, 5);
    this.seg3.setRotationPoint(-12.0F, 0.0F, 0.0F);
    this.seg3.setTextureSize(64, 32);
    this.seg3.mirror = true;
    setRotation(this.seg3, 0.0F, 0.0F, 0.0F);
  }

  public void render(Entity entity, float partialTick, PosRotate3D[] pos, float modelScale)
  {
    super.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, modelScale);

    if (pos.length >= 16)
    {
      this.head.setRotationPoint((float)pos[0].getPosX(), (float)pos[0].getPosY(), (float)pos[0].getPosZ());
      setRotation(this.head, pos[0].getRotX(), pos[0].getRotY(), pos[0].getRotZ());
      this.seg1.setRotationPoint((float)pos[1].getPosX(), (float)pos[1].getPosY(), (float)pos[1].getPosZ());
      setRotation(this.seg1, pos[1].getRotX(), pos[1].getRotY(), pos[1].getRotZ());
      this.seg2.setRotationPoint((float)pos[2].getPosX(), (float)pos[2].getPosY(), (float)pos[2].getPosZ());
      setRotation(this.seg2, pos[2].getRotX(), pos[2].getRotY(), pos[2].getRotZ());
      this.seg3.setRotationPoint((float)pos[3].getPosX(), (float)pos[3].getPosY(), (float)pos[3].getPosZ());
      setRotation(this.seg3, pos[3].getRotX(), pos[3].getRotY(), pos[3].getRotZ());
      this.head.render(modelScale);
      this.seg1.render(modelScale);
      this.seg2.render(modelScale);
      this.seg3.render(modelScale);
    }
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}