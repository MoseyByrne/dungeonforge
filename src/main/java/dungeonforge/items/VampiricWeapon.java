package dungeonforge.items;

/**
 * Vampiric enchantment decorator.
 *
 * Adds lifesteal equal to one quarter of the wrapped weapon's damage,
 * rounded down with a minimum of 1, while preserving existing lifesteal.
 * Adds 90 gold in value.
 */
public class VampiricWeapon extends WeaponDecorator {

    public VampiricWeapon(Weapon wrapped) { super(wrapped); }

    @Override
    public int lifesteal() { return wrapped.lifesteal() + Math.max(1, wrapped.getDamage() / 4); }

    @Override protected String prefix() { return "Vampiric"; }
    @Override protected int valueBonus() { return 90; }
}
