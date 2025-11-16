package dev.mattidragon.jsonpatcher.docgen;

import org.jspecify.annotations.Nullable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

class Util {
    static void clearOutputFolder(Path outFolder) throws IOException {
        Files.walkFileTree(outFolder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        Files.createDirectories(outFolder);
    }

    static String cleanTemplatePrefix(String prefix) {
        if (prefix.endsWith("/")) {
            return prefix;
        } else if (prefix.endsWith("\\")) {
            return prefix.substring(0, prefix.length() - 1) + "/";
        } else {
            return prefix + "/";
        }
    }

    static TemplateEngine prepareTemplateEngine(String templatesFolder) {
        var templateEngine = new TemplateEngine();

        var resolver = new FileTemplateResolver();
        resolver.setPrefix(cleanTemplatePrefix(templatesFolder));
        resolver.setSuffix(".html");
        resolver.setCacheable(true);
        resolver.setName("Main Resolver");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        templateEngine.addTemplateResolver(resolver);

        var linkBuilder = new OfflineLinkBuilder(System.getProperty("renderer.base_url", "/"));
        templateEngine.setLinkBuilder(linkBuilder);
        return templateEngine;
    }
}
