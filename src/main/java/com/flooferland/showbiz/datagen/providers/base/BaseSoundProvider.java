package com.flooferland.showbiz.datagen.providers.base;

import com.flooferland.showbiz.backend.registry.ModJukeboxSongs;
import com.google.gson.*;
import com.flooferland.showbiz.backend.registry.ModSounds;
import com.flooferland.showbiz.backend.util.ShowbizAudio;
import com.flooferland.showbiz.backend.util.ShowbizEnv;
import com.flooferland.showbiz.backend.util.ShowbizExceptions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// TODO: Add a way to automatically find the sound via the registry ID. Would get rid of repetition.
//       Ex: `build.add(ModSound.TEST, "test")` would become `build.add(ModSound.TEST)`
//       Ex: `build.add(ModSound.TAPE_PULL, "mechanical/tape_pull")` would become `build.add(ModSound.TAPE_PULL, "mechanical")`

/** Made this as there is no built-in Fabric implementation.
 *  This also stores additional information inside `sounds.json` which should honestly be provided in vanilla
 */
public abstract class BaseSoundProvider implements DataProvider {
    protected final FabricDataOutput dataOutput;
    public BaseSoundProvider(FabricDataOutput dataOutput) {
        this.dataOutput = dataOutput;
    }

    public abstract void generateSoundInfo(SoundBuilder builder);

    public enum SoundType {
        Sound,
        MusicDisc
    }

    /// Glue interface for adding sounds.
    /// See the actual implementation in {@code BaseSoundProvider.run}
    @ApiStatus.NonExtendable
    @FunctionalInterface
    public interface SoundBuilder {
        void add(Identifier id, List<String> soundPaths, @Nullable String subtitleName, SoundType soundType, boolean shouldStream);

        // region | Full implementations
        default void addNumbered(ModSounds sound, SoundBuilderAddNumbered lambda, boolean shouldStream) {
            ArrayList<String> soundPaths = new ArrayList<>();

            SoundBuilderAddNumbered getPath = (num) ->
                    ShowbizEnv.getResourcePackAssets()
                            .resolve("sounds")
                            .resolve(lambda.function(num))
                            .toString();
            
            for (int i = 1; Files.exists(Path.of(getPath.function(i) + ".ogg")); i++) {
                soundPaths.add(lambda.function(i));
            }
            
            if (soundPaths.isEmpty()) {
                throw new RuntimeException("addNumbered failed; for loop had 0 iterations. Path: " + getPath.function(1));
            }
            if (sound == null) {
                throw new RuntimeException("addNumbered failed; sound is null. Path: " + getPath.function(1));
            }
            
            add(sound.id, soundPaths, null, SoundType.Sound, shouldStream);
        }
        /** Automatically adds several sounds using a lambda that pads out the number by 2. <br/>
         *  Example path input: <code>animatronics/something/footstep</code> (_0, _1, _2, etc. will be added automagically) */
        default void addNumberedAuto(ModSounds sound, String path, boolean shouldStream) {
            addNumbered(sound, (num) -> String.format("%s_%02d", path, num), shouldStream);
        }
        // endregion

        // TODO: Organize this hell!!!

        // region | Primary variations
        default void add(ModSounds sound, List<String> soundPaths, boolean shouldStream) {
            add(sound.id, soundPaths, null, SoundType.Sound, shouldStream);
        }
        default void add(ModSounds sound, List<String> soundPaths, String subtitleName, boolean shouldStream) {
            add(sound.id, soundPaths, subtitleName, SoundType.Sound, shouldStream);
        }
        default void addSong(ModJukeboxSongs song) {
            add(song.soundId, List.of(song.itemId.getPath()), null, SoundType.MusicDisc, true);
        }
        // endregion

        // region | Convenience variations
        default void add(ModSounds sound, List<String> soundPaths) {
            add(sound, soundPaths, null, false);
        }
        default void add(ModSounds sound, String path) {
            add(sound, List.of(path), null, false);
        }
        default void add(ModSounds sound, String path, boolean shouldStream) {
            add(sound, List.of(path), null, shouldStream);
        }
        default void add(ModSounds sound, String path, String subtitleName, boolean shouldStream) {
            add(sound, List.of(path), subtitleName, shouldStream);
        }
        
        default void add(ModSounds sound, List<String> soundPaths, String subtitleName) {
            add(sound, soundPaths, subtitleName, false);
        }
        default void add(ModSounds sound, String path, String subtitleName) {
            add(sound, List.of(path), subtitleName, false);
        }
        
        default void addNumbered(ModSounds sound, SoundBuilderAddNumbered lambda) {
            addNumbered(sound, lambda, false);
        }
        
