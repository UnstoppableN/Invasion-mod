package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelImp extends ModelBase
{
  ModelRenderer head;
  ModelRenderer body;
  ModelRenderer rightarm;
  ModelRenderer leftarm;
  ModelRenderer rightleg;
  ModelRenderer leftleg;
  ModelRenderer rshin;
  ModelRenderer rfoot;
  ModelRenderer lshin;
  ModelRenderer lfoot;
  ModelRenderer rhorn;
  ModelRenderer lhorn;
  ModelRenderer bodymid;
  ModelRenderer neck;
  ModelRenderer bodychest;
  ModelRenderer tail;
  ModelRenderer tail2;

  public ModelImp()
  {
    this(0.0F);
  }

  public ModelImp(float f)
  {
    this(f, 0.0F);
  }

  public ModelImp(float f, float f1)
  {
    this.head = new ModelRenderer(this, 44, 0);
    this.head.addBox(-2.733333F, -3.0F, -2.0F, 5, 3, 4);
    this.head.setRotationPoint(-0.4F, 9.8F, -3.3F);
    this.head.rotateAngleX = 0.15807F;
    this.head.rotateAngleY = 0.0F;
    this.head.rotateAngleZ = 0.0F;
    this.head.mirror = false;
    this.body = new ModelRenderer(this, 23, 1);
    this.body.addBox(-4.0F, 0.0F, -4.0F, 7, 4, 3);
    this.body.setRotationPoint(0.0F, 9.1F, -0.8666667F);
    this.body.rotateAngleX = 0.64346F;
    this.body.rotateAngleY = 0.0F;
    this.body.rotateAngleZ = 0.0F;
    this.body.mirror = false;
    this.rightarm = new ModelRenderer(this, 26, 9);
    this.rightarm.addBox(-2.0F, -0.7333333F, -1.133333F, 2, 7, 2);
    this.rightarm.setRotationPoint(-4.0F, 10.8F, -2.066667F);
    this.rightarm.rotateAngleX = 0.0F;
    this.rightarm.rotateAngleY = 0.0F;
    this.rightarm.rotateAngleZ = 0.0F;
    this.rightarm.mirror = false;
    this.leftarm = new ModelRenderer(this, 18, 9);
    this.leftarm.addBox(0.0F, -0.8666667F, -1.0F, 2, 7, 2);
    this.leftarm.setRotationPoint(3.0F, 10.8F, -2.1F);
    this.leftarm.rotateAngleX = 0.0F;
    this.leftarm.rotateAngleY = 0.0F;
    this.leftarm.rotateAngleZ = 0.0F;
    this.leftarm.mirror = false;
    this.rightleg = new ModelRenderer(this, 0, 17);
    this.rightleg.addBox(-1.0F, 0.0F, -2.0F, 2, 4, 3);
    this.rightleg.setRotationPoint(-2.0F, 16.9F, -1.0F);
    this.rightleg.rotateAngleX = -0.15807F;
    this.rightleg.rotateAngleY = 0.0F;
    this.rightleg.rotateAngleZ = 0.0F;
    this.rightleg.mirror = false;
    this.leftleg = new ModelRenderer(this, 0, 24);
    this.leftleg.addBox(-1.0F, 0.0F, -2.0F, 2, 4, 3);
    this.leftleg.setRotationPoint(1.0F, 17.0F, -1.0F);
    this.leftleg.rotateAngleX = -0.15919F;
    this.leftleg.rotateAngleY = 0.0F;
    this.leftleg.rotateAngleZ = 0.0F;
    this.leftleg.mirror = false;
    this.rshin = new ModelRenderer(this, 10, 17);
    this.rshin.addBox(-2.0F, 0.6F, -4.4F, 2, 3, 2);
    this.rshin.setRotationPoint(-1.0F, 16.9F, -1.0F);
    this.rshin.rotateAngleX = 0.82623F;
    this.rshin.rotateAngleY = 0.0F;
    this.rshin.rotateAngleZ = 0.0F;
    this.rshin.mirror = false;
    this.rfoot = new ModelRenderer(this, 18, 18);
    this.rfoot.addBox(-2.0F, 4.2F, -1.0F, 2, 3, 2);
    this.rfoot.setRotationPoint(-1.0F, 16.9F, -1.0F);
    this.rfoot.rotateAngleX = -0.01403F;
    this.rfoot.rotateAngleY = 0.0F;
    this.rfoot.rotateAngleZ = 0.0F;
    this.rfoot.mirror = false;
    this.lshin = new ModelRenderer(this, 10, 22);
    this.lshin.addBox(-1.0F, 0.6F, -4.433333F, 2, 3, 2);
    this.lshin.setRotationPoint(1.0F, 17.0F, -1.0F);
    this.lshin.rotateAngleX = 0.82461F;
    this.lshin.rotateAngleY = 0.0F;
    this.lshin.rotateAngleZ = 0.0F;
    this.lshin.mirror = false;
    this.lfoot = new ModelRenderer(this, 10, 27);
    this.lfoot.addBox(-1.0F, 4.2F, -1.0F, 2, 3, 2);
    this.lfoot.setRotationPoint(1.0F, 17.0F, -1.0F);
    this.lfoot.rotateAngleX = -0.01214F;
    this.lfoot.rotateAngleY = 0.0F;
    this.lfoot.rotateAngleZ = 0.0F;
    this.lfoot.mirror = false;
    
    this.rhorn = new ModelRenderer(this, 0, 0);
    this.rhorn.addBox(1.0F, -4.0F, 1.5F, 1, 1, 1);
    this.rhorn.setRotationPoint(-0.4F, 0F, -3.3F);
    this.rhorn.mirror = false;
    
    this.lhorn = new ModelRenderer(this, 0, 2);
    this.lhorn.addBox(-1.0F, -4.0F, 1.5F, 1, 1, 1);
    this.lhorn.setRotationPoint(-0.4F, 0F, -3.3F);
    this.lhorn.mirror = false;
    
    this.bodymid = new ModelRenderer(this, 1, 1);
    this.bodymid.addBox(0.0F, 0.0F, 0.0F, 7, 5, 3);
    this.bodymid.setRotationPoint(-4.0F, 12.46667F, -2.266667F);
    this.bodymid.rotateAngleX = -0.15807F;
    this.bodymid.rotateAngleY = 0.0F;
    this.bodymid.rotateAngleZ = 0.0F;
    this.bodymid.mirror = false;
    this.neck = new ModelRenderer(this, 44, 7);
    this.neck.addBox(0.0F, 0.0F, 0.0F, 3, 2, 2);
    this.neck.setRotationPoint(-2.0F, 9.6F, -4.033333F);
    this.neck.rotateAngleX = 0.27662F;
    this.neck.rotateAngleY = 0.0F;
    this.neck.rotateAngleZ = 0.0F;
    this.neck.mirror = false;
    this.bodychest = new ModelRenderer(this, 0, 9);
    this.bodychest.addBox(0.0F, -1.0F, 0.0F, 7, 6, 2);
    this.bodychest.setRotationPoint(-4.0F, 12.36667F, -3.8F);
    this.bodychest.rotateAngleX = 0.31614F;
    this.bodychest.rotateAngleY = 0.0F;
    this.bodychest.rotateAngleZ = 0.0F;
    this.bodychest.mirror = false;
    this.tail = new ModelRenderer(this, 18, 23);
    this.tail.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
    this.tail.setRotationPoint(-1.0F, 15.0F, -0.6666667F);
    this.tail.rotateAngleX = 0.47304F;
    this.tail.rotateAngleY = 0.0F;
    this.tail.rotateAngleZ = 0.0F;
    this.tail.mirror = false;
    this.tail2 = new ModelRenderer(this, 22, 23);
    this.tail2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
    this.tail2.setRotationPoint(-1.0F, 22.1F, 2.9F);
    this.tail2.rotateAngleX = 1.38309F;
    this.tail2.rotateAngleY = 0.0F;
    this.tail2.rotateAngleZ = 0.0F;
    this.tail2.mirror = false;
    
    this.head.addChild(lhorn);
    this.head.addChild(rhorn);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    this.head.render(f5);
    this.body.render(f5);
    this.rightarm.render(f5);
    this.leftarm.render(f5);
    this.rightleg.render(f5);
    this.leftleg.render(f5);
    this.rshin.render(f5);
    this.rfoot.render(f5);
    this.lshin.render(f5);
    this.lfoot.render(f5);
//    this.rhorn.render(f5);
//    this.lhorn.render(f5);
    this.bodymid.render(f5);
    this.neck.render(f5);
    this.bodychest.render(f5);
    this.tail.render(f5);
    this.tail2.render(f5);
  }

  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    this.head.rotateAngleY = (f3 / 57.29578F);
    this.head.rotateAngleX = (f4 / 57.29578F);
    
    
    this.rightarm.rotateAngleX = (MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F);
    this.leftarm.rotateAngleX = (MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F);
    this.rightarm.rotateAngleZ = 0.0F;
    this.leftarm.rotateAngleZ = 0.0F;

    this.rightleg.rotateAngleX = (MathHelper.cos(f * 0.6662F) * 1.4F * f1 - 0.158F);
    this.rshin.rotateAngleX = (MathHelper.cos(f * 0.6662F) * 1.4F * f1 + 0.82623F);
    this.rfoot.rotateAngleX = (MathHelper.cos(f * 0.6662F) * 1.4F * f1 - 0.01403F);

    this.leftleg.rotateAngleX = (MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1 - 0.15919F);
    this.lshin.rotateAngleX = (MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1 + 0.82461F);
    this.lfoot.rotateAngleX = (MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1 - 0.01214F);

    this.rightleg.rotateAngleY = 0.0F;
    this.rshin.rotateAngleY = 0.0F;
    this.rfoot.rotateAngleY = 0.0F;

    this.leftleg.rotateAngleY = 0.0F;
    this.lshin.rotateAngleY = 0.0F;
    this.lfoot.rotateAngleY = 0.0F;

    this.rightarm.rotateAngleY = 0.0F;
    this.leftarm.rotateAngleY = 0.0F;

    this.rightarm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
    this.leftarm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
    this.rightarm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
    this.leftarm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
  }
}