package dev.mattidragon.jsonpatcher.docgen;

import dev.mattidragon.jsonpatcher.docs.DocCommentHandler;
import dev.mattidragon.jsonpatcher.docs.tree.DocTree;
import dev.mattidragon.jsonpatcher.docs.type.DocType;
import dev.mattidragon.jsonpatcher.docs.write.DocTypeWriter;
import dev.mattidragon.jsonpatcher.lang.ast.meta.TreeMetadata;
import dev.mattidragon.jsonpatcher.lang.error.DiagnosticsBuilder;
import dev.mattidragon.jsonpatcher.lang.parse.Lexer;
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
            codeDocs.computeIfAbsent(name, n -> new TreeSet<>())
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
        context.setVariable("util", new Object() {
            public String formatDocType(DocType type) {
                return DocTypeWriter.write(type);
            }

            public String locatedDocRef(String ref) {
                return "";
            }
        });

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
}
