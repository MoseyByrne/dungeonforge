# The Adapter / Facade Clinic — Week 12, Part D

Fifteen points. Graded on **measurement and precision**, not prose. The rule from Week 9 still
holds: **a number without the command that produced it earns half marks.**

---

## D1 — Three classes with the same shape · 5 pts 🔎

**This is the highest-value step in the clinic, and it is the most common patterns-interview
question in existence.** You have written all three answers.

**Step 1.** Put these three declarations in a table, from your own repo, with file and line:

```java
class WeaponDecorator        implements ______  { ______ wrapped; }   // Week 9
class LegacyBestiaryAdapter  implements ______  { ______ adaptee; }   // this week
class GameSession                               { ______ ... }        // this week
```
| Class | File and lines | Declaration / held fields |
|---|---|---|
| `WeaponDecorator` | `src/main/java/dungeonforge/items/WeaponDecorator.java`, lines 5–6 | Implements `Weapon`; holds `Weapon wrapped`. |
| `LegacyBestiaryAdapter` | `src/main/java/dungeonforge/legacy/LegacyBestiaryAdapter.java`, lines 10–12 | Implements `MonsterSource`; holds `LegacyBestiary adaptee`. |
| `GameSession` | `src/main/java/dungeonforge/session/GameSession.java`, lines 22–30 | Implements no interface; holds `Player`, `GameWorld`, `EventBus`, `CommandParser`, `CommandHistory`, `GameContext`, and `TranscriptRecorder`. |

**Step 2.** For the first two, answer one question: **is the held type the same as the
implemented type?**

| | implements | holds | same type? |
|---|---|---|---|
| `WeaponDecorator` | `Weapon` | `Weapon` | Yes |
| `LegacyBestiaryAdapter` | `MonsterSource` | `LegacyBestiary` | No |

**Step 3.** That answer is the whole distinction. Write it as one sentence beginning *"A
decorator implements the same interface as the thing it holds, which is why…"*

> A decorator implements the same interface as the thing it holds, which is why decorators can stack, while our adapter accepts a LegacyBestiary and exposes the different MonsterSource interface.

**Step 4 — the consequence, not the definition.** `Flaming(Frost(sword))` is a legal weapon.
Write down what `new LegacyBestiaryAdapter(new LegacyBestiaryAdapter(b))` does.

**Try it. Paste the compiler error.**

```bash
$ mvn test-compile
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
[INFO] Nothing to compile - all classes are up to date.
[INFO]
[INFO] --- resources:3.4.0:testResources (default-testResources) @ dungeonforge ---
[INFO] skip non existing resourceDirectory C:\Users\Administrator\Desktop\CIS 18 Object Oriented Programming\dungeonforge\src\test\resources
[INFO]
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ dungeonforge ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 10 source files with javac [debug release 21] to target\test-classes
[INFO] Some messages have been simplified; recompile with -Xdiags:verbose to get full output
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR :
[INFO] -------------------------------------------------------------
[ERROR] /C:/Users/Administrator/Desktop/CIS 18 Object Oriented Programming/dungeonforge/src/test/java/dungeonforge/AdapterFacadeTest.java:[338,17] incompatible types: dungeonforge.legacy.LegacyBestiaryAdapter cannot be converted to dungeonforge.legacy.LegacyBestiary
[INFO] 1 error
[INFO] -------------------------------------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.578 s
[INFO] Finished at: 2026-11-14T11:23:27-08:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:testCompile (default-testCompile) on project dungeonforge: Compilation failure
[ERROR] /C:/Users/Administrator/Desktop/CIS 18 Object Oriented Programming/dungeonforge/src/test/java/dungeonforge/AdapterFacadeTest.java:[338,17] incompatible types: dungeonforge.legacy.LegacyBestiaryAdapter cannot be converted to dungeonforge.legacy.LegacyBestiary
[ERROR]
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```

**Now say why that error is a property of the pattern and not a limitation of your code.**
>The adapter produces a MonsterSource, but its constructor requires a LegacyBestiary, so another instance of this adapter cannot wrap it. Unlike Decorator, Adapter does not promise that its input and output types match; compatible adapters could still be chained.

**Step 5.** `GameSession` holds eight things and implements nothing. In one sentence, what makes
it a facade rather than "a class with a lot of fields"?
>GameSession is a facade because it gives callers simple start() and runLine() operations while coordinating the subsystem’s construction, subscriptions, and turn order.

---

## D2 — The missing column · 3 pts

**Step 1.** Name the three honest options for a target field with no source. ______
>Derive a value and document the rule, use an explicit documented default, or reject the unsupported conversion or operation.

**Step 2.** You took one. Paste the method.
>I derived XP and recorded the rule in translationNotes(). This excerpt is from definitions():

```java
int xp = Math.max(1, hp / 2 + attack);

notes.add(legacyId
        + ": xp derived from hp / 2 + attack; no XP column.");
```

**Step 3.** Now read Head First p. 249 — `EnumerationIterator.remove()`. **Which of your three
options did Freeman & Robson take, and why was theirs available when yours was not?**
>EnumerationIterator rejects the unsupported remove operation by throwing UnsupportedOperationException because Enumeration provides no removal operation. Removal can remain unsupported while iteration works, but producing a usable MonsterDef requires an XP value; rejecting that conversion would prevent us from importing the monster.


