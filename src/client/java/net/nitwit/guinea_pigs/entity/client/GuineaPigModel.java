package net.nitwit.guinea_pigs.entity.client;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.nitwit.guinea_pigs.entity.custom.GuineaPigVariant;
import net.nitwit.guinea_pigs.GuineaPigs;

// Defines the 3D model and animation behavior for the Guinea Pig entity
public class GuineaPigModel extends EntityModel<GuineaPigModel.GuineaPigRenderState> {

    // Defines the model layer location used in rendering
    public static final ModelLayerLocation GUINEA_PIG =
            new ModelLayerLocation(
                    Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "guinea_pig"),
                    "main"
            );

    // Root model parts
    private final ModelPart guineaPig;
    private final ModelPart head;

    private final KeyframeAnimation walkAnimation;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation sittingAnimation;

    // Constructor initializes main body and head parts from the model root
    public GuineaPigModel(ModelPart root) {
        super(root);

        this.guineaPig = root.getChild("GuineaPig");
        this.head = this.guineaPig.getChild("Head");

        this.walkAnimation = GuineaPigAnimations.ANIM_WALK.bake(root);
        this.idleAnimation = GuineaPigAnimations.ANIM_IDLE.bake(root);
        this.sittingAnimation = GuineaPigAnimations.ANIM_SITTING.bake(root);
    }

    // Builds and returns the model geometry and bone hierarchy
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        // Root part
        PartDefinition guineaPig = root.addOrReplaceChild(
                "GuineaPig",
                CubeListBuilder.create(),
                PartPose.offset(-0.5F, 24.0F, 6.0F)
        );

        // Body
        PartDefinition body = guineaPig.addOrReplaceChild(
                "Body",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(
                        0.5F,
                        -1.0F,
                        0.0F,
                        -0.0436F,
                        0.0F,
                        0.0F
                )
        );

        body.addOrReplaceChild(
                "body_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -4.0F,
                                -4.0F,
                                -1.0F,
                                5.0F,
                                5.0F,
                                8.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        1.5F,
                        -0.8F,
                        -7.0F,
                        -0.0436F,
                        0.0F,
                        0.0F
                )
        );

        // Head and ears
        PartDefinition head = guineaPig.addOrReplaceChild(
                "Head",
                CubeListBuilder.create()
                        .texOffs(0, 13)
                        .addBox(
                                -2.5F,
                                -2.0F,
                                -2.5F,
                                5.0F,
                                4.0F,
                                3.0F,
                                CubeDeformation.NONE
                        )
                        .texOffs(16, 13)
                        .addBox(
                                -2.0F,
                                -1.0F,
                                -4.5F,
                                4.0F,
                                3.0F,
                                2.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(0.5F, -4.0F, -8.0F)
        );

        head.addOrReplaceChild(
                "r_ear_r1",
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(
                                -2.0F,
                                0.0F,
                                -0.5F,
                                2.0F,
                                2.0F,
                                1.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        2.5F,
                        -2.0F,
                        -2.0F,
                        1.3963F,
                        1.0036F,
                        0.5236F
                )
        );

        head.addOrReplaceChild(
                "l_ear_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                0.0F,
                                0.0F,
                                -0.5F,
                                2.0F,
                                2.0F,
                                1.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offsetAndRotation(
                        -2.5F,
                        -2.0F,
                        -2.0F,
                        1.3963F,
                        -1.0036F,
                        -0.5236F
                )
        );

        // Front legs
        PartDefinition frontLegs = guineaPig.addOrReplaceChild(
                "FrontLegs",
                CubeListBuilder.create(),
                PartPose.offset(1.0F, -1.0F, -8.0F)
        );

        frontLegs.addOrReplaceChild(
                "FrontLeft",
                CubeListBuilder.create()
                        .texOffs(18, 6)
                        .addBox(
                                -0.5F,
                                0.0F,
                                -0.5F,
                                1.0F,
                                1.0F,
                                1.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(2.0F, 0.0F, 0.0F)
        );

        frontLegs.addOrReplaceChild(
                "FrontRight",
                CubeListBuilder.create()
                        .texOffs(13, 13)
                        .addBox(
                                -0.5F,
                                0.0F,
                                -0.5F,
                                1.0F,
                                1.0F,
                                1.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(-3.0F, 0.0F, 0.0F)
        );

        // Back legs
        PartDefinition backLegs = guineaPig.addOrReplaceChild(
                "BackLegs",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -1.0F, 0.0F)
        );

        backLegs.addOrReplaceChild(
                "BackLeft",
                CubeListBuilder.create()
                        .texOffs(18, 3)
                        .addBox(
                                -1.0F,
                                0.0F,
                                -2.0F,
                                1.0F,
                                1.0F,
                                2.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(-2.0F, 0.0F, 0.0F)
        );

        backLegs.addOrReplaceChild(
                "BackRight",
                CubeListBuilder.create()
                        .texOffs(18, 0)
                        .addBox(
                                0.0F,
                                0.0F,
                                -2.0F,
                                1.0F,
                                1.0F,
                                2.0F,
                                CubeDeformation.NONE
                        ),
                PartPose.offset(3.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    // Called to update the model's angles based on the render state
    @Override
    public void setupAnim(GuineaPigRenderState state) {
        super.setupAnim(state);

        this.setHeadAngles(state.yRot, state.xRot);

        this.walkAnimation.applyWalk(
                state.walkAnimationPos,
                state.walkAnimationSpeed,
                2.0F,
                2.5F
        );

        this.idleAnimation.apply(
                state.idleAnimationState,
                state.ageInTicks
        );

        this.sittingAnimation.apply(
                state.sittingAnimationState,
                state.ageInTicks
        );
    }

    // Applies head rotation based on yaw/pitch, clamped to prevent excessive movement
    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        this.head.yRot = headYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
    }

    /**
     * Render state used by the Guinea Pig model.
     *
     * The renderer will populate these values from GuineaPigEntity later.
     */
    public static class GuineaPigRenderState extends LivingEntityRenderState {
        public final AnimationState idleAnimationState = new AnimationState();
        public final AnimationState sittingAnimationState = new AnimationState();
        public boolean isTame;
        public boolean isSitting;
        public GuineaPigVariant variant;
    }
}