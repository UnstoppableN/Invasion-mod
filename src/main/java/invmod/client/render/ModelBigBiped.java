package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBigBiped extends ModelBase
{
  private ModelRenderer head;
  private ModelRenderer body;
  private ModelRenderer rightArm;
  private ModelRenderer leftArm;
  private ModelRenderer rightLeg;
  private ModelRenderer leftLeg;
  private ModelRenderer headwear;
  private int heldItemLeft;
  private int heldItemRight;
  private boolean isSneaking;
  private boolean aimedBow;

  public ModelBigBiped()
  {
    this.isSneaking = false;
    this.textureWidth = 64;
    this.textureHeight = 32;

    this.head = new ModelRenderer(this, 0, 0);
    this.head.addBox(-3.533333F, -7.0F, -3.5F, 7, 7, 7);
    this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.head.setTextureSize(64, 32);
    this.head.mirror = true;
    setRotation(this.head, 0.0F, 0.0F, 0.0F);
    this.body = new ModelRenderer(this, 16, 15);
    this.body.addBox(-5.0F, 0.0F, -3.0F, 10, 12, 5);
    this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.body.setTextureSize(64, 32);
    this.body.mirror = true;
    setRotation(this.body, 0.0F, 0.0F, 0.0F);
    this.rightArm = new ModelRenderer(this, 46, 15);
    this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 13, 4);
    this.rightArm.setRotationPoint(-6.0F, 2.0F, 0.0F);
    this.rightArm.setTextureSize(64, 32);
    this.rightArm.mirror = true;
    setRotation(this.rightArm, 0.0F, 0.0F, 0.0F);
    this.leftArm = new ModelRenderer(this, 46, 15);
    this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 13, 4);
    this.leftArm.setRotationPoint(6.0F, 2.0F, 0.0F);
    this.leftArm.setTextureSize(64, 32);
    this.leftArm.mirror = true;
    setRotation(this.leftArm, 0.0F, 0.0F, 0.0F);
    this.rightLeg = new ModelRenderer(this, 0, 16);
    this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
    this.rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
    this.rightLeg.setTextureSize(64, 32);
    this.rightLeg.mirror = true;
    setRotation(this.rightLeg, 0.0F, 0.0F, 0.0F);
    this.leftLeg = new ModelRenderer(this, 0, 16);
    this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
    this.leftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
    this.leftLeg.setTextureSize(64, 32);
    this.leftLeg.mirror = true;
    setRotation(this.leftLeg, 0.0F, 0.0F, 0.0F);

    this.headwear = new ModelRenderer(this, 32, 0);
    this.headwear.addBox(-3.533333F, -7.0F, -3.5F, 7, 7, 7, 0.5F);
    this.headwear.setRotationPoint(0.0F, 0.0F, 0.0F);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    this.head.render(f5);
    this.body.render(f5);
    this.rightArm.render(f5);
    this.leftArm.render(f5);
    this.rightLeg.render(f5);
    this.leftLeg.render(f5);
  }

  public void setSneaking(boolean flag)
  {
    this.isSneaking = flag;
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
  {
    this.head.rotateAngleY = (par4 / 57.295776F);
    this.head.rotateAngleX = (par5 / 57.295776F);
    this.headwear.rotateAngleY = this.head.rotateAngleY;
    this.headwear.rotateAngleX = this.head.rotateAngleX;
    this.rightArm.rotateAngleX = (MathHelper.cos(par1 * 0.6662F + 3.141593F) * 2.0F * par2 * 0.5F);
    this.leftArm.rotateAngleX = (MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F);
    this.rightArm.rotateAngleZ = 0.0F;
    this.leftArm.rotateAngleZ = 0.0F;
    this.rightLeg.rotateAngleX = (MathHelper.cos(par1 * 0.6662F) * 1.4F * par2);
    this.leftLeg.rotateAngleX = (MathHelper.cos(par1 * 0.6662F + 3.141593F) * 1.4F * par2);
    this.rightLeg.rotateAngleY = 0.0F;
    this.leftLeg.rotateAngleY = 0.0F;

    if (this.isRiding)
    {
      this.rightArm.rotateAngleX += -0.6283186F;
      this.leftArm.rotateAngleX += -0.6283186F;
      this.rightLeg.rotateAngleX = -1.256637F;
      this.leftLeg.rotateAngleX = -1.256637F;
      this.rightLeg.rotateAngleY = 0.3141593F;
      this.leftLeg.rotateAngleY = -0.3141593F;
    }

    if (this.heldItemLeft != 0)
    {
      this.leftArm.rotateAngleX = (this.leftArm.rotateAngleX * 0.5F - 0.3141593F * this.heldItemLeft);
    }

    if (this.heldItemRight != 0)
    {
      this.rightArm.rotateAngleX = (this.rightArm.rotateAngleX * 0.5F - 0.3141593F * this.heldItemRight);
    }

    this.rightArm.rotateAngleY = 0.0F;
    this.leftArm.rotateAngleY = 0.0F;

    if (this.onGround > -9990.0F)
    {
      float f = this.onGround;
      this.body.rotateAngleY = (MathHelper.sin(MathHelper.sqrt_float(f) * 3.141593F * 2.0F) * 0.2F);
      this.rightArm.rotationPointZ = (MathHelper.sin(this.body.rotateAngleY) * 5.0F);
      this.rightArm.rotationPointX = (-MathHelper.cos(this.body.rotateAngleY) * 5.0F);
      this.leftArm.rotationPointZ = (-MathHelper.sin(this.body.rotateAngleY) * 5.0F);
      this.leftArm.rotationPointX = (MathHelper.cos(this.body.rotateAngleY) * 5.0F);
      this.rightArm.rotateAngleY += this.body.rotateAngleY;
      this.leftArm.rotateAngleY += this.body.rotateAngleY;
      this.leftArm.rotateAngleX += this.body.rotateAngleY;
      f = 1.0F - this.onGround;
      f *= f;
      f *= f;
      f = 1.0F - f;
      float f2 = MathHelper.sin(f * 3.141593F);
      float f4 = MathHelper.sin(this.onGround * 3.141593F) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
      ModelRenderer tmp570_567 = this.rightArm; tmp570_567.rotateAngleX = ((float)(tmp570_567.rotateAngleX - (f2 * 1.2D + f4)));
      this.rightArm.rotateAngleY += this.body.rotateAngleY * 2.0F;
      this.rightArm.rotateAngleZ = (MathHelper.sin(this.onGround * 3.141593F) * -0.4F);
    }

    if (this.isSneaking)
    {
      this.body.rotateAngleX = 0.7F;
      this.body.rotationPointY = 1.5F;
      this.rightLeg.rotateAngleX -= 0.0F;
      this.leftLeg.rotateAngleX -= 0.0F;
      this.rightArm.rotateAngleX += 0.4F;
      this.leftArm.rotateAngleX += 0.4F;
      this.rightLeg.rotationPointZ = 7.0F;
      this.leftLeg.rotationPointZ = 7.0F;
      this.rightLeg.rotationPointY = 12.0F;
      this.leftLeg.rotationPointY = 12.0F;
      this.rightArm.rotationPointY = 3.5F;
      this.leftArm.rotationPointY = 3.5F;
      this.head.rotationPointY = 3.0F;
    }
    else
    {
      this.body.rotateAngleX = 0.0F;
      this.body.rotationPointY = 0.0F;
      this.rightLeg.rotationPointZ = 0.0F;
      this.leftLeg.rotationPointZ = 0.0F;
      this.rightLeg.rotationPointY = 12.0F;
      this.leftLeg.rotationPointY = 12.0F;
      this.rightArm.rotationPointY = 2.0F;
      this.leftArm.rotationPointY = 2.0F;
      this.head.rotationPointY = 0.0F;
      this.rightArm.rotationPointX = -6.0F;
      this.leftArm.rotationPointX = 6.0F;
    }

    this.rightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
    this.leftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
    this.rightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
    this.leftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

    if (this.aimedBow)
    {
      float f1 = 0.0F;
      float f3 = 0.0F;
      this.rightArm.rotateAngleZ = 0.0F;
      this.leftArm.rotateAngleZ = 0.0F;
      this.rightArm.rotateAngleY = (-(0.1F - f1 * 0.6F) + this.head.rotateAngleY);
      this.leftArm.rotateAngleY = (0.1F - f1 * 0.6F + this.head.rotateAngleY + 0.4F);
      this.rightArm.rotateAngleX = (-1.570796F + this.head.rotateAngleX);
      this.leftArm.rotateAngleX = (-1.570796F + this.head.rotateAngleX);
      this.rightArm.rotateAngleX -= f1 * 1.2F - f3 * 0.4F;
      this.leftArm.rotateAngleX -= f1 * 1.2F - f3 * 0.4F;
      this.rightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.leftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.rightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.leftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
    }
  }

  public void itemArmPostRender(float scale)
  {
    this.rightArm.postRender(scale);
  }
}