<html lang="en" th:replace="~{page::page('Home', ~{::content})}">
<body th:fragment="content">

# JsonPatcher Wiki
This wiki documents the JsonPatcher language and standard library.
It's primarily targeted for people who already have some programming experience.

The language is a pretty standard c-like language with a few unique features.
You can check out some [examples in the test mod](https://github.cocm/MattiDragon/JsonPatcher/tree/1.21.1/rewrite/src/test/resources) 
for some examples of what the language looks like and how to use it.
This wiki mostly contains information on how individual language features work.

Before using the mod I recommend reading the <a th:href="@{/ide-support}">IDE support page</a>.
It includes information on how to set up a VSCode extension for JsonPatcher,
which will help tremendously with writing patches.

To create your first patch, check out the Creating a Patch page.
After that you'll want to learn more about the language.
Some pages that are a good place to start are:
* <a th:href="@{/syntax/structure.html}">Structure</a> explains the general structure of programs in the language and defines terms that are used throughout the wiki.
* <a th:href="@{/language/types.html}">Types</a> explains the different types in the language and how they work.
* <a th:href="@{/syntax/operators.html}">Operators</a> explains the different operators in the language. They differ somewhat from other languages, so it's good to know how they work.
* <a th:href="@{/syntax/root-object.html}">Root Object</a> explains the root object and how it's used. This is an important concept in the JsonPatcher that's not common elsewhere.
* <a th:href="@{/syntax/variables.html}">Variables</a> explains how variables are used.

You might also want to read up on the Standard Library to see what functions are available,
as well as Statements to see the syntax for different statements.

</body>
</html>