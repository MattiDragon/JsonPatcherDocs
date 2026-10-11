<html lang="en" th:replace="~{page::page('RecipePatcher', ~{::content})}">
<body th:fragment="content">

# Transforming Recipes with RecipePatcher
RecipePatcher is a powerful tool that makes large-scale recipe changes easy.
It works by matching each recipe against a template using its `type`, then applying ingredient, output, field, and whole-recipe transforms.
It ships with JsonPatcher, but is disabled by default. To enable it, flip the feature flag in the config file.

```json
{
  "feature_flags": {
    "jsonpatcher:recipepatcher": true
  }
}
```

RecipePatcher is meant to be used from a <a th:href="@{/program_types/metapatch.html}">metapatch</a>, because it acts on many recipe files at once.
The API is exposed through the `jsonpatcher:recipepatcher` library, and the template system through `jsonpatcher:recipepatcher/templates`.
The full API

## Simple Transformations
The simplest use is replacing a single ingredient or output.
This is perfect for swapping a vanilla item to a custom one or mass-renaming recipes without touching every file directly.

```jsonpatcher
@version "2";
@metapatch;

import "jsonpatcher:recipepatcher" as recipepatcher;

recipepatcher.replaceIngredient("minecraft:stick", "minecraft:blaze_rod");
recipepatcher.replaceOutput("minecraft:stick", "minecraft:blaze_rod");
```

These replacements apply to any matching recipe fields, including both direct ingredient lists and nested recipe data.
Tags are also supported, allowing you to easily replace all usages of a tag with an item for unification, 
or all usages of an item with a tag for extending compatibility.

```jsonpatcher
@version "2";
@metapatch;

import "jsonpatcher:recipepatcher" as recipepatcher;

recipepatcher.replaceIngredient("#minecraft:planks", "minecraft:oak_planks");
recipepatcher.replaceOutput("minecraft:stick", "#minecraft:planks");
```

## Dynamic Transformations
If you need more control than a 1:1 replacement, use a transform function:

```jsonpatcher
@version "2";
@metapatch;

import "jsonpatcher:recipepatcher" as recipepatcher;

recipepatcher.transformIngredients((ingredient) -> {
    if (ingredient == "minecraft:stick") {
        return "minecraft:blaze_rod";
    }
    return ingredient;
});

recipepatcher.transformOutput((stack) -> {
    if (stack is object && stack.id == "minecraft:diamond") {
        stack.id = "minecraft:emerald";
        return stack;
    }
    return stack;
});
```

## Advanced Transformations
RecipePatcher can also target individual named fields and whole recipe objects.
This is useful when recipe data includes extras such as experience values, cooking times, or custom recipe metadata.

```jsonpatcher
@version "2";
@metapatch;

import "jsonpatcher:recipepatcher" as recipepatcher;

recipepatcher.transformField("minecraft:cooking@experience", (value) -> {
    if (value is number) {
        return value * 2;
    }
    return value;
});

recipepatcher.transformRecipes("minecraft:smelting", (recipe) -> {
    if ("cookingtime" in recipe) {
        recipe.cookingtime = 200;
    }
    return recipe;
});
```

The field names come from the registered template for each recipe type.
This makes it easy to rewrite values without writing custom file-by-file patches.

## Custom Templates
RecipePatcher is built from regular JsonPatcher code and is highly customizable.
You can register custom recipe types and custom ingredient-handling templates for modded recipe formats.

### Registering a recipe type
Built-in templates cover many vanilla recipe types already, but custom recipe types can be added with the template library.
The `templates` library exposes helpers like `object`, `array`, `map`, `ingredient`, `resultStack`, `field`, and `simpleRecipe`.

```jsonpatcher
@version "2";
@metapatch;

import "jsonpatcher:recipepatcher/templates" as t;

$'mod:custom_recipe' = t.object({
    input: t.ingredient(),
    result: t.resultStack()
});

t.register("mod:custom_recipe", $'mod:custom_recipe');
```

Once registered, RecipePatcher will automatically match recipes of that type and apply ingredient and output transforms.

### Registering custom ingredient templates
Some mods use ingredient objects instead of plain strings or arrays.
For those, you can register a dedicated ingredient template so RecipePatcher knows how to interpret the structure.

```jsonpatcher
@version "2";
@metapatch;

import "jsonpatcher:recipepatcher/templates" as t;

t.registerIngredientTemplate("fabric:any", t.object({
    ingredients: t.array(t.ingredient())
}));

t.registerIngredientTemplate("fabric:all", t.object({
    ingredients: t.array(t.ingredient())
}));
```

This lets RecipePatcher understand ingredients such as `{"fabric:type": "fabric:any", "ingredients": [...]}` and apply replacements to the nested values.

</body>
</html>
