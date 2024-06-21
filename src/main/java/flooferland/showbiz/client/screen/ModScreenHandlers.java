package flooferland.showbiz.client.screen;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.block.base.ContainerBlock;
import flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import flooferland.showbiz.client.screen.custom.ContainerBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.inventory.Inventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<ContainerBlockScreenHandler> CONTAINER_BLOCK_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of(ShowbizMod.MOD_ID, "container_block"),
                    new ExtendedScreenHandlerType<>(ContainerBlockScreenHandler::new, ContainerBlockEntity.CodecData.PACKET_CODEC)
            );
    
    /** Keeps Java from ignoring the class */
    public static void registerScreenHandlers() {}
}
