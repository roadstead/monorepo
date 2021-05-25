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
2. create the release branch
3. create the master branch 
4. migrate a single lib to its own repo
4. create a hotfix branch from master
5. merge changes back to develop
### Investigation
- do the back merged changes track easily to the new repo?
- is it easier to create a new monorepo?

### Step 2. - create the release branch
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

To "release", we `lein mono set-versions` which sets the project
versions of the sub-projects to that of the monorepo's project
version. Then we `lein mono update-dependents-all` which will update the
dependencies for each sub-project. Then we are ready for a commit.
