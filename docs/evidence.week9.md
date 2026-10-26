# Week 9 Evidence

Fill this in as you go. Parts 0–2 and Part 5 are graded on the *measurement*, not on the code.

---

## 0. The Sprint 6 carry-over (US-6.7)

Before any Decorator work. `docs/sprint-07-plan.md` has the story.

```bash
grep -n 'isRunning\|!running' src/main/java/dungeonforge/Main.java \
                              src/main/java/dungeonforge/commands/GameContext.java
```

**Paste the output. How many places spell out the game-over rule?** ___3___
```bash
$ grep -n 'isRunning\|!running' src/main/java/dungeonforge/Main.java src/main/java/dungeonforge/commands/GameContext.java
src/main/java/dungeonforge/Main.java:111:        while (ctx.isRunning() && ctx.getPlayer().isAlive()) {
src/main/java/dungeonforge/Main.java:140:            if (!ctx.isRunning() || !ctx.getPlayer().isAlive()) break;
src/main/java/dungeonforge/commands/GameContext.java:54:    public boolean isRunning()           { return running; }
src/main/java/dungeonforge/commands/GameContext.java:89:        if (!player.isAlive() || !running) {
```

**Now open `GameOverState.java`. It lists four verbs. Try to reach one in play. What happens,
and why?**
After my player died, the game displayed “You died. Type 'quests' to see how far you got, or 'quit',” but ended without giving me another game prompt. The main loop requires the player to be alive, so it exits at 0 HP and makes the Game Over commands unreachable.

**One sentence: `running` and `GameOverState` both mean "the game is over". Do they? Say what
each one actually means.**
No: running controls whether the session continues accepting input, while GameOverState means the adventure has ended, so the player should still be able to check quests, history, or status after dying.


---

## 1. BEFORE — count the problem

```bash
ls src/main/java/dungeonforge/items/ | grep -E 'Weapon|Enchant'
grep -rln "damage + 4" src/main/java/dungeonforge/items/
grep -rho "instanceof" src/main/java/dungeonforge/items/*.java | wc -l
grep -c "return null" src/main/java/dungeonforge/items/EnchantmentForge.java
wc -l src/main/java/dungeonforge/items/EnchantmentForge.java
```

| | Count |
|---|---|
| Weapon/enchantment classes | 8 |
| Files containing the fire damage bonus | 4 |
| `instanceof` tests in `items/` | 34 |
| `return null` paths in `EnchantmentForge` | 13 |
| Lines in `EnchantmentForge` | 108 |

**Open `FlamingFrostVampiricWeapon.java`. Which of its methods contain arithmetic that also
appears, word for word, in another file?**
getDamage() repeats the flaming +4 and frost +3 damage bonuses.
getValue() repeats the +60, +45, and +90 enchantment value bonuses.
These calculations are duplicated across the enchanted weapon classes.


**You want a fourth enchantment, `keen`. Before you look anything up: how many new classes
would this package need?** ______

*(Write it down before reading on.)* For N enchantments the package needs 2^N − 1 classes.
N = 3 is 7. N = 4 is 15. N = 5 is 31.

A fourth enchantment would require 8 new classes, increasing the
enchanted subclasses from 7 to 15: (2^4 - 1) - (2^3 - 1) = 8.
---
## D1 — Count the duplication, then explain who caused it · 8 pts

The starter has seven enchanted weapon classes for three enchantments. That number is annoying
but it is not the interesting number.

**Step 1.** Run this and record the output.

```bash
$ git grep -n 'damage + 4' 840a3b7 -- src/main/java/dungeonforge/items/
840a3b7:src/main/java/dungeonforge/items/FlamingFrostVampiricWeapon.java:14:    @Override public int getDamage()  { return damage + 4 + 3; }
840a3b7:src/main/java/dungeonforge/items/FlamingFrostVampiricWeapon.java:16:    @Override public int lifesteal()  { return Math.max(1, (damage + 4 + 3) / 4); }
840a3b7:src/main/java/dungeonforge/items/FlamingFrostWeapon.java:14:    @Override public int getDamage()  { return damage + 4 + 3; }
840a3b7:src/main/java/dungeonforge/items/FlamingVampiricWeapon.java:14:    @Override public int getDamage()  { return damage + 4; }
840a3b7:src/main/java/dungeonforge/items/FlamingVampiricWeapon.java:16:    @Override public int lifesteal()  { return Math.max(1, (damage + 4) / 4); }
840a3b7:src/main/java/dungeonforge/items/FlamingWeapon.java:14:    @Override public int getDamage()  { return damage + 4; }
```

