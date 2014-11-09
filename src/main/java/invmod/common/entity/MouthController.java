package invmod.common.entity;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;

public class MouthController
{
  private EntityIMLiving theEntity;
  private AnimationState mouthState;
  private int mouthOpenTime;

  public MouthController(EntityIMBird entity, AnimationState stateObject)
  {
    this.theEntity = entity;
    this.mouthState = stateObject;
    this.mouthOpenTime = 0;
  }

  public void update()
  {
    if (this.mouthOpenTime > 0)
    {
      this.mouthOpenTime -= 1;
      ensureAnimation(this.mouthState, AnimationAction.MOUTH_OPEN, 1.0F, true);
    }
    else
    {
      ensureAnimation(this.mouthState, AnimationAction.MOUTH_CLOSE, 1.0F, true);
    }
    this.mouthState.update();
  }

  public void setMouthState(int timeOpen)
  {
    this.mouthOpenTime = timeOpen;
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