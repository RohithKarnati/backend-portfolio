package com.dev.rohith.portfolio.dto;

/**
 * A single writing / notes entry.
 * {@code status} follows the same PRODUCTION / DRAFT / ARCHIVED convention as
 * {@link ExperienceEntry} — DRAFT marks a note that's titled but not yet written.
 */
public record NoteEntry(
        String status,
        String title,
        String summary
) {
}
