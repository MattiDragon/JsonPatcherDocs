<html lang="en" th:replace="~{page::page('Metadata', ~{::content})}">
<body th:fragment="content">

# Metadata
Metadata is an important part of patches. It tells JsonPatcher what the patch is for and how to apply it.
Metadata is written as tags beginning with `@` and a name. They are written in json format and end in a semicolon.
Extra metadata is ignored.

## Version
The version tag is used to specify what version of JsonPatcher the patch is for.
This is important because the language may change in the future, and patches may not work with future versions.
The version tag is required for all patches. Currently only version 1 is supported.

```py
@version 1;
```

## Target
The target tag is used to specify what files the patch should apply to.
It can be a string, an object, or an array of strings and objects.
If the target is an array, the patch will apply to all files that match any of the selectors in the array.

When the target is a string, it's directly matched against the identifier of the potential target file.
```py
@target "minecraft:recipes/stick.json";
```

When the target is an object, it's fields check different parts of the target file.
The `namespace` field checks the namespace of the target file. It should be a string.
The `path` field checks the path of the target file. 
It can be either a string or an object with the properties `begin` and `end`.
They should be strings. If the path begins with `begin` and ends with `end`, the patch will apply.
The `regex` field checks the path of the target file against a regular expression.
It should be a string. If the path matches the regex, the patch will apply.
This has quite bad performance, so it's recommended to use `begin` and `end` instead where possible.
These properties are all optional and are combined with a logical and. An empty object is not allowed.
```py
@target {
    "namespace": "minecraft",
    "path": {
        "begin": "recipes/",
        "end": ".json"
    }
};
```

When the target is an array, it's elements are treated as targets.
If any of them match, the patch will apply.
```py
@target [
    "minecraft:recipes/stick.json",
    {
        "namespace": "minecraft",
        "path": {
            "begin": "recipes/",
            "end": ".json"
        }
    }
];
```

## Enabled
The enabled tag can be used to disable a patch.
It's value should be a boolean. If it's `false`, the patch will not be applied.
```py
@enabled false;
```
This is useful the debugging as you don't have to comment out the entire patch or rename the file to disable it.

## Metapatch
The `@metapatch` tag is used to specify that the patch is a metapatch.
Metapatches are patches that instead of patching files, creates and deletes them.
This is useful for dynamically generating files or copying them around.
The contents of the tag are ignored, and it's recommended to be left empty.
See <a th:href="@{/language/metapatch.html}>Metapatches</a> for more information.
```py
@metapatch;
```

## Priority
The priority tag is used to specify the priority of the patch.
It's value should be a number. Higher numbers apply later.
The default priority is 0. Metapatches have a separate priority.
```py
@priority 1; # This patch will apply after patches with priority 0
```
Priority is useful for making sure patches apply in the correct order.
However, it's often enough to just order the packs containing the patches.

</body>
</html>
