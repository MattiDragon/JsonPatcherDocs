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
val cli: Configuration by configurations.creating {
    attributes {
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    }
}
val tooling: Configuration by configurations.creating

abstract class GenDocs : JavaExec() {
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFiles
    abstract val inputFiles: ConfigurableFileCollection
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        group = "documentation"
        description = "Generates HTML documentation from JsonPatcher files."

        classpath(project.configurations["cli"])
        mainModule = "jsonpatcher.tools.cli"

        // TODO: remove once stdlib is fixed
        isIgnoreExitValue = true

        args(
            "docs",
            "--format", "HTML",
            "join"
        )
        argumentProviders.add {
            listOf(
                "-o", outputFile.get().asFile.toString(),
                *inputFiles.asFileTree.filter { it.isFile }.map(File::toString).sorted().toTypedArray()
            )
        }

        doFirst {
            outputFile.get().asFile.parentFile.mkdirs()
        }
        doLast {
            if (outputFile.get().asFile.length() == 0L) {
                outputFile.get().asFile.writeText("<p>No documentation available.</p>\n")
            }
        }
    }

    override fun exec() {
        if (inputFiles.asFileTree.filter { it.isFile }.isEmpty) {
            outputFile.asFile.get().writeText("")
        } else {
            super.exec()
        }
    }
}

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
    cli(libs.jsonpatcher.tools.cli)
    tooling(project(":tooling"))

    stdlib(libs.jsonpatcher.lang.stdlib)
    mod(libs.jsonpatcher.mod)
}

val extractStdlib by tasks.registering(Sync::class) {
    configureExtractTask(stdlib) {
        include("stdlib/*")
        exclude("stdlib/internal_docs.jsonpatch")
    }
    into(layout.buildDirectory.dir("source/stdlib"))
}

val genStdlibDocs by tasks.registering(GenDocs::class) {
    inputFiles.from(extractStdlib.get().outputs.files)
    outputFile = layout.buildDirectory.file("code_docs/stdlib.html")
}

val extractModDocs by tasks.registering(Sync::class) {
    configureExtractTask(mod) {
        include("docs/*")
    }
    into(layout.buildDirectory.dir("source/mod/general"))
}

val genModDocs by tasks.registering(GenDocs::class) {
    inputFiles.from(extractModDocs.get().outputs.files)
    outputFile = layout.buildDirectory.file("code_docs/mod.html")
}

val extractModDataDocs by tasks.registering(Sync::class) {
    configureExtractTask(mod, 3) {
        include("data/*/jsonpatch/**")
    }
    into(layout.buildDirectory.dir("source/mod/data"))
}

val genModDataDocs by tasks.registering(GenDocs::class) {
    inputFiles.from(extractModDataDocs.get().outputs.files)
    outputFile = layout.buildDirectory.file("code_docs/mod-data.html")
}

val extractModAssetDocs by tasks.registering(Sync::class) {
    configureExtractTask(mod, 3) {
        include("assets/*/jsonpatch/**")
    }
    into(layout.buildDirectory.dir("source/mod/assets"))
}

val genModAssetDocs by tasks.registering(GenDocs::class) {
    inputFiles.from(extractModAssetDocs.get().outputs.files)
    outputFile = layout.buildDirectory.file("code_docs/mod-assets.html")
}

val genDocs by tasks.registering {
    group = "documentation"
    description = "Generates all documentation files from JsonPatcher code."

    dependsOn(
        genStdlibDocs,
        genModDocs,
        genModDataDocs,
        genModAssetDocs
    )
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

    from(layout.buildDirectory.file("code_docs")) {
        into("code_docs")
    }
    from(convertMarkdown.get().outputs.files.singleFile) {
        into("pages")
    }
    from(layout.projectDirectory.file("src/pages")) {
        into("pages")
    }

    from(layout.projectDirectory.file("src/templates"))
    into(layout.buildDirectory.dir("templates"))

    dependsOn(genDocs, convertMarkdown)
}

val renderDocumentation by tasks.registering(JavaExec::class) {
    group = "documentation"
    description = "Renders the documentation using the templates and generated files."

    classpath(tooling)
    mainClass = "dev.mattidragon.jsonpatcher.docgen.Render"

    val inDir = copyTemplates.get().outputs.files
    val outDir = layout.buildDirectory.dir("rendered")

    args(
        inDir.singleFile,
        outDir.get().asFile
    )

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