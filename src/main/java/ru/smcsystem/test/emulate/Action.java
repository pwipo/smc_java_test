package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IAction;
import ru.smcsystem.api.dto.IMessage;
import ru.smcsystem.api.enumeration.ActionType;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.ValueType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Action implements IAction {

    private final List<IMessage> messages;

    private final ActionType type;

    public Action(List<IMessage> messages, ActionType type) {
        this.messages = new ArrayList<>(messages);
        if (this.messages.stream().noneMatch(m -> MessageType.ACTION_START.equals(m.getMessageType())))
            this.messages.add(0, new Message(MessageType.ACTION_START, new Date(), new Value(ValueType.INTEGER, 1)));
        if (this.messages.stream().noneMatch(m -> MessageType.ACTION_STOP.equals(m.getMessageType())))
            this.messages.add(new Message(MessageType.ACTION_STOP, new Date(), new Value(ValueType.INTEGER, 1)));

        this.type = type;
    }

    public Action(List<IMessage> messages) {
        this(messages, ActionType.EXECUTE);
    }

    @Override
    public List<IMessage> getMessages() {
        return messages;
    }

    @Override
    public ActionType getType() {
        return type/*.name()*/;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Action{");
        sb.append("messages=").append(messages);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
