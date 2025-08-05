package dev.mattidragon.jsonpatcher.docgen;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.StandardLinkBuilder;

import java.util.Map;

class OfflineLinkBuilder extends StandardLinkBuilder {
    private final String contextPath;

    OfflineLinkBuilder(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    protected String computeContextPath(IExpressionContext context, String base, Map<String, Object> parameters) {
        return contextPath;
    }
}
