package dungeonforge.commands;

import dungeonforge.core.Player;
import dungeonforge.core.Room;
import dungeonforge.events.EventBus;

public abstract class AbstractCommand implements Command {

    protected GameContext ctx;
    protected final String args;

    private boolean actuallyRan;
    private TurnSnapshot snapshot;

    public AbstractCommand(GameContext ctx, String args) {
        this.ctx = ctx;
        this.args = args == null ? "" : args.trim();
    }

    public Player player() {
        return ctx.getPlayer();
    }

    public EventBus bus() {
        return ctx.getBus();
    }

    public Room room() {
        return ctx.getCurrentRoom();
    }

    @Override
    public final void execute() {
        if (endsTurn()) {
            snapshot = TurnSnapshot.capture(player(), room());
        }

        actuallyRan = doExecute();

        if (!actuallyRan) {
            snapshot = null;
            return;
        }

        if (endsTurn()) {
            ctx.runMonsterTurns();
        }
    }

    protected abstract boolean doExecute();

    @Override
    public void undo() {
        if (snapshot == null) {
            return;
        }

        snapshot.restore();
        ctx.setCurrentRoom(snapshot.getRoom());
        bus().message("The last action was undone.");
    }

    @Override
    public boolean isUndoable() {
        return actuallyRan;
    }

    @Override
    public String getDescription() {
        return "";
    }
}