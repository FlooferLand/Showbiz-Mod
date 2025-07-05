package com.flooferland.showbiz.datagen.providers;

import com.flooferland.showbiz.backend.registry.ModJukeboxSongs;
import com.flooferland.showbiz.backend.registry.ModSounds;
import com.flooferland.showbiz.datagen.providers.base.BaseSoundProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class ModSoundProvider extends BaseSoundProvider {
    public ModSoundProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateSoundInfo(BaseSoundProvider.SoundBuilder builder) {
        builder.addSong(ModJukeboxSongs.Y2K);

        // region | Mechanical
        builder.add(ModSounds.PNEUMATIC_FIRE, "mechanical/pneumatic_fire");
        builder.add(ModSounds.PNEUMATIC_RELEASE, "mechanical/pneumatic_release");
        builder.add(ModSounds.TAPE_CLICK, "mechanical/tape_click");
        builder.add(ModSounds.TAPE_PULL, "mechanical/tape_pull");
        // endregion
    }
}
