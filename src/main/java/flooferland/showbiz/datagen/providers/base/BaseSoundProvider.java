package flooferland.showbiz.datagen.providers.base;

import com.google.gson.*;
import flooferland.showbiz.backend.registry.ModSound;
import flooferland.showbiz.backend.util.ShowbizAudio;
import flooferland.showbiz.backend.util.ShowbizEnv;
import flooferland.showbiz.backend.util.ShowbizExceptions;
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
        
    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        JsonObject output = new JsonObject();
        generateSoundInfo(
                (Identifier id, List<String> soundPaths, @Nullable String subtitleName, boolean shouldStream) -> {
                    // Getting some stuff
                    String modId = dataOutput.getModId();
                    String subtitleKey = (subtitleName != null)
                            ? String.format("subtitle.%s.%s", modId, subtitleName)
                            : String.format("subtitle.%s.%s", modId, id.getPath());

                    // TODO: Make it throw a warning if the subtitle path and the file name aren't the same
                    // TODO: Add a easy-to-read "path not found" error and fail without crashing/stopping data generation
                    // TODO: Add the ability to optionally specify the file format (`.ogg`) after a path in the build `add` method
                    
                    // Safety check
                    for (String soundPath : soundPaths) {
                        String path = getSoundsDirectory().resolve(soundPath + ".ogg").toString();
                        if (!Files.exists(Path.of(path)))
                            throw new ShowbizExceptions.RuntimeNoTrace(
                                    String.format("Sound at relative path \"%s\" does not exist! - Full path: \"%s\"", soundPath, path)
                            );
                    }
                    
                    // region | Getting the length of the audio
                    float durationInSeconds;
                    {
                        // Getting the path to the audio
                        String audioLengthPath = soundPaths.getFirst() + ".ogg";
                        String path = getSoundsDirectory().resolve(audioLengthPath).toString();

                        // Reading the length of the audio
                        durationInSeconds = ShowbizAudio.getOggAudioLength(path);
                    }
                    // endregion
                    
                    // region | Constructing JSON data
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
                        output.add(id.getPath(), data);
                    }
                    // endregion
                }
        );
        
        // DEBUG
        // TODO: Fix the sounds provider not saving. EDIT: Check if this was already fixed
        // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // ShowbizMod.LOGGER.info(String.format("path=\"%s\"\n\n%s\n", getOutputFilePath(), gson.toJson(output)));
        
        // Writing the JSON data
        return DataProvider.writeToPath(writer, output, getOutputFilePath());
    }

    public abstract void generateSoundInfo(SoundBuilder builder);
    
    @ApiStatus.NonExtendable
    @FunctionalInterface
    public interface SoundBuilder {
        void add(Identifier id, List<String> soundPaths, @Nullable String subtitleName, boolean shouldStream);

        // region | Full implementations
        default void addNumbered(ModSound sound, SoundBuilderAddNumbered lambda, boolean shouldStream) {
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
            
            add(sound.id, soundPaths, null, shouldStream);
        }
        /** Automatically adds several sounds using a lambda that pads out the number by 2. <br/>
         *  Example path input: <code>animatronics/something/footstep</code> (_0, _1, _2, etc. will be added automagically) */
        default void addNumberedAuto(ModSound sound, String path, boolean shouldStream) {
            addNumbered(sound, (num) -> String.format("%s_%02d", path, num), shouldStream);
        }
        // endregion

        // region | Convenience variations
        // TODO: Organize this hell!!!
        default void add(ModSound sound, List<String> soundPaths, boolean shouldStream) {
            add(sound.id, soundPaths, null, shouldStream);
        }
        default void add(ModSound sound, List<String> soundPaths, String subtitleName, boolean shouldStream) {
            add(sound.id, soundPaths, subtitleName, shouldStream);
        }
        
        default void add(ModSound sound, List<String> soundPaths) {
            add(sound, soundPaths, null, false);
        }
        default void add(ModSound sound, String path) {
            add(sound, List.of(path), null, false);
        }
        default void add(ModSound sound, String path, boolean shouldStream) {
            add(sound, List.of(path), null, shouldStream);
        }
        default void add(ModSound sound, String path, String subtitleName, boolean shouldStream) {
            add(sound, List.of(path), subtitleName, shouldStream);
        }
        
        default void add(ModSound sound, List<String> soundPaths, String subtitleName) {
            add(sound, soundPaths, subtitleName, false);
        }
        default void add(ModSound sound, String path, String subtitleName) {
            add(sound, List.of(path), subtitleName, false);
        }
        
        default void addNumbered(ModSound sound, SoundBuilderAddNumbered lambda) {
            addNumbered(sound, lambda, false);
        }
        
        /** Automatically adds several sounds using a lambda that pads out the number by 2. <br/>
         *  Example path input: <code>animatronics/something/footstep</code> (_0, _1, _2, etc. will be added automagically) */
        default void addNumberedAuto(ModSound sound, String path) {
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

    public Path getSoundsDirectory() {
        return ShowbizEnv.getResourcePackAssets()
                .resolve("sounds");
    }

    public Path getOutputFilePath() {
        return dataOutput
                .resolvePath(DataOutput.OutputType.RESOURCE_PACK)
                .resolve(dataOutput.getModId())
                .resolve("sounds.json");
    }
}
