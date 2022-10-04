package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.SourceGetType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecutionContext implements IExecutionContextManaged {

    private ExecutionContextToolImpl executionContextTool;
    private Configuration configuration;
    private String name;
    private List<IExecutionContext> executionContexts;
    private List<IConfiguration> managedConfigurations;
    private SourceList sourceList;
    private Integer maxWorkInterval;
    private Boolean enable;

    public ExecutionContext(
            ExecutionContextToolImpl executionContextTool
            , Configuration configuration
            , String name
            , List<IExecutionContext> executionContexts
            , List<IConfiguration> managedConfigurations
            , List<ISourceManaged> sources
            , Integer maxWorkInterval
    ) {
        this.executionContextTool = executionContextTool;
        this.configuration = configuration;
        this.name = name;
        this.executionContexts = executionContexts != null ? new ArrayList<>(executionContexts) : new ArrayList<>();
        this.managedConfigurations = managedConfigurations != null ? new ArrayList<>(managedConfigurations) : new ArrayList<>();
        this.sourceList = new SourceList(executionContextTool, configuration.getName(), name, sources != null ? new ArrayList<>(sources) : new ArrayList<>());
        this.maxWorkInterval = maxWorkInterval != null ? maxWorkInterval : -1;
        this.enable = true;
    }

    public ExecutionContext(ExecutionContextToolImpl executionContextTool, Configuration configuration, String name, Integer maxWorkInterval) {
        this(executionContextTool, configuration, name, null, null, null, maxWorkInterval);
    }

    public ExecutionContext(String name, Integer maxWorkInterval) {
        this(null, null, name, null, null, null, maxWorkInterval);
    }

    public void setExecutionContextTool(ExecutionContextToolImpl executionContextTool) {
        this.executionContextTool = executionContextTool;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public void setMaxWorkInterval(int maxWorkInterval) {
        this.maxWorkInterval = maxWorkInterval;
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public int countExecutionContexts() {
        return executionContexts.size();
    }

    @Override
    public Optional<IExecutionContext> getExecutionContext(int id) {
        if (id <= 0 || countExecutionContexts() <= id)
            throw new ModuleException("id");
        return Optional.ofNullable(executionContexts.get(id));
    }

    @Override
    public void insertExecutionContext(int id, IExecutionContext executionContext) {
        if (id <= 0 || countExecutionContexts() < id)
            throw new ModuleException("id");
        executionContexts.add(id, executionContext);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public void removeExecutionContext(int id) {
        if (id <= 0 || countExecutionContexts() <= id)
            throw new ModuleException("id");
        executionContexts.remove(id);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public int countManagedConfigurations() {
        return managedConfigurations.size();
    }

    @Override
    public Optional<IConfiguration> getManagedConfiguration(int id) {
        if (id <= 0 || countManagedConfigurations() <= id)
            throw new ModuleException("id");
        return Optional.ofNullable(managedConfigurations.get(id));
    }

    @Override
    public void insertManagedConfiguration(int id, IConfiguration configuration) {
        if (id <= 0 || countManagedConfigurations() <= id)
            throw new ModuleException("id");
        managedConfigurations.add(id, configuration);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public void removeManagedConfiguration(int id) {
        if (id <= 0 || countManagedConfigurations() <= id)
            throw new ModuleException("id");
        managedConfigurations.size();
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public int countSource() {
        return sourceList.countSource();
    }

    @Override
    public Optional<ISource> getSource(int id) {
        return sourceList.getSource(id);
    }

    @Override
    public ISourceManaged createSource(IConfiguration configuration, SourceGetType getType, int countLast, boolean eventDriven) {
        return sourceList.createSource(configuration, getType, countLast, eventDriven);
    }

    @Override
    public ISourceManaged createSource(IExecutionContext executionContext, SourceGetType getType, int countLast, boolean eventDriven) {
        return sourceList.createSource(executionContext, getType, countLast, eventDriven);
    }

    @Override
    public ISourceManaged createSource(Object value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource() {
        return sourceList.createSource();
    }

    /*
    @Override
    public void changeOrderUp(int id) {
        sourceList.changeOrderUp(id);
    }

    @Override
    public void changeOrderDown(int id) {
        sourceList.changeOrderDown(id);
    }
    */

    @Override
    public Optional<ISourceManaged> getSourceManaged(int id) {
        return sourceList.getSourceManaged(id);
    }

    @Override
    public void removeSource(int id) {
        sourceList.removeSource(id);
    }

    @Override
    public IConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxWorkInterval() {
        return maxWorkInterval;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public Optional<ISourceListManaged> getSourceListManaged(int id) {
        return sourceList.getSourceListManaged(id);
    }
}
