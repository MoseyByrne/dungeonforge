package dungeonforge.states;

import dungeonforge.commands.GameContext;
import dungeonforge.events.GameEvent;

import java.util.List;

public class CombatState implements GameState {

    private static final List<String> VERBS = List.of(
            "attack", "use", "look", "inventory",
            "status", "history", "undo", "help", "quit"
    );

    @Override
    public void onEnter(GameContext ctx) {
        long hostiles = ctx.getCurrentRoom().getMonsters()
                .stream()
                .filter(monster -> monster.isAlive())
                .count();

        ctx.getBus().publish(GameEvent.message(
                "Combat: " + hostiles + " hostiles. Try attack."
        ));
    }

    @Override
    public void onExit(GameContext ctx) {
        ctx.getBus().publish(GameEvent.message(
                "The room falls quiet."
        ));
    }

    @Override
    public List<String> availableCommands() {
        return VERBS;
    }

    @Override
    public String name() {
        return "combat";
    }
}