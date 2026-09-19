# The "Which Factory?" Clinic — Lab 4, Part D

> Week 3's hard part was refusing a pattern. **This week's hard part is telling three very
> similar patterns apart.** Students who leave Week 4 unable to distinguish them will misuse
> all three for the rest of the semester — and Exam 1 will ask.

## D1 — The experiment: what does a fourth theme cost? · 8 pts

The Abstract Factory's whole claim is *"adding a new family is cheap and touches nothing
else."* Claims like that should be measured, not believed.

**Add a fourth theme.** Anything you like — Fungal, Drowned, Clockwork. It needs a kit class,
a couple of monster blueprints in `monsters.json`, and loot.

Before you start, **commit your current work** so `git diff --stat` is meaningful.

| Question | Your answer                              |
|---|------------------------------------------|
| How many **new** files did you create? | 1 `WinterfellThemeKit.java`              |
| How many **existing** files did you modify? | 2                                        |
| Which existing files? | `ThemeRegistry.java` and `monsters.json` |
| Did `GameWorld.java` change? | No                                       |
| Did any `RoomPopulator` subclass change? | No                                       |
| Did `Monster`, `Room`, or `DungeonLevel` change? | No                                       |

**Paste the output of `git diff --stat`:**

```bash
$ git diff HEAD --ignore-space-at-eol --stat -- src/main/java/dungeonforge/factory/ThemeRegistry.java src/main/java/dungeonforge/factory/WinterfellThemeKit.java src/main/resources/data/monsters.json
 .../java/dungeonforge/factory/ThemeRegistry.java   |  1 +
 .../dungeonforge/factory/WinterfellThemeKit.java   | 26 +++++++++++-----------
 src/main/resources/data/config.json                |  2 +-
 src/main/resources/data/monsters.json              |  6 ++-
 4 files changed, 19 insertions(+), 15 deletion(-)
```

**In two or three sentences: what does that number tell you about the Open/Closed
Principle — "open for extension, closed for modification"? Was it satisfied, and how do you
know from evidence rather than from a definition?**

Mostly, yes. I added a new kit and monster data without changing `GameWorld` or any room populators. I still had to add one line to `ThemeRegistry`, so the registry itself was not closed to changes.

> Set `dungeonDepth` to 4 in `config.json` and run it, so you can see your fourth theme.
> Then set it back to 3 before you open the PR.

## D2 — Classification · 12 pts

For each scenario: which of the three applies? Answer **Simple Factory**, **Factory Method**,
**Abstract Factory**, or **none of them** — and give a one-sentence reason.

| # | Scenario | Which? | Why |
|---|---|---|---|
| 1 | One place in the code turns a monster id string into a `Monster`, so `new Monster` appears once | Simple Factory | One factory handles monster creation from an id. |
| 2 | A boss room, a treasure room and an ordinary room each fill themselves differently, but always in the same order: prose, then monsters, then a chest | Factory Method | Each room populator chooses what to create while `populate()` keeps the same order. |
| 3 | An ice level must contain ice monsters AND ice loot AND ice prose, never a mix | Abstract Factory | One ice kit provides the matching monster, loot, and prose. |
| 4 | Week 9: a weapon can be made flaming, then vampiric, then blessed, in any combination | None of them | This sounds like the Decorator pattern, which adds effects in layers. |
| 5 | Week 12: save files must be written as JSON now and possibly as XML later, with matched reader and writer | Abstract Factory | Each format needs a matching reader and writer. |
| 6 | A method returns a `Player` object, built from the name typed at startup | None of them | Making one `Player` from a name is just ordinary object creation. |

> Scenarios 4 and 6 are traps. One is a different pattern entirely; the other is not a pattern
> at all. Say so if you think so — "none of them" is a correct answer to at least one row.

## D3 — The distinction, in your own words · 5 pts

**Simple Factory is not one of the Gang of Four patterns.** Your textbook says so explicitly
before it teaches Factory Method.

**In three or four sentences: what can Factory Method do that Simple Factory cannot?** Do not
define either one. Describe a change someone might ask you to make, and explain why it would
be easy with one and awkward with the other.

If I needed a new room type, I could add another `RoomPopulator` subclass and let it choose its encounter. The shared `populate()` steps would still run in the same order. With one Simple Factory, I would have to keep adding cases to that factory whenever a new room type came along.

## D4 — One honest question

What is still blurry about these three patterns? A specific confusion is worth more to me
than a confident summary.

I still get a little confused about where `MonsterFactory` ends and a `ThemeKit` begins. Since both help create monsters, how do I decide which one should handle a new requirement?
