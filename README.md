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
