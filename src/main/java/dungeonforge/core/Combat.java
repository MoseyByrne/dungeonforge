package dungeonforge.core;

import dungeonforge.behavior.Action;
import dungeonforge.behavior.CombatStrategy;
import dungeonforge.config.GameConfig;
import dungeonforge.events.EventBus;
import dungeonforge.events.EventType;
import dungeonforge.events.GameEvent;
import dungeonforge.states.DeadState;

import java.util.ArrayList;
import java.util.List;

public class Combat {

    private final EventBus bus;

    public Combat(EventBus bus) { this.bus = bus; }

    /** Monsters act in their current state, then transition. */
    public void monsterTurns(Player player, Room room) {
        for (Monster m : livingMonsters(room)) {
            monsterActs(m, player, room);

            String beforeState = m.getState().label();
            CombatStrategy beforeStrategy = m.getStrategy();
            m.setState(m.getState().next(m, player));

            if (m.getStrategy() != beforeStrategy) {
                bus.publish(GameEvent.of(
                        EventType.STRATEGY_CHANGED,
                        "name", m.getName(),
                        "from", beforeStrategy == null
                                ? "none" : beforeStrategy.name(),
                        "to", m.getStrategy() == null
                                ? "none" : m.getStrategy().name()));
            }

            if (!beforeState.equals(m.getState().label())) {
                bus.publish(GameEvent.of(EventType.MONSTER_STATE_CHANGED,
                        "name", m.getName(),
                        "from", beforeState,
                        "to", m.getState().label()));
            }

            if (!player.isAlive()) break;
        }

        if (!player.isAlive()) {
            bus.publish(GameEvent.of(EventType.PLAYER_DIED));
        } else if (!hasLiving(room)) {
            bus.publish(GameEvent.of(
                    EventType.ROOM_CLEARED,
                    "room", room.getId()));
        }
    }

    /** TODO(week 8): PLACE 2 OF 3 -- "may this monster act?", as a conditional. */
    private void monsterActs(Monster m, Player player, Room room) {
        if (!m.getState().canAct()) return;
        if (m.getStrategy() == null) return;

        Action action = m.getStrategy().chooseAction(m, player, room);
        if (action == null) return;

        switch (action.getType()) {
            case ATTACK -> {
                int dmg = damageFrom(m);
                player.takeDamage(dmg);
                bus.publish(GameEvent.of(EventType.DAMAGE_TAKEN,
                        "source", m.getName(), "amount", dmg, "flavor", action.getFlavor()));
            }
            case RANGED_ATTACK -> {
                int dmg = Math.max(1, (int) Math.round(damageFrom(m) * 0.8));
                player.takeDamage(dmg);
                bus.publish(GameEvent.of(EventType.DAMAGE_TAKEN,
                        "source", m.getName(), "amount", dmg, "flavor", action.getFlavor()));
            }
            case FLEE -> {
                room.getMonsters().remove(m);
                bus.publish(GameEvent.of(EventType.MONSTER_FLED, "name", m.getName()));
            }
            case HEAL_ALLY -> {
                if (action.getTarget() != null) {
                    action.getTarget().heal(5);
                    bus.publish(GameEvent.of(EventType.MONSTER_HEALED,
                            "healer", m.getName(), "target", action.getTarget().getName()));
                }
            }
            case WAIT -> bus.publish(GameEvent.message(action.getFlavor()));
        }
    }

    /** The current state supplies the damage multiplier. */
    private int damageFrom(Monster m) {
        return Math.max(1, (int) Math.round(
                m.getAttackPower() * m.getState().damageMultiplier()));
    }

    public void playerStrikes(Player player, Monster target) {
        int damage = player.getAttackPower();
        target.takeDamage(damage);
        bus.publish(GameEvent.of(EventType.DAMAGE_DEALT,
                "target", target.getName(), "amount", damage));

        if (!target.isAlive()) {
            target.setState(new DeadState());
            player.addXp(target.getXpReward());
            player.addGold(target.getXpReward() * 2);
            bus.publish(GameEvent.of(EventType.MONSTER_DIED,
                    "name", target.getName(), "xp", target.getXpReward()));
            bus.publish(GameEvent.of(EventType.XP_GAINED, "amount", target.getXpReward()));
            bus.publish(GameEvent.of(EventType.GOLD_GAINED, "amount", target.getXpReward() * 2));
        }
    }

    public Monster firstLivingIn(Room room) { return firstLiving(room); }

    private boolean hasLiving(Room room) { return firstLiving(room) != null; }

    private Monster firstLiving(Room room) {
        for (Monster m : room.getMonsters()) if (m.isAlive()) return m;
        return null;
    }

    private List<Monster> livingMonsters(Room room) {
        List<Monster> out = new ArrayList<>();
        for (Monster m : room.getMonsters()) if (m.isAlive()) out.add(m);
        return out;
    }

    /** Between rooms the player catches their breath. The dead do not catch their breath. */
    public static void restAfterRoom(Player player) {
        if (!player.isAlive()) return;
        player.heal(GameConfig.getInstance().getInt("restHealPerRoom"));
    }
}
