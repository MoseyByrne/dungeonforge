package dungeonforge.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Composite item whose weight and value include its contents. */
public class Chest implements Item {
    private static final double TARE_WEIGHT = 5.0;

    private final String name;
    private final List<Item> contents = new ArrayList<>();

    public Chest(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Item> getContents() {
        return contents;
    }

    public void add(Item item) {
        if (item == this) {
            throw new IllegalArgumentException(
                    "A chest cannot contain itself");
        }
        contents.add(item);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    @Override
    public double getWeight() {
        double total = TARE_WEIGHT;
        // A nested chest already includes its contents; ask each child once.
        for (Item item : contents) {
            total += item.getWeight();
        }
        return total;
    }

    @Override
    public int getValue() {
        int total = 0;
        for (Item item : contents) {
            total += item.getValue();
        }
        return total;
    }

    @Override
    public Iterator<Item> children() {
        return contents.iterator();
    }

    public int totalValue() {
        return getValue();
    }
}