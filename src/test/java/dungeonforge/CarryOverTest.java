package dungeonforge;

import dungeonforge.core.Player;
import dungeonforge.events.DamageMeter;
import dungeonforge.events.EventBus;
import dungeonforge.events.GameEvent;

import dungeonforge.events.EventType;
import dungeonforge.items.EnchantmentRegistry;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarryOverTest {

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

    @Test
    void damageMeterUsesThePlayersCurrentMaximum() {
        Player player = new Player("Tester");
        EventBus bus = new EventBus();
        List<String> warnings = new ArrayList<>();

        bus.subscribe(event -> {
            if (event.getType() == EventType.MESSAGE
                    && event.getString("text").contains("DANGER")) {
                warnings.add(event.getString("text"));
            }
        });
        bus.subscribe(new DamageMeter(bus, player, 0.25));

        int originalMax = player.getMaxHp();
        player.setHp(originalMax * 3 / 8);

        // Above the original quarter: no warning yet.
        bus.publish(GameEvent.of(EventType.DAMAGE_TAKEN));
        assertTrue(warnings.isEmpty());

        // Change the maximum after the meter has already been constructed.
        player.setMaxHp(originalMax * 2);

        // The same HP is now below a quarter of the new maximum.
        bus.publish(GameEvent.of(EventType.DAMAGE_TAKEN));
        assertEquals(1, warnings.size(),
                "The warning must use the player's current maximum HP");

        // The meter should still warn only once.
        bus.publish(GameEvent.of(EventType.DAMAGE_TAKEN));
        assertEquals(1, warnings.size());
    }

    @Test
    void raisingMaximumHpDoesNotHealThePlayer() {
        Player player = new Player("Tester");
        player.setHp(30);

        player.setMaxHp(160);

        assertEquals(160, player.getMaxHp());
        assertEquals(30, player.getHp());
    }

    @Test
    void loweringMaximumHpClampsCurrentHpAndMaximumStaysPositive() {
        Player player = new Player("Tester");
        player.setMaxHp(100);
        player.setHp(90);

        player.setMaxHp(40);
        assertEquals(40, player.getMaxHp());
        assertEquals(40, player.getHp());

        player.setMaxHp(0);
        assertEquals(1, player.getMaxHp());
        assertEquals(1, player.getHp());

        player.setHp(0);
        player.setMaxHp(-10);
        assertEquals(1, player.getMaxHp());
        assertEquals(0, player.getHp());
    }
}