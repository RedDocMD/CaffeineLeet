package org.deep.caffeine.lang;

import java.io.*;
import java.util.regex.*;

public class Python extends AbstractLanguage {
    private static final Pattern LANG_REGEX = Pattern.compile("^.*\\.py$");

    @Override
    public boolean hasFile(String name) {
        var matcher = LANG_REGEX.matcher(name);
        return matcher.matches();
    }

    @Override
    public boolean hasCompiler() {
        return false;
    }

    @Override
    public ProcessResult compile(File file, boolean debug) {
        throw new UnsupportedOperationException("Python cannot compile");
    }

    @Override
    public ProcessResult format(File file) throws IOException, InterruptedException {
        var pb = new ProcessBuilder("black", file.toString());
        var process = pb
                .directory(file.getParentFile())
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        return AbstractLanguage.processResult(process, 10);
    }

    @Override
    public ProcessResult run(File file, String input) throws IOException, InterruptedException {
        var process = new ProcessBuilder()
                .command("python3", file.toString())
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        var processInput = new BufferedWriter(new PrintWriter(process.getOutputStream()));
        processInput.write(input);
        processInput.flush();
        processInput.close();

        return AbstractLanguage.processResult(process, 5);
    }
}
