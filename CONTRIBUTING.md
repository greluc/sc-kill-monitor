# Contributing to SC Kill Monitor

First off, thanks for taking the time to contribute.

The following is a set of guidelines for contributing to SC Kill Monitor, which is hosted with GitLab.  
These are mostly guidelines, not rules.  
Use your best judgment and feel free to propose changes to this document in a pull request.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [I don't want to read this whole thing. I've a question.](#i-dont-want-to-read-this-whole-thing-ive-a-question)
- [How Can I Contribute?](#how-can-i-contribute)
    - [Reporting Bugs](#reporting-bugs)
        - [Before Submitting A Bug Report](#before-submitting-a-bug-report)
        - [How Do I Submit A (Good) Bug Report?](#how-do-i-submit-a-good-bug-report)
    - [Suggesting Enhancements](#suggesting-enhancements)
        - [Before Submitting An Enhancement Suggestion](#before-submitting-an-enhancement-suggestion)
        - [How Do I Submit A (Good) Enhancement Suggestion?](#how-do-i-submit-a-good-enhancement-suggestion)
- [Your First Code Contribution](#your-first-code-contribution)
- [Pull Requests](#pull-requests)
- [Style Guides](#style-guides)
    - [Git Commit Messages Style Guide](#git-commit-messages-style-guide)
    - [Java Style Guide](#java-style-guide)
    - [JavaDoc Style Guide](#javadoc-style-guide)

---

## Code of Conduct

This project and everyone who participates in it are governed by the [Code of Conduct](CODE_OF_CONDUCT.md).  
By participating, you're expected to uphold this code.  
Report unacceptable behavior to [lucas.greuloch@pm.me](mailto:lucas.greuloch@pm.me).

---

## I don't want to read this whole thing. I've a question.

First, you can read the documentation or search the wiki for the information you need.

If you need help by the developers, create an issue with your question and add the label **support** to it.  
**Perform a cursory search** to see if your question has already been asked.  
If it has, add a comment to the existing issue instead of opening a new one.

---

**Note:** If you find a **Closed** issue that seems like it's the same topic that you're looking for, open a new issue and include a link to the original issue in the body of your new one.

---

## How Can I Contribute?

### Reporting Bugs

This section guides you through submitting a bug report for SC Kill Monitor.  
Following these guidelines helps maintainers and the community to understand your report, reproduce the behavior, and find related reports.

Before creating bug reports, check [this list](#before-submitting-a-bug-report) as you might find out that you don't need to create one.  
When you're creating a bug report, [include as many details as possible](#how-do-i-submit-a-good-bug-report).  
Fill out the required template. The information it asks for helps us to resolve issues faster.

#### Before Submitting A Bug Report

- **Check the FAQ** for a list of common questions and problems.
- **Perform a cursory search** to see if the problem has already been reported.  
  If it has, **and the issue is still open**, add a comment to the existing issue instead of opening a new one.

---

**Note:** If you find a **Closed** issue that seems like it's the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

---

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as Issues. Provide the following information by filling in the template.

Explain the problem and include additional details to help maintainers reproduce the problem:

- **Use a clear and descriptive title** for the issue to identify the problem.
- **Describe the exact steps which reproduce the problem** in as many details as possible.  
  For example, start by explaining how you started the software. When listing steps, **don't only say what you did, but explain how you did it**.
- **Provide specific examples to demonstrate the steps.**  
  Include links to files, other projects, or copy/pasteable snippets. If you're providing snippets in the issue, use [Markdown code blocks](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax).
- **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior.
- **Explain which behavior you expected to see instead and why.**
- **Include screenshots and animated GIFs** that show you following the described steps and clearly demonstrate the problem.
- **If you're reporting that SC Kill Monitor crashed**, include a crash report with a stack trace.  
  Include the crash report in the issue in [code block](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax), a file attachment, or a [GitHub Gist](https://gist.github.com/) and provide the link to that snippet.
- **If the problem wasn't triggered by a specific action**, describe what you were doing before the problem happened.

Provide more context by answering these questions:

- **Did the problem start happening recently,** (e.g., after updating to a new version of SC Kill Monitor) or was this always a problem?
- If the problem started happening recently, **can you reproduce the problem in an older version of SC Kill Monitor?**  
  What's the most recent version in which the problem doesn't happen?
- **Can you reliably reproduce the issue?**  
  If not, provide details about how often the problem happens and under which conditions it normally happens.
- If the problem is related to working with files (e.g., opening and editing files), **does the problem happen for all files and projects or only some?**

Include details about your configuration and environment:

- **Which version of SC Kill Monitor are you using?**
- **What's the name and version of the OS you're using?**
- **What's your Java version and type** (JRE or JDK)? (Only when you use the JAR directly.)
- **What's your Java distribution** (e.g., Oracle, OpenJDK, AdoptOpenJDK, Amazon Coretto)? (Only when you use the JAR directly.)
- **Are you running SC Kill Monitor in a virtual machine or a container?**  
  If so, which VM/Container software are you using and which operating systems and versions are used for the host and the guest?

---

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for SC Kill Monitor, including new features and improvements.  
Follow [this list](#before-submitting-an-enhancement-suggestion) before creating a suggestion.  
Fill in the template with a description and the steps you'd take if the feature existed.

#### Before Submitting An Enhancement Suggestion

- **Perform a cursory search** to see if the enhancement has already been suggested.  
  Add a comment to the existing issue instead of opening a new one.

---

**Note:** If you find a **Closed** issue that is similar to your suggestion, and you have new arguments, open a new issue and include a link to the original one.

---

#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as Issues. Create an issue and provide:

- **A clear and descriptive title**
- **Step-by-step description** of the proposed feature.
- **Specific examples** (e.g., code snippets in [Markdown code blocks](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)).
- **Expected behavior** and how it differs from the current behavior.
- **Screenshots and animated GIFs** where applicable.
- **An explanation of why the enhancement would be useful**.
- **Version details for SC Kill Monitor, your browser, and your OS.**

---

## Your First Code Contribution

Look for `beginner` or `help-wanted` issues to get started:

- **Beginner issues**: Simple fixes requiring only a few lines of code.
- **Help wanted issues**: More involved tasks than beginner issues.

---

## Pull Requests

- Open a pull request (PR) against the **develop** branch.
- Prefix the MR name with one of these types: "FEATURE", "BUG", "CHORE", "META", e.g., `[FEATURE – Database]`.
- Fill in the template.
- Avoid issue numbers in titles.
- Provide screenshots or animated GIFs, where possible.
- Document new code based on [Style Guides](#style-guides).
- End all files with a newline.
- Avoid platform-dependent code.

---

## Style Guides

### Git Commit Messages Style Guide

- Use the present tense ("Add feature" not "Added feature").
- Use the imperative mood ("Move cursor to…" not "Moves cursor to…").
- Limit the first line to 72 characters or fewer.
- Reference issues and MRs (`relates to #XYZ`, `relates to !XYZ`).
- Add `[skip ci]` to skip CI pipelines if unnecessary.

### Java Style Guide

- Follow [Google's Java Style Guide](https://google.github.io/styleguide/javaguide.html).  
  IntelliJ IDEA XML settings file is available in the settings directory.

### JavaDoc Style Guide

- Always update the `@author` tag but retain earlier authors.
- Use the `@since` tag.