package dungeonforge.factory;

import dungeonforge.config.RandomSource;
import dungeonforge.core.Monster;
import dungeonforge.items.*;

public class WinterfellThemeKit implements ThemeKit {

    private final MonsterFactory factory;

    private static final String[] FLAVORS = {
            "The cold is biting at your nose, Direwolves glare from the forest edge",
            "in the distance you see smoke rising beyond the wall",
            "the blue eyes of the white walker peers into your soul",
            "a direwolf chews on a bloody bone"
    };

    public WinterfellThemeKit(MonsterFactory factory) {
        this.factory = factory;
    }

    @Override
    public String themeName() {
        return "Winterfell";
    }

    @Override
    public Monster createMonster(int depth) {
        String id = RandomSource.getInstance().pick(factory.idsForTheme("winterfell"));
        return factory.create(id, depth);
    }

    @Override
    public Monster createBoss(int depth) {
        return factory.create("night_king", depth);
    }

    @Override
    public Item createLoot(int depth) {

        switch(RandomSource.getInstance().nextInt(4)) {
            case 0:
                return new Weapon("Valarian Steel Greatsword", 2.0, 40 + depth * 10, 5 + depth);
            case 1:
                return new Armor("Direwolf pup", 3.0, 35 + depth * 8, 2 + depth);
            case 2:
                return new Potion("Ice Magic", 0.4, 15, 18 + depth * 3);
            default:
                return new Treasure("Dragon Eggs", 0.2, 60 + depth * 15);
        }
    }

    @Override
    public String createRoomFlavor() {
        return RandomSource.getInstance().pick(FLAVORS);
    }
}
