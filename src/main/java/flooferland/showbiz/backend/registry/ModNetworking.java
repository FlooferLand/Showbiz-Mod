package flooferland.showbiz.backend.registry;

import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.networking.InteractPartPayload;
import flooferland.showbiz.backend.networking.InteractableDestroyPayload;
import flooferland.showbiz.backend.type.IMultiPartInteractable;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ModNetworking {
    public static final Identifier PART_INTERACT = Identifier.of(ShowbizMod.MOD_ID, "interactpart_interact");
    public static final Identifier PART_INTERACT_DESTROY = Identifier.of(ShowbizMod.MOD_ID, "interactpart_destroy");

    // Registering the payloads
    // NOTE: playCS2 and playS2C are not the same!
    public static void register() {
        PayloadTypeRegistry.playC2S().register(InteractPartPayload.ID, InteractPartPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(InteractableDestroyPayload.ID, InteractableDestroyPayload.CODEC);
    }
    
    public static void registerServer() {
        // Multipart interaction handler (MultiPartManager / InteractPartEntity)
        ServerPlayNetworking.registerGlobalReceiver(InteractPartPayload.ID, (payload, context) -> {
            var player = context.player();
            var server = context.server();
            ShowbizMod.LOGGER.info("InteractPartPayload:  Before server execute");
            server.execute(() -> {
                ShowbizMod.LOGGER.info("InteractPartPayload:  Received (Before player UUID check)");
                if (player.getUuid() != payload.playerId()) return;  // FIXME: This check might be redundant if context.player works
                ShowbizMod.LOGGER.info("InteractPartPayload:  Received (After player UUID check)");
                var world = player.getServerWorld();
                if (world.getBlockEntity(payload.parentBlock()) instanceof IMultiPartInteractable interactable) {
                    var mapping = interactable.getInteractionMapping();
                    ShowbizMod.LOGGER.info("InteractPartPayload:  Received an interaction!");
                    interactable.onInteract(mapping.get(payload.partName()), world, player);
                }
            });
            server.close();
        });
    }
    
    public static void registerClient() {
        // ClientBlockHighlighting.highlightBlock(client, payload.blockPos());
        ClientPlayNetworking.registerGlobalReceiver(InteractableDestroyPayload.ID, (payload, context) -> {
            var player = context.player();
            var client = context.client();
            client.execute(() -> {
                for (var entityId : payload.entities()) {
                    var entity = player.clientWorld.getEntityById(entityId);
                    if (entity != null) {
                        entity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            });
        });
    }
}
