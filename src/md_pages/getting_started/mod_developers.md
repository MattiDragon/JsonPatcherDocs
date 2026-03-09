<html lang="en" th:replace="~{page::page('Getting Started for Mod Developers', ~{::content})}">
<body th:fragment="content">

# Getting Started - For Mod Developers
There are two primary reason why you would want to use JsonPatcher in your mod:
1. Modifying json files in a compatible way
2. Extending the functionality of JsonPatcher

## Adding the Dependency
For both cases you'll first have to add a dependency on JsonPatcher to your mod as a gradle dependency.
If you're using JsonPatcher to implement features of your mod, consider adding it as a JiJ dependency,
the full jar including all dependencies is only about 1 MiB.

<details>
<summary>Changes to <code>build.gradle</code></summary>

```gradle
repositories {
    maven {
        url = 'https://jitpack.io'
    }
}

dependencies {
    modImplementation "dev.mattidragon:JsonPatcher:$jsonpatcher_version"
    // optional
    include "dev.mattidragon:JsonPatcher:$jsonpatcher_version"
}
```

</details>
<details>
<summary>Changes to <code>build.gradle.kts</code></summary>

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    modImplementation("dev.mattidragon:JsonPatcher:$jsonpatcher_version")
    // optional
    include("dev.mattidragon:JsonPatcher:$jsonpatcher_version")
}
```

</details>

## Writing Patches
Mods can contain patches in their `data` and `assets` directories, just like data and resource packs can.
These patches are ideal for cases where you need to modify vanilla files without any extra fuss.
These patches also get access to reflection, in case you need to perform operations not normally possible.
Do note, however, that these patches run on resource loads, which makes them badly suited for general modifications
to game state.

Mods can also place patches within a `jsonpatcher/scripts` resource directory. These patches are loaded like global
patches, allowing you to provide additional libraries and utilize `@init` to run code on startup (although you should 
usually prefer java when writing a mod).

</body>
</html>
