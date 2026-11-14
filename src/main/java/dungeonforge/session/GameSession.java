package dungeonforge.session;

import dungeonforge.commands.Command;
import dungeonforge.commands.CommandHistory;
import dungeonforge.commands.CommandParser;
import dungeonforge.commands.GameContext;
import dungeonforge.core.Combat;
import dungeonforge.core.GameWorld;
import dungeonforge.core.Player;
import dungeonforge.events.*;
import dungeonforge.factory.MonsterFactory;
import dungeonforge.factory.MonsterSource;
import dungeonforge.items.BasicWeapon;
import dungeonforge.items.Hourglass;
import dungeonforge.items.Potion;
import dungeonforge.legacy.LegacyBestiary;
import dungeonforge.legacy.LegacyBestiaryAdapter;

import java.util.ArrayList;
import java.util.List;

public final class GameSession {

    private final Player player;
    private final GameWorld world;
    private final EventBus bus;
    private final CommandParser parser;
    private final CommandHistory history;
    private final GameContext ctx;
    private final TranscriptRecorder recorder;

    private GameSession(Player player, GameWorld world, EventBus bus,
                        CommandParser parser, CommandHistory history,
                        GameContext ctx, TranscriptRecorder recorder) {
        this.player = player;
        this.world = world;
        this.bus = bus;
        this.parser = parser;
        this.history = history;
        this.ctx = ctx;
        this.recorder = recorder;
    }

    public static GameSession start(String name, GameEventListener... views) {
        GameWorld world = buildWorldOnly(name);
        Player player = world.getPlayer();
        EventBus bus = new EventBus();

        QuestTracker quests = new QuestTracker(bus);
        AchievementSystem achievements = new AchievementSystem(bus);
        CombatLog log = new CombatLog(200);
        DamageMeter meter = new DamageMeter(bus, player, 0.25);
        TranscriptRecorder recorder = new TranscriptRecorder();

        bus.subscribe(quests);
        bus.subscribe(achievements);
        bus.subscribe(log);
        bus.subscribe(meter);
        bus.subscribe(recorder);

        for (GameEventListener view : views) {
            bus.subscribe(view);
        }

        CommandHistory history = new CommandHistory();
        GameContext ctx = new GameContext(
                world, player, bus, new Combat(bus), history);
        CommandParser parser = new CommandParser(quests, achievements);

        GameSession session = new GameSession(
                player, world, bus, parser, history, ctx, recorder);

        bus.message("Type 'help' for what you can do. You are carrying an "
                + "hourglass -- 'use hourglass' rewinds a turn.");
        session.runLine("look");
        return session;
    }

    public static GameWorld buildWorldOnly(String name) {
        Player player = new Player(name);
        player.addItem(new Potion("Small Healing Draught", 0.3, 20, 22));
        player.addItem(new Hourglass(2));
        player.addItem(new BasicWeapon("Notched Shortsword", 2.5, 25, 5));

        MonsterFactory factory = new MonsterFactory();
        MonsterSource source = new LegacyBestiaryAdapter(
                new LegacyBestiary("bestiary.csv"), "0.4 export");
        source.definitions().forEach(factory::register);

        // GameWorld generates immediately, so all blueprints must be registered first.
        return new GameWorld(player, factory);
    }

    public void runLine(String line) {
        Command command = parser.parse(line, ctx);
        command.execute();
        history.push(command);
        ctx.syncState();
    }

    public Player getPlayer() {
        return player;
    }

    public GameWorld getWorld() {
        return world;
    }

    public EventBus getBus() {
        return bus;
    }

    public GameContext getContext() {
        return ctx;
    }

    public CommandHistory getHistory() {
        return history;
    }

    public boolean isRunning() {
        return ctx.isRunning();
    }

    public int listenerCount() {
        return bus.listenerCount();
    }

    public List<String> transcript() {
        return List.copyOf(recorder.lines);
    }

    public String prompt() {
        return "\n[" + ctx.getState().name()
                + " | HP " + player.getHp() + "/" + player.getMaxHp() + "] > ";
    }

    public String epilogue() {
        return "\n  " + player.describe()
                + (player.isAlive() ? "" : "\n  You die in the dark.")
                + "\n  Actions this session: " + history.replayLog().size()
                + "   |   undo stack depth: " + history.depth();
    }

    private static final class TranscriptRecorder implements GameEventListener {
        private final CombatLog formatter = new CombatLog(1);
        private final List<String> lines = new ArrayList<>();

        @Override
        public void onEvent(GameEvent event) {
            if (event.getType() == EventType.ROOM_CLEARED) return;
            formatter.onEvent(event);
            lines.addAll(formatter.getLines());
            formatter.clear();
        }
    }
}