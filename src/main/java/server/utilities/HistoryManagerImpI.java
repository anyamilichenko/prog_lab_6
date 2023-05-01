package server.utilities;

import common.utilities.HistoryManager;

import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;

public class HistoryManagerImpI implements HistoryManager {

    private static final int CAPACITY = 14;
    private final Queue<String> history = new ArrayBlockingQueue<>(CAPACITY);

    public void addNote(String note) {
        if (history.size() == CAPACITY) {
            history.remove();
        }
        history.add(note);
    }

    public String niceToString() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("The last 14 commands were: ");
        for (String commandName : history) {
            stringJoiner.add(commandName);
        }
        return stringJoiner.toString();
    }
}
