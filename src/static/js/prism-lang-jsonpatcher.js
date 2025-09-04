Prism.languages["jsonpatcher"] = {
    'string': {
        pattern: /(['"])(?:\\.|(?!\1).)*\1/,
        greedy: true,
        inside: {
            'escape': /\\./
        }
    },
    'comment': {
        pattern: /#.*/,
        greedy: true
    },
    'number': /\b([0-9]+(\.[0-9]*)?)\b/,
    'keyword': /\b(var|val|delete|import|function|apply|if|else|while|foreach|for|return|break|continue|null|as)\b/,
    'boolean': /\b(true|false)\b/,
    'null': {
        pattern: /\bnull\b/,
        alias: "keyword"
    },
    'function': /\b[A-Za-z_][\w$]*(?= *\()/,
    'metadata-tag': {
        pattern: /@[A-Za-z_][\w$]*\b/,
        alias: ["annotation", "decorator", "atrule"]
    },
    'root': {
        pattern: /(?<!\w)\$/,
        alias: "keyword"
    },
    'operator': /\b(is|in)\b|!=|==|<=|>=|\*=|\+=|-=|\/=|%=|&=|\|=|\^=|&&|\|\||--|\+\+|\*\*|!!|->|[=<>&|^!?@+*/%~-]/,
    'punctuation': /[{}()\[\].,:;]/
}
