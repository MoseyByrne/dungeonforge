# The Twins Clinic — Lab 8, Part D

> Week 5 you were asked to guess what could possibly distinguish State from Strategy, given
> that their UML is nearly identical. **This is where you find out.**

## D1 — Grade your own Week 5 prediction · 8 pts

Open `docs/strategy-observer-clinic.md` from Week 5 and find your D4 answer.

**Paste it here, unedited:**

```
I didt complete Week 5 D4 prediction, so I dont have a prediction to compare with my current understanding.
```

**Now, having built both, mark your own work:**

| | Strategy | State |
|---|---|---|
| Who decides which one is active? |A client or factory assigns it. In DungeonForge, a state’s entry hook can also change it. | Monster states select successors through next(). Game modes are selected by GameContext.|
| Do the alternatives know about each other? |Strategies generally don’t reference other strategies. | States may reference the states they can transition into|
| What question does it answer about a monster? |How does the monster act—for example, heal or attack? | When can it act, and what lifecycle behavior applies?|
| Can it change while the object is alive? |Yes, when code explicitly swaps the strategy. | Yes, as events and conditions trigger transitions.|

```
Both patterns use composition and delegation, so their class diagrams look similar. Their purposes differ: Strategy provides interchangeable ways to perform an action, while State changes behavior as an object moves through its lifecycle.
```

**How close were you?** Circle one and justify it in a sentence:
*dead on · half right · reasonable but wrong ·* nowhere near


> **Nowhere near,** because I din't complete the assignment in week 5.

## D2 — What does a new state cost? · 7 pts

The Week 7 code stored a monster's state as a **string** with conditionals in three places.
Your Week 8 code stores it as an **object**. Measure the difference.

### First, on the STARTER code (before your refactor)

Add a `"stunned"` state that lasts one turn and prevents acting.

| Question | Your answer                                                                                                                        |
|---|------------------------------------------------------------------------------------------------------------------------------------|
| Which methods in `Combat` did you have to edit? | monsterTurns(), updateMonsterState(), and monsterActs(). I also temporarily edited playerStrikes() to trigger stunned for testing. |
| How many conditionals did you add or change? | 2                                                                                                                                  |
| What stops someone typing `setStateName("stnuned")` and getting no error? |Nothing. The setter accepts any String, so the typo compiles without an error even though it has no defined state behavior.                                                                                                                                    |

```bash
[HP 80/80] > north

  You go north.
  ACHIEVEMENT: Cornered -- Force an enemy to change tactics
  Ghoul changes tactics: idle -> hunting.

[HP 80/80] > attack

  You hit Ghoul for 10.
  Ghoul changes tactics: stunned -> hunting.

[HP 80/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Hostile: Ghoul (8/18 HP, ATK 2, hunting)
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Bone Shortsword [dmg 6] (2.0kg, 50g)
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[HP 80/80] > 
```

> Revert this experiment before continuing. It exists to be measured, not kept.

### Then, on YOUR refactored code

Add the same `StunnedState`.

| Question | Your answer |
|---|-------------|
| New files | 0           |
| Existing files modified | 3           |
| Did `Combat.java` change? | yes         |
| Did `Monster.java` change? | no          |

**Paste `git diff --stat`:**

```bash
$ git diff --stat
 docs/state-clinic.md                               | 22 ++++++++++++--------
 src/main/java/dungeonforge/core/Combat.java        |  5 +++++
 .../java/dungeonforge/states/StunnedState.java     | 24 ++++++++++++++++++++--
 3 files changed, 40 insertions(+), 11 deletions(-)
```

**One paragraph: what did the compiler start doing for you that it could not do when the
state was a string?**
Once the state became an object, the compiler could check that each state class exists, implements `MonsterState`, and provides the required methods with the correct return types. With strings, a typo like `"stnuned"` still compiled because Java treated it as ordinary text. A misspelled class name such as `new StnunedState()` now produces a compile-time error. This catches structural mistakes before the game runs, although tests are still needed to verify that transitions happen correctly.


## D3 — Rejected versus impossible · 7 pts

In Week 7, typing `north` during a fight got you told off. In Week 8 it is not on the menu.

**Paste your Week 7 behaviour (from `git show` on an old tag, or just describe it):**

```bash
[HP 78/80] > help

  Available: north, south, down, attack, take, drop, use, loot, undo, look, inventory, status, quests, history, help, quit

[HP 78/80] > north

  You cannot walk away with enemies still standing.
```

**Paste your Week 8 behaviour, including the `help` output in both modes:**

```bash
[exploring | HP 80/80] > help

  [exploring] Available: north, south, down, look, take, loot, drop, use, inventory, pack, status, quests, history, undo, help, quit
  
[combat | HP 80/80] > help

  [combat] Available: attack, use, look, inventory, status, history, undo, help, quit
  
[combat | HP 80/80] > north

  You cannot do that in combat mode.
  Available here: attack, use, look, inventory, status, history, undo, help, quit
```

### The questions

**1.** Count the mode guards you deleted from command classes: __3__
Which files were they in? MoveCommand, TakeCommand, and DropCommand

**2.** `help` now prints a different list in each mode, and you did not write any code to make
that happen. **Why not?** Each game state defines its available commands. Help reads that same list through allowedVerbs(), so it automatically displays the commands permitted in the current mode.

**3.** Here is the one that matters. Suppose a teammate adds an `equip` command next week and
forgets to think about combat. **In the Week 7 design, what goes wrong? In the Week 8 design,
what goes wrong?** Be specific about *when* the mistake would be noticed.
In the old design, forgetting a combat guard could allow equip during a fight, and the mistake might go unnoticed until testing or gameplay. In the State design, registering equip alone does not make it available: it must also appear in a state’s allowed verbs. If omitted from combat, the parser refuses it with a message. This makes commands unavailable by default until explicitly permitted.


## D4 — One honest question · 3 pts

`GameContext.syncState()` deliberately does **not** pull the player out of the pack screen
when a monster arrives.

**Is that the right call?** Argue either side in a short paragraph. There is no answer key —
real games disagree about this, and knowing that a design decision is *contested* is worth
more than picking the popular side.
I think the pack should stay open until the player chooses back, because opening it is an intentional mode change. Automatically switching to combat would interrupt the player’s inventory management. Our syncState() preserves InventoryState while the player is alive. After the player closes the pack, synchronization checks the room and enters combat if a living monster is present. Death takes priority over the pack, so a dead player still transitions to GameOverState.


**And anything still unclear:**

