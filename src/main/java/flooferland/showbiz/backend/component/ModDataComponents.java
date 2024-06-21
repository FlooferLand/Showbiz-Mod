package flooferland.showbiz.backend.component;

import flooferland.showbiz.ShowbizMod;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class ModDataComponents {
    public static final ComponentType<Integer> TAPE_LENGTH = register(
            "tape_length",
            builder -> builder.
                    codec(Codecs.NONNEGATIVE_INT)
                    .packetCodec(PacketCodecs.VAR_INT)
    );

    @SuppressWarnings("SameParameterValue")
    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);
        return Registry.register(Registries.DATA_COMPONENT_TYPE, identifier, builderOperator.apply(ComponentType.builder()).build());
    }
    
    /** Keeps Java from ignoring the class */
    public static void registerDataComponents() {}
}
