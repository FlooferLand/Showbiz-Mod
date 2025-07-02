package com.flooferland.showbiz.backend.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.flooferland.chirp.safety.Option;
import com.flooferland.chirp.util.ChirpMath;
import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.backend.util.MultiPartManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// TODO: Add Java vanilla model support

/**
 * Handles loading data from
 */
public class MultiPartResourceReloader extends SinglePreparationResourceReloader<MultiPartResourceReloader.ModelMap> implements IdentifiableResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Identifier.of(ShowbizMod.MOD_ID, "modelpart_loader");
    }

    @Override
    protected MultiPartResourceReloader.ModelMap prepare(ResourceManager manager, Profiler profiler) {
        var modelMap = new MultiPartResourceReloader.ModelMap();
        ShowbizMod.LOGGER.info("Preparing MultiPartResourceReloader");
        var resources = manager.findResources("geckolib/models/block", e -> true);
        for (var entry : resources.entrySet()) {
            var id = entry.getKey();
            var res = entry.getValue();
            try {
                var json = JsonParser.parseReader(res.getReader()).getAsJsonObject();
                if (json.has("minecraft:geometry")) {
                    var geometryInfo = json.get("minecraft:geometry").getAsJsonArray().get(0).getAsJsonObject();
                    var bones = geometryInfo.get("bones").getAsJsonArray();
                    var boneMap = new HashMap<String, PartData>();
                    for (var b : bones) {
                        var bone = b.getAsJsonObject();
                        var boneName = bone.get("name").getAsString();
                        String parent = null;
                        if (bone.has("parent")) {
                            parent = bone.get("parent").getAsString();
                        }
                        if (bone.has("cubes")) {
                            var emptyArray3 = new JsonArray(3);
                            emptyArray3.add(0);
                            emptyArray3.add(0);
                            emptyArray3.add(0);
                            var cubes = bone.get("cubes").getAsJsonArray();
                            for (int i = 0; i < cubes.size(); i++) {
                                var cube = cubes.get(i).getAsJsonObject();
                                var origin = Objects.requireNonNullElse(cube.get("origin"), emptyArray3).getAsJsonArray();
                                var pivot = Objects.requireNonNullElse(cube.get("pivot"), emptyArray3).getAsJsonArray();
                                var size = Objects.requireNonNullElse(cube.get("size"), emptyArray3).getAsJsonArray();
                                var rotation = Objects.requireNonNullElse(cube.get("rotation"), emptyArray3).getAsJsonArray();
                                var _parent = Option.conditional(parent != null, parent);

                                // TODO: Handle rotation for multi-part interaction block size
                                var gridSize = 8;
                                var piv = new Vec3d(
                                    pivot.get(0).getAsDouble() / gridSize,
                                    pivot.get(1).getAsDouble() / gridSize,
                                    pivot.get(2).getAsDouble() / gridSize
                                );
                                var pos = new Vec3d(
                                        origin.get(0).getAsDouble() / gridSize,
                                        origin.get(1).getAsDouble() / gridSize,
                                        origin.get(2).getAsDouble() / gridSize
                                );
                                var rot = new Vec3d(
                                        rotation.get(0).getAsDouble(),
                                        rotation.get(1).getAsDouble(),
                                        rotation.get(2).getAsDouble()
                                );
                                var width = (size.get(0).getAsFloat() + size.get(2).getAsFloat()) / 2;
                                var height = size.get(1).getAsFloat();
                                width = width / gridSize;
                                height = height / gridSize;
                                
                                var partData = new PartData(pos, new Vec2f(width, height), piv, rot, _parent);
                                if (i == 0) boneMap.put(boneName, partData);
                                boneMap.put(boneName + "/" + i, partData);
                            }
                        }
                    }
                    
                    // Applying cube origin based rotations
                    for (var boneEntry : boneMap.entrySet()) {
                        var name = boneEntry.getKey();
                        var part = boneEntry.getValue();
                        
                        var resultPair = ChirpMath.rotateAround(part.pos, part.pivot, part.rotation.x, part.rotation.y, part.rotation.z);
                        part.pos = resultPair.position();
                        part.rotation = resultPair.rotation();
                    }
                    
                    modelMap.put(id, boneMap);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ShowbizMod.LOGGER.info("Found resource with id {}", id);
        }
        ShowbizMod.LOGGER.info("Found {} multi-part assets", modelMap.size());
        return modelMap;
    }

    @Override
    protected void apply(MultiPartResourceReloader.ModelMap prepared, ResourceManager manager, Profiler profiler) {
        MultiPartManager.multiPartData = prepared;
    }
    
    /** A map holding the parts by name, under the ID of the object */
    public static class ModelMap extends HashMap<Identifier, Map<String, PartData>> {}

    /** Holds data about the multipart part */
    public static class PartData {
        public @NotNull Vec3d pos;
        public @NotNull Vec2f size;
        public @NotNull Vec3d pivot;
        public @NotNull Vec3d rotation;
        public @NotNull Option<String> parent;
        public static final PartData ZERO = new PartData(Vec3d.ZERO, new Vec2f(1f, 1f), Vec3d.ZERO, Vec3d.ZERO, Option.none());
        public PartData(@NotNull Vec3d pos, @NotNull Vec2f size, @NotNull Vec3d pivot, @NotNull Vec3d rotation, @NotNull Option<String> parent) {
            this.pos = pos;
            this.size = size;
            this.pivot = pivot;
            this.rotation = rotation;
            this.parent = parent;
        }
     }
}
