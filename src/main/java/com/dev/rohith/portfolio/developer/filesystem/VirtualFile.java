package com.dev.rohith.portfolio.developer.filesystem;

import java.util.function.Supplier;

/**
 * A file in the virtual filesystem. Content is lazily supplied rather than stored as a static
 * string so files backed by live data (e.g. github/profile.txt) always reflect current state.
 */
public final class VirtualFile {

    private final String name;
    private final Supplier<String> content;

    public VirtualFile(String name, Supplier<String> content) {
        this.name = name;
        this.content = content;
    }

    public VirtualFile(String name, String staticContent) {
        this(name, () -> staticContent);
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content.get();
    }
}
