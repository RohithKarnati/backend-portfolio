package com.dev.rohith.portfolio.dto;

import java.util.List;

/**
 * A single employment / deployment history entry.
 * When {@code placeholder} is true, the entry is template data meant to be
 * replaced with real employment history in {@link com.dev.rohith.portfolio.service.ProfileService}.
 */
public record ExperienceEntry(
        String status,
        String role,
        String company,
        String duration,
        List<String> highlights,
        boolean placeholder
) {
}
