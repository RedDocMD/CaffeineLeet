package org.deep.caffeine.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileTreeNode {
    private final File file;
    private List<FileTreeNode> children;
    private boolean isRoot;

    public FileTreeNode(File file, boolean isRoot) {
        this.file = file;
        this.isRoot = isRoot;
    }

    public FileTreeNode(File file) {
        this(file, false);
    }

    public List<FileTreeNode> children() {
        if (this.children != null) {
            return this.children;
        }
        var childNames = file.list();
        if (childNames == null) {
            return null;
        } else {
            var childFiles = Arrays.stream(childNames)
                    .map(name -> new File(file, name))
                    .sorted((left, right) -> {
                        var rightIsDir = right.isDirectory();
                        var leftIsDir = left.isDirectory();
                        if (leftIsDir && !rightIsDir)
                            return -1;
                        if (rightIsDir && !leftIsDir)
                            return -1;
                        return right.getName().compareTo(left.getName());
                    })
                    .map(FileTreeNode::new)
                    .collect(Collectors.toList());
            this.children = childFiles;
            return childFiles;
        }
    }

    @Override
    public String toString() {
        if (file.exists())
            return isRoot ? file.getPath() : file.getName();
        else
            return "<unknown>";
    }

    public int childCount() {
        return this.children().size();
    }

    public boolean isLeaf() {
        return !this.file.isDirectory();
    }

    public boolean hasChild(FileTreeNode node) {
        if (node == null)
            return false;
        var currPath = file.getPath();
        var nodePath = node.file.getPath();
        return nodePath.startsWith(currPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileTreeNode that = (FileTreeNode) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
