package invmod.client.render.animation;

public class Transition
{
  private AnimationAction newAction;
  private float sourceTime;
  private float destTime;

  public Transition(AnimationAction newAction, float sourceTime, float destTime)
  {
    this.newAction = newAction;
    this.sourceTime = sourceTime;
    this.destTime = destTime;
  }

  public AnimationAction getNewAction()
  {
    return this.newAction;
  }

  public float getSourceTime()
  {
    return this.sourceTime;
  }

  public float getDestTime()
  {
    return this.destTime;
  }
}