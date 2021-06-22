# Monorepo sub-folder breakout example
## Scenario
Within a single git repository, manage several projects with
interdependencies. This approach is called "monorepo" and has some
advantages, but when the size of the repo grows, and the
responsibility for sub-projects in the monorepo is spread across
multiple teams, it can result in complex dependencies, unnecessary
deployments, and long build times.
## Experiment
This is an experiment to create the conditions which exist in the [E&G
Monorepo](https://github.com/skm-ice/ejendomme-og-grunde.git), and
follows the simplified workflow:
1. create the monorepo on develop branch
2. create the `release` branch
3. create the `master` branch 
4. migrate a single lib to its own repo
4. create a hotfix branch from `master`
5. merge changes back to develop
### Step 2. - create the `release` branch
We are starting with the current project deps:
```
{:dependencies
 {"monorepo/monorepo" #{},
  "lib-b/lib-b" #{},
  "deployable-b/deployable-b" #{"deployable-a/deployable-a"},
  "lib-a/lib-a" #{},
  "deployable-a/deployable-a" #{"lib-a/lib-a" "lib-b/lib-b"}},
 :dependents
 {"deployable-a/deployable-a" #{"deployable-b/deployable-b"},
  "lib-a/lib-a" #{"deployable-a/deployable-a"},
  "lib-b/lib-b" #{"deployable-a/deployable-a"}}}
```

To "release": 
- `lein mono release-version`
- `lein mono set-versions`
- `lein mono update-dependents-all` 

This sets the parent monorepo version to a release version, then sets
the project versions of the sub-projects to that of the monorepo's
project version. Finally, update the dependencies for each
sub-project. Then we are ready to buils and deploy for UAT. The
`release` branches project version should be bumped a minor increment
and returned to SNAPSHOT.

### Investigation
- do the back merged changes track easily to the new repo?
- is it easier to create a new monorepo?

### Step 3. - create the `master` branch
After UAT has accepted the changes on the `release` branch, it is time
to "release" this production. From the `release` branch, create the
`master` branch (or, later create a PR to be merged to
`master`). Immediately create a release version, tag, then bump to
snapshot on `master`, build the tag, and deploy. The SNAPSHOT version
should be the same on both branches (`release` and `master`), although
the release tag is what gets deployed.

### Step 4. - backmerge `master` to `develop`
Immediately after the `master` tag is created (and deployed), the
changes on `master` should be merged back to `develop`. (Note: the
`master` project version is now one snapshot version ahead of
`develop`) Since we may have already begun the next round of
development, this backmerge should not change the project version -
already a vesion higher.

### Step 5. - split out a sub-folder to become its own repository
Using: [Github doc: Splitting a subfolder out into a new repository](https://docs.github.com/en/github/getting-started-with-github/using-git/splitting-a-subfolder-out-into-a-new-repository).
Warning: [Not recommended by git docs](https://git-scm.com/docs/git-filter-branch#_warning)
- `cd ..`
- `git clone https://github.com/roadstead/monorepo.git lib-a`
- `cd lib-a`
- `git filter-branch --prune-empty --subdirectory-filter lib-a develop`
- Create a new repo in github, in this case "monorepo-a"
- `git remote set-url origin https://github.com/roadstead/lib-a.git`
### Step 5. (alternate) - split out multiple sub-folders for a new monorepo
Using: [git-filter-repo](https://github.com/newren/git-filter-repo/)
- `python3 -m pip install --user git-filter-repo`
- `git clone https://github.com/roadstead/monorepo.git monorepo-b`
- `cd monorepo-b`
- `git filter-repo --path lib-b/ --path deployable-b --path project.clj`
- `git remote add origin https://github.com/roadstead/monorepo-b.git`
- `git branch -M develop`
- `git push -u origin develop`

### Step 6. refactor
There are now 3 repo's, all on `develop`, with SNAPSHOT versions. Let's create seperate versioning.
Create a new release version
- `lein mono release-version`
- `lein mono set-versions`
- `lein mono update-dependents-all` 
- `lein mono install-all`
- foo

...

### Step 7. hotfix master branch and backmerge
Checkout the master branch, create hotfix, modify a project we have
migrated out of the monorepo (lib-a) on a snapshot branch, merge this
to master, create a release and publish to origin.

Next, create a sync branch and merge master to develop. What occurs is
that the final release of the lesser version is in conflict - requires
manual fix.

The downside of this is that the changes to lib-a have now been added
back into the cleaned up, and will have to be "killed". 

Strategy to merge changes from master on `monorepo` to develop on `lib-a`:
- `cd ./lib-a`
- `git remote add monorepo ../monorepo`
- `git remote update`
- `git switch -c sync/0.1.0-10`
- `git merge --allow-unrelated-histories monorepo/master`
