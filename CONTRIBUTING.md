# Contributing to cookie-twist

:tada: Thanks for taking the time to contribute! :tada:

The following is a set of guidelines for contributing to cookie-twist library.
These are mostly guidelines, not rules. Use your best judgment.

#### Table Of Contents

[What should I know before I get started?](#what-should-i-know-before-i-get-started)
  * [Tornado web framework cookies and this library](#tornado-web-framework-cookies-and-this-library)
  * [Minimal development environment](#minimal-development-environment)

[How Can I Contribute?](#how-can-i-contribute)
  * [Reporting Bugs](#reporting-bugs)
  * [Pull Requests](#pull-requests)

[Styleguides](#styleguides)
  * [Git Commit Messages](#git-commit-messages)
  * [Java Styleguide](#java-styleguide)

## What should I know before I get started?

### Tornado web framework cookies and this library

[Tornado](http://www.tornadoweb.org/en/stable/) is a Python web framework and
asynchronous networking library that comes with useful web components, like the
[signed secure cookies](http://www.tornadoweb.org/en/stable/guide/security.html#cookies-and-secure-cookies)
and the current `cookie-twist` scope is to port *only* that behavior in Java.

### Minimal development environment

As far you have a JDK 1.8.xxx or greater you'll be fine, since all the
development related tasks are summarized in the `build.gradle` file which can
be executed by using the gradle wrapper. As for example run all the test cases
you only need to prompt:

```shell
# In *nix like OS's 
./gradlew test
```

```powershell
# In Microsoft OS's
.\gradlew.bat test
```

You can find a more accurate list of the available task by typing `./gradlew
tasks` in your terminal.

## How Can I Contribute?

### Reporting Bugs

When you are creating a bug report, please include as many details as possible.
Provide the following information by filling in the template. Explain the
problem and include additional details to help maintainers reproduce the
problem:

* **Use a clear and descriptive title** for the issue to identify the problem.
* **Describe the exact scenario and configurations which reproduce the problem** in as many details as possible.
* **Explain which behavior you expected to see instead and why.**

Include details about your configuration and environment:

* **Which version of cookie-twist are you using?**
* **Which version of Java Runtime Environment (JRE) are you using?**
* **Which application framework you were using (if applies)?**
* **What's the name and version of the OS you're using**?

### Pull Requests

* Fill in the required template
* Do not include issue numbers in the PR title
* Follow the [Java styleguide](#java-styleguide)
* Include thoughtfully-worded, well-structured [Junit](https://jasmine.github.io/) tests in the `src/test` folder
* Document new code based on the [Documentation Styleguide](#documentation-styleguide)
* End all files with a newline

## Styleguides

### Git Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Look up for..." not "Looks up for...")
* Limit the first line (commit title) to 72 characters or less
* Reference issues and pull requests liberally after the first line
* When only changing documentation, include `[ci skip]` in the commit title
* Don't pollute the git history, rebase and squash the commits with meaningless comments

### Java Styleguide

All the Java code must adhere to the repository's [checkstyle rules](https://github.com/jossemarGT/cookie-twist/blob/master/gradle/config/checkstyle/checkstyle.xml)
which are a sub-set of the [Sun Microsystem ones](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/sun_checks.xml).

### Specs Styleguide

The well known Java testing framework [JUnit4](https://junit.org/junit4/)
comes with its own [best practices](https://junit.org/junit4/faq.html#Best_Practices)
and conventions which should be follow by all the test cases written in this
repository. Adding to that, the test cases in this repository thrives to follow
a Data/Table Driven Testing pattern similar to the ones in
[Golang](https://github.com/golang/go/wiki/TableDrivenTests) projects.

### Documentation Styleguide

* Use [JavaDoc](http://www.oracle.com/technetwork/articles/java/index-137868.html) for code documentation.
* Use [Github Markdown](https://daringfireball.net/projects/markdown) for repository documentation.

<!-- Based on Atom Contribuiting Guidelines https://github.com/atom/atom/blob/master/CONTRIBUTING.md -->
