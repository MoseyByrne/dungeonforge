# Week 7 Evidence

## 1. BEFORE

```bash
mvn -q exec:java
```

Play a few turns, then answer:

**Paste five consecutive `else if` branches from `gameLoop()`:**

```bash
} else if (verb.equals("north") || verb.equals("south") || verb.equals("down")) {
    if (here.hasLivingMonsters()) {
        System.out.println("  You cannot walk away with enemies still standing.");
    } else {
        Room next = here.getExit(verb);
        if (next == null) {
            System.out.println("  There is no way " + verb + ".");
        } else {
            here = next;
            Combat.restAfterRoom(player);
            System.out.println(describe(world, here));
            tookATurn = true;
        }
    }

} else if (verb.equals("attack")) {
    Monster target = here.firstLivingIn();
    if (target == null) {
        System.out.println("  Nothing here to attack.");
    } else {
        combat.playerStrikes(player, target);
        tookATurn = true;
    }

} else if (verb.equals("look")) {
    System.out.println(describe(world, here));

} else if (verb.equals("take")) {
    Item found = null;

    for (Item i : here.getFloorItems()) {
        if (i.getName().toLowerCase().contains(rest.toLowerCase())) {
            found = i;
            break;
        }
    }

    if (found != null) {
        here.getFloorItems().remove(found);
    } else if (here.getChest() != null) {
        for (Item i : here.getChest().getContents()) {
            if (i.getName().toLowerCase().contains(rest.toLowerCase())) {
                found = i;
                break;
            }
        }

        if (found != null) {
            here.getChest().getContents().remove(found);
        }
    }

    if (found == null) {
        System.out.println("  There is nothing like that here.");
    } else {
        player.addItem(found);
        System.out.println("  You take " + found.getName() + ".");
        tookATurn = true;
    }

} else if (verb.equals("use")) {
    Item item = player.findItem(rest);

    if (item == null) {
        System.out.println("  You are not carrying anything like that.");
    } else if (item instanceof Potion p) {
        player.heal(p.getHealAmount());
        player.removeItem(item);
        System.out.println(
                "  You drink " + p.getName() + ". HP " + player.getHp()
        );
        tookATurn = true;
    } else {
        System.out.println(
                "  You cannot think of a use for " + item.getName() + "."
        );
    }
``` 

**You want to add `equip`. List every edit you would make:**

```
1. Add equipped weapon and armor fields to Player.
2. Add Player methods for equipping and retrieving those items.
3. Import Weapon and Armor into Main.
4. Add an else-if branch for equip inside gameLoop().
5. Search the inventory using the command argument.
6. Reject missing or non-equippable items.
7. Equip a valid item and set tookATurn to true.
8. Add equip <item> to the help text.
9. Add tests for successful and unsuccessful equip commands.
```

**You want `e` to mean `equip`. List every *additional* edit:**

```
change } else if (verb.equals("equip")) to } else if (verb.equals("equip") || verb.equals("e")) {
```

**You want undo. Where in `gameLoop()` would the code go?** *(There is no good answer. Say
why in one sentence.)*

> There is no good place to add undo in `gameLoop()` because the original `if/else` branches perform actions immediately without storing the commands or the game state from before each action.


## 2. AFTER — US-5.1

**Paste your new `gameLoop()` in full. It should fit in a dozen lines:**

```bash
private static void gameLoop(CommandParser parser, GameContext ctx) {
    BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));

    while (ctx.isRunning() && ctx.getPlayer().isAlive()) {
        System.out.print("\n[HP " + ctx.getPlayer().getHp()
                + "/" + ctx.getPlayer().getMaxHp() + "] > ");
        System.out.flush();

        String line;
        try { line = in.readLine(); } catch (Exception e) { break; }
        if (line == null) break;
        System.out.println();

        Command command = parser.parse(line, ctx);
        command.execute();
        ctx.getHistory().push(command);
    }
}
```

**Paste the registry lines that add `drop` and its alias:**

```bash
register("drop", DropCommand::new);
```


## 2. AFTER — US-5.1

**Paste your new `gameLoop()` in full. It should fit in a dozen lines:**

```bash
private static void gameLoop(CommandParser parser, GameContext ctx) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (ctx.isRunning() && ctx.getPlayer().isAlive()) {
            System.out.print("\n[HP " + ctx.getPlayer().getHp() + "/" + ctx.getPlayer().getMaxHp() + "] > ");
            System.out.flush();

            String line;
            try { line = in.readLine(); } catch (Exception e) { break; }
            if (line == null) break;
            System.out.println();

            Command command = parser.parse(line, ctx);
            command.execute();
        }
    }
```

