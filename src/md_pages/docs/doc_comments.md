<html lang="en" th:replace="~{page::page('Doc Comments', ~{::content})}">
<body th:fragment="content">

# Doc Comments
JsonPatcher code can be documented with a system of doc comments. They are used to generate the symbol pages of the docs.
They're also used by the JsonPatcher language server to determine information about programs.

A doc comment consists of one or more consecutive lines, all starting with the marker `#|`.
The first line of a doc comment is known as the header, and specifies what is being documented. 
Doc comments are completely detached from source code. The processor never cares about where a doc comment is placed, 
although it's conventional to place them before the thing being documented.

After the first line, the body of the comment follows. 
The body consists of free Markdown markup content describing the thing being documented.
After the body, tags may optionally be placed to provide additional information in a structured way.

## Types of Comments
### Namespace
Namespaces group together related doc entries. They are automatically generated when necessary,
but can be manually created when you want to add a description.
```jsonpatcher
#| namespace namespace1
#| This is a sample namespace

#| namespace namespace1.namespace2
#| Namespaces can be nested
```

### Library
Library entries describe <a th:href="@{/program_types/library.html}">importable libraries</a>. 
Their header can optionally specify their import location.
```jsonpatcher
#| library my_library
#| This is a sample library

#| library namespace1.my_library at "mod:mymod:my_library"
#| Another library, but with an explicit import path
```

### Type
A type describes a structured type. As the JsonPatcher language doesn't support custom types,
these docs mostly serve to inform users about the behavior of certain objects.
Types are specified to be based on either `object` or `special`. 
`special` is only used for types which extend `SpecialValue` in java code.
```jsonpatcher
#| type MyType: object
#| A simple type

#| type namespace1.MyType2: object
#| A type under a namespace
```

### Property
A property specifies a property accessible via `.` syntax on something.
Properties can be declared for libraries, types and global libraries.
```jsonpatcher
#| property my_library.myProperty: string
#| A property of a library

#| property namespace1.MyType2.myProperty: number
#| A property of a type
```

The same syntax is used to declare functions:
```jsonpatcher
#| property my_library.myFunction: (a: string, b: number) -> boolean
#| A function property of a library
```

### Typealias
A typealias creates a name for an existing type. Properties cannot be added to typealiases.
```jsonpatcher
#| typealias MyAlias: string[] | number[]
#| A simple typealias

#| typealias namespace1.MyAlias: namespace1.MyType2
#| A typealias under a namespace
```

### Metadata
Metadata declarations document <a th:href="@{/syntax/metadata.html}">metadata tags</a>.
Their declarations contain the type of data that they should hold, for empty tags, this should be null.
```jsonpatcher
#| metadata namespace1.my_metadata: string
#| This is a sample metadata tag

#| metadata other_meta: number | string
#| This is a sample metadata tag
```

### Global
Global metadata comes in two forms: global values and global libraries. 
Regular users should rarely have any reason to declare them, as only JsonPatcher and other mods can add them.
Global values are variables that are always available. Global libraries are almost the same, 
but instead of being declared with a type, they behave as one instead.
```jsonpatcher
#| global my_global: string
#| This is a sample global variable

#| global library my_global_library
#| This is a sample global library

#| property my_global_library.myProperty: string
#| A property of the global library
```

## Tags
Tags specify additional structured information about doc comments, which can be used by tools or rendered differently.
Each tag takes up exactly one line of the doc comment, which begins `#| @`.

### Requires
The `requires` tag marks that a doc entry is only available under certain conditions. 
See <a th:href="@{/docs/doc_conditions.html}">Doc Conditions</a> for more details on the condition syntax.
```jsonpatcher
#| library my_library
#| @requires libgroup("+minecraft-data")
```

### Optional
The `optional` tag marks that a doc entry is optional. This is used for properties that may not be present on all instances of a type.
```jsonpatcher
#| type MyType: object

#| property MyType.optionalProperty: string
#| @optional
```

### Method
The `method` tag marks that a property is a method. 
This is only applicable to standard library methods that can be directly called on things like numbers and arrays.

</body>
</html>
