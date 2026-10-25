package dungeonforge.items;

import java.util.Objects;

public abstract class WeaponDecorator implements Weapon {

    protected final Weapon wrapped;

    protected WeaponDecorator(Weapon wrapped) {
        this.wrapped = Objects.requireNonNull(
                wrapped, "A decorator must wrap a weapon."
        );
    }

    protected abstract String prefix();

    protected abstract int valueBonus();

    @Override
    public String getName() {
        return prefix() + " " + wrapped.getName();
    }

    @Override
    public double getWeight() {
        return wrapped.getWeight();
    }

    @Override
    public int getValue() {
        return wrapped.getValue() + valueBonus();
    }

    @Override
    public int getDamage() {
        return wrapped.getDamage();
    }

    @Override
    public int lifesteal() {
        return wrapped.lifesteal();
    }

    @Override
    public String describe() {
        return getName() + " [dmg " + getDamage() + "]"
                + " (" + String.format("%.1f", getWeight())
                + "kg, " + getValue() + "g)";
    }
}
