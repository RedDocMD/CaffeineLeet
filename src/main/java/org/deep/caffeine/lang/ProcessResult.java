package org.deep.caffeine.lang;

import java.io.File;
import java.util.Optional;

public class ProcessResult {
    private final String stdoutValue;
    private final String stderrValue;
    private final int exitCode;
    private final Optional<File> createdFile;

    public ProcessResult(String stdoutValue, String stderrValue, int exitCode, File createdFile) {
        this.stdoutValue = stdoutValue;
        this.stderrValue = stderrValue;
        this.exitCode = exitCode;
        this.createdFile = Optional.of(createdFile);
    }

    public String getStdoutValue() {
        return stdoutValue;
    }

    public String getStderrValue() {
        return stderrValue;
    }

    public int getExitCode() {
        return exitCode;
    }

    public Optional<File> getCreatedFile() {
        return createdFile;
    }

    public ProcessResult(String stdoutValue, String stderrValue, int exitCode) {
        this.stdoutValue = stdoutValue;
        this.stderrValue = stderrValue;
        this.exitCode = exitCode;
        this.createdFile = Optional.empty();
    }
}