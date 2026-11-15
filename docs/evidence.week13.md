# Week 13 Evidence

Fill this in as you go. Parts 0–3 and Part 5 are graded on the **measurement**, not the prose.

**Every counting command pipes through `scripts/codeonly.py`.** The reference solution's own
javadoc contains the strings `getChest()` and `private Chest chest`. Grep the code, not the
commentary.

---

## 0. The Sprint 9 carry-over (US-9.4)

Open `events/DamageMeter.java` and find where its warning level comes from.

**Paste that line.** ___this.threshold = (int)Math.round(threshold * GameConfig.getInstance().getDouble("playerStartingHp"));___

**`playerStartingHp` is a constant in `config.json`. In one sentence: what is the meter supposed
to be asking, and who is it asking instead?**
>The meter should ask the player for its current maximum HP, but instead it asks GameConfig for the starting HP.


**Now try to write a test that proves it is wrong.**

**What stops you?** ___Entity exposes getMaxHp() but has no public setMaxHp(), so the test cannot change the existing player’s maximum HP through its public API.___

**One sentence: why did this defect survive four sprints?**
>The defect survived because a fixed starting maximum made the incorrect calculation behave like the correct one, and no test changed that maximum.

>Before the fix: CarryOverTest ran 6 tests with 1 failure.
damageMeterUsesThePlayersCurrentMaximum expected 1 warning but received 0.
>After the fix: mvn clean test ran 167 tests with 0 failures,
0 errors, and 0 skipped, including 8 CarryOverTest tests.

---

## 1. BEFORE — count the files

```bash
for f in $(find src/main/java -name '*.java'); do
  python3 scripts/codeonly.py "$f" | grep -qE 'getChest\(\)|hasChest\(\)|setChest\(' && echo "$f"
done
```

**List them:** ___src/main/java/dungeonforge/commands/CommandParser.java
src/main/java/dungeonforge/commands/TakeCommand.java
src/main/java/dungeonforge/commands/TurnSnapshot.java
src/main/java/dungeonforge/core/GameWorld.java
src/main/java/dungeonforge/core/Room.java
src/main/java/dungeonforge/factory/RoomPopulator.java___

**How many?** ___6___

