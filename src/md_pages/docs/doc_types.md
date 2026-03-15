<html lang="en" th:replace="~{page::page('Doc Types', ~{::content})}">
<body th:fragment="content">

# Doc Types
Doc types describe the shape of values in doc comments, such as property types and type aliases.
They are only used by documentation tools and the language server, but they closely mirror the JsonPatcher type system.

```jsonpatcher
#| property values.freeze: <$T>(value: $T) -> $T
#| Freezes a value, causing it to be immutable.
```

## Basic Names
A doc type can be a simple name like `string` or `MyType`.
Names can also be namespaced with dots, like `std.values` or `my_namespace.MyType`.

The following built in types exist:
- `string`, `number`, `boolean`, `null`: self-explanatory
- `array`, `object`, `function`: represent more complex types in cases where information is missing or irrelevant
- `any`: the top type, accepts any value
- `never`: the bottom type, doesn't accept anything
- `unknown`: type checks always pass on unknown types, which allows describing code that cannot be typed properly

Custom names refer to types documented with `#| type` or `#| typealias`.

## Unions
Use `|` to describe a value that can be one of multiple types.
Unions in JsonPatcher are really a hybrid of unions and intersections;
the type-checker will accept any operation that is valid for at least one of the member types.

```jsonpatcher
#| property my_lib.find: (value: string, search: string | number) -> number | null
```

## Arrays and Maps
Arrays use `[]` and maps (objects keyed by strings) use `{}`:

```jsonpatcher
#| property my_lib.keys: (value: object) -> string[]
#| property my_lib.options: (value: object) -> string{}
```

If the inner type is a union or a function, wrap it in `{}` before adding `[]` or `{}`:

```jsonpatcher
#| property my_lib.values: (value: object) -> {string | number}[]
```

## Functions
Function types use the form `(args) -> return`.
Arguments can be named, optional, or varargs:

- `name: type` named argument
- `type?` optional argument
- `type*` varargs argument

```jsonpatcher
#| property my_lib.assert: (value: any, message: string?) -> null
#| property my_lib.join: (values: string*, separator: string?) -> string
```

If an optional or varargs argument is a union or function type, wrap it in `{}`:

```jsonpatcher
#| property my_lib.call: (callback: {(string) -> null}?) -> null
```

### Generic Function Types
Functions can declare type arguments using `$` names in angle brackets.
Type arguments can be used in the argument or return types, and may include an optional bound.

```jsonpatcher
#| property values.freeze: <$T>(value: $T) -> $T
#| property my_lib.cast: <$T: object>(value: any) -> $T
```

Type arguments are only in scope for the function type that declares them.

## Grouping
Curly braces can also be used to group a type where needed, especially with unions or functions:

```jsonpatcher
#| property my_lib.maybe: (value: {string | number}?) -> {string | number}
```

</body>
</html>
