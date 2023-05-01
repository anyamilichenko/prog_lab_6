package server;

import common.utilities.CollectionManager;
import common.utilities.HistoryManager;
import server.utilities.CollectionManagerImpl;
import server.utilities.FileManager;
import server.utilities.HistoryManagerImpI;

import java.io.IOException;
import java.util.logging.Logger;

public class Server {

    private static int serverPort;
    private static String serverIp;
    private static String filename;
    private static final int MAX_PORT = 65535;
    private static final Logger LOGGER
            = LoggerFactory.getLogger(Server.class);

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            setParameterValues(args);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            LOGGER.error("Found Invalid arguments. Please use this program as \"java -jar <name> <datafile name> <server port> <ip>\"");
            return;
        }
        CollectionManager collectionManager = new CollectionManagerImpl();
        HistoryManager historyManager = new HistoryManagerImpI();
        FileManager fileManager = new FileManager(filename);
        ServerApp serverApp;
        try {
            serverApp = new ServerApp(historyManager, collectionManager, fileManager, LOGGER);
            serverApp.start(serverPort, serverIp);
        } catch (IOException e) {
            LOGGER.error("An unexpected IO error occurred. The message is: " + e.getMessage());
        }
    }

    private static void setParameterValues(String[] args) throws IllegalArgumentException, IndexOutOfBoundsException {
        filename = args[0];
        serverPort = Integer.parseInt(args[1]);
        if (serverPort > MAX_PORT) {
            throw new IllegalArgumentException("Port number out of range");
        }
        serverIp = args[2];
    }
}
