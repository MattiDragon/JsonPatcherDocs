import org.jspecify.annotations.NullMarked;

@NullMarked
module jsonpatcher.docs.tooling {
    requires org.jspecify;
    requires thymeleaf;
    requires org.slf4j;
    requires org.commonmark;
    requires org.commonmark.ext.gfm.strikethrough;
    requires org.commonmark.ext.gfm.tables;
    requires org.commonmark.ext.gfm.alerts;
    requires org.commonmark.ext.heading.anchor;
    requires jsonpatcher.tools.doctool;
    requires jsonpatcher.lang.parser;
    requires jsonpatcher.lang.ast;

    exports dev.mattidragon.jsonpatcher.docgen;
}