**Paste the registry lines that add `drop` and its alias:**

```bash
register("drop", DropCommand::new);
```

## 3. AFTER — US-5.2

**The naive-undo failure table from Part D2 step 2:**

| | HP | XP | Gold | Monster HP |
|---|----|----|------|------------|
| Before attack | 80 | 0  | 0    | 17         |
| After attack | 76 | 0  | 0    | 7          |
| After naive undo | 76 | 0  | 0    | 17         |

**The same table after `TurnSnapshot`:**

| | HP | XP | Gold | Monster HP |
|---|----|----|------|------------|
| Before attack | 80 | 0  | 0    | 17         |
| After attack | 78 | 0  | 0    | 7          |
| After snapshot undo | 80 | 0  | 0    | 17         |

## 4. AFTER — US-5.3 and US-5.4

**Paste a `loot` that picks up three things, then an `undo` that puts them all back:**
**Paste the hourglass rewinding a turn, with `inventory` before and after:**

```bash
loot
[HP 76/80] > 
  You take Bone Shortsword.
  You take Bone Shortsword.
  You take Grave Shroud.

i
[HP 76/80] > 
  You are carrying:
    - Small Healing Draught [heals 22] (0.3kg, 20g)
    - Chronomaster's hourglass [2 charges] (rewinds time)
    - Bone Shortsword [dmg 6] (2.0kg, 50g)
    - Bone Shortsword [dmg 6] (2.0kg, 50g)
    - Grave Shroud [def 3] (3.0kg, 43g)

undo
[HP 76/80] > 
  The last action was undone.
  The last action was undone.
  The last action was undone.

i
[HP 76/80] > 
  You are carrying:
    - Small Healing Draught [heals 22] (0.3kg, 20g)
    - Chronomaster's hourglass [2 charges] (rewinds time)
```

**How long did US-5.4 actually take you?** _unsure___ minutes

## 5. The replay

**Paste your `history` output and the first ten lines of replaying it with `--script=`:**

```bash
[INFO] 
[INFO] --- exec:3.2.0:java (default-cli) @ dungeonforge ---
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.6.0   seed 20260829

  type 'help' for what you can do. you are carrying an hourglass -- use hourglass rewinds a turn
  [L1R0] Level 1: Crypt
    "burial niches line the wall. Must be empty. MOST."
    Exits: north

[script] >help
  Available: [north, south, down, attack, take, loot, drop, use, look, inventory, status, quests, undo, help, quit]
 Aliases: {n=north, s=south, d=down, a=attack, kill=attack, hit=attack, get=take, t=take, drink=use, quaff=use, i=inventory, ?=help, exit=quit}

[script] >n
  You go north.
  Ghoul Ghoul looses a shot for 2

[script] >a
  You hit Ghoul for 10
  Ghoul Ghoul looses a shot for 2

[script] >kill
  You hit Ghoul for 10
  ACHIEVEMENT: First Blood--Defeat your first monster
  Ghoul dies.12

[script] >loot
  You take Bone Shortsword.
  You take Bone Shortsword.
  You take Grave Shroud.

[script] >n
  You go north.

[script] >look
  [L1R2] Level 1: Crypt
    "burial niches line the wall. Must be empty. MOST."
    Exits: south, north

[script] >quit
  You lay down your pack.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

## 6. Tests and CI

**`mvn test` summary:**

```bash
Administrator@XENB MINGW64 ~/Desktop/CIS 18 Object Oriented Programming/dungeonforge (feat/US-5.3_5.4)
$ mvn test
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.7.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ dungeonforge ---
[INFO] Copying 3 resources from src\main\resources to target\classes
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ dungeonforge ---
[INFO] Recompiling the module because of added or removed source files.
[INFO] Compiling 60 source files with javac [debug release 21] to target\classes
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory C:\Users\Administrator\Desktop\CIS 18 Object Oriented Programming\dungeonforge\src\test\resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 5 source files with javac [debug release 21] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.CommandTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.150 s -- in dungeonforge.CommandTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.045 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.018 s -- in dungeonforge.SingletonTest
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 s -- in dungeonforge.StrategyObserverTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 64, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.869 s
[INFO] Finished at: 2026-10-12T14:36:06-07:00
[INFO] ------------------------------------------------------------------------
```

**Green CI URL:**

## 7. Sprint review — one sentence

> What can the project do now that it could not do last week?
The project can now represent actions as command objects, allowing the 
> player to undo complete turns, use aliases, loot several items with one 
> command, and record commands for replay.
