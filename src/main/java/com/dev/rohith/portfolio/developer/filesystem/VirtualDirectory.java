package com.dev.rohith.portfolio.developer.filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** A directory in the virtual filesystem — an in-memory node, never backed by a real path. */
public final class VirtualDirectory {

    private final String name;
    private final Map<String, VirtualDirectory> directories = new LinkedHashMap<>();
    private final Map<String, VirtualFile> files = new LinkedHashMap<>();

    public VirtualDirectory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VirtualDirectory addDirectory(VirtualDirectory directory) {
        directories.put(directory.getName(), directory);
        return this;
    }

    public VirtualDirectory addFile(VirtualFile file) {
        files.put(file.getName(), file);
        return this;
    }

    public Optional<VirtualDirectory> getDirectory(String name) {
        return Optional.ofNullable(directories.get(name));
    }

    public Optional<VirtualFile> getFile(String name) {
        return Optional.ofNullable(files.get(name));
    }

    /** Directory names first (suffixed with "/"), then file names, alphabetical within each group. */
    public List<String> listEntries() {
        List<String> dirNames = new ArrayList<>();
        directories.keySet().forEach(d -> dirNames.add(d + "/"));
        Collections.sort(dirNames);

        List<String> fileNames = new ArrayList<>(files.keySet());
        Collections.sort(fileNames);

        List<String> entries = new ArrayList<>(dirNames);
        entries.addAll(fileNames);
        return entries;
    }
}
