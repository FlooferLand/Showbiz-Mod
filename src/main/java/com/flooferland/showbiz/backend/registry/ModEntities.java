package com.flooferland.showbiz.backend.registry;

import com.flooferland.showbiz.ShowbizMod;
import com.flooferland.showbiz.client.entity.custom.InteractPartEntity;
import com.flooferland.showbiz.client.entity.renderer.InteractPartEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/** Registry class for entities */
public class ModEntities {
    public static EntityType<InteractPartEntity> INTERACT_PART;

    public interface IEntityBuilder<E extends Entity> { E create(World world); }
    public interface IEntityDataBuilder<E extends Entity> { EntityType.Builder<E> build(EntityType.Builder<E> builder); }
    private static <E extends Entity> EntityType<E> registerEntity(String id, IEntityBuilder<E> entityBuilder, IEntityDataBuilder<E> additionalBuilder, SpawnGroup spawnGroup) {
        var identifier = Identifier.of(ShowbizMod.MOD_ID, id);
        var builder = EntityType.Builder.create((e, world) -> entityBuilder.create(world), spawnGroup);
        var entityType = additionalBuilder.build((EntityType.Builder<E>) builder).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, identifier));
        
        return Registry.register(Registries.ENTITY_TYPE, identifier, entityType);
    }
    
    @Environment(EnvType.CLIENT)
    public static void registerClientEntities() {
        INTERACT_PART = registerEntity(
                "interact_part",
                InteractPartEntity::new,
                (builder) -> builder,
                SpawnGroup.MISC
        );

        EntityRendererRegistry.register(INTERACT_PART, InteractPartEntityRenderer::new);
    }

    /** Keeps Java from ignoring the class */
    public static void registerEntities() {
        // FabricDefaultAttributeRegistry.register(PLACEHOLDER_ENTITY, PlaceholderEntity.setAttributes());
    }
}
