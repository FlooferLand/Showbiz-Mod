package com.flooferland.showbiz.client.registry;
import com.mojang.brigadier.context.CommandContext;
import com.flooferland.showbiz.client.entity.custom.InteractPartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;

@Environment(EnvType.CLIENT)
public class ModClientCommands {
    // region | Commands
    protected static int useTheSubcommands(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("Use the subcommands"));
        return -1;
    }
    protected static int debug_spawnInteractable(CommandContext<FabricClientCommandSource> context) {
        var world = context.getSource().getWorld();
        var player = context.getSource().getPlayer();
        
        var pos = player.getBlockPos();
        var entity = new InteractPartEntity(world, "TEST_ENTITY", pos.toCenterPos(), new Vec2f(0.5f, 0.5f), pos);
        world.addEntity(entity);

        context.getSource().sendFeedback(Text.literal("Spawned an entity"));
        return 1;
    }
    protected static int debug_killInteractable(CommandContext<FabricClientCommandSource> context) {
        var world = context.getSource().getWorld();
        var player = context.getSource().getPlayer();
        var entities = world.getEntitiesByClass(InteractPartEntity.class, Box.of(player.getPos(), 20, 20, 20), p -> true);
        int killed = 0;
        for (var entity : entities) {
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
        context.getSource().sendFeedback(Text.of(String.format("Killed %s entities", killed)));
        return 1;
    }
    // endregion
    
    public static void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("showbiz")
                    .executes(ModClientCommands::useTheSubcommands)
                    .then(
                        ClientCommandManager.literal("debug")
                            .executes(ModClientCommands::useTheSubcommands)
                            .then(
                                    ClientCommandManager.literal("spawn_interactable")
                                            .executes(ModClientCommands::debug_spawnInteractable)
                            )
                            .then(
                                    ClientCommandManager.literal("kill_interactable")
                                            .executes(ModClientCommands::debug_killInteractable)
                            )
                    )
            )
        );
    }
}
