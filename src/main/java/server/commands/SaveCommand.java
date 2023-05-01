package server.commands;

import common.commands.AbstractCommand;
import common.dto.CommandResultDto;
import common.utilities.CollectionManager;
import common.utilities.HistoryManager;
import server.utilities.FileManager;
import server.utilities.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;

public class SaveCommand extends AbstractCommand {
    private final FileManager fileManager;

    public SaveCommand(FileManager fileManager){
        super("", "save");
        this.fileManager = fileManager;
    }


    @Override
    public CommandResultDto execute(CollectionManager collectionManager, HistoryManager historyManager){
        try{
            fileManager.save(new JsonParser().serialize(collectionManager.getMainData()));
        }catch (FileNotFoundException e) {
            return new CommandResultDto("Возникла проблема с сохранением файла. Пожалуйста, перезапустите программу снова");
        }
        return new CommandResultDto("Данные были успешно сохранены");
    }
}
