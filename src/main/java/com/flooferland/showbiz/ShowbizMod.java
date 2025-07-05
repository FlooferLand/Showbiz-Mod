package com.flooferland.showbiz;

import com.flooferland.showbiz.backend.registry.*;
import com.flooferland.showbiz.backend.resource.MultiPartResourceReloader;
import com.flooferland.showbiz.backend.resource.SoundsResourceReloader;
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
		var clientResources = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
		var serverResources = ResourceManagerHelper.get(ResourceType.SERVER_DATA);
		clientResources.registerReloadListener(new SoundsResourceReloader());
		clientResources.registerReloadListener(new MultiPartResourceReloader());
		
		// Registering content; Making sure classes won't get optimized away
		ModBlocks.registerBlocks();
		ModBlocksWithEntities.registerBlocks();
		ModEntities.registerEntities();
		ModItems.registerItems();
		ModCommands.registerCommands();
		ModSounds.registerSounds();
		ModJukeboxSongs.registerMusic();
		ModDataComponents.registerDataComponents();
		ModRecipes.registerRecipes();
		ModNetworking.register();
		
		LOGGER.info("The {} mod initialized!", MOD_ID);
	}

	public static Path getConfigPath(boolean isServer) {
		return FabricLoader.getInstance().getConfigDir()
				.resolve(MOD_ID)
				.resolve((isServer ? "server" : "client") + ".json");
	}
}