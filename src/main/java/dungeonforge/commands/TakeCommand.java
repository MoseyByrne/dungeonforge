package dungeonforge.commands;

import dungeonforge.events.GameEvent;
import dungeonforge.items.Item;

import java.util.Iterator;

/** Picks up an item using the room's contents iterator. */
public class TakeCommand extends AbstractCommand {

    public TakeCommand(GameContext ctx, String itemName) {
        super(ctx, itemName);
    }

    @Override
    protected boolean doExecute() {
        Iterator<Item> it = room().contents();

        while (it.hasNext()) {
            Item item = it.next();
            if (matches(item)) {
                it.remove();
                player().addItem(item);
                bus().publish(GameEvent.message(
                        "You take " + item.getName() + "."));
                return true;
            }
        }

        bus().message("There is nothing like that here.");
        return false;
    }

    private boolean matches(Item item) {
        return args.isBlank()
                || item.getName().toLowerCase().contains(args.toLowerCase());
    }

    @Override
    public String getDescription() {
        return ("take " + args).trim();
    }
}