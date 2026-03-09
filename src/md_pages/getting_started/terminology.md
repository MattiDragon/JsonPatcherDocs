<html lang="en" th:replace="~{page::page('Terminology', ~{::content})}">
<body th:fragment="content">

# Terminology
This page lists some common words that you might encounter while working with JsonPatcher.
They can be good to keep track of while reading these docs and other resources.

| Word                                   | Definition                                                                                   |
|----------------------------------------|----------------------------------------------------------------------------------------------|
| JsonPatcher                            | The minecraft mod that runs your code                                                        |
| JsonPatcherLang                        | The programming language that programs are written in                                        |
| <span class="empty-row-marker"></span> |
| Program<sup>[[1]](#1)</sup>            | A `.jsonpatch` file                                                                          |
| Patch<sup>[[1]](#1)</sup>              | A program that modifies a json files                                                         |
| Script<sup>[[1]](#1)</sup>             | A program that is executed outside the context of any specific json file                     |
| Metapatch                              | A script executed in the context of a resource reload with the ability to change json files. |

<section>
<p id="1">[1] This terminology may be used inconsistently in older documentation and code</p>
</section>

</body>
</html>
