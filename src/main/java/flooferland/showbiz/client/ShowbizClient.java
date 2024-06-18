package flooferland.showbiz.client;

import flooferland.showbiz.backend.entity.ModEntities;
import flooferland.showbiz.client.blockEntity.ModClientBlocksComplex;
import flooferland.showbiz.client.item.ModItemGroups;
import net.fabricmc.api.ClientModInitializer;

public class ShowbizClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // TODO: Register client-side config here

        ModItemGroups.registerItemGroups();
        ModEntities.registerClientEntities();
        ModClientBlocksComplex.registerClientBlockEntities();
    }
}
