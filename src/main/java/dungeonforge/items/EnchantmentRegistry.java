package dungeonforge.items;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.Locale;

public class EnchantmentRegistry {
    private static final Map<String, UnaryOperator<Weapon>> SPELLS = new LinkedHashMap<>();

    private EnchantmentRegistry() {}

    static {
        SPELLS.put("flaming", FlamingWeapon::new);
        SPELLS.put("frost", FrostWeapon::new);
        SPELLS.put("vampiric", VampiricWeapon::new);
        SPELLS.put("keen", KeenWeapon::new);
    }

    public static boolean isKnown(String enchantment) {
        return SPELLS.containsKey(normalise(enchantment));
    }

    public static List<String> known() { return new ArrayList<>(SPELLS.keySet()); }

    public static Weapon apply(String enchantment, Weapon weapon) {
        UnaryOperator<Weapon> spell = SPELLS.get(normalise(enchantment));
        if (spell == null) { throw new IllegalArgumentException("no such enchantment: " + enchantment); }
        return spell.apply(weapon);
    }

    private static String normalise(String enchantment) {
        return enchantment == null
                ? ""
                : enchantment.trim().toLowerCase(Locale.ROOT);
    }

}
