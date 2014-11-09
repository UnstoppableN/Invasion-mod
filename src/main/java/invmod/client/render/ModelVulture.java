package invmod.client.render;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.client.render.animation.BonesBirdLegs;
import invmod.client.render.animation.BonesMouth;
import invmod.client.render.animation.BonesWings;
import invmod.client.render.animation.ModelAnimator;
import invmod.common.util.MathUtil;
import java.util.EnumMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelVulture extends ModelBase
{
  private ModelAnimator animationFlap;
  private ModelAnimator animationRun;
  private ModelAnimator animationBeak;
  ModelRenderer body;
  ModelRenderer rightThigh;
  ModelRenderer rightLeg;
  ModelRenderer rightAnkle;
  ModelRenderer rightToeB;
  ModelRenderer rightClawB;
  ModelRenderer rightToeL;
  ModelRenderer rightClawL;
  ModelRenderer rightToeM;
  ModelRenderer rightClawM;
  ModelRenderer rightToeR;
  ModelRenderer rightClawR;
  ModelRenderer leftThigh;
  ModelRenderer leftLeg;
  ModelRenderer leftAnkle;
  ModelRenderer leftToeB;
  ModelRenderer leftClawB;
  ModelRenderer leftToeL;
  ModelRenderer leftClawL;
  ModelRenderer leftToeM;
  ModelRenderer leftClawM;
  ModelRenderer leftToeR;
  ModelRenderer leftClawR;
  ModelRenderer neck1;
  ModelRenderer neck2;
  ModelRenderer neck3;
  ModelRenderer head;
  ModelRenderer upperBeak;
  ModelRenderer upperBeakTip;
  ModelRenderer lowerBeak;
  ModelRenderer lowerBeakTip;
  ModelRenderer leftWing1;
  ModelRenderer leftWing2;
  ModelRenderer leftWing3;
  ModelRenderer tail;
  ModelRenderer rightWing1;
  ModelRenderer rightWing2;
  ModelRenderer rightWing3;

  public ModelVulture()
  {
    this(0.0F);
  }

  public ModelVulture(float par1)
  {
    this.body = new ModelRenderer(this, 0, 0);
    this.body.setTextureSize(128, 128);
    this.body.addBox(-10.0F, -10.0F, -10.0F, 20, 30, 20);
    this.body.setRotationPoint(0.0F, -19.0F, 0.0F);
    this.rightThigh = new ModelRenderer(this, 84, 82);
    this.rightThigh.setTextureSize(128, 128);
    this.rightThigh.addBox(-4.5F, -3.5F, -4.5F, 9, 15, 9);
    this.rightThigh.setRotationPoint(-5.0F, 20.0F, -2.0F);
    this.rightLeg = new ModelRenderer(this, 56, 50);
    this.rightLeg.setTextureSize(128, 128);
    this.rightLeg.addBox(-2.0F, -3.0F, -2.0F, 4, 16, 4);
    this.rightLeg.setRotationPoint(0.0F, 11.0F, 0.0F);
    this.rightAnkle = new ModelRenderer(this, 16, 16);
    this.rightAnkle.setTextureSize(128, 128);
    this.rightAnkle.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0);
    this.rightAnkle.setRotationPoint(0.0F, 12.0F, 0.0F);
    this.rightToeB = new ModelRenderer(this, 60, 0);
    this.rightToeB.setTextureSize(128, 128);
    this.rightToeB.addBox(-1.0F, -1.0F, -1.0F, 2, 8, 2);
    this.rightToeB.setRotationPoint(0.0F, 0.0F, 2.0F);
    this.rightClawB = new ModelRenderer(this, 0, 11);
    this.rightClawB.setTextureSize(128, 128);
    this.rightClawB.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.rightClawB.setRotationPoint(0.0F, 6.0F, 0.0F);
    this.rightToeL = new ModelRenderer(this, 0, 0);
    this.rightToeL.setTextureSize(128, 128);
    this.rightToeL.addBox(-1.0F, 0.5F, -1.0F, 2, 9, 2);
    this.rightToeL.setRotationPoint(-0.5F, 0.0F, 1.0F);
    this.rightClawL = new ModelRenderer(this, 0, 11);
    this.rightClawL.setTextureSize(128, 128);
    this.rightClawL.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.rightClawL.setRotationPoint(0.0F, 9.0F, 0.0F);
    this.rightToeM = new ModelRenderer(this, 8, 0);
    this.rightToeM.setTextureSize(128, 128);
    this.rightToeM.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2);
    this.rightToeM.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.rightClawM = new ModelRenderer(this, 0, 11);
    this.rightClawM.setTextureSize(128, 128);
    this.rightClawM.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.rightClawM.setRotationPoint(0.0F, 9.0F, 0.0F);
    this.rightToeR = new ModelRenderer(this, 0, 0);
    this.rightToeR.setTextureSize(128, 128);
    this.rightToeR.addBox(-1.0F, -0.5F, -1.0F, 2, 9, 2);
    this.rightToeR.setRotationPoint(1.0F, 0.0F, 1.0F);
    this.rightClawR = new ModelRenderer(this, 0, 11);
    this.rightClawR.setTextureSize(128, 128);
    this.rightClawR.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.rightClawR.setRotationPoint(0.0F, 8.0F, 0.0F);
    this.leftThigh = new ModelRenderer(this, 84, 82);
    this.leftThigh.setTextureSize(128, 128);
    this.leftThigh.addBox(-4.5F, -3.5F, -4.5F, 9, 15, 9);
    this.leftThigh.setRotationPoint(5.0F, 20.0F, -2.0F);
    this.leftThigh.mirror = true;
    this.leftLeg = new ModelRenderer(this, 56, 50);
    this.leftLeg.setTextureSize(128, 128);
    this.leftLeg.addBox(-2.0F, -3.0F, -2.0F, 4, 16, 4);
    this.leftLeg.setRotationPoint(0.0F, 11.0F, 0.0F);
    this.leftLeg.mirror = true;
    this.leftAnkle = new ModelRenderer(this, 16, 16);
    this.leftAnkle.setTextureSize(128, 128);
    this.leftAnkle.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0);
    this.leftAnkle.setRotationPoint(0.0F, 12.0F, 0.0F);
    this.leftToeB = new ModelRenderer(this, 60, 0);
    this.leftToeB.setTextureSize(128, 128);
    this.leftToeB.addBox(-1.0F, -1.0F, -1.0F, 2, 8, 2);
    this.leftToeB.setRotationPoint(0.0F, 0.0F, 2.0F);
    this.leftClawB = new ModelRenderer(this, 0, 11);
    this.leftClawB.setTextureSize(128, 128);
    this.leftClawB.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.leftClawB.setRotationPoint(0.0F, 6.0F, 0.0F);
    this.leftToeL = new ModelRenderer(this, 0, 0);
    this.leftToeL.setTextureSize(128, 128);
    this.leftToeL.addBox(-1.0F, 0.5F, -1.0F, 2, 9, 2);
    this.leftToeL.setRotationPoint(0.5F, 0.0F, 1.0F);
    this.leftClawL = new ModelRenderer(this, 0, 11);
    this.leftClawL.setTextureSize(128, 128);
    this.leftClawL.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.leftClawL.setRotationPoint(0.0F, 9.0F, 0.0F);
    this.leftToeM = new ModelRenderer(this, 8, 0);
    this.leftToeM.setTextureSize(128, 128);
    this.leftToeM.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2);
    this.leftToeM.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.leftClawM = new ModelRenderer(this, 0, 11);
    this.leftClawM.setTextureSize(128, 128);
    this.leftClawM.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.leftClawM.setRotationPoint(0.0F, 9.0F, 0.0F);
    this.leftToeR = new ModelRenderer(this, 0, 0);
    this.leftToeR.setTextureSize(128, 128);
    this.leftToeR.addBox(-1.0F, -0.5F, -1.0F, 2, 9, 2);
    this.leftToeR.setRotationPoint(-1.0F, 0.0F, 1.0F);
    this.leftClawR = new ModelRenderer(this, 0, 11);
    this.leftClawR.setTextureSize(128, 128);
    this.leftClawR.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2);
    this.leftClawR.setRotationPoint(0.0F, 8.0F, 0.0F);
    this.neck1 = new ModelRenderer(this, 43, 95);
    this.neck1.setTextureSize(128, 128);
    this.neck1.addBox(-7.0F, -7.0F, -6.5F, 14, 10, 13);
    this.neck1.setRotationPoint(0.0F, -10.0F, 1.0F);
    this.neck2 = new ModelRenderer(this, 50, 73);
    this.neck2.setTextureSize(128, 128);
    this.neck2.addBox(-5.0F, -4.0F, -5.0F, 10, 8, 10);
    this.neck2.setRotationPoint(0.0F, -8.0F, 0.0F);
    this.neck3 = new ModelRenderer(this, 80, 65);
    this.neck3.setTextureSize(128, 128);
    this.neck3.addBox(-4.0F, -5.5F, -5.0F, 8, 5, 10);
    this.neck3.setRotationPoint(0.0F, -2.0F, 0.0F);
    this.head = new ModelRenderer(this, 14, 108);
    this.head.setTextureSize(128, 128);
    this.head.addBox(-4.5F, -5.0F, -9.5F, 9, 8, 11);
    this.head.setRotationPoint(0.0F, -4.0F, 0.0F);
    this.upperBeak = new ModelRenderer(this, 54, 118);
    this.upperBeak.setTextureSize(128, 128);
    this.upperBeak.addBox(-2.5F, -1.0F, -5.0F, 5, 2, 8);
    this.upperBeak.setRotationPoint(0.0F, -0.8F, -10.0F);
    this.upperBeakTip = new ModelRenderer(this, 72, 118);
    this.upperBeakTip.setTextureSize(128, 128);
    this.upperBeakTip.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
    this.upperBeakTip.setRotationPoint(0.0F, 0.0F, -6.0F);
    this.lowerBeak = new ModelRenderer(this, 80, 118);
    this.lowerBeak.setTextureSize(128, 128);
    this.lowerBeak.addBox(-2.5F, -1.0F, -5.0F, 5, 2, 8);
    this.lowerBeak.setRotationPoint(0.0F, 1.5F, -10.0F);
    this.lowerBeakTip = new ModelRenderer(this, 78, 121);
    this.lowerBeakTip.setTextureSize(128, 128);
    this.lowerBeakTip.addBox(-1.0F, -0.5F, -1.0F, 2, 1, 2);
    this.lowerBeakTip.setRotationPoint(0.0F, -0.5F, -6.0F);
    this.leftWing1 = new ModelRenderer(this, 0, 50);
    this.leftWing1.mirror = true;
    this.leftWing1.setTextureSize(128, 128);
    this.leftWing1.addBox(-0.5F, -4.5F, -1.5F, 25, 29, 3);
    this.leftWing1.setRotationPoint(7.0F, -8.0F, 6.0F);
    this.leftWing2 = new ModelRenderer(this, 0, 82);
    this.leftWing2.mirror = true;
    this.leftWing2.setTextureSize(128, 128);
    this.leftWing2.addBox(-2.5F, -5.0F, -1.0F, 23, 24, 2);
    this.leftWing2.setRotationPoint(23.0F, 1.0F, 0.0F);
    this.leftWing3 = new ModelRenderer(this, 80, 0);
    this.leftWing3.mirror = true;
    this.leftWing3.setTextureSize(128, 128);
    this.leftWing3.addBox(-2.5F, -5.0F, -0.5F, 23, 22, 1);
    this.leftWing3.setRotationPoint(21.0F, 0.2F, 0.3F);
    this.tail = new ModelRenderer(this, 80, 23);
    this.tail.setTextureSize(128, 128);
    this.tail.addBox(-8.5F, -5.0F, -1.0F, 17, 40, 2);
    this.tail.setRotationPoint(0.0F, 19.0F, 8.0F);
    this.rightWing1 = new ModelRenderer(this, 0, 50);
    this.rightWing1.setTextureSize(128, 128);
    this.rightWing1.addBox(-24.5F, -4.5F, -1.5F, 25, 29, 3);
    this.rightWing1.setRotationPoint(-7.0F, -8.0F, 6.0F);
    this.rightWing2 = new ModelRenderer(this, 0, 82);
    this.rightWing2.setTextureSize(128, 128);
    this.rightWing2.addBox(-20.5F, -5.0F, -1.0F, 23, 24, 2);
    this.rightWing2.setRotationPoint(-23.0F, 1.0F, 0.0F);
    this.rightWing3 = new ModelRenderer(this, 80, 0);
    this.rightWing3.setTextureSize(128, 128);
    this.rightWing3.addBox(-20.5F, -5.0F, -0.5F, 23, 22, 1);
    this.rightWing3.setRotationPoint(-21.0F, 0.2F, 0.3F);

    this.rightToeB.addChild(this.rightClawB);
    this.rightToeR.addChild(this.rightClawR);
    this.rightToeM.addChild(this.rightClawM);
    this.rightToeL.addChild(this.rightClawL);
    this.leftToeB.addChild(this.leftClawB);
    this.leftToeR.addChild(this.leftClawR);
    this.leftToeM.addChild(this.leftClawM);
    this.leftToeL.addChild(this.leftClawL);
    this.rightAnkle.addChild(this.rightToeB);
    this.rightAnkle.addChild(this.rightToeR);
    this.rightAnkle.addChild(this.rightToeM);
    this.rightAnkle.addChild(this.rightToeL);
    this.leftAnkle.addChild(this.leftToeB);
    this.leftAnkle.addChild(this.leftToeR);
    this.leftAnkle.addChild(this.leftToeM);
    this.leftAnkle.addChild(this.leftToeL);
    this.rightLeg.addChild(this.rightAnkle);
    this.leftLeg.addChild(this.leftAnkle);
    this.rightThigh.addChild(this.rightLeg);
    this.leftThigh.addChild(this.leftLeg);
    this.upperBeak.addChild(this.upperBeakTip);
    this.lowerBeak.addChild(this.lowerBeakTip);
    this.head.addChild(this.upperBeak);
    this.head.addChild(this.lowerBeak);
    this.neck3.addChild(this.head);
    this.neck2.addChild(this.neck3);
    this.neck1.addChild(this.neck2);
    this.leftWing2.addChild(this.leftWing3);
    this.leftWing1.addChild(this.leftWing2);
    this.rightWing2.addChild(this.rightWing3);
    this.rightWing1.addChild(this.rightWing2);
    this.body.addChild(this.rightThigh);
    this.body.addChild(this.leftThigh);
    this.body.addChild(this.neck1);
    this.body.addChild(this.tail);
    this.body.addChild(this.leftWing1);
    this.body.addChild(this.rightWing1);

    EnumMap legMap = new EnumMap(BonesBirdLegs.class);
    legMap.put(BonesBirdLegs.LEFT_KNEE, this.leftThigh);
    legMap.put(BonesBirdLegs.RIGHT_KNEE, this.rightThigh);
    legMap.put(BonesBirdLegs.LEFT_ANKLE, this.leftLeg);
    legMap.put(BonesBirdLegs.RIGHT_ANKLE, this.rightLeg);
    legMap.put(BonesBirdLegs.LEFT_METATARSOPHALANGEAL_ARTICULATIONS, this.leftAnkle);
    legMap.put(BonesBirdLegs.RIGHT_METATARSOPHALANGEAL_ARTICULATIONS, this.rightAnkle);
    legMap.put(BonesBirdLegs.LEFT_BACK_CLAW, this.leftToeB);
    legMap.put(BonesBirdLegs.RIGHT_BACK_CLAW, this.rightToeB);
    this.animationRun = new ModelAnimator(legMap, AnimationRegistry.instance().getAnimation("bird_run"));

    EnumMap wingMap = new EnumMap(BonesWings.class);
    wingMap.put(BonesWings.LEFT_SHOULDER, this.leftWing1);
    wingMap.put(BonesWings.RIGHT_SHOULDER, this.rightWing1);
    wingMap.put(BonesWings.LEFT_ELBOW, this.leftWing2);
    wingMap.put(BonesWings.RIGHT_ELBOW, this.rightWing2);
    this.animationFlap = new ModelAnimator(wingMap, AnimationRegistry.instance().getAnimation("wing_flap_2_piece"));

    EnumMap beakMap = new EnumMap(BonesMouth.class);
    beakMap.put(BonesMouth.UPPER_MOUTH, this.upperBeak);
    beakMap.put(BonesMouth.LOWER_MOUTH, this.lowerBeak);
    this.animationBeak = new ModelAnimator(beakMap, AnimationRegistry.instance().getAnimation("bird_beak"));

    setRotation(this.body, 0.7F, 0.0F, 0.0F);
    setRotation(this.rightThigh, -0.39F, 0.0F, 0.09F);
    setRotation(this.leftThigh, -0.39F, 0.0F, -0.09F);
    setRotation(this.rightLeg, -0.72F, 0.0F, 0.0F);
    setRotation(this.leftLeg, -0.72F, 0.0F, 0.0F);
    setRotation(this.rightAnkle, 0.1F, 0.2F, 0.0F);
    setRotation(this.leftAnkle, 0.1F, -0.2F, 0.0F);
    setRotation(this.rightToeB, 1.34F, 0.0F, 0.0F);
    setRotation(this.rightToeR, -0.8F, -0.28F, -0.28F);
    setRotation(this.rightToeM, -0.8F, 0.0F, 0.0F);
    setRotation(this.rightToeL, -0.8F, 0.28F, 0.28F);
    setRotation(this.leftToeB, 1.34F, 0.0F, 0.0F);
    setRotation(this.leftToeR, -0.8F, 0.28F, 0.28F);
    setRotation(this.leftToeM, -0.8F, 0.0F, 0.0F);
    setRotation(this.leftToeL, -0.8F, -0.28F, -0.28F);
    setRotation(this.rightClawB, -36.0F, 0.0F, 0.0F);

    setRotation(this.neck1, -0.18F, 0.0F, 0.0F);
    setRotation(this.neck2, 0.52F, 0.0F, 0.0F);
    setRotation(this.neck3, 0.26F, 0.0F, 0.0F);
    setRotation(this.head, -0.97F, 0.0F, 0.0F);

    setRotation(this.tail, 0.3F, 0.0F, 0.0F);
  }

  public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
  {
    this.body.render(par7);
  }

  public void setFlyingAnimations(AnimationState wingState, AnimationState legState, AnimationState beakState, float roll, float headYaw, float headPitch, float parTick)
  {
    AnimationAction legAction = legState.getCurrentAction();
    AnimationAction wingAction = wingState.getCurrentAction();
    float flapProgress = wingState.getCurrentAnimationTimeInterp(parTick);
    float legProgress = legState.getCurrentAnimationTimeInterp(parTick);
    float beakProgress = beakState.getCurrentAnimationTimeInterp(parTick);
    this.animationFlap.updateAnimation(flapProgress);
    this.animationRun.updateAnimation(legProgress);
    this.animationBeak.updateAnimation(beakProgress);
    if (legAction == AnimationAction.RUN)
    {
      if ((legProgress >= 0.109195F) && (legProgress < 0.5373563F))
      {
        legProgress += 0.03735632F;
        if (legProgress >= 0.5373563F) {
          legProgress -= 0.4281609F;
        }
        float t = 25.132742F * legProgress / 0.8908046F;
        this.body.rotateAngleX += (float)(-Math.cos(t) * 0.04D);
        this.neck1.rotateAngleX += (float)(Math.cos(t) * 0.08D);
        this.body.rotationPointY += -(float)(Math.cos(t) * 1.9D);
      }
    }

    if (wingAction == AnimationAction.WINGFLAP)
    {
      float flapCycle = flapProgress / 0.2714932F;

      this.body.rotationPointY += MathHelper.cos(flapCycle * 3.141593F * 2.0F) * 1.4F;
      ModelRenderer tmp244_241 = this.rightThigh; tmp244_241.rotateAngleX = ((float)(tmp244_241.rotateAngleX + MathHelper.cos(flapCycle * 3.141593F * 2.0F) * 0.08726646324990228D));
      ModelRenderer tmp274_271 = this.leftThigh; tmp274_271.rotateAngleX = ((float)(tmp274_271.rotateAngleX + MathHelper.cos(flapCycle * 3.141593F * 2.0F) * 0.08726646324990228D));
      ModelRenderer tmp304_301 = this.tail; tmp304_301.rotateAngleX = ((float)(tmp304_301.rotateAngleX + MathHelper.cos(flapCycle * 3.141593F * 2.0F) * 0.03490658588512815D));
    }

    this.body.rotateAngleZ = (-roll / 180.0F * 3.141593F);

    headPitch = (float)MathUtil.boundAngle180Deg(headPitch);
    if (headPitch > 37.16F)
      headPitch = 37.16F;
    else if (headPitch < -56.650002F) {
      headPitch = -56.650002F;
    }
    float pitchFactor = (headPitch + 56.650002F) / 93.800003F;
    this.head.rotateAngleX += -0.96F + pitchFactor * -0.1400001F;
    this.neck3.rotateAngleX += 0.378F + pitchFactor * -0.528F;
    this.neck2.rotateAngleX += 0.4F + pitchFactor * -0.4F;
    this.neck1.rotateAngleX += 0.513F + pitchFactor * -0.613F;

    headYaw = (float)MathUtil.boundAngle180Deg(headYaw);
    if (headYaw > 30.5F)
      headYaw = 30.5F;
    else if (headYaw < -30.5F) {
      headYaw = -30.5F;
    }
    float yawFactor = (headYaw + 30.5F) / 61.0F;
    this.head.rotateAngleZ += 0.8F + yawFactor * 2.0F * -0.8F;
    this.neck3.rotateAngleZ += 0.38F + yawFactor * 2.0F * -0.38F;
    this.neck2.rotateAngleZ += 0.14F + yawFactor * 2.0F * -0.14F;
    this.head.rotateAngleY += -0.7F + yawFactor * 2.0F * 0.7F;
    this.neck3.rotateAngleY += -0.12F + yawFactor * 2.0F * 0.12F;
  }

  public void setRotationAngles(float limbPeriod, float limbMaxMovement, float ticksExisted, float headYaw, float entityPitch, float unitScale, Entity entity)
  {
    super.setRotationAngles(limbPeriod, limbMaxMovement, ticksExisted, headYaw, entityPitch, unitScale, entity);
    ModelRenderer tmp19_16 = this.body; tmp19_16.rotateAngleX = ((float)(tmp19_16.rotateAngleX + (0.8707963705062867D - entityPitch / 180.0F * 3.141593F)));
    float pitchFactor = entityPitch / 50.0F;
    if (pitchFactor > 1.0F)
      pitchFactor = 1.0F;
    else if (pitchFactor < 0.0F)
      pitchFactor = 0.0F;
  }

  public void resetSkeleton()
  {
    setRotation(this.body, 0.7F, 0.0F, 0.0F);
    setRotation(this.rightThigh, -0.39F, 0.0F, 0.09F);
    setRotation(this.leftThigh, -0.39F, 0.0F, -0.09F);
    setRotation(this.rightLeg, -0.72F, 0.0F, 0.0F);
    setRotation(this.leftLeg, -0.72F, 0.0F, 0.0F);
    setRotation(this.rightAnkle, 0.1F, 0.2F, 0.0F);
    setRotation(this.leftAnkle, 0.1F, -0.2F, 0.0F);
    setRotation(this.rightToeB, 1.34F, 0.0F, 0.0F);
    setRotation(this.rightToeR, -0.8F, -0.28F, -0.28F);
    setRotation(this.rightToeM, -0.8F, 0.0F, 0.0F);
    setRotation(this.rightToeL, -0.8F, 0.28F, 0.28F);
    setRotation(this.leftToeB, 1.34F, 0.0F, 0.0F);
    setRotation(this.leftToeR, -0.8F, 0.28F, 0.28F);
    setRotation(this.leftToeM, -0.8F, 0.0F, 0.0F);
    setRotation(this.leftToeL, -0.8F, -0.28F, -0.28F);

    setRotation(this.neck1, 0.0F, 0.0F, 0.0F);
    setRotation(this.neck2, 0.0F, 0.0F, 0.0F);
    setRotation(this.neck3, 0.0F, 0.0F, 0.0F);
    setRotation(this.head, 0.0F, 0.0F, 0.0F);
    setRotation(this.tail, 0.3F, 0.0F, 0.0F);
    setRotation(this.upperBeak, 0.0F, 0.0F, 0.0F);
    setRotation(this.lowerBeak, 0.0F, 0.0F, 0.0F);

    this.body.setRotationPoint(0.0F, -19.0F, 0.0F);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}