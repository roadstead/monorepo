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
### Investigation
- do the back merged changes track easily to the new repo?
- is it easier to create a new monorepo?

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
From the existing project folder, cd to a 

