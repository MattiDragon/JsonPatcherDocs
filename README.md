# JsonPatcher Docs
This repo contains the source code for the docs for JsonPatcher v2. 
The docs are available at [jsonpatcher-docs.mattidragon.dev](https://jsonpatcher-docs.mattidragon.dev).
Feel free to contribute content if you have something to add.

## Development
The docs page is build using a custom static site generator based on Thymeleaf and commonmark-java.
The build is managed by a gradle buildscript with some additional code in the `tooling` subproject.

Some documentation is automatically generated from doc comments in the JsonPatcher jars.
This is configured in the buildscript and should rarely be touched besides bumping version in the version catalog.
Most of the site is however built from markdown and html files in the `src` directory.
It's divided into 4 subdirectories which each behave differently:

* `md_pages` contains markdown files which get converted to html and then processed like `pages`.
* `pages` contains html and css files, which are templated using thymeleaf and then included in the final site.
* `static` contains resource files which are directly copied into the final page without any processing.
* `templates` contains thymeleaf templates that aren't processed automatically. 
  These can be referred to from `pages` and `md_pages`.

Most content should live in `md_pages` as markdown files with inline html for thymeleaf templating.
`pages` is used for templated css and html which doesn't benefit from markdown.

### Gradle Tasks
| Task       | Purpose                                                                     |
|------------|-----------------------------------------------------------------------------|
| `serve`    | Builds the site and runs a basic html server to view it locally             |
| `joinSite` | Builds the site. Use this to update the files without restarting the server |
| `build`    | Builds the site for shipping. Output will be in `build/out`                 |

The `base_url` gradle property can be set to control the base url for links within the page.
Set this if you intend to host the page somewhere else than the root of the domain.