**Now the sentence. Name the count and say what it is a count OF.** *(Two points. "Six files use
getChest" is not it — six files know *something*, and naming that thing is the answer.)*

> Six files know that a room stores items in two separate places: its floor collection and its chest’s contents.

**And one more count:**

```bash
grep -rln "carriedWeight\|totalWeight" src/main/java/
```

**Result:** ___0___

**Every `Item` has declared `getWeight()` since Week 1. In one sentence, what does that result
mean about the number on your status line?**
>The displayed carrying capacity does not tell us the actual combined weight of the items the player carries.

---

## 2. BEFORE — the two halves

Open `commands/TakeCommand.java`. Put `takeFromFloor()` and `takeFromChest()` side by side.

**Paste both:**

```java
    private Item takeFromFloor() {
        for (Item i : room().getFloorItems()) {
            if (matches(i)) { room().getFloorItems().remove(i); return i; }
        }
        return null;
    }

    private Item takeFromChest() {
        if (!room().hasChest()) return null;
        for (Item i : room().getChest().getContents()) {
            if (matches(i)) { room().getChest().getContents().remove(i); return i; }
        }
        return null;
    }
```

**Ignoring the collection each one walks, how many lines actually differ?** ___1___

**Now play the game.** `mvn -q exec:java`, then: go north, kill whatever is there, `look`,
`take <the chest's name>`.

**Paste those four lines of output:**

```bash
[Exploring | HP 75/80] > take Iron-bound Chest

  There is nothing like that here.
```

**The game listed a chest, let you take something out of it, and then said it does not exist.
Which of those three statements is false?**
>“There is nothing like that here” is false: the chest exists, but TakeCommand searches only loose items and the items inside the chest.

**Open `items/Chest.java` and read the comment at the top. When was it written?** ___week 4___

---

## 3. BEFORE — the counting

```bash
C="python3 scripts/codeonly.py"
$C src/main/java/dungeonforge/core/Room.java | grep -cE 'List<Item> floorItems|private Chest chest'
$C src/main/java/dungeonforge/commands/TakeCommand.java | grep -cE 'private Item takeFrom'
$C src/main/java/dungeonforge/items/Chest.java | grep -c 'implements Item'
```

| Measurement | Before | After |
|---|---|---|
| Files using the separate chest accessors | 6 | 0 |
| Collections a `Room` keeps items in | 2 | 1 |
| Separate `takeFrom…` search helpers in `TakeCommand` | 2 | 0; one iterator search remains |
| `Chest` is an `Item` | No | Yes |
| Files that sum item weight | 0 | 2: `Player.java` and `Chest.java` |
| `take <chest>` works | No | Yes |
| `totalLoot()` after adding two loose floor items | Not measured on the original build | 35 → 37 on the finished build |

*(After column at the end of the sprint. **Nothing in it may be a guess.**)*

>US-10.1 verification:
mvn clean test — 176 tests, 0 failures, 0 errors, 0 skipped.
IteratorCompositeTest — 9 tests passed.
>
>The code-only chest-accessor search now lists four files:
CommandParser.java, TurnSnapshot.java, Room.java, and RoomPopulator.java.
>
>TakeCommand contains one room().contents() call and no takeFromFloor
or takeFromChest helpers. The countingLootNoLongerForgetsTheFloor test
confirms that adding two loose items increases totalLoot() by two.

---

## 4. The gap US-10.2 leaves on purpose

Finish US-10.2, then play the game again and try `take <the chest's name>`.

**What happened?**
```bash
[Exploring | HP 75/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[Exploring | HP 75/80] > take Iron-bound Chest

  There is nothing like that here.
```

**Complete this sentence:**

> A chest is an `Item` now and I still cannot pick one up, because the current room iterator visits floor items and chest contents but never returns the chest itself.


**Which story owns that, and which of its acceptance criteria?** ___US-10.3, AC7 covers taking the chest; AC4 supplies the removal behavior it depends on.___

---

## 5. AFTER — the receipt

### The deletion

```bash
ls src/main/java/dungeonforge/core/RoomContentsIterator.java
grep -rl 'RoomContentsIterator' src/
mvn test
```

**Paste all three.**

```bash
$ ls src/main/java/dungeonforge/core/RoomContentsIterator.java
ls: cannot access 'src/main/java/dungeonforge/core/RoomContentsIterator.java': No such file or directory

$ grep -rl 'RoomContentsIterator' src/
# No output.

$ mvn test
[INFO] Tests run: 194, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**US-10.1's tests were written against a class that no longer exists, and they still pass. In one
sentence: what were they actually asserting?**
>The behavioral tests asserted traversal and removal through Room’s public API rather than a particular iterator class; assertions tied to the old storage structure were updated because the new traversal also returns containers.

**US-10.3 gameplay verification.**
```bash
[Exploring | HP 75/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[Exploring | HP 75/80] > status

  Delver  HP 75/80  ATK 10  DEF 3  Gold 28  XP 14  Carry 3.3/60.0kg  (unarmed)

[Exploring | HP 75/80] > take Iron-bound Chest

  You take Iron-bound Chest.

[Exploring | HP 75/80] > inventory

  You are carrying:
    - Small Healing Draught [heals 22] (0.3kg, 20g)
    - Chronomancer's Hourglass [2 charges] (rewinds one turn)
    - Notched Shortsword [dmg 5] (2.5kg, 25g)
    - Iron-bound Chest (10.4kg, 208g)

[Exploring | HP 75/80] > status

  Delver  HP 75/80  ATK 10  DEF 3  Gold 28  XP 14  Carry 13.7/60.0kg  (unarmed)

[Exploring | HP 75/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Exits: south, north

[Exploring | HP 75/80] > undo

  You take that back.

[Exploring | HP 75/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[Exploring | HP 75/80] > inventory

  You are carrying:
    - Small Healing Draught [heals 22] (0.3kg, 20g)
    - Chronomancer's Hourglass [2 charges] (rewinds one turn)
    - Notched Shortsword [dmg 5] (2.5kg, 25g)

[Exploring | HP 75/80] > status

  Delver  HP 75/80  ATK 10  DEF 3  Gold 28  XP 14  Carry 3.3/60.0kg  (unarmed)
```

| Measurement | Before pickup | After pickup | After undo |
|---|---:|---:|---:|
| Carried weight | 3.3 kg | 13.7 kg | 3.3 kg |
| Chest in room | Yes | No | Yes |
| Chest in inventory | No | Yes | No |

### The bug nobody reported

**Read the version of `totalLoot()` you started the week with. What did it count? What did it NOT count?**
>it counted chest contents only, ignoring loose floor items.

**Why was that invisible?** ___Generated loot was placed inside chests, so counting only chest contents appeared correct until loose items were added to a floor.___

**Paste your before-and-after measurement** — `totalLoot()` with nothing on the floor, then after
dropping two items.

```bash
$ mvn -Dtest=IteratorCompositeTest#countingLootNoLongerForgetsTheFloor test

totalLoot before adding two floor items: 35
totalLoot after adding two floor items: 37

[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

The test adds two loose items directly to the starting room's floor,
simulating the floor contents after dropping them rather than executing
the drop command. totalLoot() increases from 35 to 37, counting both items.

**One sentence, and be careful with it: did Iterator FIND this bug?** *(There is a wrong answer
that sounds good.)*
>No. Inspecting the old implementation and testing loose floor items exposed the bug; using one iterator-based traversal fixed the omission.

---

## 6. AFTER — what an iterator cannot do

**After US-10.1, how many files still knew a room had two containers?** ______

**Name them, and for each one say why `Room.contents()` could not serve it:**

| File | Why `Room.contents()` alone could not serve it |
|---|---|
| `src/main/java/dungeonforge/commands/CommandParser.java` | Room descriptions need to preserve the distinction between loose items and items inside a chest. |
| `src/main/java/dungeonforge/commands/TurnSnapshot.java` | Undo needs original membership and ownership, which a flat traversal alone does not record. |
| `src/main/java/dungeonforge/factory/RoomPopulator.java` | It must insert a chest into the room; traversal does not provide insertion. |

**One of those three still reads the room's collections directly in your finished code. Which,
and is that a failure to finish the refactor? Answer yes or no and defend it.**
>CommandParser does, and that is intentional: loot targets top-level items, while room descriptions preserve container grouping. TurnSnapshot also retains a shallow copy of the floor collection because restoring room membership requires it.

**For the other two: what fixed them — a better iterator, or something else?**
>Composite changed the storage model: RoomPopulator now adds a chest like any other item, and TurnSnapshot records each chest’s contents by identity while retaining the floor membership snapshot. The recursive iterator helps discover nested containers, but traversal alone does not supply restoration semantics.

---

## 7. AFTER — the honest accounting

### Composite's trade

**Head First puts `add()` on the Component and lets leaves throw (p. 364). You did not.**

**Name one thing a caller cannot write because of that choice.** ___item.add(thing) when item is declared as Item.___

**Name one thing that cannot go wrong at run time because of it.** ___Calling add() on a potion through Item cannot unexpectedly throw at runtime, because that call does not compile.___

**Which would you choose for a library used by strangers? Is that the same answer you gave for
this codebase?**
>I would keep add() off Item and expose it through a separate container interface, so callers cannot accidentally try to put something inside a potion. Yes, that is the same safety choice made for this codebase

### The two walks

| Command | Walks | Means |
|---|---|---|
| `take <name>` | The complete tree, parent before children | Find a matching container or an item inside one. |
| `loot` | Top-level room items | Pick up each loose item or container, bringing its contents along. |

**One sentence: what goes wrong if `loot` walks the tree?**
>If loot walks the tree, it queues containers and their contents separately, causing later requests to fail after the container has already left the room.

### Leaves

**How many Leaf classes did you change this week?** ___0___

**One sentence on what that number says about the pattern.**
>Existing leaf classes continued to work through Item while containers supplied the new aggregate behavior.

### Retro line

| Pattern | Gave | Took |
|---|---|---|
| Iterator | One traversal interface independent of storage details | Explicit location information; removal must preserve the necessary ownership internally |
| Composite | Uniform weight and value operations for individual items and nested containers | Simplicity in mutation and undo, which must now respect a tree |

**And: the composite walk yields a container before its contents. Name the player-facing bug
that comes back if you reverse it.**
>Visiting children first changes first-match behavior: an unnamed or ambiguous take can select a child before its container. An exact chest-name search would still eventually find the chest, so reversing traversal alone does not necessarily recreate the original “chest does not exist” failure.

