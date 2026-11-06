# Week 10 Evidence

Parts 1–3 and Part 5 are graded on the measurement, not the code, and all of them happen
**before** you write anything.

---

## 0. The Sprint 7 carry-over (US-7.5)

Before any Template Method work. `docs/sprint-08-plan.md` has the story.

```bash
grep -rn 'unwrap' src/ | grep -v 'public Weapon unwrap'
grep -rl 'WEEK 9 STARTER' src/main/java | wc -l
```

**Paste the output of the first command.** How many callers does `unwrap()` have? __0____

**And its javadoc says it is "used by undo". Open `TurnSnapshot.java`. Is it?** ___no___

**One sentence on why dead code with a comment vouching for it is worse than dead code.**
>The misleading comment makes unused code look necessary, so someone maintaining the project may trust it and leave it in place.


**The second command prints a number.** __10____ Open any one of those files and read its header.
**Name one sentence in it that is no longer true, and say what made it stop being true.**
>BasicWeapon.java said, “The problem is the seven classes that extend it.” That stopped being true when we replaced the combination subclasses with decorators that wrap a Weapon.

### The limit you will find

Build `disenchant`, then run this in the game:

```
enchant shortsword with frost
enchant shortsword with flaming
disenchant shortsword
```

**What is in your hand now?** __frost shortsword____

**Now get to a weapon that is Flaming but not Frost, starting from Flaming(Frost(sword)), using
only `disenchant`.**

**Write down what happened and why.** *(One sentence. It is the same sentence as US-7.4's,
pointed the other way.)*
>I could not get a Flaming-only weapon because disenchant removes only the outermost decorator, so reaching Frost requires removing Flaming first.

### US-7.5 implementation and verification

Implemented `peel()` and the `disenchant` command. Disenchant removes
only the outermost enchantment, updates the equipped weapon when needed,
and supports undo through the existing snapshot system.

Removed the unused `unwrap()` method and updated outdated Week 9 comments.

Tests run: 116, Failures: 0, Errors: 0, Skipped: 0.

Six new tests cover basic-weapon peeling, returning the exact wrapped
weapon, removing only the outermost layer, repeated disenchanting,
updating the equipped weapon, and undo restoring inventory and equipment.

---

## 1. BEFORE — run the tests, then run the diagnostic three times

```bash
mvn test
mvn -q exec:java -Dexec.args="--report"
mvn -q exec:java -Dexec.args="--report --seed=3"
mvn -q exec:java -Dexec.args="--report --seed=7"
```

**How many tests pass?** ____110__

**Paste all three dungeon reports:**

```bash
$ mvn test
mvn -q exec:java -Dexec.args="--report"
mvn -q exec:java -Dexec.args="--report --seed=3"
mvn -q exec:java -Dexec.args="--report --seed=7"
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.10.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ dungeonforge ---
[INFO] Copying 3 resources from src\main\resources to target\classes
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ dungeonforge ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 83 source files with javac [debug release 21] to target\classes
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory C:\Users\Administrator\Desktop\CIS 18 Object Oriented Programming\dungeonforge\src\test\resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 7 source files with javac [debug release 21] to target\test-classes
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.CommandTest
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.194 s -- in dungeonforge.CommandTest
[INFO] Running dungeonforge.DecoratorTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.070 s -- in dungeonforge.DecoratorTest
[INFO] Running dungeonforge.FactoryTest
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.037 s -- in dungeonforge.FactoryTest
[INFO] Running dungeonforge.SingletonTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in dungeonforge.SingletonTest
[INFO] Running dungeonforge.SkeletonTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in dungeonforge.SkeletonTest
[INFO] Running dungeonforge.StateTest
[INFO] Tests run: 29, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.117 s -- in dungeonforge.StateTest
[INFO] Running dungeonforge.StrategyObserverTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.034 s -- in dungeonforge.StrategyObserverTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 110, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.302 s
[INFO] Finished at: 2026-11-05T15:01:16-08:00
[INFO] ------------------------------------------------------------------------
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.10.0   seed 20260818


  DUNGEON REPORT
  ------------------------------------------------------------------
  LEVEL  THEME         ROOMS  ORPHANS   SPAWN   EXIT
  ------------------------------------------------------------------
  1      Crypt             8        0       0    yes
  2      Forge             8     4 !!       0    yes
  3      Frost             8        0       0    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 4   spawn-camped levels: 0   unreachable exits: 0
  AT LEAST ONE LEVEL IS BROKEN. Which builder produced it, and what did it skip?

=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.10.0   seed 3


  DUNGEON REPORT
  ------------------------------------------------------------------
  LEVEL  THEME         ROOMS  ORPHANS   SPAWN   EXIT
  ------------------------------------------------------------------
  1      Crypt             8        0       0    yes
  2      Forge             8     4 !!       0    yes
  3      Frost             8        0    1 !!    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 4   spawn-camped levels: 1   unreachable exits: 0
  AT LEAST ONE LEVEL IS BROKEN. Which builder produced it, and what did it skip?

=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.10.0   seed 7


  DUNGEON REPORT
  ------------------------------------------------------------------
  LEVEL  THEME         ROOMS  ORPHANS   SPAWN   EXIT
  ------------------------------------------------------------------
  1      Crypt             8        0       0    yes
  2      Forge             8     5 !!       0  NO !!
  3      Frost             8        0       0    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 5   spawn-camped levels: 0   unreachable exits: 1
  AT LEAST ONE LEVEL IS BROKEN. Which builder produced it, and what did it skip?
```

