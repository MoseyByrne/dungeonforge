# Week 3 Evidence — the before-and-after

> Your Definition of Done asks for evidence that the acceptance criteria are met. This file
> is where it goes. Fill it in as you work, not at the end.

## 1. BEFORE — the problem, demonstrated

Do this **before writing any code**:

```bash
mvn -q exec:java > run1.txt
mvn -q exec:java > run2.txt
diff run1.txt run2.txt
```

**Paste a few lines of the diff:**

```
$ diff run1.txt run2.txt
10,14c10,14
< L1R0: Skeleton (16/16 HP, ATK 5)  Bone Priest (16/16 HP, ATK 4)
< L1R1: Wight (17/17 HP, ATK 4)  Crypt Rat (16/16 HP, ATK 5)
< L1R2: Skeleton (15/15 HP, ATK 6)  Wight (17/17 HP, ATK 5)
< L1R3: Wight (14/14 HP, ATK 6)  Skeleton (17/17 HP, ATK 6)
< L1R4: Crypt Rat (14/14 HP, ATK 4)  Crypt Rat (18/18 HP, ATK 4)
---
> L1R0: (empty)
> L1R1: (empty)
> L1R2: Wight (14/14 HP, ATK 6)
> L1R3: Crypt Rat (15/15 HP, ATK 4)  Skeleton (15/15 HP, ATK 5)
> L1R4: Crypt Rat (16/16 HP, ATK 4)  Bone Priest (16/16 HP, ATK 6)
16,17c16,17
< L1R6: Skeleton (16/16 HP, ATK 6)
< L1R7: Skeleton (16/16 HP, ATK 6)  Crypt Rat (16/16 HP, ATK 5)
---
> L1R6: (empty)
> L1R7: Wight (15/15 HP, ATK 5)
19,26c19,26
< L2R0: Bone Priest (19/19 HP, ATK 6)
< L2R1: Skeleton (21/21 HP, ATK 6)  Skeleton (20/20 HP, ATK 6)
< L2R2: Crypt Rat (20/20 HP, ATK 6)  Wight (22/22 HP, ATK 7)
< L2R3: Crypt Rat (21/21 HP, ATK 7)  Wight (22/22 HP, ATK 6)
< L2R4: Wight (20/20 HP, ATK 7)
< L2R5: Crypt Rat (19/19 HP, ATK 7)  Wight (22/22 HP, ATK 7)
< L2R6: Skeleton (18/18 HP, ATK 7)
< L2R7: Crypt Rat (19/19 HP, ATK 5)  Wight (19/19 HP, ATK 6)
---
> L2R0: (empty)
> L2R1: Skeleton (20/20 HP, ATK 5)
> L2R2: Skeleton (18/18 HP, ATK 6)  Crypt Rat (21/21 HP, ATK 6)
> L2R3: Wight (22/22 HP, ATK 6)  Bone Priest (19/19 HP, ATK 6)
> L2R4: Wight (18/18 HP, ATK 6)  Crypt Rat (22/22 HP, ATK 6)
> L2R5: Bone Priest (20/20 HP, ATK 6)  Crypt Rat (20/20 HP, ATK 7)
> L2R6: (empty)
> L2R7: Bone Priest (22/22 HP, ATK 7)  Wight (20/20 HP, ATK 7)
28c28
< L3R0: (empty)
---
> L3R0: Skeleton (23/23 HP, ATK 6)
31,35c31,35
< L3R3: (empty)
< L3R4: Wight (23/23 HP, ATK 8)
< L3R5: (empty)
< L3R6: Crypt Rat (26/26 HP, ATK 7)  Wight (22/22 HP, ATK 6)
< L3R7: Bone Priest (22/22 HP, ATK 8)
---
> L3R3: Crypt Rat (22/22 HP, ATK 8)
> L3R4: Bone Priest (22/22 HP, ATK 7)
> L3R5: Skeleton (24/24 HP, ATK 6)  Wight (23/23 HP, ATK 6)
> L3R6: Crypt Rat (24/24 HP, ATK 8)  Crypt Rat (25/25 HP, ATK 7)
> L3R7: Crypt Rat (24/24 HP, ATK 7)  Wight (24/24 HP, ATK 8)
37c37
< Total monsters: 30
---
> Total monsters: 26
```

**How many separate `Random` objects did you find in the starter?** 3
(`grep -rn "new Random(" src/main/java`)

```bash
src/main/java/dungeonforge/core/GameWorld.java:19:    private final Random random = new Random();
src/main/java/dungeonforge/core/Monster.java:16:    private static final Random RNG = new Random();
src/main/java/dungeonforge/core/Room.java:15:    private final Random rng = new Random();
```

**In one sentence: why does that make a bug report like "the boss room on level 2 was empty"
impossible for me to act on?** Because you won't be able to duplicate the same environment the bug happened in.


## 2. AFTER — US-1.1, settings live in one place

```bash
grep -rn "playerStartingHp\|60\|new Random(" src/main/java/dungeonforge/core
```

**Paste the output. AC2 wants zero hardcoded literals outside the config class:**

```

```

**Change `playerStartingHp` in `config.json` to 200, run, and paste the player line:**

```

```

**Rename `config.json` to `config.json.bak`, run again, and paste what happens (AC4):**

```

```

## 3. AFTER — US-1.2, the same seed produces the same dungeon

```bash
mvn -q exec:java > after1.txt
mvn -q exec:java > after2.txt
diff after1.txt after2.txt && echo "IDENTICAL"
```

**Result:**

```

```

**Now a different seed (AC4). Paste enough to show the world changed:**

```

```

## 4. AFTER — US-1.3, the rule is enforced

**Paste your `mvn test` summary:**

```

```

**Paste the URL of the green CI check on your pull request:**


## 5. The one-line summary for your Sprint Review

> What can the project do now that it could not do last week?


