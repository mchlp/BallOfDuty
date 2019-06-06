package game.util;

public class TimeSync {
    private long ns_per_tick;
    private long lastTick;

    public TimeSync(long ns_per_tick) {
        this.ns_per_tick = ns_per_tick;
    }

    public long getNsPerTick() {
        return ns_per_tick;
    }

    public void setNsPerTick(long ns_per_tick) {
        this.ns_per_tick = ns_per_tick;
    }

    public void sync() {
        long current = System.nanoTime();
        if (current - lastTick < ns_per_tick * 19 / 20) {
            long delay = ns_per_tick + lastTick - current;
            try {
                Thread.sleep(delay / 1000000, (int) (delay % 1000000));
            } catch (InterruptedException ignored) {
            }
        } else {
            lastTick = System.nanoTime();
        }
    }
}
