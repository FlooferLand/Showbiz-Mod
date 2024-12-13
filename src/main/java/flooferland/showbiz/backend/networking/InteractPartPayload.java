package flooferland.showbiz.backend.networking;

import flooferland.showbiz.backend.registry.ModNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public record InteractPartPayload(String partName, BlockPos parentBlock, UUID playerId) implements CustomPayload {
    public static final Id<InteractPartPayload> ID = new Id<>(ModNetworking.PART_INTERACT);
    public static final PacketCodec<RegistryByteBuf, InteractPartPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, InteractPartPayload::partName,
            BlockPos.PACKET_CODEC, InteractPartPayload::parentBlock,
            Uuids.PACKET_CODEC, InteractPartPayload::playerId,
            InteractPartPayload::new
    );
    
    @Override
    public Id<InteractPartPayload> getId() {
        return ID;
    }
}

