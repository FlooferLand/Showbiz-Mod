package flooferland.showbiz.backend.util;

import flooferland.showbiz.ShowbizMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

public final class ShowbizEnv {
    public static boolean isToughAsNailsLoaded() {
        return FabricLoader.getInstance().isModLoaded("toughasnails");
    }
    
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
    
    /** Returns true if the environment is the data generator */
    public static boolean isDataGenerator() {
        // TODO: Remove this workaround for detecting data generation
        return (Arrays.asList("1", "true", "TRUE")).contains(System.getenv("DATAGEN"));
        // return !FabricLoader.getInstance().getEntrypoints(ShowbizMod.MOD_ID, ShowbizDataGenerator.class).isEmpty();
    }
}
