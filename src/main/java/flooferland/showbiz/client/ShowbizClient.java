package flooferland.showbiz.client;

import flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import flooferland.showbiz.client.registry.ModClientCommands;
import flooferland.showbiz.backend.registry.ModEntities;
import flooferland.showbiz.backend.registry.ModNetworking;
import flooferland.showbiz.client.registry.ModItemGroups;
import flooferland.showbiz.client.registry.ModScreenHandlers;
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