**Step 2.** Suppose the designer decides fire should do 5 damage instead of 4. List every file
you must edit, and every *line* within each file.

| File | Line(s) | What the edit is |
|---|---|---|
| `FlamingWeapon.java` | 14 | Change the damage bonus from `+ 4` to `+ 5`. |
| `FlamingFrostWeapon.java` | 14 | Change the fire bonus from `+ 4` to `+ 5`. |
| `FlamingVampiricWeapon.java` | 14, 16 | Change `+ 4` to `+ 5` in the damage and lifesteal calculations. |
| `FlamingFrostVampiricWeapon.java` | 14, 16 | Change `+ 4` to `+ 5` in the damage and lifesteal calculations. |
| `EnchantmentForge.java` | 89, 90, 91, 93 | Change `- 4` to `- 5` when recovering base damage. |

**Step 3.** Now the question that matters. `FlamingFrostWeapon` needs the fire arithmetic because
it is a flaming weapon. It inherits from `Weapon`, which does not have it. It cannot inherit from
`FlamingWeapon` *and* `FrostWeapon`.

**In one sentence: which language rule forced the duplication?**
Java allows a class to extend only one class, so this inheritance-based design cannot inherit both FlamingWeapon’s and FrostWeapon’s implementations.

**Step 4.** Head First, p. 86, says inheritance is "powerful but... not necessarily the best way
to achieve flexibility." Rewrite that sentence as a specific claim about this package, with your
number from Step 1 in it.

> *Answer:* In this package, inheritance repeats the fire arithmetic on 6 lines across 4 weapon classes, and changing its strength also requires updating 4 recovery lines in EnchantmentForge; the decorator version keeps the fire bonus in one class.

---

## 2. BEFORE — reproduce all four defects

You start carrying a **Notched Shortsword**, so this works on turn one. Run
`mvn -q exec:java` and type exactly this:

```
equip shortsword
status
enchant shortsword with flaming
status
inventory
equip shortsword
status
enchant shortsword with flaming
enchant shortsword with keen
quit
```

**(a) Paste the two `status` lines from before and after the first `enchant`:**

```bash
Before enchanting:
Delver  HP 80/80  ATK 15  DEF 3  Gold 0  XP 0  Carry 60.0kg  wielding Notched Shortsword [dmg 5] (2.5kg, 25g)

After enchanting:
Delver  HP 80/80  ATK 15  DEF 3  Gold 0  XP 0  Carry 60.0kg  wielding Notched Shortsword [dmg 5] (2.5kg, 25g)
```

**Did your damage change? Explain in one sentence why not.**
My damage did not change because enchanting replaced the weapon in my inventory with a new object, but my equipped weapon still referred to the original object.

**(b) Paste the refusal from the second `enchant ... with flaming`:**

```bash
That weapon cannot take another flaming enchantment.
```

**Is there anything wrong with what you asked for? Yes or no — then say what the refusal was
really reporting.**
No, there is nothing wrong with requesting the same enchantment twice. The refusal reflects the design’s limitation: it has no class representing a weapon with two flaming enchantments.

**(c) Paste the response to `enchant ... with keen`:**

```bash
No such enchantment: keen
```

**(d) Open `EnchantmentForge.baseDamage()`. In one sentence: why does this method exist at all?
What would have to be true for it to be unnecessary?**
baseDamage() exists to remove existing bonuses before constructing a replacement weapon; it would be unnecessary if each new enchantment wrapped the existing weapon and added its own behavior without rebuilding it.

**(e) Classify all four.** Two of these are the *design*; one is an ordinary *bug* that would
exist in any design. Which is which, and how do you know?

| Defect | Design, or ordinary bug? | How you can tell |
|---|---|---|
| (a) stale hand | Ordinary bug | Enchanting replaces the inventory weapon but does not update the equipped reference; either design needs that reference updated. |
| (b) no double enchantment | Design | The class hierarchy has no class for repeated enchantments, so the forge rejects the request. |
| (c) no fourth enchantment | Design | Adding keen requires new combination classes and changes to the forge’s selection logic. |
| (d) base-value recovery | Design | The forge must identify each weapon type and subtract its bonuses to rebuild a different combination; wrapping the existing weapon removes that need. |

