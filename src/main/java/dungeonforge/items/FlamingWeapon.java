package dungeonforge.items;

public class FlamingWeapon extends WeaponDecorator {

    private static final int FIRE_BONUS = 4;

    public FlamingWeapon(Weapon wrapped) {
        super(wrapped);
    }

    @Override
    public int getDamage() {
        return wrapped.getDamage() + FIRE_BONUS;
    }

    @Override
    protected String prefix() {
        return "Flaming";
    }

    @Override
    protected int valueBonus() {
        return 60;
    }
}