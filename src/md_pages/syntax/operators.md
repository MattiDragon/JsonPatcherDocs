<html lang="en" th:replace="~{page::page('Operators', ~{::content})}">
<body th:fragment="content">

# Operators
This page has a list of all operators for quick lookup. 
It's worth to read the [precedence section](#precedence) to understand how operators interact with each other.
More info on some operators is available at the <a th:href="@{language/types.html}>types page</a>.

## Table of Contents
* [Table of Contents](#table-of-contents)
* [Arithmetic Operators](#arithmetic-operators)
* [Bitwise Operators](#bitwise-operators)
* [Comparison Operators](#comparison-operators)
* [Logical Operators](#logical-operators)
* [Equality Operators](#equality-operators)
* [Assignment](#assignment)
* [The `in` operator](#the-in-operator)
* [The `is` operator](#the-is-operator)
* [Property and Array Access](#property-and-array-access)
* [Function Call](#function-call)
* [Increment, Decrement and Negate](#increment-decrement-and-negate)
* [Multiplication of Arrays and Strings](#multiplication-of-arrays-and-strings)
* [Concatenation of Arrays, Strings and Objects](#concatenation-of-arrays-strings-and-objects)
* [Precedence](#precedence)

## Arithmetic Operators
These operators are used to perform arithmetic operations on numbers.
They are infix operators unless noted otherwise.

| Operator     | Description    |
|--------------|----------------|
| `+`          | Addition       |
| `-`          | Subtraction    |
| `-` (prefix) | Negation       |
| `*`          | Multiplication |
| `/`          | Division       |
| `%`          | Modulo         |
| `**`         | Exponentiation |


## Bitwise Operators
These operators are used to perform bitwise operations on numbers.
As JsonPatches only has floating point numbers, 
both numbers are converted to 32-bit signed integers before the operation.
These operators can also be used as non-shorting versions of their boolean counterparts.
They are infix operators unless noted otherwise.

| Operator     | Description |
|--------------|-------------|
| `^`          | Bitwise XOR |
| `&`          | Bitwise AND |
| `\|`         | Bitwise OR  |
| `~` (prefix) | Bitwise NOT |

## Comparison Operators
These operators are used to compare numbers. They are all infix operators.

| Operator | Description              |
|----------|--------------------------|
| `<`      | Less than                |
| `>`      | Greater than             |
| `<=`     | Less than or equal to    |
| `>=`     | Greater than or equal to |

## Logical Operators
These operators are used to perform logical operations on booleans.
They are short-circuiting infix operators unless noted otherwise. 
Meaning that if the first operand is enough to determine the result, the second operand won't be evaluated.

| Operator     | Description                    |
|--------------|--------------------------------|
| `!` (prefix) | Logical NOT                    |
| `&&`         | Logical AND                    |
| `\|\|`       | Logical OR                     |
| `^`          | Logical XOR (can't be shorted) |

## Equality Operators
The equality operator `==` is used to check if two values are equal.
The inequality operator `!=` is used to check if two values are not equal.
The following rules are used in equality checks:
1. If the types are different then the values are not equal.
2. If the values are strings, booleans, numbers or null they are compared by value.
3. If the values are arrays, objects or functions they are compared by identity.

## Assignment
The assignment operator is used to assign a value to a variable. 
It is a binary infix operator with the syntax `variable = value`.
All assignment operators return the assigned value, although utilizing this often leads to confusing code.

Assignment can be combined with other infix operators, except for shorting boolean operators and comparison operators, to create compound assignment operators.
They work by applying the other operator on the variable and the value and then assigning the result to the variable.

```jsonpatcher
var a = 1;
a = 2; # a is now 2
a += 3; # a is now 5
a -= 2; # a is now 3
a *= 2; # a is now 6
```

## The `in` operator
The in operator is used to check if a key is present in an object or if a value is present in an array.
It is a binary infix operator with the syntax `key in object` or `value in array`.
It returns a boolean. Note that this compares the identity of objects and arrays like the equality operators.

```jsonpatcher
var a = [1, 2, 3];
1 in a; # true
4 in a; # false

var b = {a: 1, b: 2};
"a" in b; # true
"c" in b; # false
```

## The `is` operator
The is operator is used to check the type of a value.
It might look like an infix operator, but it's actually a prefix operator. 

The available types are: `number`, `string`, `boolean`, `null`, `array`, `object`, `function`.
These are not values (besides null) and can't be used outside the is operator. You also can't use a variable as the type.

```jsonpatcher
var a = 1;
a is number; # true
a is string; # false
```

## Property and Array Access
The dot operator `.` is used to access properties on objects through literal keys.
Literal names can be surrounded by single quotes to escape and names that would otherwise be invalid.
When the key you want to access is dynamic you can use a variable with the bracket operator `[]` instead.
This is also how you access arrays, albeit with numeric keys. 
All these operators form <a th:href="@{/syntax/index.html#references}">references</a> which can be used in operators that assign values.

```jsonpatcher
var a = {a: 1, b: 2};
a.a; # 1
a["b"]; # 2

var b = [1, 2, 3];
b[0]; # 1
b[1]; # 2
```

## Function Call
The function call operator `()` is used to call functions.
It's a postfix operator, meaning that it's used after the function reference.
It can be used on any value, but will error if it's not a function.
You can pass arguments as a comma separated list in the parentheses.
For more information on functions see <a th:href="@{/syntax/functions.html}>Functions</a>.

```jsonpatcher
function a() {
    return 1;
};

a(); # 1

function b(a, b) {
    return a + b;
};

b(1, 2); # 3
```

## Increment, Decrement and Negate
The increment operator `++` is used to increment a number by one. It can be used as a prefix or postfix operator.
The same applies to `--`, which decrements a number by one and `!!` which negates booleans.
These operators have to be used on <a th:href="@{/syntax/index.html#references}">references</a>.
If they are used as prefix operators, they return the value before modification.
If they are used as postfix operators, they return the value after modification.

```jsonpatcher
var a = 1;
++a; # 2
a++; # 2
a; # 3

var b = true;
b!!; # true
b; # false
```

## Multiplication of Arrays and Strings
Arrays and strings can be multiplied with numbers. 
This creates a new array or string with the original repeated the specified number of times.

```jsonpatcher
var a = [1, 2, 3];
a * 3; # [1, 2, 3, 1, 2, 3, 1, 2, 3]

var b = "test";
b * 3; # "testtesttest"
```

## Concatenation of Arrays, Strings and Objects
Arrays, strings and objects can be concatenated with the `+` operator.
For arrays and strings, this creates a new array or string with the original and the second concatenated.
For objects, this creates a new object with the keys and values from both objects, where the second object overwrites any keys that are present in both.

```jsonpatcher
var a = [1, 2, 3];
var b = [4, 5, 6];
a + b; # [1, 2, 3, 4, 5, 6]

var c = "test";
var d = "ing";
c + d; # "testing"

var e = {a: 1, b: 2};
var f = {b: 3, c: 4};
e + f; # {a: 1, b: 3, c: 4}
```

## Precedence
Operators have a precedence that determines the order in which they are evaluated.
This is the same as in most languages with c-like syntax, but I'll list them here for completeness.

| Precedence                | Operator(s)                             |
|---------------------------|-----------------------------------------|
| Assignment                | `=`, `+=` (and co.)                     |
| Boolean OR                | `\|\|`                                  |
| Boolean AND               | `&&`                                    |
| Bitwise OR                | `\|`                                    |
| Bitwise (and boolean) XOR | `^`                                     |
| Bitwise AND               | `&`                                     |
| Equality                  | `==`, `!=`                              |
| Comparison                | `<`, `>`, `<=`, `>=`, `in`              |
| Sum                       | `+`, `-`                                |
| Product                   | `*`, `/`, `%`                           |
| Exponentiation            | `**`                                    |
| Prefix                    | `!`, `-`, `~`, `++`, `--`, `!!`         |
| Postfix                   | `.`, `[]`, `()`, `++`, `--`, `!!`, `is` |

</body>
</html>
