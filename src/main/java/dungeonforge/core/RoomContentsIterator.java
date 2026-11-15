package dungeonforge.core;

import dungeonforge.items.Item;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/** Walks floor items first, then chest contents. */
public final class RoomContentsIterator implements Iterator<Item> {
    private final Iterator<Item> floor;
    private final Iterator<Item> chest;
    private Iterator<Item> lastReturnedBy;

    public RoomContentsIterator(
            List<Item> floorItems, List<Item> chestContents) {
        floor = floorItems.iterator();
        chest = chestContents == null
                ? Collections.emptyIterator()
                : chestContents.iterator();
    }

    @Override
    public boolean hasNext() {
        return floor.hasNext() || chest.hasNext();
    }

    @Override
    public Item next() {
        Iterator<Item> source;
        if (floor.hasNext()) {
            source = floor;
        } else if (chest.hasNext()) {
            source = chest;
        } else {
            throw new NoSuchElementException();
        }

        Item item = source.next();
        lastReturnedBy = source;
        return item;
    }

    @Override
    public void remove() {
        if (lastReturnedBy == null) {
            throw new IllegalStateException(
                    "Call next() before each remove()");
        }

        lastReturnedBy.remove();
        lastReturnedBy = null;
    }
}