plugins {
    base
    `jvm-toolchains`
}

val stdlib: Configuration by configurations.creating {
    isCanBeResolved = true
    isTransitive = false
}
val mod: Configuration by configurations.creating {
    isCanBeResolved = true
    isTransitive = false
}
val tooling: Configuration by configurations.creating

fun Sync.configureExtractTask(configuration: Configuration, segmentsToDrop: Int = 1, filter: PatternFilterable.() -> Unit) {
    group = "documentation"
    description = "Extracts files from the ${configuration.name} configuration."

    dependsOn(configuration)
    from(zipTree(configuration.resolve().single())) {
        filter()
        eachFile {
            val segments = relativePath.segments.drop(segmentsToDrop).toTypedArray()
            relativePath = RelativePath(true, *segments)
        }
        includeEmptyDirs = false
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    tooling(project(":tooling"))

    stdlib(libs.jsonpatcher.lang.stdlib)
    mod(libs.jsonpatcher.mod)
}

val extractStdlib by tasks.registering(Sync::class) {
    configureExtractTask(stdlib) {
        include("stdlib/*.jsonpatch")
        exclude("stdlib/internal_docs.jsonpatch")
    }
    into(layout.buildDirectory.dir("source/stdlib"))
}

val extractModDocs by tasks.registering(Sync::class) {
    configureExtractTask(mod) {
        include("docs/*.jsonpatch")
    }
    into(layout.buildDirectory.dir("source/mod/general"))
}

val extractModDataDocs by tasks.registering(Sync::class) {
    configureExtractTask(mod, 3) {
        include("data/*/jsonpatch/**.jsonpatch")
    }
    into(layout.buildDirectory.dir("source/mod/data"))
}

val extractModAssetDocs by tasks.registering(Sync::class) {
    configureExtractTask(mod, 3) {
        include("assets/*/jsonpatch/**.jsonpatch")
    }
    into(layout.buildDirectory.dir("source/mod/assets"))
}

val extractFavicon by tasks.registering(Sync::class) {
    configureExtractTask(mod, 2) {
        include("assets/jsonpatcher/icon.png")
    }
    into(layout.buildDirectory.dir("source/favicon"))
}

val convertMarkdown by tasks.registering(JavaExec::class) {
    group = "documentation"
    description = "Converts Markdown files to HTML."

    classpath(tooling)
    mainClass = "dev.mattidragon.jsonpatcher.docgen.ConvertMarkdown"

    val inDir = layout.projectDirectory.dir("src/md_pages")
    val outDir = layout.buildDirectory.dir("md_docs")

    args(
        inDir.asFile,
        outDir.get().asFile
    )

    outputs.dir(outDir)
    inputs.dir(inDir)
}

val copyTemplates by tasks.registering(Sync::class) {
    group = "documentation"
    description = "Prepares templates for rendering documentation."

    from(convertMarkdown.get().outputs.files.singleFile) {
        into("pages")
    }
    from(layout.projectDirectory.file("src/pages")) {
        into("pages")
    }

    from(layout.projectDirectory.file("src/templates"))
    into(layout.buildDirectory.dir("templates"))

    dependsOn(convertMarkdown)
}

val renderDocumentation by tasks.registering(JavaExec::class) {
    group = "documentation"
    description = "Renders the documentation using the templates."

    classpath(tooling)
    mainClass = "dev.mattidragon.jsonpatcher.docgen.Render"

    val inDir = copyTemplates.get().outputs.files
    val outDir = layout.buildDirectory.dir("rendered")

    args(
        inDir.singleFile,
        outDir.get().asFile
    )
    argumentProviders.add {
        mapOf("stdlib" to extractStdlib, "mod" to extractModDocs, "data" to extractModDataDocs, "assets" to extractModAssetDocs)
            .mapValues { (_, task) ->
                task.get().outputs.files.asFileTree.files.map { it.toString() }
            }
            .flatMap { (name, files) ->
                files.flatMap {
                    file -> listOf("-c", name, file)
                }
            }
    }

    jvmArgumentProviders.add {
        project.property("base_url")?.let {
            listOf(
                "-Drenderer.base_url=${it}"
            )
        } ?: listOf()
    }

    outputs.dir(outDir)
    inputs.dir(inDir.singleFile)
    inputs.property("base_url", project.property("base_url") ?: "")
    dependsOn(copyTemplates)
    dependsOn(extractStdlib, extractModDocs, extractModDataDocs, extractModAssetDocs)
}

val joinSite by tasks.registering(Sync::class) {
    group = "documentation"
    description = "Joins all generated documentation files into a single output directory."

    from(renderDocumentation.get().outputs.files)
    from(layout.projectDirectory.dir("src/static"))
    from(extractFavicon.get().outputs.files)

    into(layout.buildDirectory.dir("out"))
}

tasks.assemble {
    dependsOn(joinSite)
}

tasks.register<Exec>("serve") {
    group = "documentation"
    description = "Serves the generated documentation using a simple HTTP server."

    val contentDir = layout.buildDirectory.dir("out")

    executable(
        // Find a compiler (likely to be jdk) and find the corresponding jwebserver executable
        javaToolchains.compilerFor {
            languageVersion = JavaLanguageVersion.of(21)
        }
            .get()
            .metadata
            .installationPath
            .file("bin/jwebserver")
    )

    args(
        "-d", contentDir.get().asFile,
        "-p", "8080",
        "-b", "::"
    )

    inputs.dir(contentDir)
    dependsOn(joinSite)
}