package com.flooferland.showbiz.client;

import com.flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import com.flooferland.showbiz.client.registry.ModClientCommands;
import com.flooferland.showbiz.backend.registry.ModEntities;
import com.flooferland.showbiz.backend.registry.ModNetworking;
import com.flooferland.showbiz.client.registry.ModItemGroups;
import com.flooferland.showbiz.client.registry.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;

public class ShowbizClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // TODO: Register client-side config here

        ModItemGroups.registerItemGroups();
        ModBlocksWithEntities.registerClient();
        ModEntities.registerClientEntities();
        ModClientCommands.registerClientCommands();
        ModScreenHandlers.registerScreenHandlers();
        ModNetworking.registerClient();
    }
}
