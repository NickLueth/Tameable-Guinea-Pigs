package net.nitwit.guinea_pigs.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.nitwit.guinea_pigs.entity.ModEntities;

public class ModEntitySpawns {
    // Call this method during mod initialization to add entity spawns to biomes
    public static void addSpawns() {
        // Add Guinea Pig spawns to specified forest-like biomes with spawn weight and group sizes
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        Biomes.BIRCH_FOREST,
                        Biomes.FOREST,
                        Biomes.FLOWER_FOREST,
                        Biomes.DARK_FOREST,
                        Biomes.CHERRY_GROVE,
                        Biomes.TAIGA),
                MobCategory.CREATURE,
                ModEntities.GUINEA_PIG,
                8,
                2,
                4
        );

        // Register spawning restrictions for Guinea Pigs:
        // They spawn on the ground on blocks with heightmap MOTION_BLOCKING_NO_LEAVES,
        // and use the natural spawn condition of animals.
        SpawnPlacements.register(
                ModEntities.GUINEA_PIG,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules
        );
    }
}