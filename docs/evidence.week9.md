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

## 3. AFTER — US-7.3, the receipt

**Paste `git diff --stat HEAD~1` for the commit that added the fourth enchantment:**

```

```

**New files:** ______  **Modified files:** ______  **Lines added to modified files:** ______

**Now re-run the Part 1 counts on the finished package:**

| | Before | After |
|---|---|---|
| Weapon/enchantment classes | | |
| Files containing the fire bonus | | |
| `instanceof` in `items/` | | |
| `return null` paths in the selection class | | |
| Lines in the selection class | | |

---

## 4. AFTER — US-7.4, order

**Paste your two order tests, and their four numbers:**

```java

```

| Stack | Result |
|---|---|
| `Flaming(Keen(sword))` | dmg ______ |
| `Keen(Flaming(sword))` | dmg ______ |
| `Vampiric(Flaming(sword))` | drain ______ |
| `Flaming(Vampiric(sword))` | drain ______ |

**One paragraph. Why is this correct behaviour rather than a bug?** *(There is one idea to
state, and everything else follows from it.)*


---

## 5. The payoff — what did NOT change

```bash
git diff --stat main -- src/main/java/dungeonforge/core/
```

**Which files in `core/` changed during this entire sprint?** ______

**`Combat.playerStrikes()` computes the damage the player deals and the hit points they drink
back. You added four enchantments this week, two of which change damage and one of which
changes healing. How many lines of `Combat.java` did you edit?** ______

**Explain that number** in two or three sentences, using the phrase *programs to the
interface* and naming which interface.


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
