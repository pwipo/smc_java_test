package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.ISourceFilter;
import ru.smcsystem.api.enumeration.SourceFilterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SourceFilter implements ISourceFilter {
    private SourceFilterType type;
    private List<Object> params;

    public SourceFilter(SourceFilterType type, List<Object> params) {
        this.type = type;
        this.params = params != null ? new ArrayList<>(params) : new ArrayList<>();
    }

    public SourceFilterType getType() {
        return type;
    }

    public List<Object> getParams() {
        return params;
    }

    @Override
    public int countParams() {
        switch (getType()) {
            case POSITION:
                return 4;
            case NUMBER:
                return 2;
            case STRING_EQUAL:
                return 2;
            case STRING_CONTAIN:
                return 2;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<Object> getParam(int id) {
        return Optional.ofNullable(id >= 0 && id < params.size() ? params.get(id) : null);
    }

}
