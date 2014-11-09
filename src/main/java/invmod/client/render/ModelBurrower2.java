package invmod.client.render;

import invmod.common.util.PosRotate3D;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBurrower2 extends ModelBase
{
  ModelRenderer head;
  ModelRenderer[] segments;

  public ModelBurrower2(int numberOfSegments)
  {
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.head = new ModelRenderer(this, 0, 0);
    this.head.addBox(-1.0F, -3.0F, -3.0F, 2, 6, 6);
    this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.head.setTextureSize(64, 32);
    this.head.mirror = true;
    setRotation(this.head, 0.0F, 0.0F, 0.0F);
    this.segments = new ModelRenderer[numberOfSegments];
    for (int i = 0; i < numberOfSegments; i++)
    {
      this.segments[i] = new ModelRenderer(this, 0, 0);

      if (i % 2 == 0)
        this.segments[i].addBox(-0.5F, -3.5F, -3.5F, 2, 7, 7);
      else {
        this.segments[i].addBox(-0.5F, -2.5F, -2.5F, 2, 5, 5);
      }
      this.segments[i].setRotationPoint(-4.0F, 0.0F, 0.0F);
      this.segments[i].setTextureSize(64, 32);
      this.segments[i].mirror = true;
      setRotation(this.segments[i], 0.0F, 0.0F, 0.0F);
    }
  }

  public void render(Entity entity, float partialTick, PosRotate3D[] pos, float modelScale)
  {
    super.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, modelScale);

    this.head.setRotationPoint((float)pos[0].getPosX(), (float)pos[0].getPosY(), (float)pos[0].getPosZ());
    setRotation(this.head, pos[0].getRotX(), pos[0].getRotY(), pos[0].getRotZ());
    for (int i = 0; i < this.segments.length; i++)
    {
      this.segments[i].setRotationPoint((float)pos[(i + 1)].getPosX(), (float)pos[(i + 1)].getPosY(), (float)pos[(i + 1)].getPosZ());
      setRotation(this.segments[i], pos[(i + 1)].getRotX(), pos[(i + 1)].getRotY(), pos[(i + 1)].getRotZ());
      this.segments[i].render(modelScale);
    }
    this.head.render(modelScale);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}