package org.deep.caffeine.lang;

import java.io.*;

public abstract class AbstractLanguage implements Language {
    @Override
    public ProcessResult run(File file, String input) throws IOException, InterruptedException {
        var process = new ProcessBuilder()
                .command(file.toString())
                .directory(file.getParentFile())
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();

        var processInput = new BufferedWriter(new PrintWriter(process.getOutputStream()));
        processInput.write(input);
        processInput.flush();
        processInput.close();

        var exitCode = process.waitFor();

        var processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        var output = processOutput.lines().reduce("", (old, line) -> old + "\n" + line);

        var processError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        var error = processError.lines().reduce("", (old, line) -> old + "\n" + line);

        return new ProcessResult(output, error, exitCode);
    }
}
