package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IAction;
import ru.smcsystem.api.dto.ICommand;
import ru.smcsystem.api.enumeration.CommandType;

import java.util.ArrayList;
import java.util.List;

public class Command implements ICommand {

    private final List<IAction> actions;

    private final CommandType type;

    public Command(List<IAction> actions, CommandType type) {
        this.actions = new ArrayList<>(actions);
        this.type = type;
    }

    @Override
    public List<IAction> getActions() {
        return actions;
    }

    @Override
    public CommandType getType() {
        return type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Command{");
        sb.append("actions=").append(actions);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
