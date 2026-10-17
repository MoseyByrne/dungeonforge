package dungeonforge.states;

import dungeonforge.commands.GameContext;

import java.util.List;

public class GameOverState implements GameState {

    private static final List<String> VERBS = List.of(
            "quests", "history", "status", "quit"
    );

    @Override
    public void onEnter(GameContext ctx) {
        ctx.getBus().message(ctx.getPlayer().isAlive()
                ? "You leave the dungeon behind. Type 'quests' for your record, or 'quit'."
                : "You died. Type 'quests' to see how far you got, or 'quit'.");
    }

    @Override
    public List<String> availableCommands() {
        return VERBS;
    }

    @Override
    public String name() {
        return "game over";
    }
}