package dev.mattidragon.jsonpatcher.docgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class Render {
    private static final Logger LOGGER = LoggerFactory.getLogger("Site Renderer");

    public static void main(String[] argArray) throws IOException {
        var args = new ArrayList<>(Arrays.asList(argArray));
        var templatesFolder = args.removeFirst();
        var outFolder = Path.of(args.removeFirst());
        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unexpected arguments: " + args);
        }

        var templateEngine = new TemplateEngine();
        var resolver = new FileTemplateResolver();

        resolver.setPrefix(cleanPrefix(templatesFolder));
        resolver.setSuffix(".html");
        resolver.setCacheable(true);
        resolver.setName("Main Resolver");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        templateEngine.addTemplateResolver(resolver);

        Util.clearOutputFolder(outFolder);

        try (var stream = Files.walk(Path.of(templatesFolder).resolve("pages"))) {
            stream.parallel()
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        var relativePath = Path.of(templatesFolder).relativize(path);
                        var templateName = relativePath.toString().replace('\\', '/');
                        var spec = new TemplateSpec(templateName, TemplateMode.HTML);
                        LOGGER.info("Processing template: {}", templateName);
                        try {
                            var context = new Context();
                            try (var writer = Files.newBufferedWriter(outFolder.resolve(relativePath.getFileName()))) {
                                templateEngine.process(spec, context, writer);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to process template: " + templateName, e);
                        }
                    });
        }
        LOGGER.info("Documentation generation completed successfully.");
    }

    private static String cleanPrefix(String prefix) {
        if (prefix.endsWith("/")) {
            return prefix;
        } else if (prefix.endsWith("\\")) {
            return prefix.substring(0, prefix.length() - 1) + "/";
        } else {
            return prefix + "/";
        }
    }
}
