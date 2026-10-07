package net.nitwit.guinea_pigs.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.nitwit.guinea_pigs.GuineaPigs;
import net.nitwit.guinea_pigs.entity.custom.GuineaPigEntity;
import net.nitwit.guinea_pigs.entity.custom.GuineaPigVariant;

import java.util.Map;

// Handles rendering of the Guinea Pig entity, including texture selection and model scaling
public class GuineaPigRenderer
        extends MobRenderer<GuineaPigEntity, GuineaPigModel.GuineaPigRenderState, GuineaPigModel> {

    // Maps each GuineaPigVariant to its corresponding texture file
    private static final Map<GuineaPigVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GuineaPigVariant.class), map -> {
                map.put(GuineaPigVariant.WHITE,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/white.png"));
                map.put(GuineaPigVariant.TRICOLOR,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/tricolor.png"));
                map.put(GuineaPigVariant.RED,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/red.png"));
                map.put(GuineaPigVariant.HIMALAYAN,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/himalayan.png"));
                map.put(GuineaPigVariant.GRAY,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/gray.png"));
                map.put(GuineaPigVariant.DUTCH,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/dutch.png"));
                map.put(GuineaPigVariant.BROWN_DUTCH,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/brown_dutch.png"));
                map.put(GuineaPigVariant.BLACK_TAN,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/black_tan.png"));
                map.put(GuineaPigVariant.BLACK,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/black.png"));
                map.put(GuineaPigVariant.ACORN_SQUASH,
                        Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "textures/entity/guinea_pig/acorn_squash.png"));
            });

    // Constructor sets the model and shadow size for the guinea pig
    public GuineaPigRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new GuineaPigModel(context.bakeLayer(GuineaPigModel.GUINEA_PIG)),
                0.2f
        );
    }

    @Override
    public GuineaPigModel.GuineaPigRenderState createRenderState() {
        return new GuineaPigModel.GuineaPigRenderState();
    }

    @Override
    public void extractRenderState(
            GuineaPigEntity entity,
            GuineaPigModel.GuineaPigRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.variant = entity.getVariant();
        state.isTame = entity.isTame();
        state.isSitting = entity.isInSittingPose();

        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.sittingAnimationState.copyFrom(entity.sittingAnimationState);
    }

    // Returns the texture based on the entity's variant
    @Override
    public Identifier getTextureLocation(GuineaPigModel.GuineaPigRenderState state) {
        return LOCATION_BY_VARIANT.get(state.variant);
    }

    // Scales the guinea pig model based on age (baby vs adult)
    @Override
    protected void scale(GuineaPigModel.GuineaPigRenderState state, PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }
    }
}