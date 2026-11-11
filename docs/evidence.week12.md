# Week 12 Evidence

Fill this in as you go. Parts 1–3 and Part 5 are graded on the **measurement**, not on the code.

**Every counting command here pipes through `scripts/codeonly.py`.** The reason is in that
file's header: a bare grep for `ctx.syncState()` finds the javadoc that *mentions*
`ctx.syncState()`. Grep the code, not the commentary.

---

## 0. The Sprint 8 carry-over (US-8.5)

```bash
grep -rn 'MONSTER_SATE_CHANGED' src/main/java | wc -l
```

**How many files share the misspelling?** ___3___

**It works perfectly. In one sentence: what does it cost, and when?**
>The shared misspelling works internally, but it costs debugging time when another developer or integration uses the correctly spelled event name and cannot find it.

**Now the other one.** Before changing anything, write a test asserting
`EnchantmentRegistry.isKnown("FLAMING")`. Run it.

**Paste the failure.**

```bash
$ mvn -Dtest=QuietDefectsTest test
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------< edu.redwoods.cis18:dungeonforge >-------------------
[INFO] Building DungeonForge 0.12.0
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- resources:3.4.0:resources (default-resources) @ dungeonforge ---
[INFO] Copying 4 resources from src\main\resources to target\classes
[INFO]
[INFO] --- compiler:3.13.0:compile (default-compile) @ dungeonforge ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 91 source files with javac [debug release 21] to target\classes
[INFO]
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory C:\Users\Administrator\Desktop\CIS 18 Object Oriented Programming\dungeonforge\src\test\resources
[INFO]
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 9 source files with javac [debug release 21] to target\test-classes
[INFO]
[INFO] --- surefire:3.2.5:test (default-test) @ dungeonforge ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running dungeonforge.QuietDefectsTest
[ERROR] Tests run: 2, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.047 s <<< FAILURE! -- in dungeonforge.QuietDefectsTest
[ERROR] dungeonforge.QuietDefectsTest.correctlySpelledMonsterStateEventExists -- Time elapsed: 0.023 s <<< FAILURE!
org.opentest4j.AssertionFailedError: Unexpected exception thrown: java.lang.IllegalArgumentException: No enum constant dungeonforge.events.EventType.MONSTER_STATE_CHANGED
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:152)
        at org.junit.jupiter.api.AssertDoesNotThrow.createAssertionFailedError(AssertDoesNotThrow.java:84)
        at org.junit.jupiter.api.AssertDoesNotThrow.assertDoesNotThrow(AssertDoesNotThrow.java:75)
        at org.junit.jupiter.api.AssertDoesNotThrow.assertDoesNotThrow(AssertDoesNotThrow.java:58)
        at org.junit.jupiter.api.Assertions.assertDoesNotThrow(Assertions.java:3228)
        at dungeonforge.QuietDefectsTest.correctlySpelledMonsterStateEventExists(QuietDefectsTest.java:18)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1612)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1612)
Caused by: java.lang.IllegalArgumentException: No enum constant dungeonforge.events.EventType.MONSTER_STATE_CHANGED
        at java.base/java.lang.Enum.valueOf(Enum.java:293)
        at dungeonforge.events.EventType.valueOf(EventType.java:4)
        at dungeonforge.QuietDefectsTest.lambda$correctlySpelledMonsterStateEventExists$0(QuietDefectsTest.java:19)
        at org.junit.jupiter.api.AssertDoesNotThrow.assertDoesNotThrow(AssertDoesNotThrow.java:71)
        ... 6 more

[ERROR] dungeonforge.QuietDefectsTest.uppercaseEnchantmentIsRecognised -- Time elapsed: 0.006 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <true> but was: <false>
        at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
        at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
        at org.junit.jupiter.api.AssertTrue.failNotTrue(AssertTrue.java:63)
        at org.junit.jupiter.api.AssertTrue.assertTrue(AssertTrue.java:36)
        at org.junit.jupiter.api.AssertTrue.assertTrue(AssertTrue.java:31)
        at org.junit.jupiter.api.Assertions.assertTrue(Assertions.java:183)
        at dungeonforge.QuietDefectsTest.uppercaseEnchantmentIsRecognised(QuietDefectsTest.java:13)
        at java.base/java.lang.reflect.Method.invoke(Method.java:565)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1612)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1612)

[INFO]
[INFO] Results:
[INFO]
[ERROR] Failures:
[ERROR]   QuietDefectsTest.correctlySpelledMonsterStateEventExists:18 Unexpected exception thrown: java.lang.IllegalArgumentException: No enum constant dungeonforge.events.EventType.MONSTER_STATE_CHANGED
[ERROR]   QuietDefectsTest.uppercaseEnchantmentIsRecognised:13 expected: <true> but was: <false>
[INFO]
[ERROR] Tests run: 2, Failures: 2, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.192 s
[INFO] Finished at: 2026-11-10T15:58:16-08:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.2.5:test (default-test) on project dungeonforge: There are test failures.
[ERROR]
[ERROR] Please refer to C:\Users\Administrator\Desktop\CIS 18 Object Oriented Programming\dungeonforge\target\surefire-reports for the individual test results.
[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```
>After both fixes:
Tests run: 142, Failures: 0, Errors: 0, Skipped: 0.
The misspelled event name has zero matches in src/main/java.
MONSTER_STATE_CHANGED appears in Combat.java, CombatLog.java,
and EventType.java.

