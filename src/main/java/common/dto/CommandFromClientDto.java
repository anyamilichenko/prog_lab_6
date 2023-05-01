package common.dto;

import common.commands.AbstractCommand;

import java.io.Serializable;
import java.util.Objects;

public class CommandFromClientDto implements Serializable {

    private final AbstractCommand abstractCommand;

    public CommandFromClientDto(AbstractCommand abstractCommand) {
        this.abstractCommand = abstractCommand;
    }

    public AbstractCommand getCommand() {
        return abstractCommand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandFromClientDto that = (CommandFromClientDto) o;
        return Objects.equals(abstractCommand, that.abstractCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractCommand);
    }
}
