package flooferland.showbiz.backend.type.math;

import java.time.Duration;

// TODO: FIX THIS! THE MATH IS CURRENTLY BROKEN AS HELL

/**
 * The measurement for the speed of a tape (PER SECOND) <br/>
 * RAE seem to use 7.5 IPS <br/>
 * <a href="https://chatgpt.com/share/e812c190-6e61-4074-af48-a3e9c1d7c398">ChatGPT explanation of stuff</a>
 */
public class TapeSpeed {
    protected final LengthUnit speedPerSecond;
    private TapeSpeed(double speedIps) {
        this.speedPerSecond = LengthUnit.ofInches(speedIps);
    }
    
    // region | Creating from several units
    /** Creates a new TapeSpeed object from a speed (measured in Inches-Per-Second) */
    public static TapeSpeed ofInchesPerSecond(double inches) {
        return new TapeSpeed(inches);
    }
    /** Creates a new TapeSpeed object from a speed (measured in Meters-Per-Second) */
    public static TapeSpeed ofMetersPerSecond(double meters) {
        return new TapeSpeed(LengthUnit.ofMeters(meters).asInches());
    }
    // endregion
    
    // region | Fetching data in several units
    public double asInchesPerSecond() {
        return speedPerSecond.asInches();
    }
    public double asMetersPerSecond() {
        return speedPerSecond.asMeters();
    }
    // endregion

    // region | Math
    /** Adds {@code other} to this unit */
    public TapeSpeed add(TapeSpeed other) {
        return new TapeSpeed(speedPerSecond.add(other.speedPerSecond).asInches());
    }
    /** Subtracts {@code other} from this unit */
    public TapeSpeed sub(TapeSpeed other) {
        return new TapeSpeed(speedPerSecond.sub(other.speedPerSecond).asInches());
    }
    /** Multiplies this unit by {@code other} */
    public TapeSpeed mul(TapeSpeed other) {
        return new TapeSpeed(speedPerSecond.mul(other.speedPerSecond).asInches());
    }
    /** Divides this unit by {@code other} */
    public TapeSpeed div(TapeSpeed other) {
        return new TapeSpeed(speedPerSecond.div(other.speedPerSecond).asInches());
    }

    /** Gets the tape length from a duration */
    public LengthUnit calculateLength(Duration duration) {
        long seconds = duration.getSeconds();
        double nanoseconds = duration.getNano() / 1_000_000_000.0;
        double totalSeconds = seconds + nanoseconds;
        return speedPerSecond.mul(totalSeconds);
    }
    // endregion

    @Override
    public String toString() {
        return speedPerSecond + " ips";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TapeSpeed tapeSpeed) {
            return this.speedPerSecond == tapeSpeed.speedPerSecond;
        }
        return false;
    }
}
