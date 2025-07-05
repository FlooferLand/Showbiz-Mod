package com.flooferland.showbiz.backend.resource;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.backend.registry.ModSounds;
import com.flooferland.showbiz.backend.type.SoundInfo;
import com.flooferland.showbiz.backend.util.ShowbizEnv;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/**
 * Loads in additional sound metadata from the mod's resource pack
 */
public class SoundsResourceReloader extends SinglePreparationResourceReloader<HashMap<String, Float>> implements IdentifiableResourceReloadListener {
    @Override
    protected HashMap<String, Float> prepare(ResourceManager manager, Profiler profiler) {
        HashMap<String, Float> lengths = new HashMap<>();
        // if (ShowbizEnv.isDataGenerator()) return lengths;
        
        // region | Reading the sounds.json
        final JsonObject json;
        {
            Identifier soundsJsonId = Identifier.of(ShowbizMod.MOD_ID, "sounds.json");
            Optional<Resource> resource = manager.getResource(soundsJsonId);

            if (resource.isPresent()) {
                Resource loadedResource = resource.get();
                try {
                    BufferedReader reader = loadedResource.getReader();
                    json = JsonParser.parseReader(reader).getAsJsonObject();
                    reader.close();
                } catch (IOException | JsonIOException e) {
                    throw new RuntimeException("Error reading " + soundsJsonId + ": " + e);
                } catch (JsonParseException e) {
                    throw new RuntimeException("Error parsing " + soundsJsonId + ": " + e);
                }
            } else {
                throw new RuntimeException("Missing asset: " + soundsJsonId);
            }
        }
        // endregion

        // Reading the length
        json.asMap().forEach((key, value) -> {
            float length = value.getAsJsonObject().get("length").getAsJsonPrimitive().getAsFloat();
            lengths.put(key, length);

            // Info
            ShowbizMod.LOGGER.debug(String.format("Loaded advanced sound \"%s\" (length=%s)", key, length));

            // Safety check
            if (length <= 0.0f) {
                ShowbizMod.LOGGER.error(String.format("Something went wrong retrieving the length for SoundEvent \"%s\" (length=%s)", key, length));
            }
        });

        ShowbizMod.LOGGER.debug("Successfully loaded the sounds from disk!");

        // Returning all the lengths
        return lengths;
    }

    @Override
    protected void apply(HashMap<String, Float> prepared, ResourceManager manager, Profiler profiler) {
        ModSounds.soundsFileData = prepared;
        for (ModSounds sound : ModSounds.values()) {
            // Guards
            if (ShowbizEnv.isDataGenerator()) return;
            if (ModSounds.soundsFileData.isEmpty()) {
                throw new RuntimeException("soundsFileData is empty");
            }

            // Retrieving the lengths
            var length = ModSounds.soundsFileData.get(sound.id.getPath());
            if (length == null || length <= 0.0f) {
                throw new RuntimeException(String.format("Something went wrong retrieving the length from the file data for SoundEvent \"%s\" (length=%s)", sound.id.getPath(), length));
            }

            // Constructing a new mod sound
            sound.info = new SoundInfo(sound.id, length);
            sound.event = SoundEvent.of(sound.id);
        }
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(ShowbizMod.MOD_ID, "sound_loader");
    }
}
