package invmod.client.render.animation;

import java.util.List;

public class AnimationState<T extends Enum<T>>
{
  private Animation<T> animation;
  private float currentTime;
  private float animationSpeed;
  private boolean pauseAtTransition;
  private boolean pauseAfterSetAction;
  private boolean isPaused;
  private AnimationPhaseInfo currentPhase;
  private Transition nextTransition;
  private AnimationAction setAction;

  public AnimationState(Animation<T> animation)
  {
    this(animation, 0.0F);
  }

  public AnimationState(Animation<T> animation, float startTime)
  {
    this.animation = animation;
    this.pauseAtTransition = false;
    this.pauseAfterSetAction = false;
    this.isPaused = false;
    this.currentTime = startTime;
    this.animationSpeed = animation.getBaseSpeed();
    updatePhase(this.currentTime);
    this.nextTransition = this.currentPhase.getDefaultTransition();
    this.setAction = this.nextTransition.getNewAction();
  }

  public void setNewAction(AnimationAction action)
  {
    this.setAction = action;
    updateTransition(action);
    this.pauseAtTransition = false;
    this.pauseAfterSetAction = false;
    this.isPaused = false;
  }

  public void setNewAction(AnimationAction action, float animationSpeedFactor, boolean pauseAfterAction)
  {
    setNewAction(action);
    setAnimationSpeed(animationSpeedFactor);
    setPauseAfterSetAction(pauseAfterAction);
  }

  public void setPauseAfterCurrentAction(boolean shouldPause)
  {
    this.pauseAtTransition = shouldPause;
  }

  public void setPauseAfterSetAction(boolean shouldPause)
  {
    this.pauseAfterSetAction = shouldPause;
  }

  public void setPaused(boolean isPaused)
  {
    this.isPaused = isPaused;
  }

  public void update()
  {
    if (this.isPaused) {
      return;
    }
    this.currentTime += this.animationSpeed;
    if (this.currentTime >= this.nextTransition.getSourceTime())
    {
      if ((this.setAction == this.currentPhase.getAction()) && (this.pauseAfterSetAction))
      {
        this.pauseAfterSetAction = false;
        this.pauseAtTransition = true;
      }

      if (!this.pauseAtTransition)
      {
        float overflow = this.currentTime - this.nextTransition.getSourceTime();
        this.currentTime = this.nextTransition.getDestTime();
        updatePhase(this.currentTime);
        float phaseLength = this.currentPhase.getTimeEnd() - this.currentPhase.getTimeBegin();
        if (overflow > phaseLength) {
          overflow = phaseLength;
        }
        updateTransition(this.setAction);
        this.currentTime += overflow;
        this.isPaused = false;
      }
      else
      {
        this.currentTime = this.nextTransition.getSourceTime();
        this.isPaused = true;
      }
    }
  }

  public AnimationAction getNextSetAction()
  {
    return this.setAction;
  }

  public AnimationAction getCurrentAction()
  {
    return this.currentPhase.getAction();
  }

  public float getCurrentAnimationTime()
  {
    return this.currentTime;
  }

  public float getCurrentAnimationTimeInterp(float parTick)
  {
    if (this.isPaused) {
      parTick = 0.0F;
    }
    float frameTime = this.currentTime + parTick * this.animationSpeed;
    if (frameTime < this.nextTransition.getSourceTime())
    {
      return frameTime;
    }

    float overFlow = frameTime - this.nextTransition.getSourceTime();
    float phaseLength = this.currentPhase.getTimeEnd() - this.currentPhase.getTimeBegin();
    if (overFlow > phaseLength) {
      overFlow = phaseLength;
    }
    return this.nextTransition.getDestTime() + overFlow;
  }

  public float getCurrentAnimationPercent()
  {
    return (this.currentTime - this.currentPhase.getTimeBegin()) / (this.currentPhase.getTimeEnd() - this.currentPhase.getTimeBegin());
  }

  public float getAnimationSpeed()
  {
    return this.animationSpeed;
  }

  public Transition getNextTransition()
  {
    return this.nextTransition;
  }

  public float getAnimationPeriod()
  {
    return this.animation.getAnimationPeriod();
  }

  public float getBaseAnimationTime()
  {
    return this.animation.getBaseSpeed();
  }

  public List<AnimationPhaseInfo> getAnimationPhases()
  {
    return this.animation.getAnimationPhases();
  }

  public void setAnimationSpeed(float speedFactor)
  {
    this.animationSpeed = (this.animation.getBaseSpeed() * speedFactor);
  }

  private boolean updateTransition(AnimationAction action)
  {
    if (this.currentPhase.hasTransition(action))
    {
      this.nextTransition = this.currentPhase.getTransition(action);
      if (this.currentTime > this.nextTransition.getSourceTime())
      {
        this.nextTransition = this.currentPhase.getDefaultTransition();
        return false;
      }
    }
    else
    {
      this.nextTransition = this.currentPhase.getDefaultTransition();
    }
    return true;
  }

  private void updatePhase(float time)
  {
    this.currentPhase = findPhase(time);
    if (this.currentPhase == null)
    {
      this.currentTime = 0.0F;
      this.currentPhase = ((AnimationPhaseInfo)this.animation.getAnimationPhases().get(0));
    }
  }

  private AnimationPhaseInfo findPhase(float time)
  {
    for (AnimationPhaseInfo phase : this.animation.getAnimationPhases())
    {
      if ((phase.getTimeBegin() <= time) && (phase.getTimeEnd() > time))
        return phase;
    }
    return null;
  }
}