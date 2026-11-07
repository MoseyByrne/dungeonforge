# The Template Method Clinic — Week 10, Part D

Four experiments. D3 is the one worth doing carefully — it is the only experiment this semester
where you deliberately break your own code to see what was holding it together.

---

## D0 — The carry-over, and the limit it exposes · 4 pts

You built `disenchant` in US-7.5. This step is about what building it told you.

**Step 1.** Before US-7.5, `WeaponDecorator.unwrap()` had how many callers? ___0___
And its javadoc claimed what? ___unwrap()___

**Step 2.** Write the two implementations of "is there anything to remove?" side by side:

```
// (a) the type test
if (w instanceof WeaponDecorator d) {
        // Remove the outer decorator.
        }

// (b) asking the weapon itself
Weapon peeled = w.peel();
if (peeled == w) {
        // Nothing to remove.
        } else {
        // Replace w with peeled.
        }
```

**One sentence on why (b) is better**, in terms of what each one would need changing if a third
kind of `Weapon` were added next semester.
>The type test assumes removable weapons inherit from WeaponDecorator, while peel() lets a new kind of Weapon define its own removal behavior without changing the command.

**Step 3.** Start from `Flaming(Frost(sword))`. Using only `disenchant`, reach a weapon that is
Flaming but not Frost.

**What happened?** __I could not reach Flaming without Frost using only disenchant: the first removal produced Frost(sword), and the second produced the plain sword, so Flaming was removed before I could reach Frost.____

**Step 4 — the one that is worth the points.** US-7.4 ended with the sentence *a decorator can
only see what is underneath it*. Rewrite that sentence so it describes what you just found out
about **removal** rather than about damage numbers.

> A decorator can only expose what is underneath it when removed, so peeling cannot remove an inner enchantment while preserving the outer ones.

**Step 5.** Name one feature a game might reasonably want that this limit makes awkward, and say
what you would have to do to support it. *(You do not have to build it. "Decorator is the wrong
tool for that" is an acceptable and correct answer, and saying it about a pattern you just spent
a week on is the point.)*
>A game might let players remove a specific enchantment, such as Frost, while keeping Flaming. Supporting that would require inspecting and rebuilding the decorator chain without the selected enchantment, or representing enchantments as a collection that supports removing individual entries.

---

## D1 — The Template Method you already wrote · 5 pts

You have been using this pattern since Week 7. You did not know its name.

**Step 1.** Open `commands/AbstractCommand.java` and read `execute()`.

**Step 2.** Fill this in from that file, not from the reading.

| | In `AbstractCommand` |
|---|---|
| The template method | 	execute()|
| Is it final? | 	Yes: public final void execute()|
| The primitive operation subclasses must implement | subclasses must implement	protected abstract boolean doExecute()|
| How many steps does the template fix the order of? | Three: capture the snapshot, perform the action, then let monsters respond. Snapshot and monster response are conditional on endsTurn(), and a refused action skips monster response.|
| What happens to a subclass that forgets a step? |The subclass cannot omit the surrounding template steps because it cannot override execute(). A concrete subclass that does not implement doExecute() fails to compile. |

**Step 3.** `AbstractCommand.execute()` snapshots, acts, and then lets the monsters respond — **in
that order, always.**

**Name one bug that ordering makes impossible.** *(Hint: think about what undo needs, and when
the snapshot has to be taken.)*
>Taking the undo snapshot after the action would save the changed state, so undo could not restore the state from before the command. The template captures the snapshot before doExecute() runs.

**Step 4.** In one sentence: why did Week 7 need this pattern before Week 10 taught it?

> *Answer:* _Week 7 needed every command to follow the same snapshot, action, and monster-response sequence without making each command implement that sequence separately.

---

## D2 — Three copies, three different mistakes · 6 pts

**Step 1.** From Part 2 of your evidence sheet, reproduce the count:

| Builder | Steps it performs | Steps it skips |
|---|---|----------------|
| `buildCorridorLevel` |	All five: create rooms, connect them, entrance flavour, populate from r = 1, prune unreachable rooms. | none           |
| `buildCaveLevel` | 	Create rooms, connect them, entrance flavour, populate from r = 1.|            Prune unreachable room    |
| `buildRingLevel` |All five, but population starts at r = 0. |       	It skips entrance protection by including the entrance in population.       |

