package com.dev.rohith.portfolio.developer.filesystem;

import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The application-level virtual filesystem backing Developer Mode. Purely an in-memory tree of
 * {@link VirtualDirectory}/{@link VirtualFile} nodes — it never touches the real server filesystem,
 * so there is no path on disk to traverse to or escape from.
 */
@Component
public class VirtualFileSystem {

    public static final String ROOT = "/";
    public static final String HOME = "/home/rohith";

    private final VirtualDirectory root;

    public VirtualFileSystem(TerminalContentService content) {
        this.root = buildTree(content);
    }

    private VirtualDirectory buildTree(TerminalContentService content) {
        VirtualDirectory rohith = new VirtualDirectory("rohith");
        rohith.addFile(new VirtualFile("README.md", content::renderReadme));
        rohith.addFile(new VirtualFile("about.txt", content::renderAbout));
        rohith.addFile(new VirtualFile("contact.txt", content::renderContact));

        VirtualDirectory experience = new VirtualDirectory("experience");
        content.experienceFiles().forEach(experience::addFile);
        rohith.addDirectory(experience);

        VirtualDirectory skills = new VirtualDirectory("skills");
        content.skillFiles().forEach(skills::addFile);
        rohith.addDirectory(skills);

        VirtualDirectory projects = new VirtualDirectory("projects");
        content.projectFiles().forEach(projects::addFile);
        rohith.addDirectory(projects);

        VirtualDirectory github = new VirtualDirectory("github");
        github.addFile(new VirtualFile("profile.txt",
                () -> Optional.ofNullable(content.renderGithubProfile()).orElse("GitHub data unavailable.")));
        rohith.addDirectory(github);

        VirtualDirectory resume = new VirtualDirectory("resume");
        resume.addFile(new VirtualFile("rohith-kumar-resume.pdf",
                "Binary file (application/pdf) — use the 'resume' command or download /resume.pdf"));
        rohith.addDirectory(resume);

        VirtualDirectory home = new VirtualDirectory("home");
        home.addDirectory(rohith);

        VirtualDirectory root = new VirtualDirectory("");
        root.addDirectory(home);
        return root;
    }

    /** Resolves an absolute, already-normalized path (as produced by {@link #normalize}) to a directory. */
    public Optional<VirtualDirectory> resolveDirectory(String absolutePath) {
        VirtualDirectory current = root;
        for (String segment : splitPath(absolutePath)) {
            Optional<VirtualDirectory> next = current.getDirectory(segment);
            if (next.isEmpty()) {
                return Optional.empty();
            }
            current = next.get();
        }
        return Optional.of(current);
    }

    /** Resolves an absolute, already-normalized path to a file. */
    public Optional<VirtualFile> resolveFile(String absoluteDirPath, String fileName) {
        return resolveDirectory(absoluteDirPath).flatMap(dir -> dir.getFile(fileName));
    }

    /**
     * Normalizes {@code target} against {@code currentPath} into an absolute virtual path.
     * Handles "..", ".", "~" and absolute ("/...") targets. The result may not exist — callers
     * check that separately via {@link #resolveDirectory}. Because this only ever manipulates an
     * in-memory string of path segments used to walk our own tree, there is no way for any input
     * (including "../../../etc/passwd"-style traversal attempts) to reach the real filesystem.
     */
    public String normalize(String currentPath, String target) {
        if (target.equals("~")) {
            return HOME;
        }
        if (target.equals("/")) {
            return ROOT;
        }

        List<String> segments = target.startsWith("/")
                ? new ArrayList<>()
                : new ArrayList<>(splitPath(currentPath));

        for (String segment : target.split("/")) {
            if (segment.isBlank() || segment.equals(".")) {
                continue;
            }
            if (segment.equals("~")) {
                segments = new ArrayList<>(splitPath(HOME));
            } else if (segment.equals("..")) {
                if (!segments.isEmpty()) {
                    segments.remove(segments.size() - 1);
                }
            } else {
                segments.add(segment);
            }
        }

        return ROOT + String.join("/", segments);
    }

    private static List<String> splitPath(String path) {
        List<String> segments = new ArrayList<>();
        if (path == null || path.isBlank()) {
            return segments;
        }
        for (String part : path.split("/")) {
            if (!part.isBlank()) {
                segments.add(part);
            }
        }
        return segments;
    }
}
