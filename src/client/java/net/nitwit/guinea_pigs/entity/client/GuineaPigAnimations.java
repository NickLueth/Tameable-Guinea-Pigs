package net.nitwit.guinea_pigs.entity.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

// Holds animation definitions for the Guinea Pig entity
public class GuineaPigAnimations {

        // Idle animation: subtle head rotation loop
        public static final AnimationDefinition ANIM_IDLE = AnimationDefinition.Builder.withLength(2.0F)
                .looping()

                .addAnimation("Body", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .addAnimation("Body", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .addAnimation("Head", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .addAnimation("Head", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .build();


        // Walk animation: cycles leg and head movement in a loop
        public static final AnimationDefinition ANIM_WALK = AnimationDefinition.Builder.withLength(0.5F)
                .looping()

                .addAnimation("GuineaPig", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .addAnimation("Head", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .addAnimation("Head", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                // Front right leg movement
                .addAnimation("FrontRight", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.3333F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .addAnimation("FrontRight", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                // Back left leg movement
                .addAnimation("BackLeft", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .addAnimation("BackLeft", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                // Back right leg movement
                .addAnimation("BackRight", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3333F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .addAnimation("BackRight", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                // Front left leg movement
                .addAnimation("FrontLeft", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .addAnimation("FrontLeft", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.3F),
                                AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.LINEAR)
                ))

                .build();


        // Sitting animation: head tilt and body shift downward
        public static final AnimationDefinition ANIM_SITTING = AnimationDefinition.Builder.withLength(0.5F)

                .addAnimation("Body", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(9.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .addAnimation("Body", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .addAnimation("Head", new AnimationChannel(
                        AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(5.0F, -21.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .addAnimation("Head", new AnimationChannel(
                        AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -1.2F, 0.0F),
                                AnimationChannel.Interpolations.CATMULLROM)
                ))

                .build();
}