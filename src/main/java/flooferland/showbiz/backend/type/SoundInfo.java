package flooferland.showbiz.backend.type;

import flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.util.Identifier;

public class SoundInfo {
    public final Identifier id;
    public final int length;
    public final int lengthSeconds;
    
    public SoundInfo(Identifier id, float lengthInSeconds) {
        this.id = id;
        this.length = ShowbizUtil.secondsToTicks(lengthInSeconds);
        this.lengthSeconds = (int) lengthInSeconds;
    }
}
