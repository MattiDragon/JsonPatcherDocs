<html lang="en" th:replace="~{page::page('Root Object', ~{::content})}">
<body th:fragment="content">

# Types
The jsonpatcher language is dynamically typed, meaning that everything only has a type at runtime.
This makes it easier to write simple scripts, which is the primary purpose of jsonpatcher.

The language has seven types: strings, numbers, booleans, arrays, objects, functions, and null.
The type system is mostly derived from json,
but the addition of functions as values allows for easier creation of libraries.

## Strings
Strings are created by surrounding text with double quotes: `"Hello, world!"`.
String literals in jsonpatcher support basic escaping: `\n`, `\r`, `\t`, `\b`, `\"`, `\'`, `\\`, `\0` (null), `\x` (unicode escape), `\u` (unicode escape).
Strings can be concatenated with `+` and repeated with `*`:
```jsonpatcher
"Hello, " + "world!" # -> "Hello, world!" 
"test" * 3 # -> "testtesttest"
```
There's a standard library of functions available for strings. See <a th:href="@{/symbols/stdlib.html#strings}">Standard Library/Strings</a> for more information.

## Numbers
Numbers are created like in most programming languages. There is only one number type for simplicity: 64-bit floating point numbers.
Scientific notation is not yet supported.

Numbers support the following operations:
* `num1 + num2` Addition
* `num1 - num2` Subtraction
* `num1 * num2` Multiplication
* `num1 / num2` Division
* `num1 % num2` Modulo
* `num1 ** num2` Exponentiation
* `num1 ^ num2` Bitwise XOR on an integer representation
* `num1 & num2` Bitwise AND on an integer representation
* `num1 | num2` Bitwise OR on an integer representation
* `~num` Bitwise NOT on an integer representation
* `-num` Negation

They can also be compared with the usual comparison operators:
`==`, `!=`, `<`, `>`, `<=` and `>=`

Numbers currently don't have any standard library functions, but the `math` library contains trigonometric functions and more.
See <a th:href="@{/symbols/stdlib.html#math}">Standard Library/Math</a> for more information.

## Booleans and Null
Booleans are either `true` or `false`. They are used for comparisons and control flow.

They can be interacted with using the usual operators:
* `!bool` Logical NOT
* `bool1 && bool2` Logical AND
* `bool1 || bool2` Logical OR
* `bool1 ^ bool2` Logical XOR
* `bool1 == bool2` Equality

Null is a special type that only has one value: `null`. It is used to represent the absence of a value.

## Arrays
Arrays store a list of values. They are created by surrounding a comma-separated list of values with square brackets: `[1, 2, 3]`.
The values in an array can be of any type. Arrays can be nested to create multidimensional arrays.

Values from an array can be accessed by index: `array[0]`, `array[index]`.
Arrays can be concatenated using `+` and repeated with `*` like strings.

There's a standard library of functions available for arrays. See <a th:href="@{/symbols/stdlib.html#arrays}">Standard Library/Arrays</a> for more information.

## Objects
Objects store a list of key-value pairs.
They are created by surrounding a comma-separated list of key-value pairs with curly brackets: `{key1: value1, key2: value2}`.

The keys in an object must be strings, but the values can be of any type.
Objects can be combined with `+`. This merges the two objects, overwriting any keys that are present in both with values from the second.

Values from an object can be accessed by key: `object["key"]`, `object[key]`.
Or by using the dot operator: `object.key`.

There is no standard library for objects (yet).

## Functions
Functions are values that can be passed around and called.
For more information on functions, see <a th:href="@{/syntax/functions.html}">Functions</a>.
They have a few standard library functions, viewable at <a th:href="@{/symbols/stdlib.html#functions}">Standard Library/Functions</a>.

</body>
</html>
