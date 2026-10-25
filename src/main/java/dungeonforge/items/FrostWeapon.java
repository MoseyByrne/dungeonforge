package dungeonforge.items;

public class FrostWeapon extends WeaponDecorator {

    private static final int FROST_BONUS = 3;

    public FrostWeapon(Weapon wrapped) {
        super(wrapped);
    }

    @Override
    public int getDamage() {
        return wrapped.getDamage() + FROST_BONUS;
    }

    @Override
    protected String prefix() {
        return "Frost";
    }

    @Override
    protected int valueBonus() {
        return 45;
    }
}