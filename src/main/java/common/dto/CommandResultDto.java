package common.dto;

import java.io.Serializable;

public class CommandResultDto implements Serializable {
    private final Serializable output;
    public CommandResultDto(Serializable output){
        this.output = output;
    }

    public Serializable getOutput(){
        return output;
    }

    @Override
    public  String toString(){
        return "CommandResult{"
                + "output='" + output + '\''
                + '}';
    }
}
