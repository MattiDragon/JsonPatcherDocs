<html lang="en" th:replace="~{page::page('Loops', ~{::content})}">
<body th:fragment="content">

# Loops
Loops in jsonpatcher work like they do in most languages with c-like syntax.
There are three types of loops: `for`, `while`, and `foreach`. A `do while` loop is currently not available.

## For-loop
The for loop consists of three parts: the initializer, the condition, and the incrementer as well as a body.
The initializer is executed once before the loop starts, the condition is checked before each iteration 
and the incrementer is executed after each iteration. The body is executed as long as the condition is true.
```jsonpatcher
for (var i = 0; i < 10; i++) {
    # The body is executed 10 times
}
```
Each part can be omitted. If the condition is omitted, it will always be true. 
If the initializer or incrementer is omitted they will do nothing.
If all three are omitted, you get an infinite loop, but you should probably use a while loop instead.
```jsonpatcher
for (;;) {
    # Infinite loop
}
```

## While-loop
The while loop consists of a condition and a body. The body is executed as long as the condition is true.
While loops are useful when you don't know how many iterations you need to do. 
They are often combined with `break` statements to exit the loop early.
```jsonpatcher
var i = 1;
while (i < 10) {
    i++;
}

while (true) {
    # Infinite loop (done properly)
}
```

## Foreach-loop
The foreach loop is used to iterate over arrays. It consists of a variable name, a value to iterate, and a body.
The body is executed once for each value in the array. A variable with the specified name will hold the current element in the body.
```jsonpatcher
foreach (i in [1, 2, 3]) {
    debug.print(i);
}
```

## Break
The break statement is used to exit a loop early. It can be used in any loop, but they are most common in while and foreach loops.
Break statements must always appear physically contained within the loop body, and may not be nested within functions.
```jsonpatcher
var i = 0;
while (true) {
    i++;
    if (i > 10) {
        break;
    }
}
```

## Continue
The continue statement is used to skip the rest of the current iteration and continue with the next one.
It's useful when you are doing a lot of processing in a loop and know early that an element won't need further work.
Continue statements must always appear physically contained within the loop body, and may not be nested within functions.
```jsonpatcher
foreach (i in [1, 2, 3]) {
    # Skip 2
    if (i == 2) {
        continue;
    }
    debug.print(i);
}
```

</body>
</html>
