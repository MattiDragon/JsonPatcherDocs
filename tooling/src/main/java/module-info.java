import org.jspecify.annotations.NullMarked;

@NullMarked
module jsonpatcher.docs.tooling {
    requires org.jspecify;
    requires thymeleaf;
    requires org.slf4j;
    requires org.commonmark;
    requires org.commonmark.ext.gfm.strikethrough;
    requires org.commonmark.ext.gfm.tables;

    exports dev.mattidragon.jsonpatcher.docgen;
}