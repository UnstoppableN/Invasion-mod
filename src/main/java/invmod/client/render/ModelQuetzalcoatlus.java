package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelQuetzalcoatlus extends ModelBase
{
//fields
ModelRenderer Tail;
ModelRenderer LowerBody;
ModelRenderer UpperBody;
ModelRenderer Neck1;
ModelRenderer Neck2;
ModelRenderer TailTip;
ModelRenderer LeftLefPiece;
ModelRenderer RightLegPiece;
ModelRenderer HeadPiece;
ModelRenderer RightWingPiece;
ModelRenderer RightWingPiece2;
ModelRenderer RightWingPiece3;
ModelRenderer LeftWingPiece;
ModelRenderer LeftWingPiece2;
ModelRenderer LeftWingPiece3;

public ModelQuetzalcoatlus()
{
textureWidth = 500;
textureHeight = 150;

Tail = new ModelRenderer(this, 3, 93);
Tail.addBox(0F, -2F, 0F, 3, 2, 5);
Tail.setRotationPoint(-1.5F, 18.5F, 11F);
Tail.setTextureSize(500, 150);
Tail.mirror = true;
setRotation(Tail, 0F, 0F, 0F);
LowerBody = new ModelRenderer(this, 108, 104);
LowerBody.addBox(-4.5F, -2F, 11F, 9, 7, 7);
LowerBody.setRotationPoint(0F, 18F, -7F);
LowerBody.setTextureSize(500, 150);
LowerBody.mirror = true;
setRotation(LowerBody, 0F, 0F, 0F);
UpperBody = new ModelRenderer(this, 95, 81);
UpperBody.addBox(-5F, -2F, 0F, 10, 8, 11);
UpperBody.setRotationPoint(0F, 18F, -7F);
UpperBody.setTextureSize(500, 150);
UpperBody.mirror = true;
setRotation(UpperBody, 0F, 0F, 0F);
Neck1 = new ModelRenderer(this, 1, 73);
Neck1.addBox(-2.5F, -5F, -11F, 5, 7, 10);
Neck1.setRotationPoint(0F, 21F, -6F);
Neck1.setTextureSize(500, 150);
Neck1.mirror = true;
setRotation(Neck1, 0F, 0F, 0F);
Neck2 = new ModelRenderer(this, 28, 73);
Neck2.addBox(-1F, -4F, -33F, 4, 6, 23);
Neck2.setRotationPoint(-1F, 20F, -7F);
Neck2.setTextureSize(500, 150);
Neck2.mirror = true;
setRotation(Neck2, 0F, 0F, 0F);
TailTip = new ModelRenderer(this, 3, 104);
TailTip.addBox(0F, -3F, 4F, 2, 2, 3);
TailTip.setRotationPoint(-1F, 19.5F, 12F);
TailTip.setTextureSize(500, 150);
TailTip.mirror = true;
setRotation(TailTip, 0F, 0F, 0F);
LeftLefPiece = new ModelRenderer(this, "LeftLefPiece");
LeftLefPiece.setRotationPoint(3F, 19F, 10F);
setRotation(LeftLefPiece, 0F, 0F, 0F);
LeftLefPiece.mirror = true;
/* error*/ //LeftLeg2.mirror = true;
LeftLefPiece.addBox( 0F, -2F, 8F, 2, 2, 14);
/* error*/ //LeftLeg2.mirror = false;
LeftLefPiece.addBox( 0F, -2F, -2F, 2, 4, 10);
LeftLefPiece.addBox(-0.5F, -2F, 22F, 3, 1, 4);
/* error*/ //modelQuetz.addChildModelRenderer(LeftLefPiece);
RightLegPiece = new ModelRenderer(this, "RightLegPiece");
RightLegPiece.setRotationPoint(-3F, 19F, 10F);
setRotation(RightLegPiece, 0F, 0F, 0F);
RightLegPiece.mirror = true;
/* error*/ //RightLeg1.mirror = true;
RightLegPiece.addBox( -2F, -2F, -2F, 2, 4, 10);
/* error*/ //RightLeg1.mirror = false;
RightLegPiece.addBox( -2F, -2F, 8F, 2, 2, 14);
RightLegPiece.addBox( -2.5F, -2F, 22F, 3, 1, 4);
/* error*/ //modelQuetz.addChildModelRenderer(RightLegPiece);
HeadPiece = new ModelRenderer(this, "HeadPiece");
HeadPiece.setRotationPoint(0F, 19F, -40F);
setRotation(HeadPiece, 0F, 0F, 0F);
HeadPiece.mirror = true;
HeadPiece.addBox( -2.5F, -3.5F, -11F, 5, 7, 11);
HeadPiece.addBox( 0F, -9F, -13F, 0, 7, 16);
HeadPiece.addBox( -2F, -2.5F, -21F, 4, 6, 10);
HeadPiece.addBox( -1.5F, -1.5F, -30F, 3, 5, 9);
/* error*/ //modelQuetz.addChildModelRenderer(HeadPiece);
RightWingPiece = new ModelRenderer(this, "RightWingPiece");
RightWingPiece.setRotationPoint(-5F, 18F, -5F);
setRotation(RightWingPiece, 0F, 0F, 0F);
RightWingPiece.mirror = true;
RightWingPiece.addBox( -8F, 0F, 3F, 9, 1, 29);
RightWingPiece.addBox( -8F, -1.5F, -2F, 8, 4, 5);
/* error*/ RightWingPiece2 = new ModelRenderer(this, "RightWingPiece2");
/* error*/ RightWingPiece2.setRotationPoint(-8F, 0F, 0F);
/* error*/ setRotation(RightWingPiece2, 0F, 0F, 0F);
/* error*/ RightWingPiece2.mirror = true;
/* error*/ RightWingPiece2.addBox( -15F, -1F, -2F, 15, 3, 4);
/* error*/ RightWingPiece2.addBox( -15F, 0F, 2F, 15, 1, 30);
/* error*/ RightWingPiece3 = new ModelRenderer(this, "RightWingPiece3");
/* error*/ RightWingPiece3.setRotationPoint(-12F, 0F, -1F);
/* error*/ setRotation(RightWingPiece3, 0F, 0F, 0F);
/* error*/ RightWingPiece3.mirror = true;
/* error*/ RightWingPiece3.addBox( -21F, -1F, -1F, 18, 3, 3);
/* error*/ RightWingPiece3.addBox( -21F, 0F, 2F, 18, 1, 31);
/* error*/ RightWingPiece3.addBox( -57F, -0.5F, -1F, 36, 2, 2);
/* error*/ RightWingPiece3.addBox( -57F, 0F, 1F, 36, 1, 32);
/* error*/ RightWingPiece3.addBox( -21F, 0F, -4F, 6, 1, 3);
/* error*/ RightWingPiece2.addChild(RightWingPiece3);
RightWingPiece.addChild(RightWingPiece2);
/* error*/ //modelQuetz.addChildModelRenderer(RightWingPiece);
LeftWingPiece = new ModelRenderer(this, "LeftWingPiece");
LeftWingPiece.setRotationPoint(5F, 18F, -5F);
setRotation(LeftWingPiece, 0F, 0F, 0F);
LeftWingPiece.mirror = true;
LeftWingPiece.addBox( 0F, -1.5F, -2F, 8, 4, 5);
LeftWingPiece.addBox( -1F, 0F, 3F, 9, 1, 29);
/* error*/ LeftWingPiece2 = new ModelRenderer(this, "LeftWingPiece2");
/* error*/ LeftWingPiece2.setRotationPoint(8F, 0F, 0F);
/* error*/ setRotation(LeftWingPiece2, 0F, 0F, 0F);
/* error*/ LeftWingPiece2.mirror = true;
/* error*/ LeftWingPiece2.addBox( 0F, 0F, 2F, 15, 1, 30);
/* error*/ LeftWingPiece2.addBox( 0F, -1F, -2F, 15, 3, 4);
/* error*/ LeftWingPiece3 = new ModelRenderer(this, "LeftWingPiece3");
/* error*/ LeftWingPiece3.setRotationPoint(15F, 0F, -1F);
/* error*/ setRotation(LeftWingPiece3, 0F, 0F, 0F);
/* error*/ LeftWingPiece3.mirror = true;
/* error*/ LeftWingPiece3.addBox( 0F, -1F, -1F, 18, 3, 3);
/* error*/ LeftWingPiece3.addBox( 0F, 0F, 2F, 18, 1, 31);
/* error*/ LeftWingPiece3.addBox( 18F, 0F, 1F, 36, 1, 32);
/* error*/ LeftWingPiece3.addBox( 18F, -0.5F, -0.5F, 36, 2, 2);
/* error*/ LeftWingPiece3.addBox( 12F, 0F, -4F, 6, 1, 3);
/* error*/ LeftWingPiece2.addChild(LeftWingPiece3);
LeftWingPiece.addChild(LeftWingPiece2);
/* error*/ //modelQuetz.addChildModelRenderer(LeftWingPiece);
}

public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
{
super.render(entity, f, f1, f2, f3, f4, f5);
setRotationAngles(f, f1, f2, f3, f4, f5);
//Tail.render(f5);
//LowerBody.render(f5);
//UpperBody.render(f5);
//Neck1.render(f5);
//Neck2.render(f5);
//TailTip.render(f5);
//LeftLefPiece.render(f5);
//RightLegPiece.render(f5);
//HeadPiece.render(f5);
//RightWingPiece.render(f5);
//LeftWingPiece.render(f5);

Tail.render(f5);
LowerBody.render(f5);
UpperBody.render(f5);
Neck1.render(f5);
Neck2.render(f5);
TailTip.render(f5);
LeftLefPiece.render(f5);
RightLegPiece.render(f5);
HeadPiece.render(f5);
RightWingPiece.render(f5);
RightWingPiece2.render(f5);
RightWingPiece3.render(f5);
LeftWingPiece.render(f5);
LeftWingPiece2.render(f5);
LeftWingPiece3.render(f5);
}

private void setRotation(ModelRenderer model, float x, float y, float z)
{
model.rotateAngleX = x;
model.rotateAngleY = y;
model.rotateAngleZ = z;
}

public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
{
 super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
}

}

