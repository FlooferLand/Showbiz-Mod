package flooferland.showbiz.backend.networking;

import com.mojang.serialization.Codec;
import flooferland.showbiz.backend.registry.ModNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** When a multipart interactable block is destroyed */
public record InteractableDestroyPayload(List<Integer> entities) implements CustomPayload {
    public static final Id<InteractableDestroyPayload> ID = new Id<>(ModNetworking.PART_INTERACT_DESTROY);
    public static final PacketCodec<RegistryByteBuf, InteractableDestroyPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.codec(Codec.INT.listOf()), InteractableDestroyPayload::entities,
            InteractableDestroyPayload::new
    );
    
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
