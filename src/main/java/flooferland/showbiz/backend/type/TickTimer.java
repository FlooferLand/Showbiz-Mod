package flooferland.showbiz.backend.type;

import flooferland.showbiz.backend.util.ShowbizUtil;
import net.minecraft.world.World;

public final class TickTimer {
    public final long time;
    private int ticksThisSecond;
    private long tickCount;
    private long startTick;
    
    public TickTimer(long timeInTicks) {
        this.time = timeInTicks;
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
        return this.tickCount >= this.startTick + this.time;
    }
}
