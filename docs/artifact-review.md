# Artifact Review Clinic — Lab 2, Part C

> **This is the only document you write this week.** Everything else — the epics, the
> stories, the acceptance criteria, the Definition of Done, the sprint plans — was written
> for you.
>
> Reading critically is a harder and more useful skill than writing from a blank page, and it
> is the one that will make your own stories good when you start writing them in Week 6.

Read all three before answering:
- `docs/backlog.md`
- `docs/definition-of-done.md`
- `docs/sprint-01-plan.md`

---

## C1 — Find the three planted flaws · 12 pts

There is **exactly one deliberate defect in each of the three documents**: one bad user
story, one unverifiable Definition-of-Done criterion, and one sprint-plan item that isn't
what it claims to be.

> **Hint for the story:** re-read INVEST first. The bad one fails more than one letter.
>
> **Hint for the DoD:** ask of every checkbox — *could two reasonable people disagree about
> whether this is true?* If yes, it isn't a criterion. It's an opinion.

### Flaw 1 — in `docs/backlog.md`

**Which item:** US 1.4

**What's wrong with it:**
**Which INVEST letter(s) it violates, and how:**
I - yes.
N - no, because it's telling the dev how to do it.
V - subjectable.
E - yes.
S - yes.
T - "professional" is subjectable.

**My repaired version:**
the story should be removed.

```
As a ...,
I want ...,
so that ...

Acceptance Criteria
- Given ..., when ..., then ...
- Given ..., when ..., then ...
```

---

### Flaw 2 — in `docs/definition-of-done.md`

**Which checkbox:** "The code is well written and easy for others to understand"

**Why it can't actually be checked:** A machine couldn't check it off. It's an opinion. 

**My replacement, phrased so that it can be:** “The code passes the required style checker with zero errors or warnings, and every class and method has a descriptive name.”

---

### Flaw 3 — in `docs/sprint-01-plan.md`

**Which item:** I might get busy this week.

**Why it isn't really what the document calls it:** It is too vague and doesnt explain what could cause the problem or how it would affect the project.

**My repaired version, including a mitigation someone could actually act on:** If school or work responsibilities reduce the time available for this project, I may miss the deadline. To reduce this risk, I will schedule two project work sessions now and begin the assignment early.

---

## C2 — Say what's good, and why · 9 pts

Pick the **three strongest user stories** in `docs/backlog.md`. For each, two or three
sentences.

> Praise is harder than criticism, and it's where most of the learning is. "It's clear" earns
> nothing. "Its third criterion names an observable output — the same object reference — so
> two people would always agree whether it passed" earns full marks.

### Strong story 1: Minimal program that compiles and passes tests

**INVEST letters it satisfies especially well:** Small, Valuable, and Testable.

**What specifically makes its acceptance criteria checkable:** The program can be compiled, and the automated test suite can be run to confirm that every test passes.

### Strong story 2: Tunable numbers stored in one configuration file

**INVEST letters it satisfies especially well:** Valuable, Estimable, and Testable.

**What specifically makes its acceptance criteria checkable:** A reviewer can confirm that all tunable numbers are in the configuration file and that changing them changes the game without recompiling it.

### Strong story 3: One seeded source of randomness

**INVEST letters it satisfies especially well:** Small, Valuable, and Testable.

**What specifically makes its acceptance criteria checkable:** The program can be run more than once with the same seed to confirm that it produces the same results each time.

---

## C3 — Trace a story to code · 4 pts

Take **US-1.1** (settings live in one place). **Write no Java.** In plain English, describe
what you'd expect to see in the pull-request diff when this story is done, and which
acceptance criterion each piece satisfies.

| What I'd expect in the diff | Which acceptance criterion it satisfies |
|-----------------------------|-----------------------------------------|
| config.json                 | AC1                                     |
| Config class                | AC3                                     |
| GameWorld                   | AC2                                     |
| Main class                  | AC1                                     |

**One sentence: how did the acceptance criteria help you predict the shape of the work?**

---

## C4 — The bonus catch · up to +3 bonus

Once you have dealt with the bad story, something in `docs/sprint-01-plan.md` no longer adds
up the way it did.

**What is it:**

**What a real team would do about it in sprint planning:**

**What this suggests about the relationship between vague work and over-committed sprints:**

---

## C5 — One honest question

What is one thing about the Scrum process you still don't understand after this week? A good
question here is worth more to me than a confident wrong answer.

Do you know of a good scrum checklist for someone doing scrum on their own? 