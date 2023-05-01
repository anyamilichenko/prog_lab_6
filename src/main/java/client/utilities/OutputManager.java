package client.utilities;

import java.io.PrintStream;

public class OutputManager {
    private final PrintStream printStream;

    public OutputManager(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void println(String string) {
        printStream.println(string);
    }

    public void print(String string) {
        printStream.print(string);
    }
}
