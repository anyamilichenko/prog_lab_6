package common.commands;

import common.data.Dragon;
import common.dto.CommandResultDto;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;

public class RemoveGreaterCommand extends AbstractCommand{
    public RemoveGreaterCommand(Dragon arg){
        super(arg, "remove_greater");
    }

    @Override
    public CommandResultDto execute(CollectionManager collectionManager, HistoryManager historyManager){
        historyManager.addNote(this.getName());
        Dragon dragon = (Dragon) arg;
        collectionManager.getMainData().removeIf(x->x.compareTo(dragon) > 0);
        return new CommandResultDto("Greater elements были успешно удалены");
    }
}
