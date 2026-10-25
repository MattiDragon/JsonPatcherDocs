<html lang="en" th:replace="~{page::page('Configuration', ~{::content})}">
<body th:fragment="content">

# Configuring JsonPatcher
The JsonPatcher config file is located in the standard `config` directory of your instance.
It is **not** located in the `jsonpatcher` directory, where other JsonPatcher files live.
Below is the default config file, with comments explaining each option.

```json5
{
  // The maximum amount of time to wait for a patch to finish executing before timing out, in milliseconds.
  // This needs to be higher than it normally takes for your patches to execute, as the first runs can be very slow due to the JIT compiler.
  // Setting this to -1 will disable the timeout, but be aware that this can cause your game to hang if a patch is stuck in an infinite loop.
  "patch_timeout_millis": 500,
  
  // Whether to throw an exception if a patch fails to apply.
  // Throwing an exception will generally prevent the reload from successfully completing.
  // If this is undesired, set this to false in order to log the error and continue the reload.
  "throw_on_failure": true,
  
  // Whether to dump the patched files to disk after a reload.
  // The files are dumped to the `jsonpatcher/dump/{assets,data}` directories, and are useful for debugging.
  "dump_patched_files": false,
  
  // Whether to dump the compiled patches to disk after a reload.
  // The compiled patches are dumped to the `jsonpatcher/dump/classes/{assets,data,global}` directories.
  // They are raw class files, requiring a decompiler to view them. VineFlower is recommended for its superior invokedynamic pseudocode.
  "dump_compiled_patches": false,
  
  // The minimum trust level required for access to the reflection API.
  // The following trust levels are available:
  // - "untrusted": Every program has access to reflection. This is dangerous, 
  //                as it allows servers to send resource packs that can execute arbitrary code on the client.
  // - "local": Only programs from packs locally installed on the client have access to reflection. 
  //            This is somewhat safer, but still allows custom worlds to execute arbitrary code on the client.
  // - "modpack": Only programs loaded from the `jsonpatcher` directory and from mods have access to reflection. 
  //            This is the generally safe, and is the default value.
  // - "mod": Only programs loaded from mods have access to reflection. This is the safest option, but also the most restrictive.
  "reflection_min_trust_level": "modpack",
  
  // Whether to enable hotswapping event listeners. 
  // This option allows you to replace native event handlers defined in global scripts with new ones without restarting the game.
  // This incurs a small performance penalty, and should be disabled for production modpacks.
  // If you don't use event listeners in your global scripts, this option can be left on without any issues.
  "hotswap_events": true,
  
  // Feature flags allow you to enable or disable certain features of JsonPatcher.
  // Feature flags defined by JsonPatcher itself will automatically be added to the config file,
  // but you can also define your own feature flags for use in your own patches.
  "feature_flags": {
    // Enables the RecipePatcher library, which allows you to perform large-scale recipe transformations.
    "jsonpatcher:recipepatcher": false
  }
}
```

</body>
</html>