**One sentence: 137 tests passed and this did not work. What were those 137 tests about?**
>The 137 passing tests checked the behaviors and inputs already covered, but none required uppercase enchantment names to work or the monster-state event name to be spelled correctly.

---

## 1. BEFORE — count the listeners

```bash
ls src/main/java/dungeonforge/events/*.java | xargs grep -l 'implements GameEventListener' | wc -l
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'bus.subscribe('
```

**Listener classes in `events/`:** ___5___  **`bus.subscribe` calls in `Main`:** ___4___

**Those two numbers are equal and that is not reassuring. Why not?** *(Look at what the fourth
subscription actually subscribes, and where that class lives.)*
>They are not equal in my project: there are five listener classes and four subscriptions. The fourth subscription is Main’s ConsolePrinter, so only three of the five listener classes in events/ are subscribed.


**Which listener class is never subscribed?** DamageMeter and DangerMeter.

**Open it. What does it do, and when was it written?**
>DamageMeter issues a one-time warning when a DAMAGE_TAKEN event occurs and the player’s HP is at or below its configured threshold. DangerMeter warns whenever that event occurs with HP below its threshold. Both existed before this sprint; their source files do not identify when they were originally written.


**Now the sentence. Put both counts and the consequence in one sentence.** *(This is the most
important sentence you will write this week. Two points.)*

> Five listener classes exist in events/, but Main makes only four subscriptions, one of which is ConsolePrinter, leaving both health-warning meters disconnected.

**Nobody broke it. In one sentence: what kind of mistake is this, if it is not a bug?**
>It is an assembly omission: the listener code exists, but nothing connects it to the event bus, so its behavior never runs.


---

## 2. BEFORE — the two games

```bash
printf 'north\nlook\nnorth\nlook\n' > demo.txt
mvn -q exec:java -Dexec.args="--script=demo.txt"
```

**Paste the scripted output:**

```bash
$ printf 'north\nlook\nnorth\nlook\n' > demo.txt
mvn -q exec:java -Dexec.args="--script=demo.txt"
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.12.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north

[script] > north
  You go north.
  Ghoul is now hunting.

[script] > look
  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Hostile: Ghoul (18/18 HP, ATK 2, hunting)
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[script] > north
  You go north.

[script] > look
  [L1R2] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Exits: south, north

  Delver  HP 80/80  ATK 10  DEF 3  Gold 0  XP 0  Carry 60.0kg  (unarmed)
  Actions this session: 4   |   undo stack depth: 2
```

Now run `mvn -q exec:java` and type the same four commands by hand.

**Paste that output:**

```bash
$ mvn -q exec:java
=========================================
        D U N G E O N F O R G E
  A Head First Design Patterns project
=========================================
  version 0.12.0   seed 20260818

  Type 'help' for what you can do. You are carrying an hourglass -- 'use hourglass' rewinds a turn.
  [L1R0] Level 1: Crypt
  "Burial niches line the walls. Most are empty. Most."
  Exits: north

[Exploring | HP 80/80] > n

  You go north.
  Ghoul is now hunting.
  Combat! 1 hostile(s). Try 'attack'.

[Combat | HP 80/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Hostile: Ghoul (18/18 HP, ATK 2, hunting)
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[Combat | HP 80/80] > north

  You cannot do that in Combat mode.
    Available here: attack, look, use, equip, inventory, status, history, undo, help, quit

[Combat | HP 80/80] > look

  [L1R1] Level 1: Crypt
  "Damp stone. Something drips in the dark, patiently."
  Hostile: Ghoul (18/18 HP, ATK 2, hunting)
  Iron-bound Chest:
    - Vial of Still Water [heals 21] (0.4kg, 25g)
    - Vampiric Bone Shortsword [dmg 6] [drain 1] (2.0kg, 140g
    - Grave Shroud [def 3] (3.0kg, 43g)
  Exits: south, north

[Combat | HP 80/80] > quit

  You lay down your pack.
  The room falls quiet.
  === YOU leave the dungeon behind === 'quests' for your record, or 'quit'.

  Delver  HP 80/80  ATK 10  DEF 3  Gold 0  XP 0  Carry 60.0kg  (unarmed)
  Actions this session: 5   |   undo stack depth: 1
```

