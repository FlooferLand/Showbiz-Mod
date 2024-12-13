package flooferland.showbiz.backend;

import flooferland.showbiz.backend.registry.ModNetworking;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ShowbizServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        // TODO: Register server-side config here

        ModNetworking.registerServer();
    }
}
