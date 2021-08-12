package org.deep.caffeine.lang;

import java.io.*;
import java.util.concurrent.TimeUnit;

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

        return AbstractLanguage.processResult(process, 5);
    }

    static ProcessResult processResult(Process process, int waitTime) throws InterruptedException {
        var exited = process.waitFor(waitTime, TimeUnit.SECONDS);
        if (!exited) {
            return new ProcessResult("", "Process timed out", 1);
        }

        var processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        var output = processOutput.lines().reduce("", (old, line) -> old + "\n" + line);

        var processError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        var error = processError.lines().reduce("", (old, line) -> old + "\n" + line);

        return new ProcessResult(output, error, process.exitValue());
    }
}
