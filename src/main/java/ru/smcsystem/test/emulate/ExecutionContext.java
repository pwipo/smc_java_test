package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IConfiguration;
import ru.smcsystem.api.dto.IExecutionContext;
import ru.smcsystem.api.dto.IExecutionContextManaged;
import ru.smcsystem.api.dto.ISourceManaged;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExecutionContext extends SourceList implements IExecutionContextManaged {
    private ExecutionContextToolImpl executionContextTool;
    private Configuration configuration;
    private String name;
    private List<IExecutionContext> executionContexts;
    private List<IConfiguration> managedConfigurations;
    // private SourceList sourceList;
    private Integer maxWorkInterval;
    private Boolean enable;
    private String type;

    public ExecutionContext(
            ExecutionContextToolImpl executionContextTool
            , Configuration configuration
            , String name
            , List<IExecutionContext> executionContexts
            , List<IConfiguration> managedConfigurations
            , List<ISourceManaged> sources
            , Integer maxWorkInterval
            , String type
    ) {
        super(executionContextTool, configuration.getName(), name, sources != null ? new ArrayList<>(sources) : new ArrayList<>());
        this.executionContextTool = executionContextTool;
        this.configuration = configuration;
        this.name = name;
        this.executionContexts = executionContexts != null ? new ArrayList<>(executionContexts) : new ArrayList<>();
        this.managedConfigurations = managedConfigurations != null ? new ArrayList<>(managedConfigurations) : new ArrayList<>();
        // this.sourceList = new SourceList(executionContextTool, configuration.getName(), name, sources != null ? new ArrayList<>(sources) : new ArrayList<>());
        this.maxWorkInterval = maxWorkInterval != null ? maxWorkInterval : -1;
        this.enable = true;
        this.type = type != null ? type : "default";
    }

    public ExecutionContext(ExecutionContextToolImpl executionContextTool, Configuration configuration, String name, String type, Integer maxWorkInterval) {
        this(executionContextTool, configuration, name, null, null, null, maxWorkInterval, type);
    }

    public ExecutionContext(String name, String type, Integer maxWorkInterval) {
        this(null, null, name, null, null, null, maxWorkInterval, type);
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
    public void updateExecutionContext(int id, IExecutionContext executionContext) {
        if (id <= 0 || countExecutionContexts() <= id)
            throw new ModuleException("id");
        executionContexts.set(id, executionContext);
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
        if (id <= 0 || countManagedConfigurations() < id)
            throw new ModuleException("id");
        managedConfigurations.add(id, configuration);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_UPDATE, String.format("%s.%s", configuration.getName(), getName()));
    }

    @Override
    public void updateManagedConfiguration(int id, IConfiguration configuration) {
        if (id <= 0 || countManagedConfigurations() <= id)
            throw new ModuleException("id");
        managedConfigurations.set(id, configuration);
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
    public boolean isActive() {
        return false;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
