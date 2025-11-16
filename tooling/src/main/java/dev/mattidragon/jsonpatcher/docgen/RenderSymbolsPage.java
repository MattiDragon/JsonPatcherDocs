package dev.mattidragon.jsonpatcher.docgen;

import dev.mattidragon.jsonpatcher.docs.DocCommentHandler;
import dev.mattidragon.jsonpatcher.docs.tree.DocTree;
import dev.mattidragon.jsonpatcher.lang.ast.meta.TreeMetadata;
import dev.mattidragon.jsonpatcher.lang.error.DiagnosticsBuilder;
import dev.mattidragon.jsonpatcher.lang.parse.Lexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class RenderSymbolsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger("Symbol Page Renderer");

    public static void main(String[] argArray) throws IOException {
        var args = new ArrayList<>(Arrays.asList(argArray));
        var templatesFolder = args.removeFirst();
        var template = args.removeFirst();
        var outFile = Path.of(args.removeFirst());
        var inFiles = args.stream().map(Path::of).toList();

        var diagnostics = new DiagnosticsBuilder();
        var metadata = new TreeMetadata();
        var commentHandler = new DocCommentHandler(diagnostics, metadata);
        for (var file : inFiles) {
            var code = Files.readString(file);
            Lexer.lex(code, file.toString(), diagnostics, commentHandler);
        }
        var tree = new DocTree(commentHandler.entries());

        var templateEngine = Util.prepareTemplateEngine(templatesFolder);
        Util.clearOutputFolder(outFile);

        var context = new Context();
        context.setVariable("tree", tree);
        context.setVariable("mdRenderer", ConvertMarkdown.RENDERER);

        Files.createDirectories(outFile.getParent());
        try (var writer = Files.newBufferedWriter(outFile)) {
            templateEngine.process(template, context, writer);
        }

        LOGGER.info("Documentation generation completed successfully.");
    }
}
