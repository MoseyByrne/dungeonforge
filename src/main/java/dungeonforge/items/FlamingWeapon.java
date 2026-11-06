package dungeonforge.items;

/**
 * Flaming decorator -- a weapon that burns.
 *
 * Adds 4 damage and 60 gold in value to the wrapped weapon.
 */
public class FlamingWeapon extends WeaponDecorator {

    private static final int FIRE_BONUS = 4;

    public FlamingWeapon(Weapon wrapped) { super(wrapped); }

    @Override protected String prefix() { return "Flaming"; }

    @Override protected int valueBonus() { return 60; }

    @Override
    public int getDamage() {
        return wrapped.getDamage() + FIRE_BONUS;
    }
}
