package dungeonforge.events;

import dungeonforge.core.Player;

public class DamageMeter implements GameEventListener {
    private final EventBus bus;
    private final Player player;
    private final double thresholdFraction;
    private boolean haveWarned;

    public DamageMeter(EventBus bus, Player player, double threshold) {
        this.bus = bus;
        this.player = player;
        this.thresholdFraction = threshold;
        this.haveWarned = false;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (haveWarned) return;
        if (event.getType() != EventType.DAMAGE_TAKEN) return;

        int threshold = (int) Math.round(
                thresholdFraction * player.getMaxHp());

        if (player.getHp() <= threshold) {
            haveWarned = true;
            bus.message(String.format(
                    "~~~ DANGER %s ROBINSON: You're dangerously close to death!!!\n"
                            + "      HP: %d, WARN: %d ~~~",
                    player.getName(), player.getHp(), threshold));
        }
    }
}