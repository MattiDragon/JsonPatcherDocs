package dev.mattidragon.jsonpatcher.docgen;

import dev.mattidragon.jsonpatcher.docs.DocCommentHandler;
import dev.mattidragon.jsonpatcher.docs.data.DocEntry;
import dev.mattidragon.jsonpatcher.docs.tree.DocTree;
import dev.mattidragon.jsonpatcher.docs.tree.DocTreeNamespace;
import dev.mattidragon.jsonpatcher.docs.tree.DocTreeObject;
import dev.mattidragon.jsonpatcher.docs.tree.DocTreeProperty;
import dev.mattidragon.jsonpatcher.docs.type.DocType;
import dev.mattidragon.jsonpatcher.docs.type.FunctionDocType;
import dev.mattidragon.jsonpatcher.docs.write.DocTypeWriter;
import dev.mattidragon.jsonpatcher.lang.ast.meta.TreeMetadata;
import dev.mattidragon.jsonpatcher.lang.error.DiagnosticsBuilder;
import dev.mattidragon.jsonpatcher.lang.parse.Lexer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Render {
    private static final Logger LOGGER = LoggerFactory.getLogger("Site Renderer");

    public static void main(String[] argArray) throws IOException {
        var args = new ArrayList<>(Arrays.asList(argArray));
        var templatesFolder = args.removeFirst();
        var outFolder = Path.of(args.removeFirst());
        var codeDocs = new HashMap<String, Set<Path>>();
        while (!args.isEmpty()) {
            if (!args.removeFirst().equals("-c")) {
                throw new IllegalArgumentException("Unexpected arguments: " + Arrays.toString(argArray));
            }
            var name = args.removeFirst();
            var path = Path.of(args.removeFirst());
            codeDocs.computeIfAbsent(name, _ -> new TreeSet<>())
                    .add(path);
        }

        var templateEngine = Util.prepareTemplateEngine(templatesFolder);
        Util.clearOutputFolder(outFolder);

        var docTrees = new HashMap<String, DocTree>();
        for (var entry : codeDocs.entrySet()) {
            LOGGER.info("Preparing doctree '{}'", entry.getKey());
            docTrees.put(entry.getKey(), buildDocTree(entry.getValue()));
        }

        var context = new Context();
        context.setVariable("trees", docTrees);
        context.setVariable("mdRenderer", ConvertMarkdown.RENDERER);
        context.setVariable("mdParser", ConvertMarkdown.PARSER);
        context.setVariable("util", new UtilFunctions());

        var templatesPath = Path.of(templatesFolder);
        var pagesPath = templatesPath.resolve("pages");

        try (var stream = Files.walk(pagesPath)) {
            stream/*.parallel()*/
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        var templateName = templatesPath.relativize(path).toString().replace('\\', '/');
                        LOGGER.info("Processing template: {}", templateName);
                        try {
                            var outPath = outFolder.resolve(pagesPath.relativize(path));
                            Files.createDirectories(outPath.getParent());
                            try (var writer = Files.newBufferedWriter(outPath)) {
                                templateEngine.process(templateName, context, writer);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to process template: " + templateName, e);
                        }
                    });
        }
        LOGGER.info("Documentation generation completed successfully.");
    }

    private static DocTree buildDocTree(Iterable<Path> paths) throws IOException {
        var diagnostics = new DiagnosticsBuilder();
        var metadata = new TreeMetadata();
        var commentHandler = new DocCommentHandler(diagnostics, metadata);
        for (var file : paths) {
            var code = Files.readString(file);
            Lexer.lex(code, file.toString(), diagnostics, commentHandler);
        }
        return new DocTree(commentHandler.entries());
    }

    public static class UtilFunctions {
        @SuppressWarnings("unused")
        public String formatDocType(DocType type) {
            return DocTypeWriter.write(type);
        }

        @SuppressWarnings("unused")
        public Iterable<DocTreeNamespace> sortedNamespaces(DocTree tree) {
            return tree.namespaces()
                    .values()
                    .stream()
                    .sorted(Comparator.comparing(DocTreeNamespace::description))
                    .toList();
        }

        @SuppressWarnings("unused")
        public Iterable<DocTreeObject> sortedObjects(DocTreeNamespace tree) {
            return tree.objects()
                    .values()
                    .stream()
                    .sorted(Comparator.<DocTreeObject>comparingInt(obj -> switch (obj.entry()) {
                        case DocEntry.GlobalEntry globalEntry -> 0;
                        case DocEntry.LibraryEntry libraryEntry -> 1;
                        case DocEntry.MetadataEntry metadataEntry -> 2;
                        case DocEntry.TypeDeclarationEntry typeDeclarationEntry -> 3;
                        case DocEntry.TypeAliasEntry typeAliasEntry -> 4;
                        case DocEntry.NamespaceEntry namespaceEntry -> 5;
                        case null, default -> 6;
                    }).thenComparing(DocTreeObject::name))
                    .toList();
        }

        @SuppressWarnings("unused")
        public Iterable<DocTreeProperty> sortedProperties(DocTreeObject tree) {
            return tree.properties()
                    .values()
                    .stream()
                    .sorted(Comparator.comparing(p -> p.entry().name()))
                    .toList();
        }

        @SuppressWarnings("unused")
        public String symbolTypeName(@Nullable DocEntry entry) {
            return switch (entry) {
                case DocEntry.GlobalValueEntry _ -> "global value";
                case DocEntry.GlobalLibraryEntry _ -> "global library";
                case DocEntry.LibraryEntry _ -> "library";
                case DocEntry.MetadataEntry _ -> "metadata";
                case DocEntry.NamespaceEntry _ -> "namespace";
                case DocEntry.PropertyEntry e when e.type() instanceof FunctionDocType -> "function";
                case DocEntry.PropertyEntry _ -> "property";
                case DocEntry.TypeAliasEntry _ -> "type alias";
                case DocEntry.TypeDeclarationEntry _ -> "type";
                case null -> "unknown";
            };
        }
    }
}
