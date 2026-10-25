<html lang="en" th:replace="~{page::page('Doc Conditions', ~{::content})}">
<body th:fragment="content">

# Doc Conditions
Doc conditions are used to describe when a doc entry should be shown as available.
They are written as part of the <a th:href="@{/docs/doc_comments.html}">doc comments</a> system with the `@requires` tag.
Doc conditions are primarily checked by the language server in order to hide inaccessible content.

It should be noted that doc conditions are **not** an access control mechanism.
They're only intended to be used for content that is already unavailable due to other mechanisms.

## Condition Syntax
Conditions are written as function-like expressions: `name(...)`.
Function names and metadata keys must be simple identifiers (`[A-Za-z_][A-Za-z0-9_]*`).

## Available Conditions
### `libgroup("<group>")`
Indicates that a specific library group is needed. 
Library groups are mainly used to restrict access to libraries using java code.
Fake library groups starting with a `+` are also used to indicate content only available to certain types of programs.
Library groups starting with a `%` indicate that a specific feature flag is required.

```jsonpatcher
#| @requires libgroup("reflection")
```

The library groups allowed in a workspace are controlled by the `allowed_library_groups` field in
`jsonpatcher-workspace.json`.

```json
{
  "schema_version": 1,
  "lang_version": [2, 0, 0],
  "allowed_library_groups": ["default", "reflection"]
}
```

#### Existing library groups
| Group                                  | Description                                                                     |
|----------------------------------------|---------------------------------------------------------------------------------|
| `default`                              | The default library group. Most libraries get placed here                       |
| `jsonpatcher:reflection`               | This group contains libraries that are dangerous for untrusted code to access.  |
| `jsonpatcher:internals`                | This group contains libraries that only the jsonpatcher language should access. |
| `jsonpatcher:metapatch`                | This group is available in metapatches, and contains the `metapatch` library.   |
| <span class="empty-row-marker"></span> |                                                                                 |
| `+jsonpatcher:global_scripts`          | This fake group indicates that content is only available for scripts.           |
| `+jsonpatcher:minecraft_data`          | This fake group indicates that content is only available for data patches.      |
| `+jsonpatcher:minecraft_assets`        | This fake group indicates that content is only available for asset patches.     |


### `metadata(<key>)`
Checks whether the current patch has a metadata tag with the given name.
Only the presence of the tag is checked; value comparisons are not supported yet.

```jsonpatcher
#| @requires metadata(metapatch)
```

### `version(<comparison><major>.<minor>.<patch>)`
Checks the language version for the current workspace.
The comparison is one of:

- `>` greater than (strictly newer)
- `<` less than (strictly older)
- `=` exactly equal
- `^` same major version, and at least the given minor/patch
- `~` same major and minor version, and at least the given patch

```jsonpatcher
#| @requires version(>2.0.0)
#| @requires version(^2.1.0)
#| @requires version(~2.1.3)
```

The language version is read from the `lang_version` field in `jsonpatcher-workspace.json`.

## Combining Conditions
Complex conditions can be built by combining other conditions:

- `all(...)` or `and(...)` - all child conditions must match
- `any(...)` or `or(...)` - at least one child condition must match
- `none(...)` - none of the child conditions match (equivalent to `not(any(...))`)
- `not(...)` - negates a single condition

```jsonpatcher
#| @requires all(
#|     version(^2.0.0),
#|     any(libgroup("default"), libgroup("reflection")),
#|     not(metadata(metapatch))
#| )
```

</body>
</html>
