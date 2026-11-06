package dungeonforge.items;

/**
 * The concrete component in the weapon decorator design.
 *
 * Stores a weapon's base name, weight, value, and damage.
 * Enchantment decorators wrap this object to add their effects.
 */
public class BasicWeapon implements Weapon {

    protected final String name;
    protected final double weight;
    protected final int value;
    protected final int damage;

    public BasicWeapon(String name, double weight, int value, int damage) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.damage = damage;
    }

    @Override public String getName()   { return name; }
    @Override public double getWeight() { return weight; }
    @Override public int getValue()     { return value; }

    public int getDamage() { return damage; }

    /**
     * A basic weapon restores no hit points when it strikes.
     */
    public int lifesteal() { return 0; }

    @Override
    public String describe() {
        return name + " [dmg " + getDamage() + "] (" + String.format("%.1f", weight) + "kg, " + getValue() + "g)";
    }
}
