package dungeonforge.items;

/**
 * The shared interface for basic weapons and enchanted weapons.
 *
 * Callers use the same operations regardless of how many decorators
 * wrap a weapon. Peeling an undecorated weapon returns the weapon itself.
 */
public interface Weapon extends Item {

    int getDamage();

    /**
     * Hit points restored to the wielder when this weapon lands a blow.
     * Defaults to zero for weapons without lifesteal.
     */
    default int lifesteal() { return 0; }

    default Weapon peel() {
        return this;
    }
}
