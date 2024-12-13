package flooferland.showbiz.backend.type;

import flooferland.showbiz.ShowbizMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AdvancedItemGroup {
    public ItemGroup itemGroup;
    public RegistryKey<ItemGroup> key;
    public AdvancedItemGroup(String id, ItemGroup.Builder group) {
        Identifier identifier = Identifier.of(ShowbizMod.MOD_ID, id);
        itemGroup = Registry.register(
                Registries.ITEM_GROUP,
                identifier,
                group.displayName(Text.translatable("itemGroup." + ShowbizMod.MOD_ID + "." + id))
                        .build()
        );
        key = RegistryKey.of(RegistryKeys.ITEM_GROUP, identifier);
    }
}
