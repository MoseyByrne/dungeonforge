# Week 7 Evidence

## 1. BEFORE

```bash
mvn -q exec:java
```

Play a few turns, then answer:

**Paste five consecutive `else if` branches from `gameLoop()`:**

```bash
if (verb.isEmpty()) {
            } else if (verb.equals("north") || verb.equals("south") || verb.equals("down")) {
            } else if (verb.equals("attack")) {
            } else if (verb.equals("look")) {
            } else if (verb.equals("take"))
``` 

**You want to add `equip`. List every edit you would make:**

```bash
} else if (verb.equals("equip"))
```

**You want `e` to mean `equip`. List every *additional* edit:**

```bash
} else if (verb.equals("e"))
```

**You want undo. Where in `gameLoop()` would the code go?** *(There is no good answer. Say
why in one sentence.)*

There is no good answer because of how tightly coupled the if/else chain is.


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
|---|---|---|---|---|
| Before attack | | | | |
| After attack | | | | |
| After snapshot undo | | | | |

## 4. AFTER — US-5.3 and US-5.4

**Paste a `loot` that picks up three things, then an `undo` that puts them all back:**

```

```

**Paste the hourglass rewinding a turn, with `inventory` before and after:**

```

```

**How long did US-5.4 actually take you?** ____ minutes

## 5. The replay

**Paste your `history` output and the first ten lines of replaying it with `--script=`:**

```

```

## 6. Tests and CI

**`mvn test` summary:**

```

```

**Green CI URL:**

## 7. Sprint review — one sentence

> What can the project do now that it could not do last week?