**Step 2.** Notice that no two of them skip the *same* step. Finish this sentence:

> With N copies of one algorithm, the set of steps that *some* copy gets wrong grows
> as the copies drift independently, because each copy is another place where a step can be omitted, reordered, or implemented incorrectly.

**Step 3.** A tempting fix: add a comment at the top of each method listing the five steps in
order. Say in one sentence why that does not work.
>A comment describes the required sequence but cannot prevent a builder from skipping or changing a step, and the compiler does not enforce comments.

**Step 4.** A second tempting fix: extract the five steps into five helper methods and have each
builder call them in order. That is genuinely better than what is there now.
>Shared helpers remove duplicated step implementations, but each builder still controls which helpers it calls and in what order, so it can still omit or reorder them.

**But it does not solve the problem.** Say why in one sentence, then name the thing Template
Method adds that this fix does not.

> *(This is the exam question for this week. The answer is one word long and it appears on the
> signature of `generate()`.)*

> final.
> Template Method puts the sequence in one final generate() method that subclasses cannot override, while allowing them to implement the designated primitive operations and hooks.

---

## D3 — Remove the `final` and watch · 6 pts 🔨

**Do this after US-8.2 is finished and committed, on a scratch branch you will throw away.**

**Step 1.** Confirm you are starting clean, on both seeds.

```bash
$ mvn -q exec:java -Dexec.args="--report"
mvn -q exec:java -Dexec.args="--report --seed=3"
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
  2      Forge             8        0       0    yes
  3      Frost             8        0       0    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 0   spawn-camped levels: 0   unreachable exits: 0
  every level satisfies every invariant.

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
  2      Forge             8        0       0    yes
  3      Frost             8        0       0    yes
  4      Winterfell        8        0       0    n/a
  ------------------------------------------------------------------
  orphaned rooms: 0   spawn-camped levels: 0   unreachable exits: 0
  every level satisfies every invariant.
```

Record the orphan and spawn totals: default __0____ / ____0__   seed 3 ___0___ / ____0__

**Step 2.** Remove `final` from `LevelGenerator.generate(...)`.

**Step 3.** Write one more shape — call it `SloppyGenerator` — that extends `LevelGenerator`,
implements `connectRooms` however you like, **and overrides `generate()`** to create rooms,
connect them, populate them, and stop there.

You will find you cannot call the template's private steps, so you will have to write your own
versions. **Write them badly on purpose** — populate from index 0, skip the reachability check.
That is the whole point of the experiment.

**Step 4.** Register it and run `--report` again on both seeds.

| | orphans | spawn (monsters in entrance) |
|---|---|---|
| with `final` — default | 0 | 0 |
| with `final` — seed 3 | 0 | 0 |
| without `final` — default | 6 | 0 |
| without `final` — seed 3 | 6 | 2 |

**Step 5.** Answer three questions.

1. Which of your existing tests failed?
>aGeneratedWorldSatisfiesEveryInvariantOnEveryLevel
depthOneIsAlwaysACorridorSoTheGameOpensSimply
everyLevelButTheLastHasAReachableStaircase
theInvariantsHoldOnTheSeedThatExposedTheOldBugs
theRegistryHoldsOneEntryPerShape
theTemplateMethodIsFinalSoNoSubclassCanReorderTheSteps
2. Which test told you the `final` was gone?
>theTemplateMethodIsFinalSoNoSubclassCanReorderTheSteps
3. Did the compiler complain at any point?
>No. After removing final, the override compiled successfully. Maven failed
during testing because six assertions failed, not because of a compiler error.

**Step 6.** Put the `final` back, delete `SloppyGenerator`, and run `mvn test`.
```bash
[INFO] Tests run: 135, Failures: 0, Errors: 0, Skipped: 0
```

