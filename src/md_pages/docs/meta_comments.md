<html lang="en" th:replace="~{page::page('Meta Comments', ~{::content})}">
<body th:fragment="content">

# Meta Comments
Meta comments is a general term for comments whose purpose is to tell some tool something about the code,
without actually being part of it. They begin with `# @@`, followed by a name and content.
Each tool reads these separately, so you won't ever get any errors if their names are incorrect.

## `@@suppress`
Suppresses diagnostics (warnings and errors). This is currently only respected by the LSP and CLI.
The body consists of a diagnostic id of the form XXX-NNN (where XXX is some string and NNN is a number),
and optionally a range specifier.

```jsonpatcher
# Examples of suppressing unused variable warnings

# One line
# @@suppress VAR-3
val a = 1;

# Multiple lines
# @@suppress VAR-3 3 lines
val b = 1;
val c = 2;
val d = 3;

# Region
# @@suppress VAR-3 start
val e = 1;
# @@suppress VAR-3 end

# Whole file
# @@suppress VAR-3 file
```

## `@@type`
Allows specifying the type of a local variable in cases where inference fails or is insufficient.
```jsonpatcher
# Typing a function:
# @@type (number, number) -> number
function myFunc(a, b) {
    return a + b;
}

# Removing '| unknown' from a mutable variable:
# @@type number
var a = 1;
```

## `@@arg`
Similar to `@@type`, but sets the type a named function argument.
```jsonpatcher
# @@arg a number
# @@arg b number
function myFunc(a, b) {
    # LSP knows that a and b are numbers here
}
```

## `@@doc_namespace`
Sets a default namespace prefix for all following doc entries in a file.
If used multiple times the prefix is replaced. If used without a body, this clears the prefix.
Does not affect references to other doc entries in types.
```jsonpatcher
# @@doc_namespace my_namespace

#| type MyType: object
#| A type under my_namespace, so it will be called my_namespace.MyType in the

# @@doc_namespace

#| type MyType2: object
#| A type without a namespace, so it will be called MyType2
```

</body>
</html>
