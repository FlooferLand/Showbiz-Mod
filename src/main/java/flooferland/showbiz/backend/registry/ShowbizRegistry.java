package flooferland.showbiz.backend.registry;

import com.mojang.datafixers.util.Function3;
import flooferland.showbiz.ShowbizMod;
import flooferland.showbiz.backend.item.base.BlockEntityItem;
import flooferland.showbiz.backend.type.BlockEntityData;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ObjectUtils;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public final class ShowbizRegistry {
	public static class BlockSettingsPatch<TBlock extends Block> extends AbstractBlock.Settings {
		public final Identifier identifier;
		public BlockSettingsPatch(Identifier identifier) {
			super();
			this.registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier));
			this.identifier = identifier;
		}
		public static <TBlock extends Block> BlockSettingsPatch<TBlock> create(Identifier identifier) {
			return new BlockSettingsPatch<>(identifier);
		}
		public Block.Settings copy(Block block) {
			return super.copy(block)
				.registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier));
		}
	}
	
	/// Registers a block, as well as an item for it
	public static <TBlock extends Block, TItem extends BlockItem> TBlock block(
			String name,
			Function<BlockSettingsPatch<TBlock>, TBlock> blockBuilder,
			Function3<TBlock, TItem.Settings, ObjectUtils.Null, TItem> itemBuilder
		) {
		var identifier = Identifier.of(ShowbizMod.MOD_ID, name);
		var blockSettings = BlockSettingsPatch.create(identifier);
		var itemSettings = new TItem.Settings()
			.registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier));
		
		var block = blockBuilder.apply((BlockSettingsPatch<TBlock>) blockSettings);
		var item = itemBuilder.apply(block, itemSettings, ObjectUtils.NULL);

		Registry.register(Registries.ITEM, identifier, item);
		return Registry.register(Registries.BLOCK, identifier, block);
	}

	/// Registers an item
	public static <TItem extends Item> TItem registerItem(String name, Function<TItem.Settings, TItem> itemBuilder) {
		var identifier = Identifier.of(ShowbizMod.MOD_ID, name);
		var itemSettings = new TItem.Settings()
			.registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier));
		return Registry.register(Registries.ITEM, identifier, itemBuilder.apply(itemSettings));
	}

	/// Registers a BlockEntity, as well as a block, and an item for it
	public static <TBlock extends BlockWithEntity, TEntity extends BlockEntity & GeoBlockEntity> BlockEntityData<TEntity> blockWithEntity(
		String id,
		Function<BlockSettingsPatch<TBlock>, TBlock> blockBuilder,
		BlockItem.Settings itemSettings,
		FabricBlockEntityTypeBuilder.Factory<TEntity> entity,
		GeoModel<BlockEntityItem<TEntity>> model) {

		Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);
		itemSettings = itemSettings.registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier));

		// Making the block
		var block = (Block & BlockEntityProvider) blockBuilder.apply(BlockSettingsPatch.create(identifier));

		// Registering the item
		BlockEntityItem<TEntity> itemType = new BlockEntityItem<>(itemSettings, block, model);
		var item = Registry.register(Registries.ITEM, identifier, itemType);

		// Registering the block
		Registry.register(Registries.BLOCK, identifier, block);
		block.getSettings().registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier));

		// Registering the block entity
		var blockEntityBuilder = FabricBlockEntityTypeBuilder.create(entity, block).build();
		BlockEntityType<TEntity> blockEntity = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			identifier,
			blockEntityBuilder
		);

		// Returning
		return new BlockEntityData<>(item, block, blockEntity, identifier);
	}
}
