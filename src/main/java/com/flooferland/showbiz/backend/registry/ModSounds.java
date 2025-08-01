package com.flooferland.showbiz.backend.registry;

import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.backend.type.SoundInfo;
import com.flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry class for sounds.
 * Don't forget to run data generation to generate a sounds.json entry
 */
public enum ModSounds {
    // region | Sound definitions
    PNEUMATIC_FIRE("pneumatic_fire"),
    PNEUMATIC_RELEASE("pneumatic_release"),
    TAPE_CLICK("tape_click"),
    TAPE_PULL("tape_pull");
    // endregion

    // Static
    public static Map<String, Float> soundsFileData = new HashMap<>();
    
    // region | Variables
    public final Identifier id;
    public SoundEvent event;
    public SoundInfo info;
    ModSounds(String id) {
        this.id = Identifier.of(ShowbizMod.MOD_ID, id);
    }
    // endregion

    // region | Utility
    /** Gets the length of the sound in game ticks */
    public int lengthInTicks() {
        return info != null ? info.length : ShowbizUtil.secondsToTicks(1.0);
    }

    /** Gets the length of the sound in seconds */
    public int lengthInSeconds() {
        return info.lengthSeconds;
    }
    // endregion
    
    /** Registers all the sounds by looping through all enum values */
    public static void registerSounds() {
        for (ModSounds sound : ModSounds.values()) {
            if (Registries.SOUND_EVENT.get(sound.id) == null) {
                sound.event = SoundEvent.of(sound.id);
                Registry.register(Registries.SOUND_EVENT, sound.id, sound.event);
            }
        }
    }
}
