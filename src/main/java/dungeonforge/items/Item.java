package dungeonforge.items;

import java.util.Collections;
import java.util.Iterator;

/** Common operations for individual items and containers. */
public interface Item {
    String getName();
    double getWeight();
    int getValue();

    default Iterator<Item> children() {
        return Collections.emptyIterator();
    }

    default boolean isContainer() {
        return children().hasNext();
    }

    default String describe() {
        return getName() + " ("
                + String.format("%.1f", getWeight())
                + "kg, " + getValue() + "g)";
    }
}