<html lang="en" th:replace="~{page::page('Libraries', ~{::content})}">
<body th:fragment="content">

# Libraries
Libraries are programs with can be imported by other programs. 
They are declared using the <a th:href="@{/symbols/mod-stdlib.html#object:script.library}">`@library`</a> 
<a th:href="@{/syntax/metadata.html}">metadata tag</a>. Programs from all sources can be made into libraries: 
libraries loaded from resource files and global patches become available to others within their resource type,
libraries loaded from mod scripts and global scripts become available to all other programs.

## Declaring Libraries
In order to declare a library, simply add the following to the beginning of a new program:
```jsonpatcher
@version "2";
@library;
```

Libraries cannot have the `@target`, `@metapatch` or `@init` metadata tags. 
In order to export functions or values from a library, they must be placed into properties of the root object.
The root object will then be frozen and provided to importers by the runtime.
```jsonpatcher
@version "2";
@library;

function myLibraryFunction() {
}
$myLibraryFunction = myLibraryFunction;
```

## Importing Libraries
In order to use libraries, they must be imported. The name used to import a library depends on where it's declared:
* Libraries found in data and asset files are imported by their identifiers
* Libraries found in mods' `jsonpatcher/scripts` folder are imported with the `mod:MOD_ID:` prefix
* Libraries found in global `jsonpatcher/scripts` folder are imported with the `scripts:global:` prefix

As an example, here we import the `async` library from JsonPatcher:
```jsonpatch
import "mod:jsonpatcher:async" as async;
```

Additionally, some built-in libraries are available without any prefix:
* `reflection`
* `events`
* `metapatch`

## Shared Libraries
Libraries can optionally be made shared. A shared library is only called once, when imported the first time.
Subsequent imports will result in the same object being returned. This allows programs to communicate with each other,
but has some thread-safety concerns. Shared libraries can also be used for performance when no mutable state is exposed.

During regular patching, multiple patches may be executing at the same time. If these import the same shared library,
they may end up modifying the same state at the same time. As objects and arrays aren't thread safe, this can cause
various issues, including crashes and inconsistent states. As such, any shared libraries with mutable state, should use
the `mod:jsonpatcher:async` library to correctly lock resources.

Shared libraries are declared like this:
```jsonpatcher
@version "2";
@library {"shared": true};
```

</body>
</html>
