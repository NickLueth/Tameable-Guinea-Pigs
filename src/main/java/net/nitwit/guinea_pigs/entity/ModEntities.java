package net.nitwit.guinea_pigs.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.nitwit.guinea_pigs.GuineaPigs;
import net.nitwit.guinea_pigs.entity.custom.GuineaPigEntity;

public class ModEntities {

    // Registers the custom Guinea Pig entity type with a spawn group of 'CREATURE' and specific dimensions
    public static final EntityType<GuineaPigEntity> GUINEA_PIG = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "guinea_pig"),
            EntityType.Builder.of(GuineaPigEntity::new, MobCategory.CREATURE).sized(.6f, .4f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "guinea_pig")
                    )));

    // Logs a message to indicate mod entity registration
    public static void registerModEntities() {
        GuineaPigs.LOGGER.info("Registering Mod Entities for " + GuineaPigs.MOD_ID);
    }
}