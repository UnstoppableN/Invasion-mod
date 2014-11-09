package invmod.client.render.animation;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class Animation<T extends Enum<T>>
{
  private float animationPeriod;
  private float baseSpeed;
  private Class<T> skeletonType;
  private EnumMap<T, List<KeyFrame>> allKeyFrames;
  private List<AnimationPhaseInfo> animationPhases;

  public Animation(Class<T> skeletonType, float animationPeriod, float baseTime, EnumMap<T, List<KeyFrame>> allKeyFrames, List<AnimationPhaseInfo> animationPhases)
  {
    this.animationPeriod = animationPeriod;
    this.baseSpeed = baseTime;
    this.skeletonType = skeletonType;
    this.allKeyFrames = allKeyFrames;
    this.animationPhases = animationPhases;
  }

  public float getAnimationPeriod()
  {
    return this.animationPeriod;
  }

  public float getBaseSpeed()
  {
    return this.baseSpeed;
  }

  public List<AnimationPhaseInfo> getAnimationPhases()
  {
    return Collections.unmodifiableList(this.animationPhases);
  }

  public Class<T> getSkeletonType()
  {
    return this.skeletonType;
  }

  public List<KeyFrame> getKeyFramesFor(T skeletonPart)
  {
    if (this.allKeyFrames.containsKey(skeletonPart))
    {
      return Collections.unmodifiableList((List)this.allKeyFrames.get(skeletonPart));
    }
    return null;
  }
}