**How many of the four levels satisfy every invariant, on each seed?**
default: ___3___   seed 3: ___2___   seed 7: ___3___

**Seed 7 shows a symptom the other two do not.** Look at the EXIT column. What does it mean for
a player who reaches that level?
The exit from level 2, Forge, is unreachable. A player who reaches that level cannot get to the stairs to continue to level 3.

**Now put the test count and the default-seed result in one sentence.** *(This is the most
important sentence you will write this week. Two points.)*

> _All 110 tests pass, but only three of the four levels satisfy every invariant with the default seed, showing that the existing tests do not catch all level-generation bugs.

**The three reports disagree. Why?** *(Look at what decides whether a room gets monsters. Then say
what that implies about how long a bug like this can survive.)*
The seed controls random choices, including cave connections and whether a room gets monsters or treasure. The ring builder incorrectly populates the entrance, but that mistake only shows up as spawn camping when monsters are placed there. A bug like this can survive for a long time if we keep testing with a seed that hides it.


**What does all of this tell you about the test suite you have been building for eight weeks?**
Passing tests only show that the behaviors we actually checked are working. Our tests missed problems in the generated levels, so we need tests that check reachability and safe entrances across different seeds.

---

## 2. BEFORE — find the drift

Open `core/GameWorld.java`. The three `build*Level` methods are 90% the same code. Put two of
them side by side in two editor windows.

**Fill this in by reading. Do not guess.**

| Step | Corridor | Cave           | Ring |
|---|----------|----------------|------|
| 1. create the rooms | yes      | yes            | yes  |
| 2. connect them | Corridor | Branching cave | Ring |
| 3. entrance flavour | yes      | yes            | yes  |
| 4. populate — **which index does the loop start at?** | r=1      | r=1            | r=0  |
| 5. prune unreachable rooms | yes      | Missing        | yes  |

**Which step is genuinely different between the three?** __2____

**Which differences are NOT about the shape of the level?**
The cave builder skips pruning unreachable rooms, and the ring builder starts populating at r = 0 instead of r = 1. Reachable rooms and a safe entrance should be consistent across all shapes.

**For each, say what the consequence is for a player:**

| Drift | What the player experiences |
|---|---|
| Cave skips pruning unreachable rooms| 	Some rooms cannot be reached, leaving their contents inaccessible. The stairs can also become unreachable, preventing progress.|
|Ring populates from r = 0 | 	Monsters can spawn in the entrance room, so the player may enter the level already surrounded by enemies.|

**Neither of these is a typo. Both are what happens when one algorithm exists in three copies. In
one sentence: why did the compiler not help?**
All three methods contain valid Java, and the compiler does not know that every level should have reachable rooms and an entrance without monsters.


**A faster way to find this class of bug, which you should keep:**

```bash
grep -n 'for (int r = [01]; r < rooms.size(); r++)' src/main/java/dungeonforge/core/GameWorld.java
```

**Paste the output. What does the pattern of 0s and 1s tell you?**
>The populate loops on lines 96 and 133 start at 1, skipping the entrance room at index 0. The ring’s populate loop on line 180 starts at 0, allowing monsters in the entrance. Line 120 is the cave’s connection loop, so it is an extra match rather than another populate loop.

```bash
$ grep -n 'for (int r = [01]; r < rooms.size(); r++)' src/main/java/dungeonforge/core/GameWorld.java
96:        for (int r = 1; r < rooms.size(); r++) {
120:        for (int r = 1; r < rooms.size(); r++) {
133:        for (int r = 1; r < rooms.size(); r++) {
180:        for (int r = 0; r < rooms.size(); r++) {
```

---

## 3. BEFORE — the counting

