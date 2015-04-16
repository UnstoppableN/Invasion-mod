package com.whammich.invasion.proxies;

import com.whammich.invasion.client.render.*;
import com.whammich.invasion.client.render.animation.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import invmod.common.entity.*;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;

import java.io.File;
import java.util.*;

public class ClientProxy extends CommonProxy {


    public void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
    }

    public void printGuiMessage(IChatComponent message) {
        FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(message);
    }

    public void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityIMZombie.class, new RenderIMZombie(new ModelZombie(0.0F, true), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMZombiePigman.class, new RenderIMZombiePigman(new ModelZombie(0.0F, true), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMSkeleton.class, new RenderIMSkeleton(new ModelIMSkeleton(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMSpider.class, new RenderSpiderIM());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMPigEngy.class, new RenderPigEngy(new ModelBiped(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMImp.class, new RenderImp(new ModelImp(), 0.3F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMThrower.class, new RenderThrower(new ModelThrower(), 1.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMBurrower.class, new RenderBurrower());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMWolf.class, new RenderIMWolf());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMBoulder.class, new RenderBoulder());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMTrap.class, new RenderTrap(new ModelTrap()));
        RenderingRegistry.registerEntityRenderingHandler(EntityIMBolt.class, new RenderBolt());
        RenderingRegistry.registerEntityRenderingHandler(EntitySFX.class, new RenderInvis());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMSpawnProxy.class, new RenderInvis());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMEgg.class, new RenderEgg());

        RenderingRegistry.registerEntityRenderingHandler(EntityIMCreeper.class, new RenderIMCreeper());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMBird.class, new RenderB());
        RenderingRegistry.registerEntityRenderingHandler(EntityIMGiantBird.class, new RenderGiantBird());
    }

    public void loadAnimations() {
        EnumMap allKeyFrames = new EnumMap(BonesBirdLegs.class);
        List animationPhases = new ArrayList(2);
        int x = 17;
        float totalFrames = 331 + x;

        Map transitions = new HashMap(1);
        Transition defaultTransition = new Transition(AnimationAction.STAND, 1.0F / totalFrames, 0.0F);
        transitions.put(AnimationAction.STAND, defaultTransition);
        transitions.put(AnimationAction.STAND_TO_RUN, new Transition(AnimationAction.STAND_TO_RUN, 1.0F / totalFrames, 1.0F / totalFrames));
        transitions.put(AnimationAction.LEGS_RETRACT, new Transition(AnimationAction.LEGS_RETRACT, 1.0F / totalFrames, (211.0F + x) / totalFrames));
        transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P1, new Transition(AnimationAction.LEGS_CLAW_ATTACK_P1, 1.0F / totalFrames, (171.0F + x) / totalFrames));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND, 0.0F, 1.0F / totalFrames, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.RUN, 38.0F / totalFrames, 38.0F / totalFrames);
        transitions.put(AnimationAction.RUN, defaultTransition);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND_TO_RUN, 1.0F / totalFrames, 38.0F / totalFrames, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.RUN, (170.0F + x) / totalFrames, 38.0F / totalFrames);
        transitions.put(AnimationAction.RUN, defaultTransition);
        transitions.put(AnimationAction.STAND, new Transition(AnimationAction.STAND, (170.0F + x) / totalFrames, 0.0F));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.RUN, 38.0F / totalFrames, (170.0F + x) / totalFrames, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.LEGS_UNRETRACT, (251.0F + x) / totalFrames, (251.0F + x) / totalFrames);
        transitions.put(AnimationAction.LEGS_UNRETRACT, defaultTransition);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_RETRACT, (211.0F + x) / totalFrames, (251.0F + x) / totalFrames, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.STAND, (291.0F + x) / totalFrames, 0.0F);
        transitions.put(AnimationAction.STAND, defaultTransition);
        transitions.put(AnimationAction.LEGS_RETRACT, new Transition(AnimationAction.LEGS_RETRACT, (291.0F + x) / totalFrames, (211.0F + x) / totalFrames));
        transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P1, new Transition(AnimationAction.LEGS_CLAW_ATTACK_P1, (291.0F + x) / totalFrames, (291.0F + x) / totalFrames));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_UNRETRACT, (251.0F + x) / totalFrames, (291.0F + x) / totalFrames, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.LEGS_CLAW_ATTACK_P2, (331.0F + x) / totalFrames, (171.0F + x) / totalFrames);
        transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P2, defaultTransition);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_CLAW_ATTACK_P1, (291.0F + x) / totalFrames, (331.0F + x) / totalFrames, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.STAND, (211.0F + x) / totalFrames, 0.0F);
        transitions.put(AnimationAction.STAND, defaultTransition);
        transitions.put(AnimationAction.LEGS_RETRACT, new Transition(AnimationAction.LEGS_RETRACT, (211.0F + x) / totalFrames, (211.0F + x) / totalFrames));
        transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P1, new Transition(AnimationAction.LEGS_CLAW_ATTACK_P1, (211.0F + x) / totalFrames, (291.0F + x) / totalFrames));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_CLAW_ATTACK_P2, (171.0F + x) / totalFrames, (211.0F + x) / totalFrames, defaultTransition, transitions));

        float frameUnit = 1.0F / totalFrames;
        float runBegin = 38.0F * frameUnit;
        float runEnd = (170 + x) * frameUnit;

        List leftThighFrames = new ArrayList(13);
        leftThighFrames.add(new KeyFrame(0.0F, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
        leftThighFrames.add(new KeyFrame(1.0F * frameUnit, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
        leftThighFrames.add(new KeyFrame(5.0F * frameUnit, -12.6F, 0.2F, 5.0F, InterpType.LINEAR));
        leftThighFrames.add(new KeyFrame(10.0F * frameUnit, 21.200001F, -0.6F, 5.2F, InterpType.LINEAR));
        leftThighFrames.add(new KeyFrame(15.0F * frameUnit, -32.0F, -1.7F, 5.7F, InterpType.LINEAR));
        leftThighFrames.add(new KeyFrame(25.0F * frameUnit, -57.0F, -6.4F, 9.0F, InterpType.LINEAR));
        leftThighFrames.add(new KeyFrame(35.0F * frameUnit, -76.5F, -19.299999F, 21.200001F, InterpType.LINEAR));
        KeyFrame.toRadians(leftThighFrames);

        List leftThighRunCycle = new ArrayList(7);
        leftThighRunCycle.add(new KeyFrame(38.0F * frameUnit, -74.099998F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame(44.0F * frameUnit, -63.700001F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((80 + x) * frameUnit, 13.1F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((101 + x) * frameUnit, 35.700001F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((110 + x) * frameUnit, 20.0F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((140 + x) * frameUnit, -33.0F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((170 + x) * frameUnit, -74.099998F, 0.0F, -6.5F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((171 + x) * frameUnit, -76.0F, 0.0F, -5.6F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((211 + x) * frameUnit, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((251 + x) * frameUnit, 9.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((291 + x) * frameUnit, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
        leftThighRunCycle.add(new KeyFrame((331 + x) * frameUnit, -76.0F, 0.0F, -5.6F, InterpType.LINEAR));
        KeyFrame.toRadians(leftThighRunCycle);

        List rightThighFrames = new ArrayList(13);
        rightThighFrames.add(new KeyFrame(0.0F, -15.0F, 0.0F, 0.0F, InterpType.LINEAR));
        rightThighFrames.add(new KeyFrame(1.0F * frameUnit, -15.0F, 0.0F, 0.0F, InterpType.LINEAR));
        rightThighFrames.add(new KeyFrame(37.0F * frameUnit, -15.0F, 0.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(rightThighFrames);
        List rightThighRunCycle = KeyFrame.cloneFrames(leftThighRunCycle);
        KeyFrame.mirrorFramesX(rightThighRunCycle);
        KeyFrame.offsetFramesCircular(rightThighRunCycle, runBegin, runEnd, (runEnd - runBegin) / 2.0F);

        leftThighFrames.addAll(leftThighRunCycle);
        rightThighFrames.addAll(rightThighRunCycle);
        allKeyFrames.put(BonesBirdLegs.LEFT_KNEE, leftThighFrames);
        allKeyFrames.put(BonesBirdLegs.RIGHT_KNEE, rightThighFrames);

        List leftLegFrames = new ArrayList(19);
        leftLegFrames.add(new KeyFrame(0.0F, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegFrames.add(new KeyFrame(1.0F * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegFrames.add(new KeyFrame(10.0F * frameUnit, -80.300003F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegFrames.add(new KeyFrame(25.0F * frameUnit, -44.200001F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegFrames.add(new KeyFrame(35.0F * frameUnit, -5.6F, 0.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(leftLegFrames);


        List leftLegRunCycle = new ArrayList(16);
        leftLegRunCycle.add(new KeyFrame(38.0F * frameUnit, 6.6F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(44.0F * frameUnit, 6.5F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(47.0F * frameUnit, -11.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(50.0F * frameUnit, -24.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(53.0F * frameUnit, -32.900002F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(56.0F * frameUnit, -40.799999F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(59.0F * frameUnit, -46.700001F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(62.0F * frameUnit, -45.799999F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(82.0F * frameUnit, -45.599998F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame(97.0F * frameUnit, -17.1F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((85 + x) * frameUnit, 0.75F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((90 + x) * frameUnit, -0.4F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((101 + x) * frameUnit, -43.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((115 + x) * frameUnit, -60.099998F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((154 + x) * frameUnit, -50.5F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((170 + x) * frameUnit, 6.6F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((171 + x) * frameUnit, -37.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((211 + x) * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((251 + x) * frameUnit, 15.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((291 + x) * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftLegRunCycle.add(new KeyFrame((331 + x) * frameUnit, -37.0F, 0.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(leftLegRunCycle);

        List rightLegFrames = new ArrayList(19);
        rightLegFrames.add(new KeyFrame(0.0F, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
        rightLegFrames.add(new KeyFrame(37.0F * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(rightLegFrames);

        List rightLegRunCycle = KeyFrame.cloneFrames(leftLegRunCycle);
        KeyFrame.mirrorFramesX(rightLegRunCycle);
        KeyFrame.offsetFramesCircular(rightLegRunCycle, runBegin, runEnd, (runEnd - runBegin) / 2.0F);

        leftLegFrames.addAll(leftLegRunCycle);
        rightLegFrames.addAll(rightLegRunCycle);
        allKeyFrames.put(BonesBirdLegs.LEFT_ANKLE, leftLegFrames);
        allKeyFrames.put(BonesBirdLegs.RIGHT_ANKLE, rightLegFrames);

        List leftAnkleFrames = new ArrayList(27);
        leftAnkleFrames.add(new KeyFrame(0.0F, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleFrames.add(new KeyFrame(1.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleFrames.add(new KeyFrame(5.0F * frameUnit, 31.700001F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleFrames.add(new KeyFrame(10.0F * frameUnit, 45.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleFrames.add(new KeyFrame(20.0F * frameUnit, 52.799999F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleFrames.add(new KeyFrame(25.0F * frameUnit, 51.599998F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleFrames.add(new KeyFrame(30.0F * frameUnit, 42.299999F, -5.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(leftAnkleFrames);
        List leftAnkleRunCycle = new ArrayList(21);
        leftAnkleRunCycle.add(new KeyFrame(38.0F * frameUnit, 28.799999F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(44.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(47.0F * frameUnit, 7.6F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(50.0F * frameUnit, 12.4F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(53.0F * frameUnit, 12.6F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(56.0F * frameUnit, 11.8F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(59.0F * frameUnit, 8.5F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(62.0F * frameUnit, 1.6F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(82.0F * frameUnit, -1.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(87.0F * frameUnit, -5.5F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(90.0F * frameUnit, -0.7F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(93.0F * frameUnit, 6.8F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame(97.0F * frameUnit, -4.6F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((85 + x) * frameUnit, 20.700001F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((95 + x) * frameUnit, 34.200001F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((100 + x) * frameUnit, 45.599998F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((110 + x) * frameUnit, 36.599998F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((115 + x) * frameUnit, 38.400002F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((124 + x) * frameUnit, 50.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((140 + x) * frameUnit, 45.299999F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((154 + x) * frameUnit, 52.900002F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((170 + x) * frameUnit, 25.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((171 + x) * frameUnit, -38.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((211 + x) * frameUnit, 0.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((251 + x) * frameUnit, 22.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((291 + x) * frameUnit, 0.0F, -5.0F, 0.0F, InterpType.LINEAR));
        leftAnkleRunCycle.add(new KeyFrame((331 + x) * frameUnit, -38.0F, -5.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(leftAnkleRunCycle);

        List rightAnkleFrames = new ArrayList(27);
        rightAnkleFrames.add(new KeyFrame(0.0F, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
        rightAnkleFrames.add(new KeyFrame(1.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
        rightAnkleFrames.add(new KeyFrame(37.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(rightAnkleFrames);
        List rightAnkleRunCycle = KeyFrame.cloneFrames(leftAnkleRunCycle);
        KeyFrame.mirrorFramesX(rightAnkleRunCycle);
        KeyFrame.offsetFramesCircular(rightAnkleRunCycle, runBegin, runEnd, (runEnd - runBegin) / 2.0F);

        leftAnkleFrames.addAll(leftAnkleRunCycle);
        rightAnkleFrames.addAll(rightAnkleRunCycle);
        allKeyFrames.put(BonesBirdLegs.LEFT_METATARSOPHALANGEAL_ARTICULATIONS, leftAnkleFrames);
        allKeyFrames.put(BonesBirdLegs.RIGHT_METATARSOPHALANGEAL_ARTICULATIONS, rightAnkleFrames);

        List leftBackClawFrames = new ArrayList(21);
        leftBackClawFrames.add(new KeyFrame(0.0F, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftBackClawFrames.add(new KeyFrame((170 + x) * frameUnit, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftBackClawFrames.add(new KeyFrame((171 + x) * frameUnit, 84.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftBackClawFrames.add(new KeyFrame((211 + x) * frameUnit, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftBackClawFrames.add(new KeyFrame((251 + x) * frameUnit, -7.5F, 0.0F, 0.0F, InterpType.LINEAR));
        leftBackClawFrames.add(new KeyFrame((291 + x) * frameUnit, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
        leftBackClawFrames.add(new KeyFrame((331 + x) * frameUnit, 84.0F, 0.0F, 0.0F, InterpType.LINEAR));

        KeyFrame.toRadians(leftBackClawFrames);
        List rightBackClawFrames = KeyFrame.cloneFrames(leftBackClawFrames);
        KeyFrame.mirrorFramesX(rightBackClawFrames);

        allKeyFrames.put(BonesBirdLegs.LEFT_BACK_CLAW, leftBackClawFrames);
        allKeyFrames.put(BonesBirdLegs.RIGHT_BACK_CLAW, rightBackClawFrames);

        Animation birdRun = new Animation(BonesBirdLegs.class, 1.0F, 0.04651163F, allKeyFrames, animationPhases);
        AnimationRegistry.instance().registerAnimation("bird_run", birdRun);

        EnumMap allKeyFramesWings = new EnumMap(BonesWings.class);
        animationPhases = new ArrayList(3);

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.WINGFLAP, 0.2714932F, 0.0F);
        transitions.put(AnimationAction.WINGFLAP, defaultTransition);
        transitions.put(AnimationAction.WINGTUCK, new Transition(AnimationAction.WINGTUCK, 0.06787331F, 0.2760181F));
        transitions.put(AnimationAction.WINGGLIDE, new Transition(AnimationAction.WINGGLIDE, 0.06787331F, 0.8190045F));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.WINGFLAP, 0.0F, 0.2714932F, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.WINGSPREAD, 0.5429865F, 0.5475113F);
        transitions.put(AnimationAction.WINGSPREAD, defaultTransition);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.WINGTUCK, 0.2760181F, 0.5429865F, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.WINGTUCK, 0.8190045F, 0.2760181F);
        transitions.put(AnimationAction.WINGTUCK, defaultTransition);
        transitions.put(AnimationAction.WINGFLAP, new Transition(AnimationAction.WINGFLAP, 0.8190045F, 0.06787331F));
        transitions.put(AnimationAction.WINGGLIDE, new Transition(AnimationAction.WINGGLIDE, 0.8190045F, 0.8190045F));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.WINGSPREAD, 0.5475113F, 0.8190045F, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.WINGGLIDE, 1.0F, 0.8190045F);
        transitions.put(AnimationAction.WINGGLIDE, defaultTransition);
        transitions.put(AnimationAction.WINGFLAP, new Transition(AnimationAction.WINGFLAP, 1.0F, 0.06787331F));
        transitions.put(AnimationAction.WINGTUCK, new Transition(AnimationAction.WINGTUCK, 1.0F, 0.2760181F));
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.WINGGLIDE, 0.8190045F, 1.0F, defaultTransition, transitions));

        frameUnit = 0.004524887F;
        List rightInnerWingFrames = new ArrayList(12);
        rightInnerWingFrames.add(new KeyFrame(0.0F, 2.0F, -48.0F, 0.0F, 7.0F, -8.0F, 6.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(5.0F * frameUnit, 4.0F, -38.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(10.0F * frameUnit, 5.5F, -27.5F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(15.0F * frameUnit, 5.5F, -7.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(20.0F * frameUnit, 5.5F, 15.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(25.0F * frameUnit, 4.5F, 30.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(30.0F * frameUnit, 2.0F, 38.0F, 9.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(35.0F * frameUnit, 1.0F, 20.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(40.0F * frameUnit, 1.0F, 3.5F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(45.0F * frameUnit, 1.0F, -19.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(50.0F * frameUnit, -3.0F, -38.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(55.0F * frameUnit, -1.0F, -48.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(60.0F * frameUnit, 2.0F, -48.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(61.0F * frameUnit, 5.5F, -7.0F, 0.0F, 7.0F, -8.0F, 6.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(121.0F * frameUnit, 0.71F, 88.599998F, 0.0F, 11.0F, -8.0F, 9.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(181.0F * frameUnit, 5.5F, -7.0F, 0.0F, 7.0F, -8.0F, 6.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(209.0F * frameUnit, 5.5F, -5.0F, 0.0F, InterpType.LINEAR));
        rightInnerWingFrames.add(new KeyFrame(221.0F * frameUnit, 5.5F, -7.0F, 0.0F, InterpType.LINEAR));

        KeyFrame.toRadians(rightInnerWingFrames);
        List leftInnerWingFrames = KeyFrame.cloneFrames(rightInnerWingFrames);
        KeyFrame.mirrorFramesX(leftInnerWingFrames);
        allKeyFramesWings.put(BonesWings.LEFT_SHOULDER, rightInnerWingFrames);
        allKeyFramesWings.put(BonesWings.RIGHT_SHOULDER, leftInnerWingFrames);

        List rightOuterWingFrames = new ArrayList(13);
        rightOuterWingFrames.add(new KeyFrame(0.0F, 2.0F, 34.5F, 0.0F, 23.0F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(5.0F * frameUnit, 5.0F, 13.0F, -7.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(10.0F * frameUnit, 7.0F, 8.5F, -10.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(15.0F * frameUnit, 7.5F, -2.5F, -10.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(25.0F * frameUnit, 5.0F, 7.0F, -10.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(30.0F * frameUnit, 2.0F, 15.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(35.0F * frameUnit, -3.0F, 37.0F, 12.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(40.0F * frameUnit, -9.0F, 56.0F, 27.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(45.0F * frameUnit, -13.0F, 68.0F, 28.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(50.0F * frameUnit, -13.5F, 70.0F, 31.5F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(53.0F * frameUnit, -9.0F, 71.0F, 31.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(55.0F * frameUnit, -3.5F, 65.5F, 22.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(58.0F * frameUnit, 0.0F, 52.0F, 8.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(60.0F * frameUnit, 2.0F, 34.5F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(61.0F * frameUnit, -5.0F, -2.5F, -10.0F, 23.0F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(76.0F * frameUnit, 0.0F, 0.0F, 15.0F, 22.0F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(101.0F * frameUnit, 0.0F, 0.0F, 83.0F, 20.33F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(121.0F * frameUnit, 0.0F, 0.0F, 90.0F, 19.0F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(141.0F * frameUnit, 0.0F, 0.0F, 83.0F, 20.33F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(166.0F * frameUnit, 0.0F, 0.0F, 15.0F, 22.0F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(181.0F * frameUnit, -5.0F, -2.5F, -10.0F, 23.0F, 1.0F, 0.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(209.0F * frameUnit, -5.0F, -1.3F, -10.0F, InterpType.LINEAR));
        rightOuterWingFrames.add(new KeyFrame(221.0F * frameUnit, -5.0F, -2.5F, -10.0F, InterpType.LINEAR));
        KeyFrame.toRadians(rightOuterWingFrames);
        List leftOuterWingFrames = KeyFrame.cloneFrames(rightOuterWingFrames);
        KeyFrame.mirrorFramesX(leftOuterWingFrames);
        allKeyFramesWings.put(BonesWings.LEFT_ELBOW, rightOuterWingFrames);
        allKeyFramesWings.put(BonesWings.RIGHT_ELBOW, leftOuterWingFrames);

        Animation wingFlap = new Animation(BonesWings.class, 1.0F, 0.01666667F, allKeyFramesWings, animationPhases);
        AnimationRegistry.instance().registerAnimation("wing_flap_2_piece", wingFlap);

        EnumMap allKeyFramesBeak = new EnumMap(BonesMouth.class);
        animationPhases = new ArrayList(3);

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.MOUTH_CLOSE, 0.5F, 0.5083333F);
        transitions.put(AnimationAction.MOUTH_CLOSE, defaultTransition);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.MOUTH_OPEN, 0.0F, 0.5F, defaultTransition, transitions));

        transitions = new HashMap(1);
        defaultTransition = new Transition(AnimationAction.MOUTH_OPEN, 1.0F, 0.0F);
        transitions.put(AnimationAction.MOUTH_OPEN, defaultTransition);
        animationPhases.add(new AnimationPhaseInfo(AnimationAction.MOUTH_CLOSE, 0.5F, 1.0F, defaultTransition, transitions));

        frameUnit = 0.008333334F;
        List upperBeakFrames = new ArrayList(3);
        upperBeakFrames.add(new KeyFrame(0.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
        upperBeakFrames.add(new KeyFrame(60.0F * frameUnit, -8.0F, 0.0F, 0.0F, InterpType.LINEAR));
        upperBeakFrames.add(new KeyFrame(120.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(upperBeakFrames);
        allKeyFramesBeak.put(BonesMouth.UPPER_MOUTH, upperBeakFrames);

        List lowerBeakFrames = new ArrayList(3);
        lowerBeakFrames.add(new KeyFrame(0.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
        lowerBeakFrames.add(new KeyFrame(60.0F * frameUnit, 20.0F, 0.0F, 0.0F, InterpType.LINEAR));
        lowerBeakFrames.add(new KeyFrame(120.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
        KeyFrame.toRadians(lowerBeakFrames);
        allKeyFramesBeak.put(BonesMouth.LOWER_MOUTH, lowerBeakFrames);

        Animation beak = new Animation(BonesMouth.class, 1.0F, 0.1F, allKeyFramesBeak, animationPhases);
        AnimationRegistry.instance().registerAnimation("bird_beak", beak);
    }

    public File getFile(String fileName) {
        return new File(FMLClientHandler.instance().getClient().mcDataDir.getPath() + fileName);
    }
}