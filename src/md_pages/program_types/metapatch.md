<html lang="en" th:replace="~{page::page('Metapatches', ~{::content})}">
<body th:fragment="content">

# Metapatches
Metapatches are programs that are loaded from the same places as <a th:href="@{/program_types/regular_patch.html}">patches</a>,
but which don't target any specific file. Instead, metapatches are executed once at the start of resource loading.
They get access to the <a th:href="@{/symbols/mod-stdlib.html#object:metapatch.lib}">`metapatch` library</a>,
which allows them to preform wide changes to multiple resource files.

To create a metapatch, simply add the `@metapatch;` metadata tag. It cannot be used together with `@target` in the same program.

## Deleting Files
One of the primary strengths of metapatches is their ability to conveniently delete files.
While Minecraft already provides pack filters, they can be cumbersome to use in practice.
With metapatches you can JsonPatcher filters and other language features.

```jsonpatcher
@version "2";
@metapatch;

import "metapatch";

# Delete a specific file
metapatch.deleteFile("minecraft:recipe/diamond_boots.json");
# Delete multiple files using the same syntax as @target
metapatch.deleteFiles({namespace: "minecraft", path: {start: "recipe/", end: "_sign.json"}});
```

## Adding files
Metapatches can also be used to add new files. This is useful if you need to create large amounts of repetitive files.

```jsonpatcher
val cobble = metapatch.getFile("minecraft:loot_table/blocks/stone.json");

foreach (stoneType in ["andesite", "diorite", "granite"]) {
    metapatch.addFile("minecraft:loot_table/blocks/" + stoneType + ".json", cobble);
}
```

## Reading files
As demonstrated in the previous example, metapatches can also read files. This can be used to create new files based on existing ones,
or to read files for use in global scripts and libraries.

</body>
</html>
