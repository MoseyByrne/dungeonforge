package dungeonforge.items;

/**
 * Frost enchantment decorator.
 *
 * Adds 3 damage and 45 gold in value to the wrapped weapon.
 */
public class FrostWeapon extends WeaponDecorator {

    private static final int FROST_BONUS = 3;

    public FrostWeapon(Weapon wrapped) { super(wrapped); }

    @Override protected String prefix() { return "Frost"; }

    @Override protected int valueBonus() { return 45; }

    @Override public int getDamage() { return wrapped.getDamage() + FROST_BONUS; }
}
