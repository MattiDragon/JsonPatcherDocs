<html lang="en" th:replace="~{page::page('Naming Conventions', ~{::content})}">
<body th:fragment="content">

# Naming Conventions
Naming things is hard, but formatting names is easy. Here's a quick reference for how different names should be formatted:

<div class="language-jsonpatcher">

| Thing                                  | Convention             | Example                                                      |
|----------------------------------------|------------------------|--------------------------------------------------------------|
| Variables                              | `camelCase`            | `var myVariable = getValue();`                               |
| Variables, that are constants          | `SCREAMING_SNAKE_CASE` | `val MY_CONSTANT = 10;`                                      |
| Variables, that represent java classes | `UpperCamelCase`       | `val MyClass = reflection.findClass("com.package.MyClass");` |
| Functions                              | `camelCase`            | `function myFunction() {}`                                   |
| Properties                             | `camelCase`            | `myValue.myProperty`                                         |
| <span class="empty-row-marker"></span> |                        |                                                              |
| Doc namespaces                         | `snake_case`           | `#\| namespace my_namespace`                                 |
| Libraries                              | `snake_case`           | `#\| library my_lib at "mymod:my_lib"`                       |
| Doc type                               | `UpperCamelCase`       | `#\| type my_namespace.MyType: object`                       |
| Doc type aliases                       | `UpperCamelCase`       | `#\| typealias my_namespace.MyTypeAlias: string[]`           |
| Metadata tags                          | `snake_case`           | `@my_metadata;`                                              |
| <span class="empty-row-marker"></span> |                        |                                                              |
| Doc tags                               | `snake_case`           | `#\| @my_tag Custom content`                                 |
| Meta comments                          | `snake_case`           | `# @@doc_namespace my_namespace`                             |

</div>

</body>
</html>
