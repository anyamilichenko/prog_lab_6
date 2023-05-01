package client.commands;

import client.utilities.InputManager;
import common.dto.CommandResultDto;

import java.io.File;
import java.io.IOException;

public class ExecuteScriptCommand {
    private final String arg;
    public ExecuteScriptCommand(String arg){
        this.arg = arg;
    }

    public void execute(InputManager inputManager){
        try{
            inputManager.connectToFile(new File(arg));
            new CommandResultDto("Начинаем исполнение скрипта");
        }catch (IOException e){
            new CommandResultDto("Возникла проблема с открытием файла. Проверьте, доступен ли он и правильно ли вы записали его в командной строке");
        }catch (UnsupportedOperationException e){
            new CommandResultDto(e.getMessage());
        }
    }
}
