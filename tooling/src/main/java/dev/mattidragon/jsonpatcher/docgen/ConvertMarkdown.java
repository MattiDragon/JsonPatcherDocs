package dev.mattidragon.jsonpatcher.docgen;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertMarkdown {
    private static final List<Extension> EXTENSIONS = Arrays.asList(StrikethroughExtension.create(), TablesExtension.create());
    private static final Parser PARSER = Parser.builder()
            .extensions(EXTENSIONS)
            .build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder()
            .extensions(EXTENSIONS)
            .build();

    public static void main(String[] argArray) throws IOException {
        var args = new ArrayList<>(Arrays.asList(argArray));
        var inFolder = Path.of(args.removeFirst());
        var outFolder = Path.of(args.removeFirst());
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unexpected arguments: " + args);
        }

        if (!Files.exists(inFolder) || !Files.isDirectory(inFolder)) {
            throw new IllegalArgumentException("Input folder does not exist or is not a directory: " + inFolder);
        }
        Util.clearOutputFolder(outFolder);

        try (var stream = Files.walk(inFolder)) {
            stream.forEach(path -> {
                if (!Files.isRegularFile(path) || !path.toString().endsWith(".md")) return;
                var relativePath = inFolder.relativize(path);
                var modifiedFileName = relativePath.getFileName().toString().replaceAll("\\.md$", ".html");
                var outPath = outFolder.resolve(relativePath.resolveSibling(modifiedFileName));

                try {
                    Files.createDirectories(outPath.getParent());
                    try (var reader = Files.newBufferedReader(path); var writer = Files.newBufferedWriter(outPath)) {
                        var document = PARSER.parseReader(reader);
                        RENDERER.render(document, writer);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process file: " + path, e);
                }
            });
        }
    }
}
