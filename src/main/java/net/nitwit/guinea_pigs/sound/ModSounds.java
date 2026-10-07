package net.nitwit.guinea_pigs.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.nitwit.guinea_pigs.GuineaPigs;

public class ModSounds {
    // Define sound events with unique identifiers corresponding to your sound files
    public static final SoundEvent CHUTTING = registerSoundEvent("chutting");
    public static final SoundEvent RUMBLING = registerSoundEvent("rumbling");
    public static final SoundEvent SCREAM = registerSoundEvent("scream");
    public static final SoundEvent WHEEK = registerSoundEvent("wheek");

    // Helper method to register a sound event with the Minecraft registry
    private static SoundEvent registerSoundEvent(String name) {
        // Create a unique identifier for the sound event using your mod ID and sound name
        Identifier id = Identifier.fromNamespaceAndPath(GuineaPigs.MOD_ID, name);
        // Register the sound event and return the registered instance
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    // Call this method during your mod initialization to ensure registration and logging
    public static void registerSounds() {
        GuineaPigs.LOGGER.info("Registering Sounds for " + GuineaPigs.MOD_ID);
    }
}
