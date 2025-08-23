<html lang="en" th:replace="~{page::page('Root Object', ~{::content})}">
<body th:fragment="content">

# The Root Object
The root object is the object that is passed in to each patch file.
When a patch is being applied to a target file the root will correspond to the target files contents.
When a patch is being imported as a library the root will be an empty object that will be given to the importer as a variable.

## The `$` 
The `$` syntax is used to get the root object.
It can be used like any expression, but it isn't a <a th:href="@{/syntax/index.html#references}">reference</a>.
It can be used as a function parameter, but it can't be assigned to.

```py
val root = $; # You can store the root object in a variable

function a() {
    return $; # You can return the root object from a function
}

apply ($) { # You can use the root object as the root object (don't do this)
}
```

## Implicit Property Access
When accessing a property of the root object you can omit the dot.
This changes nothing functionally, but makes your code a bit more readable.
```py
$a; # This is the same as $.a
```

## Apply Statements
Apply statements allow you to execute code with a different root.
This is useful if you have a large piece of code that has to act on a specific part of the json.
```py
apply (a) {
    $b; # This will return the value of a.b
}
```

</body>
</html>
