package dungeonforge.legacy;

import dungeonforge.factory.MonsterDef;
import dungeonforge.factory.MonsterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LegacyBestiaryAdapter implements MonsterSource {

    private final LegacyBestiary adaptee;
    private final String name;
    private final List<String> notes = new ArrayList<>();

    public LegacyBestiaryAdapter(LegacyBestiary adaptee, String name) {
        this.adaptee = adaptee;
        this.name = name;
    }

    @Override
    public List<MonsterDef> definitions() {
        List<MonsterDef> out = new ArrayList<>();
        notes.clear();
        adaptee.rewind();

        while (adaptee.advance()) {
            String legacyId = adaptee.field("MOB_ID");
            if (legacyId.isEmpty()) continue;

            int hp = parseInt(adaptee.field("HIT_DICE"), 1) * 4 + 2;
            int attack = attackFrom(adaptee.field("DMG"));
            int xp = Math.max(1, hp / 2 + attack);

            notes.add(legacyId
                    + ": xp derived from hp / 2 + attack; no XP column.");

            out.add(new MonsterDef(
                    legacyId.toLowerCase(Locale.ROOT),
                    adaptee.field("DISPLAY"),
                    hp,
                    attack,
                    xp,
                    themeFrom(adaptee.field("ZONE")),
                    "BOSS".equals(adaptee.field("TIER")),
                    strategyFrom(adaptee.field("AI"), legacyId)
            ));
        }

        return out;
    }

    @Override
    public String sourceName() {
        return name;
    }

    public List<String> translationNotes() {
        return List.copyOf(notes);
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private int attackFrom(String damage) {
        String value = damage.trim().toLowerCase(Locale.ROOT);
        int d = value.indexOf('d');

        if (d < 0) {
            return Math.max(1, parseInt(value, 3));
        }

        int plus = value.indexOf('+', d);
        int count = parseInt(value.substring(0, d), 1);
        int sides = parseInt(
                plus < 0
                        ? value.substring(d + 1)
                        : value.substring(d + 1, plus),
                6
        );
        int bonus = plus < 0 ? 0 : parseInt(value.substring(plus + 1), 0);

        return Math.max(1, count * ((sides + 1) / 2) + bonus);
    }

    private String themeFrom(String zone) {
        return switch (zone) {
            case "1" -> "crypt";
            case "2" -> "forge";
            case "3" -> "frost";
            case "4" -> "winterfell";
            default -> throw new IllegalArgumentException(
                    "Unknown legacy zone: " + zone);
        };
    }

    private String strategyFrom(String ai, String legacyId) {
        return switch (ai) {
            case "BRAWLER" -> "aggressive";
            case "ARCHER" -> "ranged";
            case "COWARD" -> "skittish";
            case "MEDIC" -> "healer";
            default -> {
                notes.add(legacyId + ": AI " + ai
                        + " has no counterpart; using aggressive.");
                yield "aggressive";
            }
        };
    }
}