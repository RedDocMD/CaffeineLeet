package org.deep.caffeine.lang;

import java.io.*;
import java.util.regex.*;

public class Go extends AbstractLanguage {
    private static final Pattern LANG_REGEX = Pattern.compile("^.*\\.go$");

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

        var pb = new ProcessBuilder("/home/dknite/software/go/bin/go", "build", fileName);
        var process = pb
                .directory(file.getParentFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        return AbstractLanguage.processResult(process, 10).withFile(compiledFile);
    }

    @Override
    public ProcessResult format(File file) throws IOException, InterruptedException {
        var pb = new ProcessBuilder("/home/dknite/software/go/bin/gofmt", "-w", file.toString());
        var process = pb
                .directory(file.getParentFile())
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        return AbstractLanguage.processResult(process, 5);
    }
}
