# The Iterator / Composite Clinic — Week 13, Part D

Ten points. Graded on **measurement and precision**, not prose. The rule from Week 9 still
holds: **a number without the command that produced it earns half marks.**

---

## D1 — The class that was right and then was wrong · 3 pts 🔎

**Step 1.** `git log --oneline` your own branches. Find the commit where you created
`RoomContentsIterator` and the commit where you deleted it. **How many commits apart?** ___Two implementation commits apart: created in 9c050e0 and deleted in f40f3fa, with c9bdaf0 between them. This excludes merge commits.___

**Step 2.** Nothing was wrong with that class. Write down, in one sentence, **what changed about
the data** that made a correct class the wrong class.
>The data changed from two separate collections of items into a tree of items and containers that could contain other containers.


**Step 3.** Run US-10.1's tests against the finished build.

**Command:** ______
```bash
mvn '-Dtest=IteratorCompositeTest#removeBeforeNextIsAnErrorRatherThanAGuess+takeCommandHasExactlyOneSearchPath+countingLootNoLongerForgetsTheFloor' test
```

**Result:** ___Three tests passed, with zero failures, errors, or skips.___

**Step 4 — the one worth the points.** Those tests were written against a class that no longer
exists, and they never changed. **What were they asserting?**

> These tests asserted observable contracts—legal removal, one search path, and counting loose loot—rather than the identity of the iterator class. Some other tests were updated for the new storage structure, so I would not claim the entire original test file stayed unchanged.

**Step 5.** Write one sentence a future you could use as a rule, beginning *"A test should assert
…"*.
>A test should assert the behavior callers depend on, so an implementation can be replaced without breaking the test when that behavior stays the same.

---

## D2 — Transparency versus safety · 3 pts

**Step 1.** Paste the declaration of `children()` from your `Item`.

```java
default Iterator<Item> children() {
    return Collections.emptyIterator();
}
```

**Step 2.** Head First p. 364 puts `add()` on the Component. Write the two-line version of what
`Potion.add(x)` would have to do in that design.

```java
public void add(Item item) {
    throw new UnsupportedOperationException(); }
```

**Step 3.** Fill this in:

| | Our version | Head First’s |
|---|---|---|
| `item.add(thing)` compiles? | No; `Item` does not declare `add()`. | Yes; Component declares `add()`. |
| `potion.add(thing)` throws at run time? | Not applicable; it does not compile. | Yes; the leaf rejects the operation. |
| A caller putting something away needs | A container-specific type, such as `Chest`. | The common Component type, with possible runtime rejection. |

**Step 4.** In Week 12 you met the same trade twice — `Arrays.asList(...).add()` and
`EnumerationIterator.remove()`. **Which column were those in?** ___Head First’s transparency column: the interface exposes the operation, but an implementation can reject it with UnsupportedOperationException.___

**Step 5 — the one worth the points.** You are shipping this as a library. A thousand strangers
will write against `Item`. **Which column do you pick, and does your answer change?** Two
sentences, and the second one has to name what changed.
>I would keep our safety approach and expose adding through a separate container interface. My choice stays the same, but the audience changed from our own callers to a thousand independent users, making compile-time guidance more valuable.

---

## D3 — Why `remove()` could not throw · 2 pts 🔨

**Step 1.** Head First's composite iterator throws on `remove()` (p. 368). Paste your
`CompositeIterator.remove()`.

```java
@Override
public void remove() {
    if (lastUsed == null) {
        throw new IllegalStateException(
                "Call next() before each remove()");
    }

    // Removing a box must also stop traversal into that box.
    if (descendedInto != null && stack.peek() == descendedInto) {
        stack.pop();
    }

    lastUsed.remove();
    lastUsed = null;
    descendedInto = null;
}
```

**Step 2 — the experiment.** Replace its body with
`throw new UnsupportedOperationException();` and run `mvn test`.

**How many tests fail?** ______ **Name two.** ______
>Eleven tests were unsuccessful: Maven reported one assertion failure and ten errors. Two examples are removeBeforeNextIsAnErrorRatherThanAGuess and removeTakesTheItemOutOfWhicheverCollectionItWasIn.

**Put it back.**

**Step 3.** `TakeCommand` is the caller that forced this. **If `remove()` threw, what would
`TakeCommand` have to do instead?** One sentence.
>TakeCommand would have to discover which collection owned the selected item and remove it from that collection, exposing the storage details the iterator was introduced to hide.


**Step 4.** That answer is the exact thing the iterator was introduced to stop. Write the rule in
one sentence, beginning *"Whether an iterator supports removal is a question about …"*.
>Whether an iterator supports removal is a question about what its callers need to accomplish without knowing the underlying storage structure.

---

## D4 — The two walks and the leaves · 2 pts

**Step 1.**

| | walks                                                      | one example of something it must find                |
|---|------------------------------------------------------------|------------------------------------------------------|
| `take <name>` | The complete tree, depth-first and parent before children. | A potion inside a nested chest, or the chest itself. |
| `loot` | Only the room’s top-level items.                           | A chest that moves together with its contents.       |

**Step 2.** Change `loot` to walk the tree. Run it in a room with a chest holding two items.

**What happens, and why?** ______
>Loot took the chest, then printed “There is nothing like that here” five times. Walking the tree queued descendants as separate targets even though taking the chest had already moved them out of the room; the output alone does not explain why there were exactly five failed attempts. All 194 tests still passed during this experiment, so the suite did not catch the extra refusal messages.

**Put it back.**

**Step 3.** `git diff --stat` the whole sprint.

**How many Leaf classes did you change?** ______ **How many lines of `Potion.java`?** ______
>Leaf classes changed: 0. Lines changed in Potion.java: 0. The targeted comparison produced no output.

**One sentence on what those numbers say.**
>Those zeros show that containers and recursive traversal were added while existing individual items continued to work through their shared Item interface.

**Step 4 — the sentence to take into the final.**

| Pattern | What it is actually for |
|---|---|
| Iterator | Giving callers a consistent way to traverse elements without knowing how they are stored. |
| Composite | Letting individual objects and groups share operations, with containers applying those operations to their children. |

**And: which of the two did your codebase genuinely need this week, and which was mostly setup
for the other? Defend it with a number from Part 3.**
>Composite supplied the missing ability to treat a chest as an item that could be carried with its contents; Iterator prepared the callers to work with that structure. The initial iterator refactor reduced the files using the separate chest accessors from six to four, before the composite walk replaced the two-collection model.


---

## Turning it in

Commit `docs/iterator-composite-clinic.md` with D1–D4 complete, the failure list in D3 Step 2
pasted verbatim, and **both experiments put back** — `CompositeIterator.remove()` working and
`loot` walking the top level.