```bash
wc -l src/main/java/dungeonforge/core/GameWorld.java
grep -c 'new Room("L"' src/main/java/dungeonforge/core/GameWorld.java
grep -c 'createRoomFlavor()' src/main/java/dungeonforge/core/GameWorld.java
grep -c 'populatorFor(' src/main/java/dungeonforge/core/GameWorld.java
grep -c 'switch (d % ' src/main/java/dungeonforge/core/GameWorld.java
```
>GameWorld.java now has 86 lines, zero room-creation statements,
zero entrance-flavor calls, zero populatorFor references, and zero
shape-selection switches. All four levels pass the report's invariants
with seed 3.

| | Before | After |
|---|---|---|
| `GameWorld.java` lines | 269 | |
| times room creation is written | 3 | |
| times entrance flavour is written | 3 | |
| times the populate loop is written | 3 | |
| `switch` statements over level shape | 1 | |
| levels satisfying every invariant (seed 3) | 2/4 | |

*(Fill the After column at the end of the sprint. Nothing in it may be a guess.)*

---

## 4. AFTER — US-8.4, the receipt

**Paste `git diff --stat HEAD~1` for the commit that added the fourth shape:**

```bash
 .../dungeonforge/generate/GeneratorRegistry.java   |  1 +
 .../java/dungeonforge/generate/SpokeGenerator.java | 37 ++++++++++++++++++++++
 2 files changed, 38 insertions(+)
```

**New files:** ___1___  **Modified files:** ____1__  **Lines added:** ____38__

**Is `GameWorld.java` in that diff?** ___no___ **Why not?**
>Because It already selects generators through GeneratorRegistry, so adding the spoke shape only required a new generator class and one registry entry.

### The other half of the receipt — what the old design would have cost

**(a)** To add a spoke shape to the starter, which existing method would you have copied?
*(Be honest — you would copy the one whose shape is closest, not the one that is correct.)*
>buildRingLevel
______

**(b)** Roughly how many lines would that have added to `GameWorld.java`? __48____

**(c)** **The question that matters.** Look at the method you named in (a). Run the `grep` from
Part 2 on it. Does it have any of the drift you catalogued?
>Yes. Its population loop started at r = 0 instead of r = 1, so it
populated the entrance room.
______

**So what would have happened to the new level's invariants?**
>The copied spoke builder would also allow monsters to spawn in the
entrance, breaking the safe-entrance invariant whenever the random
population placed monsters there.

**(d)** Now check your answer against reality. Your instructor measured it: the fourth shape was
added to the starter by copying `buildRingLevel`, and on **the default seed** the report went
from `orphaned rooms: 4, spawn-camped levels: 0` to `orphaned rooms: 4, spawn-camped levels: 1`
— **the newly spawn-camped level being level 4, the one that had just been added.**
`GameWorld.java` went from 269 lines to 317, the count of populate loops starting at `r = 0`
went from 1 to 2, and **it compiled on the first attempt.**

> Write the conclusion as a sentence with the word *propagate* in it.
> Copying a builder can propagate its bugs into a new shape, even when
the code compiles successfully.

---

## 5. AFTER — what you did not write

Your fourth shape produces levels with no orphaned rooms and an empty entrance.

**Find the reachability check in `SpokeGenerator.java`.** Where is it? ___enforceReachability()___

**Find the entrance guard in `SpokeGenerator.java`.** Where is it? __populateRooms()____

**In two or three sentences: explain how a class can satisfy an invariant it does not implement,
and what had to be true of `generate()` for that to work.**
>SpokeGenerator inherits generate(), which calls the shared private
operations that enforce reachability and skip populating the entrance.
Making generate() final prevents the subclass from replacing the
sequence, while making the fixed operations private prevents it from
overriding those operations.

---

## 6. AFTER — the honest accounting

Two of the starter's levels were broken. Both are fixed now.

**Did you write a bug fix? Quote the lines if you did.**
>if (a.getExits().containsKey(direction)) return false;
if (b.getExits().containsKey(reverse)) return false;

**If you did not, describe what you actually did instead.** *(There is a precise way to say this,
and it involves the word "deleted".)*
>I deleted the three duplicated builders and moved their shared steps into one final template method, fixing the original drift. Later, I also fixed cave exit overwriting by checking both connection points before linking and retrying when a connection was unavailable.

---

## 7. Retro line

**First, the carry-over.** One sentence on what `disenchant` taught you about Decorator that
building the decorators did not.
>Disenchant showed me that decorators form an ordered stack: I can remove
the outermost enchantment, but I cannot remove an inner one while keeping
the layers above it using peel alone.



**One sentence on something Template Method gave you, and one on something it took away.** *(It
does take something. Head First is clear about it on p. 302 — a subclass cannot change the order
of the steps, and one day you will want to.)*
>Template Method gave me one shared generation sequence that every shape
follows. It took away a subclass’s freedom to reorder or skip those steps.