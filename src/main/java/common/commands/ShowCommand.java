package common.commands;

import common.data.Dragon;
import common.dto.CommandResultDto;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;

import java.io.Serializable;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ShowCommand extends AbstractCommand{
    public ShowCommand(){
        super("", "show");
    }

    @Override
    public CommandResultDto execute(
            CollectionManager collectionManager,
            HistoryManager historyManager
    ){
        historyManager.addNote(this.getName());
        return new CommandResultDto((Serializable) collectionManager.getMainData().stream().sorted(Comparator.comparing(Dragon::getName)).collect(Collectors.toList()));
    }
}
