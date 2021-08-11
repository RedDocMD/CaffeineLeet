package org.deep.caffeine.model;

import java.io.File;

public class InterfaceModel {
    private File mainDirectory;

    public File getMainDirectory() {
        return mainDirectory;
    }

    public void setMainDirectory(String mainDirectory) {
        this.mainDirectory = new File(mainDirectory);
    }

    public File getFileChooserDirectory() {
        if (isMainDirectoryValid())
            return mainDirectory.getParentFile();
        else return new File(System.getProperty("user.home"));
    }

    public boolean isMainDirectoryValid() {
        return mainDirectory.exists() && mainDirectory.isDirectory();
    }

    public InterfaceModel() {
        mainDirectory = new File("");
    }
}
