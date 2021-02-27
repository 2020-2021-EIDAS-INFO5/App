# Contributing guidleines to the eIDAS project

This is our end-of-year academic project, in order to organize everyone's contribution to this project we have decided to put a set of rules for:

1. [Commit messages](#commit)
2. [Branches](#branches)
3. [Merge](#merge)

## Contributors <a name="contributors"></a>

#### Project owners
* [Dima ASSI](https://github.com/dimaassi22)
* [Aleck BILOUNGA](https://github.com/Hyorick)
* [Houda EL AJI](https://github.com/Elajih)
* [Otba ZERAMDINI](https://github.com/OtbaZ)



#### Teachers
* [Nicolas PALIX](https://github.com/npalix)
* [Didier DONSEZ](https://github.com/donsez)

## Commit messages <a name="commit"></a>

Start your message with `#number of associated ticket`, then `[tags form tags below]`, and finaly add your description.

So the general format of the commit is :

```
<Ticket number> <tag>: <description>
If your change is too small, you can skip this part. Because the subject line is enough to describe your change.

    The body should be separated from the subject line with a blank line.
    The body should briefly summarize the changes you have made, explaining what those changes were, why.
[optional body]

[optional footer(s)]
```
#### Tag
At the beginning of each commit name, please add one of these tags:
* `[FEAT]`: for a new feature
* `[CPTN]`: for a new component
* `[BUILD]`: for changes that affect the build system or external dependencies
* `[BUG]`: for reporting a bug
* `[FIX]`: for error correction
* `[DOC]`: for writing or updating documentation
* `[CLEAN]`: for refactoring the code
* `[TEST]`: for adding or rewriting tests
* `[COMMENT]`: for adding comment
* `[Update]`: for Updating a method, function or any code block.
* `[WIP]`: stand for Work In Progress. Use it to make a save of your work, or if no other flag match with your commit

#### Description
* use imperative, present tense: "Update" not "Updates" nor "Updating"
* summarize what you did and the changes to the commit.
* no dot (.) at the end

#### Body
If your change is too small, you can skip this part. Because the subject line is enough to describe your change.
* The body should be separated from the subject line with a blank line.
* The body should briefly summarize the changes you have made, explaining what those changes were, why.

#### Example 
* git commit -m "#20 [FEAT] Innit branch"
 
## Branches<a name="branches"></a>
We have two main branches  `master` & `dev` branches :
* The `master` branch should **only** be used for __**the last stable release and validated by the customer**__
* The `dev` branch is for the lastest code version (it doesn't matter if there are bugs)
During development we have to create a new branch if 
* We start a new ticket, we can proceed as the following

```
1) Create issue
2) git checkout dev
3) git checkout -b issueNumber-issueName
4) Check that you are on the right branch
5) git add .
6) git commit -m "#numeroIssue [FEAT] Innit branch"
7) git push
8) And normally it will ask you to push overall
```
* We want to merge a branch with `master` or `dev`, to test if the merge is successful. This branch does not necessarily need to be on the git server.

## Merge<a name="merge"></a>
* Merge every sub-branches with `dev` before merging it with `master`.
* When the creation of a release is possible : Merge `dev` with `master`. :warning: After that, you have to:
	* Create a tag from `master`
	* Update CHANGELOG.md: add at the current date the new features and updates included in the release.

