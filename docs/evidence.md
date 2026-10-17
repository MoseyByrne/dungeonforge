# Week 8 Evidence

## 1. BEFORE — the two problems

### Problem A: mode checks scattered across commands

```bash
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
```

**Paste it. How many command classes re-implement the same check?**
Three command classes: MoveCommand, TakeCommand, and DropCommand. The fourth match, in CommandParser, checks whether to display hostile monsters in the room description; it does not restrict a command.

```bash
$ grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
src/main/java/dungeonforge/commands/CommandParser.java:172:        if (room.hasLivingMonsters()) {
src/main/java/dungeonforge/commands/DropCommand.java:21:        if (room().hasLivingMonsters()) {
src/main/java/dungeonforge/commands/MoveCommand.java:19:        if (room().hasLivingMonsters()) {
src/main/java/dungeonforge/commands/TakeCommand.java:13:        if (room().hasLivingMonsters()) {
```

**Play a turn: start a fight, then type `north`. Paste what happens:**

```bash

[HP 78/80] > north

  You cannot walk away with enemies still standing.
```

**Now type `help` during that fight. Does it list `north`?** __Yes__
**Should it?**
No. Since movement is blocked during combat, help should only list commands available in the current mode.

### Problem B: the monster lifecycle as a string plus conditionals

```bash
grep -n "getStateName" src/main/java/dungeonforge/core/Combat.java
```

**Paste it. Which three methods branch on the state name?**
updateMonsterState() determines transitions, monsterActs() checks whether the monster can act, and damageFrom() checks whether it receives the enraged damage bonus.

```bash
$ grep -n "getStateName" src/main/java/dungeonforge/core/Combat.java
65:        String current = m.getStateName();
109:        String state = m.getStateName();
147:        if (m.getStateName().equals("enraged")) {
```

**Answer before you refactor:**

1. To add a "stunned" state, which methods must you edit?
   I would need to edit monsterActs() to prevent a stunned monster from acting and updateMonsterState() to make it recover after one turn. Because the starter transitions before acting, I would also adjust the ordering in monsterTurns() so the monster does not recover before it skips its action.

2. Reading `updateMonsterState()`, which states can follow `enraged`? How long did it take you
   to be sure?
   An enraged monster can become fleeing if its health falls below the flee threshold, or dead if it is no longer alive. Otherwise, it stays enraged.

3. What stops `setStateName("dancing")` from compiling?
   Nothing. The method accepts any String, so "dancing" compiles even though the game has no behavior defined for that state.

## 2. AFTER — US-6.1 and US-6.2

**Paste `help` in Exploring and in Combat, side by side:**

```

```

**Paste an attempt to move during combat:**

```

```

**Paste the grep proving the guards are gone:**

```bash
grep -rn "hasLivingMonsters" src/main/java/dungeonforge/commands/
```

```

```

**Paste entering and leaving the pack, and the prompt in each mode:**

```

```

## 3. AFTER — US-6.3

**Paste the grep proving `Combat` no longer knows any state name:**

```bash
grep -n "getStateName\|\"enraged\"\|\"idle\"" src/main/java/dungeonforge/core/Combat.java
```

```

```

**Paste a fight showing a monster moving through its lifecycle:**

```

```

## 4. AFTER — US-6.4

**Paste the test output (or a demo) showing a healer that becomes aggressive when enraged:**

```

```

**In one sentence: which one changed the other, and could it have gone the other way?**


## 5. Tests and CI

**`mvn test` summary:**

```

```

**Green CI URL:**

## 6. Sprint review — one sentence

> What can the project do now that it could not do last week?

