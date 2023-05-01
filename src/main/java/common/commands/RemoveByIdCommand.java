package common.commands;

import common.dto.CommandResultDto;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;

public class RemoveByIdCommand extends AbstractCommand{
    public RemoveByIdCommand(String arg){
        super(arg, "remove_by_id");
    }

    @Override
    public CommandResultDto execute(CollectionManager collectionManager, HistoryManager historyManager){
        historyManager.addNote(this.getName());
        Long longArg;
        try {
            longArg = Long.parseLong((String) arg);
        }catch (NumberFormatException e){
            return new CommandResultDto("Ваш аргумент некорректен. Команда не была исполнена");
        }
        if (collectionManager.getMainData().removeIf(x-> x.getID() == longArg)){
            return new CommandResultDto("Элемент был успешно удален");
        }else {
            return new CommandResultDto("Не удалось найти идентификатор. Команда не была выполнена");
        }
    }
}
