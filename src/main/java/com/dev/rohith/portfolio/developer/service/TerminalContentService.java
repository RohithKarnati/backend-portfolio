package com.dev.rohith.portfolio.developer.service;

import com.dev.rohith.portfolio.developer.filesystem.VirtualFile;
import com.dev.rohith.portfolio.dto.ExperienceEntry;
import com.dev.rohith.portfolio.dto.GithubUser;
import com.dev.rohith.portfolio.dto.ProjectEntry;
import com.dev.rohith.portfolio.dto.TechCategory;
import com.dev.rohith.portfolio.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Formats {@link ProfileService} data into terminal text. Used both by the virtual filesystem's
 * files (cat) and by the shortcut commands (about/experience/skills/projects/github/contact), so
 * the two are guaranteed to stay in sync — there is exactly one place each fact is rendered.
 */
@Service
public class TerminalContentService {

    private static final String SEPARATOR = "-".repeat(56);

    private final ProfileService profileService;

    public TerminalContentService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public String renderReadme() {
        return """
                Rohith's Developer Environment
                ===============================

                This is a virtual portfolio filesystem, not a real shell.

                Try:
                  ls            list the current directory
                  cd <dir>      change directory
                  cat <file>    read a file
                  help          list all commands""";
    }

    public String renderAbout() {
        return profileService.getAboutText();
    }

    public String renderContact() {
        return """
                CONTACT
                =======

                Email:
                %s

                LinkedIn:
                %s

                GitHub:
                %s""".formatted(
                profileService.getEmail(),
                profileService.getLinkedinUrl(),
                "https://github.com/" + ProfileService.GITHUB_USERNAME
        );
    }

    /** Formatted GitHub profile, or null if the upstream API is unavailable. */
    public String renderGithubProfile() {
        GithubUser github = profileService.getGithubProfile();
        if (github == null) {
            return null;
        }
        return """
                GitHub
                ------

                Username      %s
                Public Repos  %d
                Followers     %d
                Following     %d

                Profile:
                %s""".formatted(
                github.getLogin(),
                github.getPublic_repos(),
                github.getFollowers(),
                github.getFollowing(),
                github.getHtml_url()
        );
    }

    public List<VirtualFile> experienceFiles() {
        return profileService.getExperience().stream()
                .map(entry -> new VirtualFile(slug(entry.role()) + ".txt", () -> renderExperienceEntry(entry)))
                .collect(Collectors.toList());
    }

    public String renderExperienceEntry(ExperienceEntry entry) {
        String highlights = entry.highlights().stream()
                .map(h -> "  • " + h)
                .collect(Collectors.joining("\n"));

        return """
                [%s]
                %s
                %s
                %s

                Engineering Contributions
                --------------------------
                %s""".formatted(entry.status(), entry.role(), entry.company(), entry.duration(), highlights);
    }

    public String renderAllExperience() {
        String body = profileService.getExperience().stream()
                .map(this::renderExperienceEntry)
                .collect(Collectors.joining("\n\n" + SEPARATOR + "\n\n"));

        return "PROFESSIONAL EXPERIENCE\n========================\n\n" + body;
    }

    public List<VirtualFile> skillFiles() {
        return profileService.getTechStack().stream()
                .map(category -> new VirtualFile(slug(category.name()) + ".txt", () -> renderSkillCategory(category)))
                .collect(Collectors.toList());
    }

    public String renderSkillCategory(TechCategory category) {
        String heading = category.name().toUpperCase();
        return heading + "\n" + "-".repeat(heading.length()) + "\n" + String.join("\n", category.items());
    }

    public String renderAllSkills() {
        String body = profileService.getTechStack().stream()
                .map(this::renderSkillCategory)
                .collect(Collectors.joining("\n\n"));
        return body;
    }

    public List<VirtualFile> projectFiles() {
        return profileService.getProjects().stream()
                .map(entry -> new VirtualFile(slug(entry.name()) + ".txt", () -> renderProject(entry)))
                .collect(Collectors.toList());
    }

    public String renderProject(ProjectEntry entry) {
        return """
                %s

                Stack:
                %s

                Description:
                %s

                Problem:
                %s

                Approach:
                %s

                GitHub:
                %s""".formatted(
                entry.name(),
                String.join(", ", entry.techStack()),
                entry.description(),
                entry.problem(),
                entry.approach(),
                entry.githubUrl()
        );
    }

    public String renderAllProjects() {
        List<ProjectEntry> projects = profileService.getProjects();
        StringBuilder sb = new StringBuilder("PROJECTS\n========\n\n");
        for (int i = 0; i < projects.size(); i++) {
            if (i > 0) {
                sb.append("\n\n").append(SEPARATOR).append("\n\n");
            }
            sb.append(i + 1).append(". ").append(renderProject(projects.get(i)));
        }
        return sb.toString();
    }

    private static String slug(String value) {
        return value.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
