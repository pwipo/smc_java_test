package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IMessage;
import ru.smcsystem.api.dto.IValue;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.ValueType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.util.Date;

public class Message implements IMessage {

    private final MessageType type;
    private final Date date;
    private final IValue value;

    public Message(MessageType type,
                   Date date,
                   IValue value
    ) {
        if (type == null)
            throw new ModuleException("type");
        if (date == null)
            throw new ModuleException("date");
        if (value == null)
            throw new ModuleException("value");

        this.type = type;
        this.date = date;
        this.value = value;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public MessageType getMessageType() {
        return type/*.name()*/;
    }

    @Override
    public ValueType getType() {
        return value.getType();
    }

    @Override
    public Object getValue() {
        return value.getValue();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("type=").append(type);
        sb.append(", date=").append(date);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
