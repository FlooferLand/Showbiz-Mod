package com.flooferland.showbiz.backend.registry;

import com.flooferland.showbiz.ShowbizMod;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Registry class for Jukebox music.
 * Don't forget to run data generation to generate a sounds.json entry
 */
public enum ModJukeboxSongs {
    // region | Music definitions
    Y2K("y2k");
    // endregion

    // region | Variables
    public final Item item;
    public final Identifier rawId;
    public final Identifier itemId;
    public final Identifier soundId;
    public final RegistryKey<JukeboxSong> song;
    public final SoundEvent event;
    ModJukeboxSongs(String rawId) {
        this.rawId = Identifier.of(ShowbizMod.MOD_ID, rawId);
        this.itemId = Identifier.of(ShowbizMod.MOD_ID, "music_disc_"+rawId);
        this.soundId = Identifier.of(ShowbizMod.MOD_ID, "music_disc."+rawId);
        this.song = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(ShowbizMod.MOD_ID, rawId));
        this.event = Registry.register(Registries.SOUND_EVENT, soundId, SoundEvent.of(soundId));
        this.item = ShowbizRegistry.registerItem(itemId.getPath(), (settings) -> new Item(settings.jukeboxPlayable(song).maxCount(1)));
    }
    // endregion
    
    /** Registers all the music by looping through all enum values */
    public static void registerMusic() {
        /*for (ModJukeboxSongs sound : ModJukeboxSongs.values()) {
            if (Registries.SOUND_EVENT.get(sound.id) == null) {
                Registry.register(Registries.ITEM, sound.id, sound.item);
            }
        }*/
    }
}