        /** Automatically adds several sounds using a lambda that pads out the number by 2. <br/>
         *  Example path input: <code>animatronics/something/footstep</code> (_0, _1, _2, etc. will be added automagically) */
        default void addNumberedAuto(ModSounds sound, String path) {
            addNumberedAuto(sound, path, false);
        }
        // endregion
    }
    
    public interface SoundBuilderAddNumbered {
        String function(int i);
    }

    @Override
    public String getName() {
        return "Sound Generator";
    }

    // region | Files and paths
    public Path getSoundsDirectory() {
        return ShowbizEnv.getResourcePackAssets()
                .resolve("sounds");
    }

    public Path getSoundsJsonFilePath() {
        return dataOutput
            .resolvePath(DataOutput.OutputType.RESOURCE_PACK)
            .resolve(dataOutput.getModId())
            .resolve("sounds.json");
    }

    public Path getJukeboxSongDirectory() {
        return dataOutput
            .resolvePath(DataOutput.OutputType.DATA_PACK)
            .resolve(dataOutput.getModId())
            .resolve("jukebox_song");
    }
    // endregion

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        JsonObject soundsJson = new JsonObject();

        generateSoundInfo( // Implementation for "SoundBuilder.add"
            new SoundBuilder() {

                @Override
                public void add(Identifier soundId, List<String> soundPaths, @Nullable String subtitleName, SoundType soundType, boolean shouldStream) {
                    // Getting some stuff
                    String modId = dataOutput.getModId();
                    String subtitleKey =
                        (soundType == SoundType.Sound)
                            ? (subtitleName != null)
                                ? String.format("subtitle.%s.%s", modId, subtitleName)
                                : String.format("subtitle.%s.%s", modId, soundId.getPath())
                            : String.format("item.%s.%s.desc", modId, soundId.getPath().replace(".", "_"))
                    ;

                    // TODO: Make it throw a warning if the subtitle path and the file name aren't the same
                    // TODO: Add a easy-to-read "path not found" error and fail without crashing/stopping data generation

                    // Safety check
                    for (String soundPath : soundPaths) {
                        soundPath = soundPath.endsWith(".ogg") ? soundPath : (soundPath + ".ogg");
                        String path = getSoundsDirectory().resolve(soundPath).toString();
                        if (!Files.exists(Path.of(path)))
                            throw new ShowbizExceptions.RuntimeNoTrace(
                                String.format("Sound at relative path \"%s\" does not exist! - Full path: \"%s\"", soundPath, path)
                            );
                    }

                    // region | Getting the length of the audio
                    float durationInSeconds;
                    {
                        // Getting the path to the audio
                        String audioPath = soundPaths.getFirst() + ".ogg";
                        String path = getSoundsDirectory().resolve(audioPath).toString();

                        // Reading the length of the audio
                        durationInSeconds = ShowbizAudio.getOggAudioLength(path);
                    }
                    // endregion

                    // region | Constructing sounds.json data
                    {
                        // Top-level JSON
                        JsonObject data = new JsonObject();
                        data.add("subtitle", new JsonPrimitive(subtitleKey));
                        data.add("stream", new JsonPrimitive(shouldStream));
                        data.add("length", new JsonPrimitive(durationInSeconds));

                        // Adding the randomised audio clips
                        JsonArray variations = new JsonArray();
                        for (String path : soundPaths) {
                            variations.add(new JsonPrimitive(modId + ":" + path));
                        }
                        data.add("sounds", variations);

                        // Adding the data at the sound ID
                        soundsJson.add(soundId.getPath(), data);
                    }
                    // endregion

                    // region | Constructing music disc data file, if necessary
                    if (soundType == SoundType.MusicDisc) {
                        var rawId = soundId.getPath().toString().split("\\.")[1];

                        var songJson = new JsonObject();
                        songJson.add("comparator_output", new JsonPrimitive(15));
                        songJson.add("length_in_seconds", new JsonPrimitive(durationInSeconds));
                        songJson.add("sound_event", new JsonPrimitive(soundId.toString()));

                        // Adding the description
                        var description = new JsonObject();
                        description.add("translate", new JsonPrimitive(subtitleKey));
                        songJson.add("description", description);

                        // Writing the file
                        futures.add(
                            DataProvider.writeToPath(writer, songJson, getJukeboxSongDirectory().resolve(rawId+".json"))
                        );
                    }
                    // endregion
                }
            }
        );

        // DEBUG
        // TODO: Fix the sounds provider not saving. EDIT: Check if this was already fixed
        // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // ShowbizMod.LOGGER.info(String.format("path=\"%s\"\n\n%s\n", getOutputFilePath(), gson.toJson(output)));

        // [..]

        // Write sounds.json
        futures.add(
            DataProvider.writeToPath(writer, soundsJson, getSoundsJsonFilePath())
        );

        // Writing the JSON data
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
}
