package common.commands;

import common.dto.CommandResultDto;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("", "help");
    }

    @Override
    public CommandResultDto execute(
            CollectionManager collectionManager,
            HistoryManager historyManager
    ) {
        historyManager.addNote(this.getName());
        // stream api would not help
        return new CommandResultDto(
                "•\thelp : вывести справку по доступным командам\n" +
                            "•\tinfo : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                            "•\tshow : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                            "•\tadd {element} : добавить новый элемент в коллекцию\n" +
                            "•\tupdate id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                            "•\tremove_by_id id : удалить элемент из коллекции по его id\n" +
                            "•\tclear : очистить коллекцию\n" +
                            "•\tsave : сохранить коллекцию в файл\n" +
                            "•\texecute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                            "•\texit : завершить программу (без сохранения в файл)\n" +
                            "•\tremove_last : удалить последний элемент из коллекции\n" +
                            "•\tremove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                            "•\treorder : отсортировать коллекцию в порядке, обратном нынешнему\n" +
                            "•\tremove_all_by_weight weight : удалить из коллекции все элементы, значение поля weight которого эквивалентно заданному\n" +
                            "•\tcount_greater_than_age age : вывести количество элементов, значение поля age которых больше заданного\n" +
                            "•\tprint_field_descending_character : вывести значения поля character всех элементов в порядке убывания\n");
    }
}