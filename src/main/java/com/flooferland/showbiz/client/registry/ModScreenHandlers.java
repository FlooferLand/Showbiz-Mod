package com.flooferland.showbiz.client.registry;

import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.backend.blockEntity.base.ContainerBlockEntity;
import com.flooferland.showbiz.client.screen.custom.ContainerBlockScreen;
import com.flooferland.showbiz.client.screen.custom.ContainerBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<ContainerBlockScreenHandler> CONTAINER_BLOCK_SCREEN_HANDLER =
            register(
                    "container_block",
                    ContainerBlockScreenHandler::new,
                    ContainerBlockScreen::new,
                    ContainerBlockEntity.CodecData.PACKET_CODEC
            );
    
    /** the registration tower of hell */
    public static <
            THandler extends ScreenHandler,
            TScreen extends Screen & ScreenHandlerProvider<THandler>,
            TCodec
    > ScreenHandlerType<THandler> register(
            String id,
            ExtendedScreenHandlerType.ExtendedFactory<THandler, TCodec> handler,
            HandledScreens.Provider<THandler, TScreen> provider,
            PacketCodec<RegistryByteBuf, TCodec> codec
    ) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);

        ScreenHandlerType<THandler> type = new ExtendedScreenHandlerType<>(handler, codec);
        
        HandledScreens.register(type, provider);
        return Registry.register(Registries.SCREEN_HANDLER, identifier, type);
    }
    
    /** Keeps Java from ignoring the class */
    public static void registerScreenHandlers() {}
}
