package invmod.client.render;

import invmod.client.render.animation.Animation;
import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationPhaseInfo;
import invmod.client.render.animation.BonesWings;
import invmod.client.render.animation.Transition;
import invmod.common.mod_Invasion;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationRegistry
{
  private static final AnimationRegistry instance = new AnimationRegistry();
  private Map<String, Animation> animationMap;
  private Animation emptyAnim;

  private AnimationRegistry()
  {
    this.animationMap = new HashMap(4);
    EnumMap allKeyFramesWings = new EnumMap(BonesWings.class);
    List animationPhases = new ArrayList(1);
    animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND, 0.0F, 1.0F, new Transition(AnimationAction.STAND, 1.0F, 0.0F)));
    this.emptyAnim = new Animation(BonesWings.class, 1.0F, 1.0F, allKeyFramesWings, animationPhases);
  }

  public void registerAnimation(String name, Animation animation)
  {
    if (!this.animationMap.containsKey(name))
    {
      this.animationMap.put(name, animation);
      return;
    }
    mod_Invasion.log("Register animation: Name \"" + name + "\" already assigned");
  }

  public Animation getAnimation(String name)
  {
    if (this.animationMap.containsKey(name))
    {
      return (Animation)this.animationMap.get(name);
    }

    mod_Invasion.log("Tried to use animation \"" + name + "\" but it doesn't exist");
    return this.emptyAnim;
  }

  public static AnimationRegistry instance()
  {
    return instance;
  }
}