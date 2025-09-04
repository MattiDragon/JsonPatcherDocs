<html lang="en" th:replace="~{page::page('If, Else and Blocks', ~{::content})}">
<body th:fragment="content">

# If, Else and Blocks
## Blocks
Blocks in the JsonPatcher language work like they do in most languages with c-like syntax.
They are declared with curly braces and act as their own scope for <a th:href="@{/syntax/variables}">variables</a>. 
Blocks inherit variables from blocks outside them.

```jsonpatcher
{ # A block
    var a = 1;
    {
        var b = 2;
        # a and b are accessible here
    }
    # a is accessible here, but b isn't
}
```

## If and Else
The if statement is used to execute code conditionally. It consists of a condition and a body.
The syntax is the same as in most languages with c-like syntax. The body is executed if the condition is true.

```jsonpatcher
var a = 1;
if (a == 1) {
    # This will be executed
}
```

The else statement can be used to execute code if the condition is false.

```jsonpatcher
var a = 1;
if (a == 2) {
    # This won't be executed
} else {
    # This will be executed
}
```

The else statement can also be used with an if statement to chain multiple conditions.

```jsonpatcher
var a = 1;
if (a == 2) {
    # This won't be executed
} else if (a == 1) {
    # This will be executed
} else {
    # This won't be executed
}
```

## Truthiness
If statements don't just check if the condition is true. 
You can pass in any value to the if statement, and it will be converted to a boolean for the check.
Here's a table of how types are converted to booleans:

| Type     | Truthiness                         |
|----------|------------------------------------|
| String   | `false` if empty, `true` otherwise |
| Number   | `false` if 0, `true` otherwise     |
| Boolean  | self explanatory                   |
| Null     | `false`                            |
| Array    | `false` if empty, `true` otherwise |
| Object   | `false` if empty, `true` otherwise |
| Function | `true`                             |

</body>
</html>
