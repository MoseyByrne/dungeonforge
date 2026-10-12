package dungeonforge.commands;

import dungeonforge.core.Monster;

public class AttackCommand extends AbstractCommand {

    private int targetHpBefore;

    public AttackCommand(GameContext ctx, String args) {
        super(ctx, args);
    }

    @Override
    protected boolean doExecute() {
        Monster target = findTarget();

        if (target == null) {
            bus().message(args.isBlank()
                    ? "There is nothing here to attack."
                    : "There is nothing here called \""
                    + args + "\"."
            );
            return false;
        }

        targetHpBefore = target.getHp();
        ctx.getCombat().playerStrikes(player(), target);
        return true;
    }

    private Monster findTarget() {
        if (args.isBlank()) {
            return room().firstLivingIn();
        }

        for (Monster m : room().getMonsters()) {
            if (m.isAlive()
                    && m.getName().toLowerCase()
                    .contains(args.toLowerCase())) {
                return m;
            }
        }

        return null;
    }

    @Override
    public void undo() {
        Monster target = findTarget();

        if (target != null) {
            target.setHp(targetHpBefore);
            bus().message("The attack against "
                    + target.getName()
                    + " was undone.");
        }
    }

    @Override
    public String getDescription() {
        return ("attack " + args).trim();
    }
}