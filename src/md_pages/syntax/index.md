<html lang="en" th:replace="~{page::page('Syntax', ~{::content})}">
<body th:fragment="content">

# Syntax
This page explains the general structure of programs in the language.
This is useful for understanding other pages in the wiki.

## Expressions
Expressions are the core building blocks for computation.
They return values when evaluated. 
An expression can be the application of some operator or a literal value.
Expressions are combined inside each other to form more complex expressions.

You can read more on different kinds of expressions on the following pages:
* <a th:href="@{/syntax/operators}">Operators</a>
* <a th:href="@{/syntax/literals}">Literals</a>
* <a th:href="@{/syntax/variables}">Variables</a>
* <a th:href="@{/syntax/functions}">Functions</a>
* <a th:href="@{/syntax/root-object}">Root Object</a>

## References
Some expressions are references. These are expressions that refer to a place that stores a value.
When evaluated normally, they return the value stored at that place, 
but the can also be assigned to change the value stored there as well as be deleted with the `delete` statement.

Some operations that require references are: 
<a th:href="@{/syntax/operators#assignment}">assignment</a>, 
<a th:href="@{/syntax/operators#property-and-array-access}">property access and array access</a>, 
and <a th:href="@{/syntax/operators#increment-decrement-and-negate}">increment and decrement</a>

## Statements
Statements are parts of code that do something. 
This includes the expression statement, that evaluates an expression (with possible side effects)
and the block statement, that groups statements together and creates a new scope
as well as many others. See <a th:href="@{/syntax/statements}">the full page</a> for more information.

</body>
</html>