---
D2 — The refusal that is not about the player · 7 pts

**Step 1.** In the running game, enchant a weapon with `flaming` twice. Record the message.
That weapon cannot take another flaming enchantment.

**Step 2.** Open `EnchantmentForge.enchant()` and find the line that produced it. Paste it.
```bash
if (w instanceof FlamingWeapon) return null; // no class for double fire
```

**Step 3.** Answer three questions in one sentence each.

1. Is a twice-flaming weapon unreasonable as a *game* idea? No, a twice-flaming weapon is a reasonable game idea if repeated bonuses are allowed.
2. Is it unreasonable as a *code* idea?  No, it is a reasonable code idea because one FlamingWeapon decorator can wrap another.
3. What, precisely, is the refusal actually reporting? The refusal reports that the starter has no class representing two flaming enchantments, rather than an intentional game rule.

**Step 4.** Count the branches that exist purely to say no.

```bash
grep -c "return null" src/main/java/dungeonforge/items/EnchantmentForge.java
```

___13__ branches. Now predict: after US-7.2, how many `return null` branches will
`EnchantmentRegistry` have? ___0___

**Step 5 — after you finish US-7.2.** Run the count again on the new class. **Strip comments
first** — the registry's own comment says *"apply() cannot return null"*, and a naive grep
counts it. That is the same trap as US-6.7's T8, and it catches people twice in one week.

```bash
perl -0777 -pe 's{/\*.*?\*/}{}gs; s{//[^\n]*}{}g' src/main/java/dungeonforge/items/EnchantmentRegistry.java | grep -c 'return null'
0
```

> **The lesson to write down:** a rule the player experiences should come from a decision
> somebody made, not from a class that nobody wrote. When you catch yourself explaining a game
> rule by describing your class hierarchy, the hierarchy is wrong.

---


## 3. AFTER — US-7.3, the receipt

**Paste `git diff --stat HEAD~1` for the commit that added the fourth enchantment:**

```bash
 .../dungeonforge/items/EnchantmentRegistry.java    |  1 +
 src/main/java/dungeonforge/items/KeenWeapon.java   | 25 ++++++++++++++++++++++
 2 files changed, 26 insertions(+)
```

**New files:** ___1___  **Modified files:** ___1___  **Lines added to modified files:** ____1__

**Now re-run the Part 1 counts on the finished package:**

| | Before | After |
|---|---|---|
| Weapon/enchantment classes | 8 | 7 |
| Files containing the fire bonus | 4 | 1 |
| `instanceof` in `items/` | 34 | 0 |
| `return null` paths in the selection class | 13 | 0 |
| Lines in the selection class | 108 | 40 |

---

## D3 — The fourth enchantment, measured twice · 6 pts

This is the headline experiment. You will measure the *same feature* in both designs.

**Step 1 — the old design, on paper.** Do not write the code. Using 2^N − 1 and the structure of
`EnchantmentForge`, work out what adding `keen` would have cost the starter:

| | Count |
|---|---|
| New classes needed | 8 |
| Existing classes needing edits | 1 — EnchantmentForge |
| New `instanceof` tests in `EnchantmentForge` | 55 |

*(For the third row: every existing enchantment's block gains one branch per new combination,
and both `baseDamage()` and `baseValue()` gain one line per new class.)*

**Step 2 — the new design, for real.** Do US-7.3. Commit only the two files it touches. Then:

```bash
git show --stat --oneline 0a9a7ff
 .../dungeonforge/items/EnchantmentRegistry.java    |  1 +
 src/main/java/dungeonforge/items/KeenWeapon.java   | 25 ++++++++++++++++++++++
 2 files changed, 26 insertions(+)
```

**Step 3.** Put the two side by side.

| | Starter design | Decorator design |
|---|---|---|
| New files | 8 | 1 |
| Modified files | 1 — EnchantmentForge.java | 1 — EnchantmentRegistry.java |
| Lines added to modified files | At least 55 new type-test lines, plus registration and control-flow code (estimated, not implemented) | 1 |
| `instanceof` count afterwards | 89 (projected using the same table structure) | 0 (measured in items/) |

**Step 4.** One sentence: what is the *shape* of each design's growth curve? Use the words
"linear" and "exponential" correctly, or do not use them at all.
The inheritance design grows exponentially, requiring 2^N − 1 enchanted classes, while the decorator design grows linearly, adding one decorator class and one registry entry per enchantment.

