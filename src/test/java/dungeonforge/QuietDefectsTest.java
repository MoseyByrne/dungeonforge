package dungeonforge;

import dungeonforge.events.EventType;
import dungeonforge.items.EnchantmentRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuietDefectsTest {

    @Test
    void uppercaseEnchantmentIsRecognised() {
        assertTrue(EnchantmentRegistry.isKnown("FLAMING"));
    }

    @Test
    void correctlySpelledMonsterStateEventExists() {
        assertDoesNotThrow(
                () -> EventType.valueOf("MONSTER_STATE_CHANGED"));
    }

    @Test
    void mixedCaseAndSpacesAreRecognisedAndApplied() {
        String[] inputs = {"FLAMING", "  frost  ", "Vampiric"};
        String[] names = {
                "Flaming Iron Sword",
                "Frost Iron Sword",
                "Vampiric Iron Sword"
        };

        for (int i = 0; i < inputs.length; i++) {
            assertTrue(EnchantmentRegistry.isKnown(inputs[i]));

            var sword = new dungeonforge.items.BasicWeapon(
                    "Iron Sword", 2.0, 20, 8);
            var enchanted = EnchantmentRegistry.apply(inputs[i], sword);

            assertEquals(names[i], enchanted.getName());
        }
    }

    @Test
    void unknownEnchantmentIsStillRefused() {
        assertFalse(EnchantmentRegistry.isKnown("  UNKNOWN  "));

        var sword = new dungeonforge.items.BasicWeapon(
                "Iron Sword", 2.0, 20, 8);

        assertThrows(IllegalArgumentException.class,
                () -> EnchantmentRegistry.apply("  UNKNOWN  ", sword));
    }

    @Test
    void noEventNameContainsTheMisspelling() {
        for (EventType type : EventType.values()) {
            assertFalse(type.name().contains("SATE"), type.name());
        }
    }
}