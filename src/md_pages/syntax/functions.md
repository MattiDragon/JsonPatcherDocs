<html lang="en" th:replace="~{page::page('Functions', ~{::content})}">
<body th:fragment="content">

# Functions
Functions are an important part of most programming languages.
They allow you to reuse code and make it easier to read and understand.

## Calling Functions
Functions are called by using parentheses with arguments inside after a function reference. 
The reference can be a variable, a property on an object, the result of a function call, or any other value.
```py
math.sin(1);
```

## Defining Functions
Functions are defined by the function keyword, followed by a name, a list of arguments, and a body.
The function will be bound to an immutable variable with the specified name.
Functions can also be defined with arrow syntax. The arrow syntax is an expression and will return the function.
```py
function add(a, b) {
    return a + b;
}

var subtract = (a, b) -> {
    return a - b;
};
```

## Arguments
When defining a function you specify the arguments that it should accept as a comma separated list of names in the parentheses.
The special name `$` can be used to bind an argument as the root object of the body. 
If this isn't done, the root object is the one of defining scope.

Function arguments may optionally be followed by an equals sign and an expression for a default value.
Default values are evaluated each time a function is executed and all required arguments must appear before any optional arguments.
Named arguments are *not* supported. This means that optional arguments must be passed positionally like regular arguments.

The last argument in a function may optionally be followed by `*`. This indicates a *varargs* argument.
When calling the function, all extra arguments at the end will be collected into an array and passed to the function as this parameter.
Varargs arguments are considered optional, and can thus appear after other optional arguments.

```py
function example(
    a,
    b = math.sin(5), # Default values can be any expression
    c = "a",
    d* # Varargs argument
) {}

example(10);
example(10, 3);
example(10, 3, "b");
example(10, 3, "b", "more", "arguments");
```

## Functions as Values
In JsonPatcher, functions are values just like objects and arrays.
They can be stored in variable, passed as arguments, and returned from functions.

## Return Values
Functions can return values by using the `return` keyword. The values are then accessible to the caller. 
You can also return without a value, in which case the return value will be `null`.
If you return outside a function you will end the program.

</body>
</html>
