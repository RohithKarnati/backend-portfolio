package com.dev.rohith.portfolio.dto;

import java.util.List;

/**
 * A single portfolio project entry.
 * {@code category} groups related entries in the UI (e.g. "Featured" vs "LLD / Fundamentals").
 * When {@code placeholder} is true, the entry is template data meant to be
 * replaced with a real project in {@link com.dev.rohith.portfolio.service.ProfileService}.
 */
public record ProjectEntry(
        String name,
        String category,
        String description,
        String problem,
        String approach,
        List<String> techStack,
        String githubUrl,
        boolean placeholder
) {
}
