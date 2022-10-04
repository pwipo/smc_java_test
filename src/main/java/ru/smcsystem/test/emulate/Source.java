package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.SourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Source implements ISourceManaged {

    private final ExecutionContextToolImpl executionContextTool;

    private final String configurationName;

    private final String executionContextName;

    private IExecutionContext executionContextSource;

    private IConfiguration configurationSource;

    private Boolean eventDriven;

    private IValue valueSource;

    private SourceType type;

    private Integer order;

    private SourceList sourceList;
    private List<SourceFilter> filters;

    private Source(
            ExecutionContextToolImpl executionContextTool, String configurationName, String executionContextName, IExecutionContext executionContextSource
            , IConfiguration configurationSource
            , IValue valueSource
            , Boolean eventDriven
            , List<ISourceManaged> sources
            , SourceType type
            , Integer order
    ) {
        this.executionContextTool = executionContextTool;
        this.configurationName = configurationName;
        this.executionContextName = executionContextName;
        this.executionContextSource = executionContextSource;
        this.configurationSource = configurationSource;
        this.eventDriven = eventDriven != null ? eventDriven : false;
        this.valueSource = valueSource;
        this.type = type;
        this.order = order;

        if (SourceType.MULTIPART.equals(type)) {
            sourceList = new SourceList(this.executionContextTool, this.configurationName, this.executionContextName, sources);
        } else {
            sourceList = null;
        }
        this.filters = new ArrayList<>();
    }

    public Source(ExecutionContextToolImpl executionContextTool, String configurationName, String executionContextName, IExecutionContext executionContextSource, boolean eventDriven, Integer order) {
        this(executionContextTool, configurationName, executionContextName, executionContextSource, null, null, eventDriven, null, SourceType.EXECUTION_CONTEXT, order);
    }

    public Source(ExecutionContextToolImpl executionContextTool, String configurationName, String executionContextName, IConfiguration configurationSource, boolean eventDriven, Integer order) {
        this(executionContextTool, configurationName, executionContextName, null, configurationSource, null, eventDriven, null, SourceType.MODULE_CONFIGURATION, order);
    }

    public Source(ExecutionContextToolImpl executionContextTool, String configurationName, String executionContextName, IValue valueSource, Integer order) {
        this(executionContextTool, configurationName, executionContextName, null, null, valueSource, null, null, SourceType.STATIC_VALUE, order);
    }

    public Source(ExecutionContextToolImpl executionContextTool, String configurationName, String executionContextName, List<ISourceManaged> sources, Integer order) {
        this(executionContextTool, configurationName, executionContextName, null, null, null, null, sources, SourceType.MULTIPART, order);
    }

    @Override
    public SourceType getType() {
        return type;
    }

    /*
    @Override
    public Object getValue() {
        switch (type) {
            case MODULE_CONFIGURATION:
                return configurationSource;
            case EXECUTION_CONTEXT:
                return executionContextSource;
            case STATIC_VALUE:
                return valueSource;
            case MULTIPART:
                break;
            case CALLER:
                break;
            case CALLER_RELATIVE_NAME:
                return valueSource != null ? valueSource.getValue() : "";
        }
        return null;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean isEventDriven() {
        return eventDriven;
    }
    */

    @Override
    public int countParams() {
        switch (type) {
            case MODULE_CONFIGURATION:
                return 4;
            case EXECUTION_CONTEXT:
                return 4;
            case STATIC_VALUE:
                return 1;
            case MULTIPART:
                return 0;
            case CALLER:
                return 0;
            case CALLER_RELATIVE_NAME:
                return 0;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Optional<Object> getParam(int id) {
        return Optional.empty();
    }

    @Override
    public int countFilters() {
        return filters.size();
    }

    @Override
    public Optional<ISourceFilter> getFilter(int id) {
        return Optional.ofNullable(id >= 0 && filters.size() > id ? filters.get(id) : null);
    }

    @Override
    public ISourceFilter createFilter(List<Integer> range, int period, int countPeriods, int startOffset) {
        return null;
    }

    @Override
    public ISourceFilter createFilter(double min, double max) {
        return null;
    }

    @Override
    public ISourceFilter createFilterStrEq(boolean needEquals, String value) {
        return null;
    }

    @Override
    public ISourceFilter createFilterStrContain(boolean needContain, String value) {
        return null;
    }

    @Override
    public void removeFilter(int id) {
        if (id >= 0 && id < filters.size())
            filters.remove(id);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
