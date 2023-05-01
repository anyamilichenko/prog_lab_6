package common.commands;

import common.data.Dragon;
import common.dto.CommandResultDto;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;

import java.nio.channels.NotYetBoundException;

public class UpdateCommand extends AbstractCommand {

    private final String idArg;

    public UpdateCommand(Dragon dragon, String id) {
        super(dragon, "update");
        this.idArg = id;
    }

    @Override
    public CommandResultDto execute(CollectionManager collectionManager, HistoryManager historyManager){
        historyManager.addNote(this.getName());
        Long longArg;
        try {
            longArg = Long.parseLong(idArg);
        }catch (NumberFormatException e){
            return new CommandResultDto("Ваш аргумент некорректен. Команда не выполнена");
        }
        if (collectionManager.getMainData().removeIf(x -> x.getID() == longArg)){
            Dragon dragon = (Dragon) arg;
            dragon.setId(longArg);
            collectionManager.getMainData().add(dragon);
            return new CommandResultDto("Элемент был успешно обновлен");
        }else {
            return new CommandResultDto("Идентификатор не найден. Команда не была выполнена");
        }
    }
}
