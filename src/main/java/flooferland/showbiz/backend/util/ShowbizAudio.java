package flooferland.showbiz.backend.util;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

import java.io.*;

public final class ShowbizAudio {
    public static void playDoorSound(World world, BlockPos position, boolean open, float size) {
        size = MathHelper.clamp(size, 0f, 1f);
        SoundEvent sound = open
                ? SoundEvents.BLOCK_WOODEN_DOOR_OPEN
                : SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
        world.playSound(null, position, sound, SoundCategory.BLOCKS, 0.1f + (size*0.9f), 1f - size);;
    }
    
    /**
     * Plays a sound over and over again. Should be called in `tickUntilEnd`.
     * @param soundLength The length of the sound event in seconds
     */
    public static void playLoopingSound(World world, BlockPos position, SoundEvent sound, float soundLength) {
        int lengthInTicks = ShowbizUtil.secondsToTicks(soundLength);
        
        // TODO: WHY WAS THIS NEVER FINISHED. WTF WAS IT SUPPOSED TO DO??
    }

    /** Gets the length of an ogg file.
     *  Formula stolen from <a href="https://stackoverflow.com/questions/3009908/how-do-i-get-a-sound-files-total-time-in-java">Stack Overflow</a>
     * */
    public static float getOggAudioLength(String path) {
        double length;
        try {
            File file = new File(path);
            MultimediaObject media = new MultimediaObject(file);
            length = media.getInfo().getDuration();
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        
        if (length <= 1.0d)
            throw new RuntimeException(String.format("Could not get the duration of the audio file at path \"%s\".", path));
        
        return (float) (length / 1000d);
    }
}
