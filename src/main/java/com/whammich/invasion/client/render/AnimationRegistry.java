package com.whammich.invasion.client.render;

import com.whammich.invasion.client.render.animation.*;
import invmod.Invasion;

import java.util.*;

public class AnimationRegistry {
    private static final AnimationRegistry instance = new AnimationRegistry();
    private Map<String, Animation> animationMap;
    private Animation emptyAnim;

    private AnimationRegistry() {
        this.animationMap = new HashMap(4);
        EnumMap allKeyFramesWings = new EnumMap(BonesWings.class);
        List animationPhases = new ArrayList(1);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND, 0.0F, 1.0F, new Transition(AnimationAction.STAND, 1.0F, 0.0F)));
        this.emptyAnim = new Animation(BonesWings.class, 1.0F, 1.0F, allKeyFramesWings, animationPhases);
    }

    public static AnimationRegistry instance() {
        return instance;
    }

    public void registerAnimation(String name, Animation animation) {
        if (!this.animationMap.containsKey(name)) {
            this.animationMap.put(name, animation);
            return;
        }
        Invasion.log("Register animation: Name \"" + name + "\" already assigned");
    }

    public Animation getAnimation(String name) {
        if (this.animationMap.containsKey(name)) {
            return (Animation) this.animationMap.get(name);
        }

        Invasion.log("Tried to use animation \"" + name + "\" but it doesn't exist");
        return this.emptyAnim;
    }
}