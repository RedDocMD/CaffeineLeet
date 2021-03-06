package org.deep.caffeine.lang;

import java.io.*;
import java.util.regex.*;

public class Cpp extends AbstractLanguage {
    private static final Pattern LANG_REGEX = Pattern.compile("^.*\\.(cpp|cc)$");

    @Override
    public boolean hasFile(String name) {
        var matcher = LANG_REGEX.matcher(name);
        return matcher.matches();
    }

    @Override
    public boolean hasCompiler() {
        return true;
    }

    @Override
    public ProcessResult compile(File file, boolean debug) throws IOException, InterruptedException {
        var fileName = file.getName();
        var fileNameNoExt = fileName.substring(0, fileName.indexOf('.'));
        var compiledFile = new File(file.getParentFile(), fileNameNoExt);

        ProcessBuilder pb;
        if (debug)
            pb = new ProcessBuilder("g++", "-std=c++17", "-pthread", file.toString(), "-o", compiledFile.toString(), "-g");
        else
            pb = new ProcessBuilder("g++", "-O2", "-std=c++17", "-pthread", file.toString(), "-o", compiledFile.toString());
        var process = pb
                .directory(file.getParentFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        return AbstractLanguage.processResult(process, 10).withFile(compiledFile);
    }

    @Override
    public ProcessResult format(File file) throws IOException, InterruptedException {
        var pb = new ProcessBuilder("clang-format", "-i", file.toString());
        var process = pb
                .directory(file.getParentFile())
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        return AbstractLanguage.processResult(process, 5);
    }
}
