package flooferland.showbiz.client;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("CommentedOutCode")
@Environment(EnvType.CLIENT)
public class ShowbizModMenu implements ModMenuApi {
    
    // TODO: Add in a config screen; This used cloth config, and I dislike cloth config.
    /*@Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        Identifier background = Identifier.of(ShowbizMod.MOD_ID, "textures/gui/tile.png");

        return (ConfigScreenFactory<Screen>) screen -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(screen)
                    .setTitle(Text.translatable("title.showbiz.full"))
                    .setDefaultBackgroundTexture(background)
                    .setTransparentBackground(true)
                    .setAlwaysShowTabs(true);

            var meowEntry1 = builder.entryBuilder()
                    .startBooleanToggle(Text.of("Evil"), false)
                    .setYesNoTextSupplier((a) -> Text.of(a ? "Maybe" : "Perhaps"))
                    .build();
            var meowEntry2 = builder.entryBuilder()
                    .startIntSlider(Text.of("Test"), 0, 0, 8)
                    .setTooltip(Text.of("Interesting test here.."))
                    .build();

            // Categories
            builder.getOrCreateCategory(Text.of("Meow"))
                    .addEntry(meowEntry1)
                    .addEntry(meowEntry2)
                    .setCategoryBackground(background);

            builder.setSavingRunnable(() -> {
                // TODO: Save config to disk
            });
            return builder.build();
        };

        // return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }*/
}