**Step 4.** Try `Arrays.asList("a","b").add("c")`. **Paste the exception.** Which option is that?
```bash
$ jshell
|  Welcome to JShell -- Version 26.0.2.1
|  For an introduction type: /help intro

jshell> java.util.Arrays.asList("a", "b").add("c");
|  Exception java.lang.UnsupportedOperationException
|        at AbstractList.add (AbstractList.java:155)
|        at AbstractList.add (AbstractList.java:113)
|        at (#1:1)

jshell>
```

**Step 5 — the one that is worth the points.** A colleague changes your derivation from
`hp / 2 + attack` to `hp / 3 + attack`. Every test still passes.

**Is that a problem? Answer yes or no and defend it in two sentences.**
>Yes, an unreviewed formula change alters XP rewards even though the structural conversion tests still pass. Those tests verify that derivation is documented, but they do not establish that the numerical rule is correct or balanced.


---

## D3 — The listener nobody subscribed · 4 pts 🔨

**Step 1.** Before US-9.2, how many times did `DamageMeter` appear anywhere outside its own
file? __0__ **Command:**
```bash
git grep -n 'DamageMeter' 3aad4c2 -- src/main/java ':(exclude)src/main/java/dungeonforge/events/DamageMeter.java'
```

**Step 2.** It compiled, it was correct, and it had never run. **Name every mechanism in this
project that could have caught it, and say why each one did not:**

| Mechanism | Why it did not catch this |
|---|---|
| the Java compiler | 	An unused class is legal; the compiler does not require listeners to be subscribed.|
| the 137-test suite |	No test checked that the assembled game subscribed DamageMeter and produced its warning. |
| code review |	The listener’s implementation could look correct while the missing subscription was in another file. |
| the acceptance-criteria harness |	It had no check requiring the assembled game to produce the DamageMeter warning. |

**Step 3 — the experiment.** In your finished build, comment out `bus.subscribe(meter)` in
`GameSession.start()` and run `mvn test`.

**How many tests fail?** ___2___ **Name them.** ___theDamageMeterActuallyFiresNow
theSessionSubscribesTheListenerThatNobodyEverWiredUp___

**Put the line back.**

**Step 4.** You just demonstrated the difference between the old wiring and the new wiring, and
it is **not** that the new one cannot be wrong. State what it actually is, in one sentence.
>The wiring can still be wrong, but it now lives in one place with tests that detect a missing subscription and its missing warning behavior.

**Step 5.** A count of `bus.subscribe` calls is not evidence that a listener works. Which of your
acceptance criteria *is* that evidence, and what makes it different in kind?
>US-9.2’s acceptance criterion requiring DamageMeter to produce a warning is behavioral evidence: the test lowers the player’s HP, publishes DAMAGE_TAKEN, and checks the transcript for DANGER. This proves the listener reacted, whereas a subscription count alone cannot.

---

## D4 — What each pattern cost · 3 pts

**Step 1.** Paste `git diff --stat` for your US-9.1 commit and your US-9.2 commit.

```bash
 src/main/java/dungeonforge/Main.java               |   7 +
 .../java/dungeonforge/factory/MonsterSource.java   |   8 +
 .../dungeonforge/legacy/LegacyBestiaryAdapter.java | 115 ++++++++++++
 src/test/java/dungeonforge/AdapterFacadeTest.java  | 201 +++++++++++++++++++++
 4 files changed, 331 insertions(+)
 src/main/java/dungeonforge/Main.java               | 176 +++++----------------
 src/main/java/dungeonforge/core/GameWorld.java     |  10 +-
 .../java/dungeonforge/session/GameSession.java     | 157 ++++++++++++++++++
 src/test/java/dungeonforge/AdapterFacadeTest.java  | 103 ++++++++++++
 4 files changed, 304 insertions(+), 142 deletions(-)
```

**Step 2.** `GameSession.start()` builds eight objects and a dungeon, every time it is called.
**Write a test that needs only a `Player` and an `EventBus`.** Can you still do it?
>Yes, Player and EventBus can still be constructed directly without generating a dungeon. The facade offers a complete session through start(), but it does not remove access to the smaller subsystem classes.

**What does that tell you about the cost of a front door?**


**Step 3.** The Principle of Least Knowledge says `Main` should not have to know eight classes.
It now knows one. **Name one diagnostic or debugging task that got harder**, specifically.
>Debugging why a warning is missing now requires tracing Main into GameSession.start() to inspect subscriptions, rather than finding the setup directly in Main.


**Step 4 — the sentence to take into the final.** Adapter and Facade are both "a class standing
in front of other classes." In one sentence each, from having written both:

| Pattern | What it is actually for |
|---|---|
| Adapter | Translates an existing incompatible interface into the interface the client expects. |
| Facade | Gives callers a simpler interface for coordinating a subsystem and its required order. |

**And: which of the two did your codebase genuinely need, and which was mostly tidiness?**
Defend the answer with a number from Part 3.
>The facade addressed an existing gameplay defect by reducing two copies of the turn sequence to one. The adapter addressed the deliberately introduced legacy-format requirement; it was useful for importing ten monsters, but was not needed to fix the original game loop.

---

## Turning it in

Commit `docs/adapter-facade-clinic.md` with D1–D4 complete, the compiler error in D1 Step 4
pasted verbatim, the exception in D2 Step 4 pasted verbatim, and `bus.subscribe(meter)` **put
back** in `GameSession`.