**They differ. Name the file, the method and the line that is the difference.**
>In src/main/java/dungeonforge/Main.java, gameLoop() calls ctx.syncState() on line 153 after each command. runScript(), beginning on line 164, omits that step, so scripted commands do not update the game mode after each turn.

**Put the two turn sequences side by side:**

| | the turn |
|---|---|
| `gameLoop()` | 	Parse → execute → push to history → sync state|
| `runScript()` |	Parse → execute → push to history; missing state synchronization |

**What can a script do that a player cannot?**
>A script can move north out of a room containing a live hostile, while interactive play refuses that movement in Combat mode.

**`runScript`'s javadoc has been promising something since Week 7. Quote it, and say whether it
is true.**
>"a recorded session is just a list of strings, and replaying it uses the SAME three lines as interactive play.” Those three lines match, but the complete turn sequence does not: the script omits state synchronization, so it does not reproduce interactive behavior.

---

## 3. BEFORE — the counting

```bash
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'ctx.syncState()'
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'history.push('
python3 scripts/codeonly.py src/main/java/dungeonforge/Main.java | grep -c 'DamageMeter'
awk '/public static void main/{on=1} on{n++} on&&/^    }$/&&n>3{print n; exit}' src/main/java/dungeonforge/Main.java
```

|                                              | Before                     | After |
|----------------------------------------------|----------------------------|---|
| `main()` lines                               | 79                         | |
| `ctx.syncState()` in `Main`                  | 3                          | |
| copies of the turn sequence                  | 2                          | |
| `DamageMeter` mentioned outside its own file | Pending project-wide check | |
| listener classes subscribed, of 5            | 3/5                        | |
| monster blueprints available                 |  Pending count                          | |
| a script can walk past a live hostile        |    Pending demonstration                        | |

*(Fill the After column at the end of the sprint. **Nothing in it may be a guess.**)*

---

## 4. AFTER — the two receipts

### US-9.1, the Adapter

```bash
git diff --stat HEAD~1          # on the us-9.1 commit
diff src/main/java/dungeonforge/legacy/LegacyBestiary.java  <the file you were given>
diff src/main/java/dungeonforge/factory/MonsterFactory.java <the file you started with>
python3 scripts/codeonly.py src/main/java/dungeonforge/factory/MonsterFactory.java \
  | grep -ciE 'bestiary|csv|MOB_ID|HIT_DICE'
```

**Paste all four.**

```

```

**Two of those commands are the deliverable. Which two, and why?**


**`blueprintCount()` before:** ______ **after:** ______

### US-9.2, the Facade

```bash
git diff --stat HEAD~1          # on the us-9.2 commit
```

**Paste it.**

```

```

**One sentence each on what the two numbers mean.** *(They mean different things. One is about a
format; one is about an order.)*


---

## 5. AFTER — what you did not have to change

**List every file that learned a CSV exists.** ______

**Is `MonsterFactory` one of them?** ______

**`ThemeKit` picks which monsters appear in a room. How many lines of it did you change to make
ten new monsters appear?** ______

**In two or three sentences: explain how a class can use a blueprint that arrived from a file
format it has never heard of.**


---

## 6. AFTER — the honest accounting

### The Adapter's honest half

**`bestiary.csv` has no xp column. Which of the three options did you take?** ______

**Where in your code does a reader find out that the number is derived?** ______

**One sentence: what would have gone wrong if you had silently defaulted it to 5?**


**`SAPPER` has no counterpart. What did you do, and what happens when the eleventh unknown AI
name arrives next semester?**


### The Facade's honest half

**Complete this honestly:**

> `GameSession` now knows about eight collaborators and the order they go together in, which
> means the coupling ______________________________________________

**Name one thing that got HARDER because `Main` no longer knows those eight classes.** *(There
is a real answer. Think about what a test needs.)*


---

## 7. Retro line

**One sentence on something each pattern gave you, and one on something each one took away.**
Four sentences, and the two costs are not the same cost.

| Pattern | Gave | Took |
|---|---|---|
| Adapter | | |
| Facade | | |

**And the one-sentence distinction, in your own words, having now written both:** Adapter and
Facade are both "a class in front of other classes." What made them different in practice?


