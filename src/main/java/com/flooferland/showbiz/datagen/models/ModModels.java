package com.flooferland.showbiz.datagen.models;

import com.flooferland.showbiz.ShowbizMod;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
import net.minecraft.util.Identifier;
import java.util.Optional;

/**
 * Utilities to create vanilla models, as well as storage for them.
 * Useful for registering custom models via datagen
 */
@SuppressWarnings("unused")
public class ModModels {
    // public static final Model FLUFFY_BUSH = block("template/fluffy_bush", TextureKey.ALL);
    
    // region | Registration methods
    private static Model make(TextureKey... requiredTextureKeys) {
        return new Model(Optional.empty(), Optional.empty(), requiredTextureKeys);
    }

    private static Model block(String parent, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(ShowbizMod.MOD_ID, "block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model item(String parent, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(ShowbizMod.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model block(String parent, String variant, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(Identifier.of(ShowbizMod.MOD_ID, "block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    public final Model getBlockModelFromIdPath(String path, Optional<String> variant, TextureKey texKey) {
        return new Model(Optional.of(Identifier.of(ShowbizMod.MOD_ID, "block/" + path)), variant, texKey);
    }
    // endregion
}
