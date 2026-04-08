<html lang="en" th:replace="~{page::page('Patches', ~{::content})}">
<body th:fragment="content">

# Patches
Patches are the original program type in JsonPatcher. 
They allow you to directly modify json resources while they're being loaded.
In order to declare a patch, simply add the <a th:href="@{/symbols/mod-stdlib.html#object:patch.target}">`@target`</a> metadata tag, 
specifying which files to target.

<a th:href="@{/syntax/root-object.html}">The root object</a>, `$`, is set to the contents of the json file being patched.
Changes made to the root object will then be reflected in the final file returned to the game. 

## Targeting
The target of a patch is specified as the value of the `@target` metadata. The target can match any number of files.
Multiple targets can be combined using an array so that files matched by any one target gets patched.

### Simple targets
Simple targets target a single file by name. The resource type and `.json` suffix must be included.

```jsonpatcher
@target "minecraft:recipe/diamond_pickaxe.json";
```

### Object targets
Targets can also be specified in object form. In the object form three separate properties are available:
`namespace`, `path` and `regex`. All the present properties must match for a target to match a file.

The `namespace` field is a simple string that is matched exactly against the namespace of a file.
This is used to limit patches to files from certain mods or datapacks.

```jsonpatcher
@target {
    "namespace": "minecraft"
};
```

The `path` field is an object with two properties: `start` and `end`. 
For the path to match it must start with the `start` string and end with the `end` string. 
This is useful for targeting related files, for example all recipes.

```jsonpatcher
@target {
    "path": {
        "start": "recipe/",
        "end": ".json"
    }
};
```

The `regex` field can be set to a regular expression, which is then matched against the whole file path,
including namespace and file extension. Of all targeting methods, regexes have the worst performance,
and should thus be avoided unless necessary.

```jsonpatcher
@target {
    "regex": "minecraft:"
};
```

## Examples
### Simple Recipe Modification
This patch changes the recipe for golden apples to instead craft enchanted golden apples.
```jsonpatcher
@version "2";
@target "minecraft:recipe/golden_apple.json";

$result.id = "minecraft:enchanted_golden_apple";
```

### Biome Tweaker
This patch removes dirt blobs from all biomes, including modded ones.
```jsonpatcher
@version "2";
@target {
    "path": {
        "start": "worldgen/biome/",
        "end": ".json"
    }
};

foreach (step in $features) {
    step.removeIf((feature) -> feature == "minecraft:ore_dirt");
}
```

</body>
</html>
