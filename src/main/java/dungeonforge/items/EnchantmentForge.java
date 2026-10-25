package dungeonforge.items;

/**
 * WEEK 9 STARTER -- THE PROBLEM, STATED AS A METHOD.
 *
 * ============================ READ THIS BEFORE PART A ============================
 * Seven subclasses is bad. Having to CHOOSE between seven subclasses is worse, and this
 * class is where that cost lands.
 *
 * To enchant a weapon you must answer: "what is it already, and what does it become?" With
 * a subclass per combination, that question has no general answer -- only a table. Below is
 * that table, written out by hand, and it is already incomplete.
 *
 * Three things are wrong here, and you should be able to name all three in Part A:
 *
 *   1. It is a nested ladder of type tests. Every new enchantment multiplies its size.
 *   2. It CANNOT express some perfectly reasonable weapons. Flaming twice? There is no class
 *      for that, so the method refuses.
 *   3. It returns a NEW OBJECT. The old weapon is a different object, so anything holding a
 *      reference to it -- the equipped slot, a quest, a chest -- now points at the wrong
 *      thing. Try `equip` then `enchant` and watch your damage not change.
 *
 * Item (3) is not a bug in this method. It is a bug in the whole approach, and no amount of
 * care here will fix it. Hold that thought until Part C.
 * =================================================================================
 */
public final class EnchantmentForge {

    private EnchantmentForge() {
    }

    public static final String[] KNOWN = {"flaming", "frost", "vampiric"};

    public static boolean isKnown(String enchantment) {
        for (String k : KNOWN) if (k.equalsIgnoreCase(enchantment)) return true;
        return false;
    }

    /**
     * Returns a NEW weapon with the enchantment added, or null if no class exists for that
     * combination. Read that sentence again: the combination has to already have a class.
     */
    public static Weapon enchant(Weapon w, String enchantment) {
        return null;
    }

    private static int baseDamage(Weapon w) {
        return w.getDamage();
    }

    private static int baseValue(Weapon w) {
        return w.getValue();
    }
}
