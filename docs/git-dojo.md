# Git Dojo — my recovery notes

> Part D of Lab 2. For each drill: the command(s) you ran, **one sentence in your own
> words** on what it did, and one on when you would reach for it again.
>
> Graded on the sentences, not the commands. Commands can be copied; understanding cannot.

## The three trees — in my own words

| Tree | What lives here |
|---|---|
| Working Directory |  |
| Staging Area (Index) |  |
| HEAD |  |

---

## Drill 1 — Committed to `main` by accident

**Commands I ran:**
```bash
git switch main
echo "oops" > accident.txt
git add accident.txt
git commit -m "feat: work that should have been on a branch"
git switch -c fix/rescued-work 
git switch main
git reset --hard origin/main 
```
**What it did:** It created a new branch that saved my accidental commit, then reset main to match origin/main.

**When I would use it again:** I would use it when I accidentally commit work directly to main instead of creating a separate branch.

---

## Drill 2 — Wrong commit message / forgot a file

**Commands I ran:**
```bash
echo "x" > note.txt && git add note.txt && git commit -m "asdf"
git commit --amend -m "docs: add note file"
```
**What it did:** : It used git commit --amend to change the most recent commit message or add a forgotten file to that commit.

**Why you must not do this to a commit you already pushed:**

---

## Drill 3 — Committed a file that should be ignored

**Commands I ran:**
```bash
mkdir -p target && echo "junk" > target/Main.class
git add -f target/Main.class && git commit -m "chore: oops, committed build output"
git rm -r --cached target 
echo "target/" >> .gitignore
git add .gitignore && git commit -m 
```
**What it did:** It stopped Git from tracking the target folder without deleting my local files, then added the folder to .gitignore.

**Why adding it to `.gitignore` alone was not enough:** Amending creates a new commit with a different ID, so amending a pushed commit rewrites shared history and can cause conflicts for anyone who already pulled the original commit.

---

## Drill 4 — Merge conflict

**Commands I ran:**
```bash
git switch main
git switch -c feature/a
printf '# DungeonForge - branch A title\n' > README.md
git commit -am "docs: title from branch A"
git switch main
git switch -c feature/b
printf '# DungeonForge - branch B title\n' > README.md
git commit -am "docs: title from branch B"
git switch main
git merge feature/a 
git merge feature/b 
```
**In the conflict markers, which side was "mine"?** The `HEAD` side containing “# DungeonForge - branch A title” was mine because I was on `main`, which already had the changes from `feature/a`.

**What it did:** It created a conflict by changing the same line differently on two branches. I resolved it by choosing the content I wanted, deleting the conflict markers, and committing the result.

**How I would back out of a merge I regretted starting:**  Run `git merge --abort`.

---

## Drill 5 — "I destroyed everything"

**Commands I ran:**
```bash
git log --oneline
git reset --hard HEAD~3 
git log --oneline 
git reflog 
git reset --hard 186f9c9
```
**What `git reflog` showed me:** It showed a history of where `HEAD` had previously pointed, including the commit from before the hard reset, so I could find and restore the lost commits.

**One sentence on why this changes how nervous I should be about Git:** Knowing that `git reflog` can help me recover commits means I dont have to panic when I accidentally change Git history.

---

## Stretch — Drill 6 (detached HEAD, interactive rebase)

**Notes:** A detached HEAD means I am viewing a specific commit instead of working on a branch. If I want to keep changes made there, I should create a new branch. Interactive rebase lets me reorganize, rename, or combine commits, but I should only use it on commits I have not shared or pushed.

---

## The one command I want to remember from today
`git reflog` — because t shows where `HEAD` previously pointed and can help me recover work I thought Id lost.

