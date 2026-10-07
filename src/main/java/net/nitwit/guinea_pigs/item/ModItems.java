package net.nitwit.guinea_pigs.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.nitwit.guinea_pigs.GuineaPigs;
import net.nitwit.guinea_pigs.entity.ModEntities;
import net.nitwit.guinea_pigs.item.custom.DroppingsItem;


public class ModItems {

    // Custom item for guinea pig droppings
    public static final Item DROPPINGS = registerItem(
            "droppings",
            new DroppingsItem(new Item.Properties().setId(ResourceKey.create(
                    Registries.ITEM, Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "droppings")))));

    // Spawn egg for spawning guinea pig entities
    public static final Item GUINEA_PIG_SPAWN_EGG = registerItem(
            "guinea_pig_spawn_egg",
            new SpawnEggItem(new Item.Properties().setId(ResourceKey.create(
                    Registries.ITEM, Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, "guinea_pig_spawn_egg")))
                    .spawnEgg(ModEntities.GUINEA_PIG)));

    // Helper method to register an item in the game registry
    private static Item registerItem(String name, Item item) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item
        );
    }

    // Registers mod items and adds them to the appropriate creative mode item groups
    public static void registerModItems() {
        GuineaPigs.LOGGER.info("Registering Mod Items for " + GuineaPigs.MOD_ID);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register(entries -> entries.accept(DROPPINGS));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(entries -> entries.accept(GUINEA_PIG_SPAWN_EGG));
    }
}