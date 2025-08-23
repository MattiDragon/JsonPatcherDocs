<html lang="en" th:replace="~{page::page('Variables', ~{::content})}">
<body th:fragment="content">

# Variables
Variables in jsonpatcher are block scoped, meaning that they are only accessible in the block they are defined in and any nested blocks.
Due to a quirk in the implementation, functions can access variables from after they are defined as long as they are called after the variable definition.

Variables come in two flavors: mutable and immutable. Mutable variables can be changed after they are defined, while immutable variables can't.
You should prefer immutable variables when possible.

## Defining Variables
Variables are defined with the `var` and `val` keywords, for mutable and immutable variables respectively.
After the keyword you specify the name of the variable and then assign a value to it.

```py
var a = 1;
val b = 2;

a = 3; # we can reassign a variable with =, but only if it's mutable
b = 4; # this will cause an error

{
    val c = 4;
}
# We can't access c here
```

</body>
</html>
