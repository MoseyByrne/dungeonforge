package dungeonforge.states;

import dungeonforge.commands.GameContext;

import java.util.List;

public interface GameState {

    default void onEnter(GameContext ctx) {}

    default void onExit(GameContext ctx) {}

    default boolean isTerminal() { return false; }

    List<String> availableCommands();

    String name();
}
