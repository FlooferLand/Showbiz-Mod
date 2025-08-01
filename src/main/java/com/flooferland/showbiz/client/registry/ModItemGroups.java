package com.flooferland.showbiz.client.registry;

import com.flooferland.showbiz.backend.registry.ModBlocks;
import com.flooferland.showbiz.backend.registry.ModBlocksWithEntities;
import com.flooferland.showbiz.backend.registry.ModItems;
import com.flooferland.showbiz.backend.registry.ModJukeboxSongs;
import com.flooferland.showbiz.backend.type.AdvancedItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModItemGroups {
    // region | Item group definitions
    public static final AdvancedItemGroup GENERAL_GROUP = new AdvancedItemGroup(
            "general",
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.PIZZA_SLICE))
    );

    public static final AdvancedItemGroup TECHNICAL_GROUP = new AdvancedItemGroup(
            "technical",
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(Items.COPPER_INGOT))
    );

    public static final AdvancedItemGroup BUILDING_GROUP = new AdvancedItemGroup(
            "building",
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(Blocks.RED_CONCRETE))
    );
    // endregion

    // Add the items to the main Creative inventory (ordered)
    public static void registerItemGroups() {
        // region  |  Group (MOD) -> General
        ItemGroupEvents.modifyEntriesEvent(GENERAL_GROUP.key)
                .register(entries -> {
                    entries.add(ModItems.PIZZA_SLICE);
                    entries.add(ModBlocks.PIZZA);
                    entries.add(ModItems.KEY);
                    entries.add(ModJukeboxSongs.Y2K.item);
                });
        // endregion

        // region  |  Group (MOD) -> Technical
        ItemGroupEvents.modifyEntriesEvent(TECHNICAL_GROUP.key)
                .register(entries -> {
                    entries.add(ModItems.MAGNETIC_TAPE);
                    entries.add(ModItems.REEL);
                    entries.add(ModBlocksWithEntities.REEL_HOLDER.item());
                    entries.add(ModBlocksWithEntities.SHOW_SELECTOR.item());
                });
        // endregion

        // region  |  Group (MOD + VANILLA) -> Building
        ItemGroupEvents.ModifyEntries buildingBlocks = entries -> {
            
        };
        ItemGroupEvents.modifyEntriesEvent(BUILDING_GROUP.key).register(buildingBlocks);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(buildingBlocks);
        // endregion
    }
}
