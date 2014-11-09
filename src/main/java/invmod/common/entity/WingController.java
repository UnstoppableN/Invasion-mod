package invmod.common.entity;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import net.minecraft.entity.Entity;

public class WingController
{
  private EntityIMBird theEntity;
  private AnimationState animationFlap;
  private int timeAttacking;
  private float flapEffort;
  private float[] flapEffortSamples;
  private int sampleIndex;

  public WingController(EntityIMBird entity, AnimationState stateObject)
  {
    this.theEntity = entity;
    this.animationFlap = stateObject;
    this.timeAttacking = 0;
    this.flapEffort = 1.0F;
    this.flapEffortSamples = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F };
    this.sampleIndex = 0;
  }

  public void update()
  {
    AnimationAction currAnimation = this.animationFlap.getCurrentAction();
    AnimationAction nextAnimation = this.animationFlap.getNextSetAction();
    boolean wingAttack = this.theEntity.isAttackingWithWings();
    if (!wingAttack)
      this.timeAttacking = 0;
    else {
      this.timeAttacking += 1;
    }
    if (this.theEntity.ticksExisted % 5 == 0)
    {
      if (++this.sampleIndex >= this.flapEffortSamples.length) {
        this.sampleIndex = 0;
      }
      float sample = this.theEntity.getThrustEffort();
      this.flapEffort -= this.flapEffortSamples[this.sampleIndex] / this.flapEffortSamples.length;
      this.flapEffort += sample / this.flapEffortSamples.length;
      this.flapEffortSamples[this.sampleIndex] = sample;
    }

    if (this.theEntity.getFlyState() != FlyState.GROUNDED)
    {
      if (currAnimation == AnimationAction.WINGTUCK)
      {
        ensureAnimation(this.animationFlap, AnimationAction.WINGSPREAD, 2.2F, true);
      }
      else if (this.theEntity.isThrustOn())
      {
        ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, 2.0F * this.flapEffort, false);
      }
      else
      {
        ensureAnimation(this.animationFlap, AnimationAction.WINGGLIDE, 0.7F, false);
      }

    }
    else
    {
      boolean wingsActive = false;
      if (this.theEntity.getMoveState() == MoveState.RUNNING)
      {
        if (currAnimation == AnimationAction.WINGTUCK)
        {
          ensureAnimation(this.animationFlap, AnimationAction.WINGSPREAD, 2.2F, true);
        }
        else
        {
          ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, 1.0F, false);
          if ((!wingAttack) && (currAnimation == AnimationAction.WINGSPREAD) && (this.animationFlap.getCurrentAnimationPercent() >= 0.65F))
          {
            this.animationFlap.setPaused(true);
          }
        }
        wingsActive = true;
      }

      if (wingAttack)
      {
        float speed = (float)(1.0D / Math.min(this.timeAttacking / 40 * 0.6D + 0.4D, 1.0D));
        ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, speed, false);
        wingsActive = true;
      }

      if (!wingsActive)
      {
        ensureAnimation(this.animationFlap, AnimationAction.WINGTUCK, 1.8F, true);
      }
    }
    this.animationFlap.update();
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