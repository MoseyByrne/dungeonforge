package dungeonforge.commands;

import dungeonforge.items.EnchantmentRegistry;
import dungeonforge.items.Item;
import dungeonforge.items.Weapon;

import java.util.Locale;

/**
 * Applies an enchantment and updates the equipped weapon when necessary.
 */
public class EnchantCommand extends AbstractCommand {

    private Weapon before;
    private Weapon after;
    private boolean wasEquipped;

    public EnchantCommand(GameContext ctx, String args) {
        super(ctx, args);
    }

    @Override
    protected boolean doExecute() {
        // Split on the LAST " with ".
        int at = args.toLowerCase(Locale.ROOT).lastIndexOf(" with ");
        if (at < 0) {
            bus().message("Try: enchant <weapon> with <"
                    + String.join("|", EnchantmentRegistry.known()) + ">");
            return false;
        }

        String target = args.substring(0, at).trim();
        String enchantment = args.substring(at + 6)
                .trim().toLowerCase(Locale.ROOT);

        if (!EnchantmentRegistry.isKnown(enchantment)) {
            bus().message("No such enchantment: " + enchantment
                    + ". Known enchantments: "
                    + String.join(", ", EnchantmentRegistry.known()));
            return false;
        }

        Item item = player().findItem(target);
        if (item == null) {
            bus().message("You are not carrying anything like that.");
            return false;
        }

        if (!(item instanceof Weapon w)) {
            bus().message("Only weapons take enchantments.");
            return false;
        }

        before = w;
        after = EnchantmentRegistry.apply(enchantment, before);
        wasEquipped = player().getEquippedWeapon() == before;

        player().removeItem(before);
        player().addItem(after);

        if (wasEquipped) {
            player().equip(after);
        }

        bus().message("The " + before.getName()
                + " is now " + after.describe());

        return true;
    }

    @Override
    public String getDescription() {
        return ("enchant " + args).trim();
    }
}