**Step 7.** One paragraph. You have just demonstrated that `final` on a template method is
load-bearing. Explain what it is bearing the load *of* — and note that the thing it protects is
not a behaviour but an **agreement**.
>The final modifier protects the agreement that every level generator follows the shared steps in their defined order. Removing it allowed SloppyGenerator to replace the whole algorithm, populate the entrance, and skip reachability enforcement while still compiling successfully. The private operations alone were not enough, because the override could bypass them and write its own versions. Making generate() final prevents subclasses from replacing that sequence while still allowing them to supply their shape and optional hooks.

> **Why this experiment exists.** Every year somebody argues that `final` is over-restrictive and
> that a subclass should be trusted to call the steps in order. That argument is exactly what
> produced `buildCaveLevel`. You have now run the experiment instead of having the argument.

---

## D4 — What did not change, and what you did not write · 4 pts

**Step 1.** List every file outside `generate/` that you modified this sprint.
>I compared against the Lab 10 starter commit, da726fb, because earlier Sprint 8 stories have already been merged into main. This includes the US-7.5 carry-over changes as well as the Template Method work.

```bash
 .../java/dungeonforge/commands/CommandParser.java  |   1 +
 .../dungeonforge/commands/DisenchantCommand.java   |  54 ++++++
 .../java/dungeonforge/commands/EnchantCommand.java |  14 +-
 src/main/java/dungeonforge/core/GameWorld.java     | 201 +--------------------
 .../java/dungeonforge/factory/CryptThemeKit.java   |   4 +-
 .../java/dungeonforge/factory/ForgeThemeKit.java   |   4 +-
 .../java/dungeonforge/factory/FrostThemeKit.java   |   4 +-
 .../dungeonforge/factory/WinterfellThemeKit.java   |   5 +-
 src/main/java/dungeonforge/items/BasicWeapon.java  |  15 +-
 .../java/dungeonforge/items/FlamingWeapon.java     |   4 +-
 src/main/java/dungeonforge/items/FrostWeapon.java  |   4 +-
 .../java/dungeonforge/items/VampiricWeapon.java    |   6 +-
 src/main/java/dungeonforge/items/Weapon.java       |  20 +-
 .../java/dungeonforge/items/WeaponDecorator.java   |   5 +-
 .../java/dungeonforge/states/ExploringState.java   |   2 +-
 15 files changed, 99 insertions(+), 244 deletions(-)
```

**Step 2.** Fill this in.

| File | Lines changed this sprint |
|---|---------------------------|
| `core/GameWorld.java` | 201: 9 added, 192 deleted |
| `core/Room.java` | 0                         |
| `core/DungeonLevel.java` | 0                         |
| `core/Combat.java` | 0                         |
| `commands/MoveCommand.java` | 0                         |
| `core/DungeonReport.java` | 0                         |

**Step 3.** `DungeonReport` does its own breadth-first search. So does `LevelGenerator`. That is
duplication, and this week was about removing duplication.

**Should `DungeonReport` call the generator's version instead? Argue one way in two sentences.**
>I would keep DungeonReport’s breadth-first search independent so it can detect mistakes in the generator’s reachability code. Sharing that implementation could make the generator and its checker agree on the same incorrect result.

> *(There is a defensible answer in each direction, and the marks are for the argument. The case
> against sharing: a checker that shares code with the thing it checks cannot catch that code
> being wrong. The case for sharing: two definitions of "reachable" can drift, which is the exact
> disease this week diagnosed.)*

**Step 4.** The honest half. Head First, p. 302, is direct about what this pattern costs:
subclasses cannot change the order of the steps.

**Name one level shape you can imagine wanting, that the current template cannot express.** Then
say what you would change about `generate()` to allow it — and what you would lose.
>I could want a dungeon that populates rooms first and then connects them according to the monsters placed in them, keeping dangerous encounters farther from the entrance. The current template connects rooms before population, so I would need to revise generate() to support population before connection while keeping the final reachability check. I would lose the guarantee that every generator connects rooms before population, and would need to review the existing shapes and tests for that changed agreement.

---

## Turning it in

D1–D4 go in `docs/evidence.week10.md` and in the PR description for the story they belong to.
Command output, not paraphrases of command output. **A number without the command that produced
it earns half marks.**
