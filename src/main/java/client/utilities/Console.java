package client.utilities;

import client.ClientApp;
import client.commands.ExecuteScriptCommand;
import common.commands.*;
import common.data.Dragon;
import common.dto.CommandFromClientDto;
import common.exceptions.DataCantBeSentException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;


public class Console {
    private final OutputManager outputManager;
    private final InputManager inputManager;
    private final ClientApp clientApp;
    private final Creator creator;
    private final Collection<String> listOfCommands;

    public Console(OutputManager outputManager, InputManager inputManager, ClientApp clientApp,
                   Collection<String> listOfCommands ) {
        this.outputManager = outputManager;
        this.inputManager = inputManager;
        this.clientApp = clientApp;
        this.listOfCommands = listOfCommands;
        this.creator = new Creator( inputManager, outputManager);
    }

    public void start() throws ClassNotFoundException, IOException {
        String input;
        do {
            input = readNextCommand();
            final String[] parsedInp = parseToNameAndArg(input);
            final String commandName = parsedInp[0];
            Serializable commandArg = parsedInp[1];
            String commandArg2 = ""; // only for update command in this case
            if (listOfCommands.contains(commandName)) {
                if ("add".equals(commandName) || "add_if_min".equals(commandName) || "add_if_max".equals(commandName)) {
                    commandArg = Creator.makeDragon();
                }
                if ("update".equals(commandName)) {
                    commandArg2 = (String) commandArg;
                    commandArg = Creator.makeDragon();
                }
                if ("execute_script".equals(commandName)) {
                    new ExecuteScriptCommand((String) commandArg).execute(inputManager);
                } else {
                    try {
                        outputManager.println(
                                clientApp.sendCommand(new CommandFromClientDto(getCommandObjectByName(commandName, commandArg, commandArg2)))
                                        .getOutput().toString()
                        );
                    } catch (DataCantBeSentException e) {
                        outputManager.println("Could not send a command");
                    }
                }

            } else {
                outputManager.println("The command was not found. Please use \"help\" to know about commands.");
            }
        } while (!"exit".equals(input));
    }


    private String[] parseToNameAndArg(String input) {
        String[] arrayInput = input.split(" ");
        String inputCommand = arrayInput[0];
        String inputArg = "";

        if (arrayInput.length >= 2) {
            inputArg = arrayInput[1];
        }

        return new String[]{inputCommand, inputArg};
    }


    private String readNextCommand() throws IOException {
        outputManager.print(">>>");
        try {
            return inputManager.nextLine();
        } catch (NoSuchElementException e) {
            return "exit";
        }
    }

    private AbstractCommand getCommandObjectByName(String commandName, Serializable arg, String arg2) {
        AbstractCommand command;
        switch (commandName) {
            case "add": command = new AddCommand((Dragon) arg);
                break;
            case "help": command = new HelpCommand();
                break;
            case "info": command = new InfoCommand();
                break;
            case "show": command = new ShowCommand();
                break;
            case "update": command = new UpdateCommand((Dragon) arg, (String) arg);
                break;
            case "remove_greater": command = new RemoveGreaterCommand((Dragon) arg);
                break;
            case "clear": command = new ClearCommand();
                break;
//            case "count_greater_than_age": command = new CountGreaterThanAge();
//                break;
//            case "reorder": command = new Reorder();
//                break;
//            case "remove_last": command = new RemoveLast();
//                break;
            case "remove_by_id": command = new RemoveByIdCommand((String) arg);
                break;
//            case "remove_all_by_weight": command = new RemoveAllByWeight();
//                break;
//            case "print_field_descending_character": command = new PrintFieldDescendingCharacter();
//                break;
            default: System.out.println("Команда не найдена. Введите help для справки");
                break;
        }
        return command;
    }
}

