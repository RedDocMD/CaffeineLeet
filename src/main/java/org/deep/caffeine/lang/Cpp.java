package org.deep.caffeine.lang;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Cpp extends AbstractLanguage {
    private static final Pattern LANG_REGEX = Pattern.compile("^.*\\.(cpp|cc)$");

    @Override
    public boolean hasFile(String name) {
        var matcher = LANG_REGEX.matcher(name);
        return matcher.matches();
    }

    @Override
    public ProcessResult compile(File file, boolean debug) throws IOException, InterruptedException {
        var fileName = file.getName();
        var fileNameNoExt = fileName.substring(0, fileName.indexOf('.'));
        var compiledFile = new File(file.getParentFile(), fileNameNoExt);

        ProcessBuilder pb;
        if (debug)
            pb = new ProcessBuilder("g++", "-O2", file.toString(), "-o", compiledFile.toString(), "-g");
        else
            pb = new ProcessBuilder("g++", "-O2", file.toString(), "-o", compiledFile.toString());
        var process = pb
                .directory(file.getParentFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        return AbstractLanguage.processResult(process, 10).withFile(compiledFile);
    }
}
