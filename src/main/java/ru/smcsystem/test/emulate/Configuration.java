package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.util.*;

public class Configuration implements IConfigurationManaged {

    private ExecutionContextToolImpl executionContextTool;
    private IModule module;
    private String name;
    private String description;
    private Map<String, IValue> settings;
    private Map<String, IValue> variables;
    private List<IExecutionContextManaged> executionContexts;
    private Long bufferSize;
    private Boolean enable;
    private Container container;

    public Configuration(
            ExecutionContextToolImpl executionContextTool
            , Container container
            , IModule module
            , String name
            , String description
            , Map<String, IValue> settings
            , Map<String, IValue> variables
            , List<IExecutionContextManaged> executionContexts
            , Long bufferSize

    ) {
        if (name == null || name.isBlank())
            throw new ModuleException("name");

        this.module = module;
        this.name = name;
        this.description = description;
        this.settings = settings != null ? new HashMap<>(settings) : new HashMap<>();
        this.variables = variables != null ? new HashMap<>(variables) : new HashMap<>();
        this.executionContexts = executionContexts != null ? new ArrayList<>(executionContexts) : new ArrayList<>();
        this.executionContexts.forEach(ec -> ((ExecutionContext) ec).setConfiguration(this));
        this.bufferSize = bufferSize != null ? bufferSize : 1;
        this.enable = true;
        setExecutionContextTool(executionContextTool);
        setContainer(container);
    }

    public Configuration(
            ExecutionContextToolImpl executionContextTool
            , Container container
            , IModule module
            , String name
            , String description
    ) {
        this(executionContextTool, container, module, name, description, null, null, null, null);
    }

    public Configuration(Container container, IModule module, String name, String description) {
        this(null, container, module, name, description, null, null, null, null);
    }

    public void setExecutionContextTool(ExecutionContextToolImpl executionContextTool) {
        this.executionContextTool = executionContextTool;
        getExecutionContexts().forEach(ec -> {
            ((ExecutionContext) ec).setExecutionContextTool(executionContextTool);
        });
        if (this.container != null)
            container.setExecutionContextTool(executionContextTool);
    }

    public void setContainer(Container container) {
        if (this.container != null)
            this.container.configurations.remove(this);
        this.container = container;
        if (this.container != null)
            this.container.configurations.add(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_UPDATE, getName());
    }

    private void addMessage(MessageType messageType, Object value) {
        if (executionContextTool == null)
            return;
        executionContextTool.add(messageType, value);
    }

    @Override
    public void setSetting(String key, Object value) {
        if (value == null)
            throw new ModuleException("value");

        Optional<IValue> setting = getSetting(key);
        Value value1 = new Value(value);
        if (setting.isPresent() && !setting.get().getType().equals(value1.getType()))
            throw new ModuleException("wrong type");

        getAllSettings().put(key, value1);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_SETTING_UPDATE, String.format("%s %s", getName(), key));
    }

    @Override
    public void setVariable(String key, Object value) {
        if (value == null)
            throw new ModuleException("value");

        Optional<IValue> variable = getVariable(key);
        Value value1 = new Value(value);
        if (variable.isPresent() && !variable.get().getType().equals(value1.getType()))
            throw new ModuleException("wrong type");

        getAllVariables().put(key, value1);
        addMessage(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_VARIABLE_UPDATE, String.format("%s %s", getName(), key));
    }

    @Override
    public void removeVariable(String key) {
        Optional<IValue> variable = getVariable(key);
        variable.ifPresent(v -> getAllVariables().remove(key));
        addMessage(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_VARIABLE_REMOVE, String.format("%s %s", getName(), key));
    }

    @Override
    public void setBufferSize(long bufferSize) {
        this.bufferSize = bufferSize;
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_UPDATE, getName());
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_UPDATE, getName());
    }

    @Override
    public int countExecutionContexts() {
        return executionContexts.size();
    }

    public List<IExecutionContextManaged> getExecutionContexts() {
        return executionContexts;
    }

    @Override
    public Optional<IExecutionContextManaged> getExecutionContext(int id) {
        if (id < 0 || countExecutionContexts() <= id)
            throw new ModuleException("id");
        return Optional.ofNullable(executionContexts.get(id));
    }

    @Override
    public IExecutionContextManaged createExecutionContext(String name, int maxWorkInterval) {
        ExecutionContext executionContext = new ExecutionContext(executionContextTool, this, name, maxWorkInterval);
        executionContexts.add(executionContext);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_CREATE, String.format("%s.%s", getName(), executionContext.getName()));
        return executionContext;
    }

    @Override
    public void removeExecutionContext(int id) {
        if (id < 0 || countExecutionContexts() <= id)
            throw new ModuleException("id");
        IExecutionContextManaged executionContext = executionContexts.remove(id);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_EXECUTION_CONTEXT_REMOVE, String.format("%s.%s", getName(), executionContext.getName()));
    }

    @Override
    public IModule getModule() {
        return module;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<String, IValue> getAllSettings() {
        return settings;
    }

    @Override
    public Optional<IValue> getSetting(String key) {
        if (key == null || key.isBlank())
            throw new ModuleException("key");
        return Optional.ofNullable(getAllSettings().get(key));
    }

    @Override
    public Map<String, IValue> getAllVariables() {
        return variables;
    }

    @Override
    public Optional<IValue> getVariable(String key) {
        if (key == null || key.isBlank())
            throw new ModuleException("key");
        return Optional.ofNullable(getAllVariables().get(key));
    }

    @Override
    public long getBufferSize() {
        return bufferSize;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public IContainerManaged getContainer() {
        return container;
    }
}
