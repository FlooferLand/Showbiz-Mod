package flooferland.showbiz.backend.type;

import flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.world.World;

/**
 * Resettable timer that counts every tick and finishes when it reaches the target time.
 * Handy for other types, as well as for the mod in general.
 * Call <c>isFinished</c> to check when it finishes.
 */
public final class TickTimer {
    public final long targetTime;
    private int ticksThisSecond;
    private long tickCount;
    private long startTick;
    
    public TickTimer(long timeInTicks) {
        this.targetTime = timeInTicks;
        begin();
    }

    public void begin() {
        forceStop();
        this.startTick = tickCount;
    }
    public void forceStop() {}
    public void reset() {
        begin();
    }
    
    /** Returns true when the timer ends */
    public boolean tickUntilEnd(World world) {
        ++this.ticksThisSecond;
        if (isFinished()) {
            forceStop();
            return true;
        } else if (this.ticksThisSecond > ShowbizUtil.secondsToTicks(1)) {
            this.ticksThisSecond = 0;
        }
        ++this.tickCount;
        return false;
    }

    public boolean isFinished() {
        return this.tickCount >= this.startTick + this.targetTime;
    }
}
