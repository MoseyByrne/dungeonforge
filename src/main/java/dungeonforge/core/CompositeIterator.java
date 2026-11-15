package dungeonforge.core;

import dungeonforge.items.Item;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** Depth-first, pre-order traversal of items and their children. */
public final class CompositeIterator implements Iterator<Item> {
    private final Deque<Iterator<Item>> stack = new ArrayDeque<>();
    private Iterator<Item> lastUsed;
    private Iterator<Item> descendedInto;

    public CompositeIterator(Iterator<Item> roots) {
        stack.push(roots);
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty() && !stack.peek().hasNext()) {
            stack.pop();
        }
        return !stack.isEmpty();
    }

    @Override
    public Item next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Iterator<Item> source = stack.peek();
        Item item = source.next();
        lastUsed = source;
        descendedInto = null;

        if (item.isContainer()) {
            descendedInto = item.children();
            stack.push(descendedInto);
        }

        return item;
    }

    @Override
    public void remove() {
        if (lastUsed == null) {
            throw new IllegalStateException(
                    "Call next() before each remove()");
        }

        // Removing a box must also stop traversal into that box.
        if (descendedInto != null && stack.peek() == descendedInto) {
            stack.pop();
        }

        lastUsed.remove();
        lastUsed = null;
        descendedInto = null;
    }
}