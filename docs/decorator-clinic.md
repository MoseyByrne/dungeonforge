# The Decorator Clinic — Week 9, Part D

Four experiments. Every one produces a number, and the numbers are the grade. Do not write
"Decorator scales better" anywhere in this document without a count next to it.

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

## D2 — The refusal that is not about the player · 7 pts

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

## Turning it in

D1–D4 go in `docs/evidence.week9.md` sections 1, 2, 3 and 5. Paste command output, not
paraphrases of command output. **A number without the command that produced it earns half
marks.**
