package org.deep.caffeine.lang;

import java.io.File;
import java.io.IOException;

public interface Language {
    // Checks whether file of name can be of this language
    boolean hasFile(String name);

    // Compiles file and returns the path of the compiled file
    ProcessResult compile(File file, boolean debug) throws IOException, InterruptedException;

    // Runs the file (must be executable)
    ProcessResult run(File file, String input) throws IOException, InterruptedException;
}