---

## 4. AFTER — US-7.4, order

**Paste your two order tests, and their four numbers:**

```java
@Test
void orderChangesTheResultForMultiplyingDecorators() {
    int keenThenFire =
            new FlamingWeapon(new KeenWeapon(sword())).getDamage();
    int fireThenKeen =
            new KeenWeapon(new FlamingWeapon(sword())).getDamage();

    assertEquals(16, keenThenFire); // (8 * 1.5) + 4
    assertEquals(18, fireThenKeen); // (8 + 4) * 1.5
    assertNotEquals(keenThenFire, fireThenKeen);
}

@Test
void orderChangesHowMuchAVampiricWeaponDrinks() {
    assertEquals(3,
            new VampiricWeapon(new FlamingWeapon(sword())).lifesteal());
    assertEquals(2,
            new FlamingWeapon(new VampiricWeapon(sword())).lifesteal());
}
```

| Stack | Result |
|---|---|
| `Flaming(Keen(sword))` | dmg 16 |
| `Keen(Flaming(sword))` | dmg 18 |
| `Vampiric(Flaming(sword))` | drain 3 |
| `Flaming(Vampiric(sword))` | drain 2 |

**One paragraph. Why is this correct behaviour rather than a bug?** *(There is one idea to
state, and everything else follows from it.)*
Each decorator applies its behavior to the weapon it wraps, so changing the order changes the input each layer receives. Keen before Flaming gives (8 × 1.5) + 4 = 16, while Flaming before Keen gives (8 + 4) × 1.5 = 18. Vampiric outside Flaming calculates lifesteal from 12 damage, giving 3; inside Flaming, it calculates from 8 damage, giving 2. These differences correctly reflect the order of the operations.

---

## 5. The payoff — what did NOT change

```bash
git diff --stat main -- src/main/java/dungeonforge/core/
```

**Which files in `core/` changed during this entire sprint?** _none_

**`Combat.playerStrikes()` computes the damage the player deals and the hit points they drink
back. You added four enchantments this week, two of which change damage and one of which
changes healing. How many lines of `Combat.java` did you edit?** ___0___

**Explain that number** in two or three sentences, using the phrase *programs to the
interface* and naming which interface.
Combat programs to the interface, Weapon, through the player’s weapon methods. Basic weapons and decorated weapons implement that same interface, so Combat receives the calculated damage and lifesteal without needing to know which enchantments are present. Adding enchantments therefore required no changes to Combat.

---

## D4 — What did not change, and what you did yourself · 4 pts

**Step 1.** List the classes that compute combat outcomes:

```bash
grep -rln "getAttackPower\|lifesteal" src/main/java/dungeonforge/core/
src/main/java/dungeonforge/core/Combat.java
src/main/java/dungeonforge/core/Entity.java
src/main/java/dungeonforge/core/Player.java
```

**Step 2.** For each, run `git diff main -- <file>` and record the number of changed lines.

Compared against starter commit `840a3b7`; the diff produced no output.

| File | Lines changed since the Lab 9 starter |
|---|---|
| `core/Combat.java` | 0 |
| `core/Player.java` | 0 |
| `core/Entity.java` | 0 |
| `commands/EquipCommand.java` | 0 |

**Step 3.** Explain the small numbers. Your answer should use the phrase "programs to the
interface" and should name *which* interface.
Combat programs to the interface, Weapon, through the player's weapon methods. BasicWeapon and every decorator implement Weapon, so damage and lifesteal are calculated through the wrapped objects without Combat needing to identify individual enchantments.

**Step 4 — the honest half.** One thing in this sprint that Decorator did **not** fix, that you
had to fix yourself. Name it and say why the pattern was never going to help.
Decorator did not fix the stale equipped-weapon reference. EnchantCommand still had to check whether the original weapon was equipped and equip its replacement after updating the inventory, because wrapping an object does not automatically redirect other references to the new wrapper.

> There is a specific answer and it is in `EnchantCommand`. If you cannot find it, reread the
> sub-task list for US-7.2 — one of the tasks has a note under it saying exactly this.

---

## 6. The honest accounting

**Of the four defects in Part 2, which one did Decorator fix, and which one did you fix?**


**Quote the lines that fixed the one Decorator did not.**

```java

```

**Would those lines have worked before the refactor?** ______

---

## 7. Retro line

**One sentence on something Decorator gave you, and one on something it did not.**
