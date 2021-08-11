package org.deep.caffeine.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmptyFileTreeNode extends FileTreeNode {
    public static EmptyFileTreeNode INSTANCE = new EmptyFileTreeNode();

    private EmptyFileTreeNode() {
        super(new File(""), false);
    }

    @Override
    public List<FileTreeNode> children() {
        return new ArrayList<>();
    }

    @Override
    public int childCount() {
        return 0;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean hasChild(FileTreeNode node) {
        return false;
    }
}
