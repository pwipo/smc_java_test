package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IValue;
import ru.smcsystem.api.enumeration.ValueType;

import java.util.Objects;

public class Value implements IValue {

    private ValueType type;

    private Object value;

    public Value(ValueType type, Object value) {
        switch (type) {
            case STRING:
                if (!(value instanceof String))
                    throw new IllegalArgumentException();
                break;
            case BYTE:
                if (!(value instanceof Byte))
                    throw new IllegalArgumentException();
                break;
            case SHORT:
                if (!(value instanceof Short))
                    throw new IllegalArgumentException();
                break;
            case INTEGER:
                if (!(value instanceof Integer))
                    throw new IllegalArgumentException();
                break;
            case LONG:
                if (!(value instanceof Long))
                    throw new IllegalArgumentException();
                break;
            case FLOAT:
                if (!(value instanceof Float))
                    throw new IllegalArgumentException();
                break;
            case DOUBLE:
                if (!(value instanceof Double))
                    throw new IllegalArgumentException();
                break;
            case BYTES:
                if (!(value instanceof byte[]))
                    throw new IllegalArgumentException();
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.type = type;
        this.value = value;
    }

    public Value(Object value) {
        Objects.requireNonNull(value);
        this.value = value;

        if (value instanceof String) {
            type = ValueType.STRING;
        } else if (value instanceof Byte) {
            type = ValueType.BYTE;
        } else if (value instanceof Short) {
            type = ValueType.SHORT;
        } else if (value instanceof Integer) {
            type = ValueType.INTEGER;
        } else if (value instanceof Long) {
            type = ValueType.LONG;
        } else if (value instanceof Float) {
            type = ValueType.FLOAT;
        } else if (value instanceof Double) {
            type = ValueType.DOUBLE;
        } else if (value instanceof byte[]) {
            type = ValueType.BYTES;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ValueType getType() {
        return type/*.name()*/;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
