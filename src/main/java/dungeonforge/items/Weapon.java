package dungeonforge.items;

public interface Weapon extends Item {

    int getDamage();

    default int lifesteal() {
        return 0;
    }
}