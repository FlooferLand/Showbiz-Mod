package flooferland.showbiz.backend.util;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.datagen.ShowbizDataGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.nio.file.Path;
import java.util.Optional;

public final class ShowbizEnv {    
    public static Path getResourcePackRoot() {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(ShowbizMod.MOD_ID);
        if (container.isEmpty())
            throw new RuntimeException("Resource pack path error; Mod container is empty");
        
        // Root resource pack path (holds the `assets` and `data` folders)
        return container.get().getRootPaths().getFirst();
    }

    public static Path getResourcePackAssets() {
        return getResourcePackRoot()
                .resolve("assets")
                .resolve(ShowbizMod.MOD_ID);
    }
    
    /** Returns true if the environment is a server (includes datagen) */
    public static boolean isServer() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER    ;
    }
    
    /** Returns true if the environment is the data generator */
    public static boolean isDataGenerator() {
        return isServer() && ShowbizDataGenerator.runningDatagen;
    }
}
