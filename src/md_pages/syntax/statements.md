<html lang="en" th:replace="~{page::page('Statements', ~{::content})}">
<body th:fragment="content">

# Statements
Statements are parts of code that do something.
This page lists all the types of statements and explains how they work.

## Expression Statement
The expression statement is the simplest type of statement.
It simply evaluates an expression and discards the result.
It is used for expressions that have side effects, like function calls and assignments.

```jsonpatcher
# Assume variable value is created before this
value = 1; # This is an expression statement
```

## Block Statement
The block statement is used to group statements together and create a new scope.
It consists of a list of statements surrounded by curly braces.
The statements are executed in order. Read <a th:href="@{/syntax/if-else-and-blocks.html}">If, Else and Blocks</a> for more information.

```jsonpatcher
{
    var a = 1;
    var b = 2;
    a + b;
}
```

## If and Else Statement
Read <a th:href="@{/syntax/if-else-and-blocks.html}">If, Else and Blocks</a> for more information.

## While, For, Foreach, Break and Continue Statements
Read <a th:href="@{/syntax/loops.html}">Loops</a> for more information.

## Function Declaration and Return Statement
The return statement is used to return a value from a function.
Functions declarations with a name are also statements.
Read <a th:href="@{/syntax/functions.html#defining-functions}">Functions</a> for more information.

## Variable Declarations
Variable declarations are also considered statements.
Read <a th:href="@{/syntax/variables.html#defining-variables}">Variables</a> for more information.

## Import Statement
The import statement is used to import other files as libraries.
Each import statement executes the file on an empty object and then stores that object in a variable.
This makes it easy to build libraries that declare functions.

By default, the library is assigned to a variable with the same name as the file.
This is often impractical and leads to the need to use `'` around names to access them.
That's why you can include a name after the path to assign the library to a different variable.

```jsonpatcher
import "namespace:path/to/file";
import "namespace:path/to/file" as name;
```

## Apply Statement
The apply statement is something that's not as common in other language.
Its syntax is similar to that of an if statement, but it behaves differently.

The apply statement takes in an object and executes the body (usually a block, but it can be any statement)
with that object as the root.

```jsonpatcher
var a = {b: 1};
apply (a) {
    $b; # This will return 1
}
```

## Delete Statement
The delete statement is used to delete properties from objects, remove values from arrays and remove variables from the current scope.
It takes in a reference and deletes the value it points to.

```jsonpatcher
var a = {b: 1};
delete a.b; # This will delete the property b from a

var b = [1, 2, 3];
delete b[0]; # This will remove the first element from b

var c = 1;
delete c; # This will remove the variable c from the current scope
```

## Unnecessary Semicolons
Sometimes it's useful to have a statement that does nothing.
This can be done by using a single semicolon instead of a statement.

```jsonpatcher
while (true); # This will loop forever

# You can also use an empty block, but that will have slightly 
# worse performance because it creates an empty block.
while (true) {}
```

</body>
</html>
