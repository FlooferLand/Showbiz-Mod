package com.flooferland.showbiz.backend.type;

import com.flooferland.showbiz.backend.registry.ModSounds;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

// TODO: Clean up and refactor this code

/**
 * Manages looping sounds for block entities. <br/>
 * Primarily made for block entities and things that have access to their tick methods.
 */
public final class AmbientSound {
    public final ModSounds sound;
    @Nullable public SoundEvent endSound;
    private boolean canPlay = true;
    private boolean lastCanPlay = false;
    private boolean hasPlayedEndSound = false;
    private TickTimer timer;
    
    // region | Construction
    public static AmbientSound of(ModSounds sound) {
        return new AmbientSound(sound, null);
    }
    public static AmbientSound of(ModSounds sound, SoundEvent endSound) {
        return new AmbientSound(sound, endSound);
    }
    private AmbientSound(ModSounds sound, @Nullable SoundEvent endSound) {
        this.sound = sound;
        this.endSound = endSound;
        this.timer = new TickTimer(sound.lengthInTicks());
    }
    // endregion

    // region | Tick
    /** Tries to play the sound is <code>shouldPlay</code> is true
     *  returns <code>true</code> when the audio loops/stops*/
    public boolean tick(World world, BlockPos pos, SoundCategory category, float volume, float pitch) {
        // When started
        if (!lastCanPlay && canPlay) {
            hasPlayedEndSound = false;
            timer = new TickTimer(sound.lengthInTicks());
            timer.begin();
        } else {
            return false;
        }
        
        boolean hasTimerEnded = timer.tickUntilEnd(world);
        
        // When stopped
        if (endSound != null && hasTimerEnded && !hasPlayedEndSound) {
            world.playSoundAtBlockCenterClient(pos, endSound, category, volume * 0.3f, pitch, true);
            hasPlayedEndSound = true;
            timer.forceStop();
            return true;
        }
        
        // Loop the sound
        if ((hasTimerEnded && canPlay) || (!lastCanPlay && canPlay)) {
            world.playSoundAtBlockCenterClient(pos, sound.event, category, volume, pitch, true);
            timer.reset();
            return true;
        }
        
        return false;
    }
    
    /** Tries to play the sound is <code>shouldPlay</code> is true */
    public boolean tick(World world, BlockPos pos, float volume, float pitch) {
        return tick(world, pos, SoundCategory.AMBIENT, volume, pitch);
    }
    // endregion
    
    public void setPlayCondition(boolean condition) {
        lastCanPlay = canPlay;
        canPlay = condition;
    }
    
    /** Uses world time to determine whenever it should play the sound again. <br/>
     *  Returns true if the sound should be played again in order to seamlessly loop the sound. */
    public boolean canPlay(World world) {
        return (world.getTime() % (long) sound.lengthInTicks() == 0L);
    }
}
