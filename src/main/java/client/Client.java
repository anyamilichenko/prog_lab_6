package client;

import client.utilities.Console;
import client.utilities.InputManager;
import client.utilities.OutputManager;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class Client {
    private static final int MAX_PORT = 65635;
    private static final Collection<String> LIST_OF_COMMANDS = new HashSet<>();
    private static int serverPort;
    private static int clientPort;
    private static String clientIp;
    private static String serverIp;

    private Client() {
        throw new UnsupportedOperationException("Это служебный класс, и его экземпляр не может быть создан");
    }


    public static void main(String[] args) {
        OutputManager outputManager = new OutputManager(System.out);
        initCommandList();
        try {
            initArgs(args);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            outputManager.println("Found invalid arguments. Please use the program as \"java -jar <name> <server port> <client port> <clientIp> <serverIp>\"");
            return;
        }
        try {
            ClientApp clientApp = new ClientApp(clientPort, serverPort, clientIp, serverIp);
            new Console(outputManager, new InputManager(System.in), clientApp, LIST_OF_COMMANDS).start();
        } catch (ClassNotFoundException e) {
            outputManager.println("Found incorrect data from server.");
        } catch (IOException e) {
            outputManager.println("Something went wrong with IO, the message is: " + e.getMessage());
        }
    }

    private static void initCommandList() {
        Client.LIST_OF_COMMANDS.add("add");
        Client.LIST_OF_COMMANDS.add("help");
        Client.LIST_OF_COMMANDS.add("info");
        Client.LIST_OF_COMMANDS.add("exit");
        Client.LIST_OF_COMMANDS.add("show");
        Client.LIST_OF_COMMANDS.add("update");
        Client.LIST_OF_COMMANDS.add("remove_greater");
        Client.LIST_OF_COMMANDS.add("clear");
        Client.LIST_OF_COMMANDS.add("count_greater_than_age");
        Client.LIST_OF_COMMANDS.add("reorder");
        Client.LIST_OF_COMMANDS.add("remove_last");
        Client.LIST_OF_COMMANDS.add("remove_by_id");
        Client.LIST_OF_COMMANDS.add("remove_all_by_weight");
        Client.LIST_OF_COMMANDS.add("print_field_descending_character");
        Client.LIST_OF_COMMANDS.add("execute_script");
    }

    private static void initArgs(String[] args) throws IllegalArgumentException, IndexOutOfBoundsException {

        serverPort = Integer.parseInt(args[0]);
        clientPort = Integer.parseInt(args[1]);
        if (serverPort > MAX_PORT || clientPort > MAX_PORT || serverPort < 0 || clientPort < 0) {
            throw new IllegalArgumentException("Port number out of range");
        }
        clientIp = args[2];
        final int three = 3;
        serverIp = args[three];
    }
}
