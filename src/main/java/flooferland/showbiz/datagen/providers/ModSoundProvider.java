package flooferland.showbiz.datagen.providers;

import flooferland.showbiz.backend.registry.ModSound;
import flooferland.showbiz.datagen.providers.base.BaseSoundProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class ModSoundProvider extends BaseSoundProvider {
    public ModSoundProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateSoundInfo(BaseSoundProvider.SoundBuilder builder) {
        // region | Mechanical
        builder.add(ModSound.PNEUMATIC_FIRE, "mechanical/pneumatic_fire");
        builder.add(ModSound.PNEUMATIC_RELEASE, "mechanical/pneumatic_release");
        builder.add(ModSound.TAPE_CLICK, "mechanical/tape_click");
        builder.add(ModSound.TAPE_PULL, "mechanical/tape_pull");
        // endregion
    }
}
