package org.deep.caffeine.model;

import org.deep.caffeine.lang.Language;

import java.io.File;

public class InterfaceModel {
    private File mainDirectory;
    private File selectedFile;
    private Language selectedFileLanguage;

    public Language getSelectedFileLanguage() {
        return selectedFileLanguage;
    }

    public void setSelectedFileLanguage(Language selectedFileLanguage) {
        this.selectedFileLanguage = selectedFileLanguage;
    }

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

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public InterfaceModel() {
        this.mainDirectory = new File("");
        this.selectedFile = null;
    }
}
