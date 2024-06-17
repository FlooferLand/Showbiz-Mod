package flooferland.showbiz;

import flooferland.showbiz.backend.audio.ModSound;
import flooferland.showbiz.backend.block.ModBlocks;
import flooferland.showbiz.backend.block.ModBlocksWithEntities;
import flooferland.showbiz.backend.entity.ModEntities;
import flooferland.showbiz.backend.item.ModItems;
import flooferland.showbiz.backend.resource.SoundsResourceReloader;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ShowbizMod implements ModInitializer {
	public static final String MOD_ID = "showbiz";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Asset loading
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
				.registerReloadListener(new SoundsResourceReloader());
		
		// Registering content; Making sure classes won't get optimized away
		ModBlocks.registerBlocks();
		ModBlocksWithEntities.registerBlocks();
		ModEntities.registerEntities();
		ModItems.registerItems();
		ModSound.registerSounds();
		
		LOGGER.info(String.format("The %s mod initialized!", MOD_ID));
	}

	public static Path getConfigPath(boolean isServer) {
		return FabricLoader.getInstance().getConfigDir()
				.resolve(MOD_ID)
				.resolve((isServer ? "server" : "client") + ".json");
	}
}