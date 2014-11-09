package invmod.common.entity;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import net.minecraft.entity.Entity;

public class LegController
{
  private EntityIMBird theEntity;
  private AnimationState animationRun;
  private int timeAttacking;
  private float flapEffort;
  private float[] flapEffortSamples;
  private int sampleIndex;

  public LegController(EntityIMBird entity, AnimationState stateObject)
  {
    this.theEntity = entity;
    this.animationRun = stateObject;
    this.timeAttacking = 0;
    this.flapEffort = 1.0F;
    this.flapEffortSamples = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F };
    this.sampleIndex = 0;
  }

  public void update()
  {
    AnimationAction currAnimation = this.animationRun.getCurrentAction();
    if (this.theEntity.getMoveState() == MoveState.RUNNING)
    {
      double dX = this.theEntity.posX - this.theEntity.lastTickPosX;
      double dZ = this.theEntity.posZ - this.theEntity.lastTickPosZ;
      double dist = Math.sqrt(dX * dX + dZ * dZ);
      float speed = 0.2F + (float)dist * 1.3F;

      if (this.animationRun.getNextSetAction() != AnimationAction.RUN)
      {
        if (dist >= 1.E-005D)
        {
          if (currAnimation == AnimationAction.STAND)
          {
            ensureAnimation(this.animationRun, AnimationAction.STAND_TO_RUN, speed, false);
          }
          else if (currAnimation == AnimationAction.STAND_TO_RUN)
          {
            ensureAnimation(this.animationRun, AnimationAction.RUN, speed, false);
          }
          else
          {
            ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0F, true);
          }
        }
      }
      else
      {
        this.animationRun.setAnimationSpeed(speed);
        if (dist < 1.E-005D)
        {
          ensureAnimation(this.animationRun, AnimationAction.STAND, 0.2F, true);
        }
      }
    }
    else if (this.theEntity.getMoveState() == MoveState.STANDING)
    {
      ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0F, true);
    }
    else if (this.theEntity.getMoveState() == MoveState.FLYING)
    {
      if (this.theEntity.getClawsForward())
      {
        if (currAnimation == AnimationAction.STAND)
        {
          ensureAnimation(this.animationRun, AnimationAction.LEGS_CLAW_ATTACK_P1, 1.5F, true);
        }
        else if (this.animationRun.getNextSetAction() != AnimationAction.LEGS_CLAW_ATTACK_P1)
        {
          ensureAnimation(this.animationRun, AnimationAction.STAND, 1.5F, true);
        }
      }
      else if (((this.theEntity.getFlyState() == FlyState.FLYING) || (this.theEntity.getFlyState() == FlyState.LANDING)) && (currAnimation != AnimationAction.LEGS_RETRACT))
      {
        if (currAnimation == AnimationAction.STAND)
        {
          ensureAnimation(this.animationRun, AnimationAction.LEGS_RETRACT, 1.0F, true);
        }
        else if (currAnimation == AnimationAction.LEGS_CLAW_ATTACK_P1)
        {
          ensureAnimation(this.animationRun, AnimationAction.LEGS_CLAW_ATTACK_P2, 1.0F, true);
        }
        else
        {
          ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0F, true);
        }
      }
    }

    this.animationRun.update();
  }

  private void ensureAnimation(AnimationState state, AnimationAction action, float animationSpeed, boolean pauseAfterAction)
  {
    if (state.getNextSetAction() != action)
    {
      state.setNewAction(action, animationSpeed, pauseAfterAction);
    }
    else
    {
      state.setAnimationSpeed(animationSpeed);
      state.setPauseAfterSetAction(pauseAfterAction);
      state.setPaused(false);
    }
  }
}