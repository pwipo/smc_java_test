package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.ActionType;
import ru.smcsystem.api.enumeration.CommandType;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.exceptions.ModuleException;
import ru.smcsystem.api.tools.execution.ConfigurationControlTool;
import ru.smcsystem.api.tools.execution.ExecutionContextTool;
import ru.smcsystem.api.tools.execution.FlowControlTool;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExecutionContextToolImpl implements ExecutionContextTool, ConfigurationControlTool, FlowControlTool {

    private final List<List<IAction>> input;

    protected final List<IMessage> output;

    private final List<Configuration> managedConfigurations;

    private final List<IAction> executionContextsOutput;
    private final List<Function<List<Object>, IAction>> executionContexts;
    private final List<List<Integer>> executeInParalel;

    private final Thread currentTread;

    private final List<IModule> modules;

    private Configuration configuration;

    public ExecutionContextToolImpl(
            List<List<IAction>> input
            , List<Configuration> managedConfigurations
            , List<IAction> executionContextsOutput
            , List<Function<List<Object>, IAction>> executionContexts
    ) {
        this.input = input != null ? new ArrayList<>(input) : new ArrayList<>();
        this.output = new ArrayList<>();
        this.managedConfigurations = managedConfigurations != null ? new ArrayList<>(managedConfigurations) : new ArrayList<>();
        this.managedConfigurations.forEach(c -> ((Configuration) c).setExecutionContextTool(this));
        // this.executionContextsOutput = executionContextsOutput != null ? new ArrayList<>(executionContextsOutput) : new ArrayList<>();
        this.executionContextsOutput = executionContextsOutput != null ? new ArrayList<>(executionContextsOutput) : new ArrayList<>();
        this.executionContexts = executionContexts;
        if (executionContexts != null)
            executionContexts.forEach(ec -> this.executionContextsOutput.add(null));
        this.executeInParalel = new LinkedList<>();

        currentTread = Thread.currentThread();

        modules = new ArrayList<>();
        modules.add(new Module("Module"));
        Map<String, IModule> moduleMap = new HashMap<>();
        this.managedConfigurations.forEach(c -> moduleMap.put(c.getModule().getName(), c.getModule()));
        if (!moduleMap.isEmpty())
            modules.addAll(moduleMap.values());
    }

    public ExecutionContextToolImpl(List<List<IAction>> input, List<Configuration> managedConfigurations, List<IAction> executionContextsOutput) {
        this(input, managedConfigurations, executionContextsOutput, null);
    }

    public void init(ConfigurationToolImpl configurationTool) {
        configuration = configurationTool.getConfiguration();
    }

    public List<IMessage> getOutput() {
        return output;
    }

    private Value createValue(Object value) {
        Objects.requireNonNull(value);
        Value result = null;
        try {
            result = new Value(value);
        } catch (IllegalArgumentException e) {
            result = new Value(value.toString());
        }
        return result;
    }

    private void addMessageObject(Object value) {
        if (value == null)
            throw new ModuleException("value");
        if (value instanceof List) {
            Date date = new Date();
            ((List) value).forEach(v -> output.add(new Message(
                    MessageType.DATA,
                    date,
                    createValue(v)
            )));
        } else {
            output.add(new Message(
                    MessageType.DATA,
                    new Date(),
                    createValue(value)
            ));
        }
    }

    @Override
    public void addMessage(String value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Byte value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Short value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Integer value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Long value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Float value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Double value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(BigInteger value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(BigDecimal value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(byte[] value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(Boolean value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(ObjectArray value) {
        addMessageObject(value);
    }

    @Override
    public void addMessage(List<Object> values) {
        addMessageObject(values);
    }

    private void addErrorObject(Object value) {
        if (value == null)
            throw new ModuleException("value");
        if (value instanceof List) {
            Date date = new Date();
            ((List) value).forEach(v -> output.add(new Message(
                    MessageType.ERROR,
                    date,
                    createValue(v)
            )));
        } else {
            output.add(new Message(
                    MessageType.ERROR,
                    new Date(),
                    createValue(value)
            ));
        }
    }

    @Override
    public void addError(String value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Byte value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Short value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Integer value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Long value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Float value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Double value) {
        addErrorObject(value);
    }

    @Override
    public void addError(BigInteger value) {
        addErrorObject(value);
    }

    @Override
    public void addError(BigDecimal value) {
        addErrorObject(value);
    }

    @Override
    public void addError(byte[] value) {
        addErrorObject(value);
    }

    @Override
    public void addError(Boolean value) {
        addErrorObject(value);
    }

    @Override
    public void addError(ObjectArray value) {
        addErrorObject(value);
    }

    @Override
    public void addError(List<Object> values) {
        addErrorObject(values);
    }

    public void add(MessageType messageType, Object value) {
        output.add(new Message(messageType, new Date(), new Value(value)));
    }

    @Override
    public void addLog(String value) {
        if (value == null)
            throw new ModuleException("value");
        output.add(new Message(
                MessageType.LOG,
                new Date(),
                createValue(value)
        ));
    }

    @Override
    public int countSource() {
        return input.size();
    }

    @Override
    public Optional<ISource> getSource(int id) {
        List<ISourceManaged> sources = new ArrayList<>();
        if (input != null) {
            for (int i = 0; i < input.size(); i++) {
                sources.add(new Source(
                        this, configuration.getName(), getName(), new ExecutionContext(String.valueOf(i), null, null), false, i));
            }
        }
        return new SourceList(this, configuration.getName(), getName(), sources).getSource(id);
    }

    @Override
    public int countCommands(int sourceId) {
        return getMessagesAll(sourceId).size();
    }

    private List<IAction> getMessagesAll(int sourceId) {
        if (sourceId < 0 || countSource() <= sourceId)
            throw new ModuleException("sourceId");
        List<IAction> actions = input.get(sourceId);
        if (actions == null)
            actions = new ArrayList<>();
        return actions;
    }

    @Override
    public List<IAction> getMessages(int sourceId) {
        return filter(getMessagesAll(sourceId), ActionType.EXECUTE, MessageType.DATA);
    }

    private List<IAction> filter(List<IAction> actions, ActionType actionType, MessageType messageType) {
        return actions.stream()
                .filter(a -> actionType == null || actionType.equals(a.getType()))
                .map(a -> {
                    List<IMessage> collect = a.getMessages().stream()
                            .filter(m -> messageType == null || messageType.equals(m.getMessageType()))
                            .collect(Collectors.toList());
                    return new IAction() {
                        @Override
                        public List<IMessage> getMessages() {
                            return collect;
                        }

                        @Override
                        public ActionType getType() {
                            return a.getType();
                        }
                    };
                })
                .collect(Collectors.toList());
    }

    private boolean hasError(IAction action) {
        boolean result = true;
        do {
            if (action == null)
                break;
            if (Objects.isNull(action.getMessages()) || action.getMessages().size() == 0)
                break;
            if (action.getMessages().stream().anyMatch(m -> MessageType.ACTION_ERROR.equals(m.getMessageType()) || MessageType.ERROR.equals(m.getMessageType())))
                break;
            result = false;
        } while (false);
        return result;
    }

    @Override
    public List<IAction> getMessages(int sourceId, int fromIndex, int toIndex) {
        return getMessages(sourceId).subList(fromIndex, toIndex);
    }

    @Override
    public List<ICommand> getCommands(int sourceId) {
        return List.of(new Command(getMessagesAll(sourceId), CommandType.EXECUTE));
    }

    @Override
    public List<ICommand> getCommands(int sourceId, int fromIndex, int toIndex) {
        return getCommands(sourceId).subList(fromIndex, toIndex);
    }

    @Override
    public boolean isError(IAction action) {
        return hasError(action);
    }


    @Override
    public int countManagedConfigurations() {
        return managedConfigurations.size();
    }

    /*
    private Configuration find(String configurationName) {
        return configurations.stream()
                .filter(c -> c.name.equals(configurationName))
                .findFirst()
                .orElseThrow(() -> new ModuleException("configuration not found"));
    }

    private Configuration find(int managedId) {
        if (managedId < 0 || configurations.size() <= managedId)
            throw new ModuleException("wrong managedId");
        return configurations.get(managedId);
    }
    */

    /*
    @Override
    public Map<String, IValue> getAllSettings(int managedId) {
        return find(managedId).settings;
    }

    @Override
    public Optional<IValue> getSetting(int managedId, String key) {
        return Optional.ofNullable(getAllSettings(managedId).get(key));
    }

    @Override
    public void setSetting(int managedConfigurationId, String key, Object value) {
        Configuration configuration = find(managedConfigurationId);
        configuration.settings.put(key, createValue(value));
    }

    @Override
    public Map<String, IValue> getAllVariables(int managedId) {
        return find(managedId).variables;
    }

    @Override
    public Optional<IValue> getVariable(int managedId, String key) {
        return Optional.ofNullable(getAllVariables(managedId).get(key));
    }

    @Override
    public void setVariable(int managedConfigurationId, String key, Object value) {
        Configuration configuration = find(managedConfigurationId);
        configuration.variables.put(key, createValue(value));
    }

    @Override
    public void removeVariable(int managedId, String key) {
        Configuration configuration = find(managedId);
        configuration.variables.remove(key);
    }
    */

    /*
    @Override
    public void createConfiguration(String moduleName, String name) {
        Configuration configuration = configurations.stream()
                .filter(c -> c.name.equals(name))
                .findFirst().orElse(null);
        if (configuration != null)
            throw new ModuleException("configuration exist");
        configuration = new Configuration(moduleName, name, name);
        configurations.add(configuration);
    }
    */

    /*
    @Override
    public String getConfigurationName(int managedId) {
        return find(managedId).name;
    }

    @Override
    public String getConfigurationModuleName(int managedId) {
        return find(managedId).moduleName;
    }

    @Override
    public String getConfigurationDescription(int managedId) {
        return find(managedId).description;
    }
    */

    /*
    @Override
    public String setConfigurationDescription(int managedId, String description) {
        return find(managedId).description = description;
    }

    @Override
    public Long getConfigurationDataSavePeriod(int managedId) {
        return find(managedId).dataSavePeriod;
    }

    @Override
    public Long setConfigurationDataSavePeriod(int managedId, long dataSavePeriod) {
        return find(managedId).dataSavePeriod = dataSavePeriod;
    }
    */

    /*
    @Override
    public boolean isConfigurationEnable(int managedId) {
        return find(managedId).enable;
    }

    @Override
    public boolean setConfigurationEnable(int managedId, boolean enable) {
        return find(managedId).enable = enable;
    }
    */

    /*
    @Override
    public void removeConfiguration(int managedId) {
        Configuration configuration = find(managedId);
        configurations.remove(configuration);
    }
    */

    /*
    private List<ExecutionContext> getExecutionContexts(int managedId) {
        return find(managedId).executionContexts;
    }

    private ExecutionContext getExecutionContext(String configurationName, String name) {
        return find(configurationName).executionContexts.stream()
                .filter(ec -> ec.name.equals(name))
                .findFirst().orElseThrow(() -> new ModuleException("ExecutionContext not found"));
    }

    private ExecutionContext getExecutionContext(int managedId, int executionContextId) {
        Configuration configuration = find(managedId);
        if (executionContextId < 0 || configuration.executionContexts.size() <= executionContextId)
            throw new ModuleException("wrong executionContextId");
        return configuration.executionContexts.get(executionContextId);
    }

    @Override
    public int countExecutionContexts(int managedId) {
        return getExecutionContexts(managedId).size();
    }

    @Override
    public String getExecutionContextName(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).name;
    }
    */

    /*
    @Override
    public void createExecutionContext(int managedId, String name) {
        Configuration configuration = find(managedId);
        ExecutionContext executionContext = configuration.executionContexts.stream()
                .filter(ec -> ec.name.equals(name))
                .findFirst().orElse(null);
        if (executionContext != null)
            throw new ModuleException("ExecutionContext exist");
        configuration.executionContexts.add(new ExecutionContext(name));
    }
    */

    /*
    @Override
    public int countExecutionContextExecutionContextList(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).executionContextList.size();
    }

    @Override
    public String getExecutionContextExecutionContextListConfigurationName(int managedId, int executionContextId, int id) {
        List<String[]> executionContextList = getExecutionContext(managedId, executionContextId).executionContextList;
        if (id < 0 || executionContextList.size() <= id)
            throw new ModuleException("wrong id");
        return executionContextList.get(id)[0];
    }

    @Override
    public String getExecutionContextExecutionContextListName(int managedId, int executionContextId, int id) {
        List<String[]> executionContextList = getExecutionContext(managedId, executionContextId).executionContextList;
        if (id < 0 || executionContextList.size() <= id)
            throw new ModuleException("wrong id");
        return executionContextList.get(id)[1];
    }
    */

    /*
    @Override
    public void addExecutionContextExecutionContextList(int managedId, int executionContextId, String configurationName, String executionContextName) {
        List<String[]> executionContextList = getExecutionContext(managedId, executionContextId).executionContextList;
        executionContextList.add(new String[]{configurationName, executionContextName});
    }

    @Override
    public void removeFromExecutionContextExecutionContextList(int managedId, int executionContextId, int id) {
        List<String[]> executionContextList = getExecutionContext(managedId, executionContextId).executionContextList;
        if (id < 0 || executionContextList.size() <= id)
            throw new ModuleException("wrong id");
        executionContextList.remove(id);
    }
    */

    /*
    @Override
    public int countExecutionContextManagedConfigurationList(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).managedConfigurationList.size();
    }

    @Override
    public String getExecutionContextManagedConfigurationListName(int managedId, int executionContextId, int id) {
        List<String> managedConfigurationList = getExecutionContext(managedId, executionContextId).managedConfigurationList;
        if (id < 0 || managedConfigurationList.size() <= id)
            throw new ModuleException("wrong id");
        return managedConfigurationList.get(id);
    }
    */

    /*
    @Override
    public void addExecutionContextManagedConfigurationList(int managedId, int executionContextId, String configurationName) {
        List<String> managedConfigurationList = getExecutionContext(managedId, executionContextId).managedConfigurationList;
        managedConfigurationList.add(configurationName);
    }

    @Override
    public void removeFromExecutionContextManagedConfigurationList(int managedId, int executionContextId, int id) {
        List<String> managedConfigurationList = getExecutionContext(managedId, executionContextId).managedConfigurationList;
        if (id < 0 || managedConfigurationList.size() <= id)
            throw new ModuleException("wrong id");
        managedConfigurationList.remove(id);
    }
    */

    /*
    @Override
    public Integer getExecutionContextMaxWorkInterval(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).maxWorkInterval;
    }

    @Override
    public void setExecutionContextMaxWorkInterval(int managedId, int executionContextId, int maxWorkInterval) {
        getExecutionContext(managedId, executionContextId).maxWorkInterval = maxWorkInterval;
    }

    @Override
    public Integer isExecutionContextTypeSystem(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).system ? getExecutionContext(managedId, executionContextId).systemTriggerInterval : null;
    }
    */

    /*
    @Override
    public void setExecutionContextTypeToSystem(int managedId, int executionContextId, int systemTriggerInterval) {
        getExecutionContext(managedId, executionContextId).system = true;
        getExecutionContext(managedId, executionContextId).systemTriggerInterval = systemTriggerInterval;
    }

    @Override
    public void setExecutionContextTypeToNormal(int managedId, int executionContextId) {
        getExecutionContext(managedId, executionContextId).system = false;
    }
    */

    /*
    @Override
    public Boolean getExecutionContextEnable(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).enable;
    }

    @Override
    public void setExecutionContextEnable(int managedId, int executionContextId, boolean enable) {
        getExecutionContext(managedId, executionContextId).enable = enable;
    }

    private List<String> getContextSourceConfigurationNames(int managedId, int executionContextId) {
        return getExecutionContext(managedId, executionContextId).contextSourceList;
    }

    @Override
    public int countContextSources(int managedId, int executionContextId) {
        return getContextSourceConfigurationNames(managedId, executionContextId).size();
    }
    */

    /*
    @Override
    public String getContextSourcesConfigurationName(int managedId, int executionContextId, int id) {
        List<String> contextSourceConfigurationNames = getContextSourceConfigurationNames(managedId, executionContextId);
        if (id < 0 || contextSourceConfigurationNames.size() <= id)
            throw new ModuleException("wrong id");
        return contextSourceConfigurationNames.get(id);
    }
    */

    /*
    @Override
    public void createContextSource(int managedId, int executionContextId, String sourceConfigurationName) {
        getContextSourceConfigurationNames(managedId, executionContextId).add(sourceConfigurationName);
    }
    */

    /*
    @Override
    public void removeContextSource(int managedId, int executionContextId, int id) {
        List<String> contextSourceConfigurationNames = getContextSourceConfigurationNames(managedId, executionContextId);
        if (id < 0 || contextSourceConfigurationNames.size() <= id)
            throw new ModuleException("wrong id");
        contextSourceConfigurationNames.remove(id);
    }
    */

    /*
    @Override
    public void removeExecutionContext(int managedId, int executionContextId) {
        Configuration configuration = find(managedId);
        ExecutionContext executionContext = getExecutionContext(managedId, executionContextId);
        configuration.executionContexts.remove(executionContext);
    }
    */

    /*
    @Override
    public void flashChanges() {
    }
    */

    @Override
    public int countManagedExecutionContexts() {
        return executionContextsOutput.size();
    }

    @Override
    public void executeNow(CommandType type, int managedId, List<Object> values) {
        if (type == null)
            throw new ModuleException("type");
        if (managedId < 0 || countManagedExecutionContexts() <= managedId)
            throw new ModuleException("managedId");
        if (!Thread.currentThread().equals(currentTread))
            throw new ModuleException("this operation available only in main thread");
        MessageType messageType = null;
        switch (type) {
            case START:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_NOW_START;
                break;
            case EXECUTE:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_NOW_EXECUTE;
                break;
            case UPDATE:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_NOW_UPDATE;
                break;
            case STOP:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_NOW_STOP;
                break;
        }
        add(messageType, managedId);
        if (executionContexts != null) {
            values = values != null ?
                    values.stream()
                            .map(v -> createValue(v).getValue())
                            .collect(Collectors.toList())
                    : null;
            executionContextsOutput.set(
                    managedId,
                    executionContexts.get(managedId).apply(values)
            );
        }
    }

    @Override
    public long executeParallel(CommandType type, List<Integer> managedIds, List<Object> values, Integer waitingTacts, Integer maxWorkInterval) {
        if (type == null)
            throw new ModuleException("type");
        if (managedIds == null || managedIds.isEmpty())
            throw new ModuleException("managedIds");
        if (waitingTacts == null)
            waitingTacts = 0;
        if (waitingTacts < 0)
            throw new ModuleException("waitingTacts");
        managedIds.forEach(managedId -> {
            if (managedId < 0 || countManagedExecutionContexts() <= managedId)
                throw new ModuleException("managedId");
        });
        MessageType messageType = null;
        switch (type) {
            case START:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_PARALLEL_STOP;
                break;
            case EXECUTE:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_PARALLEL_EXECUTE;
                break;
            case UPDATE:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_PARALLEL_UPDATE;
                break;
            case STOP:
                messageType = MessageType.FLOW_CONTROL_EXECUTE_PARALLEL_STOP;
                break;
        }
        MessageType messageTypeTmp = messageType;
        managedIds.forEach(managedId -> add(messageTypeTmp, managedId));
        add(MessageType.FLOW_CONTROL_EXECUTE_PARALLEL_WAITING_TACTS, waitingTacts);
        executeInParalel.add(managedIds);
        if (executionContexts != null) {
            List<Object> valuesTmp = values != null ?
                    values.stream()
                            .map(v -> createValue(v).getValue())
                            .collect(Collectors.toList())
                    : null;
            managedIds.forEach(managedId -> executionContextsOutput.set(
                    managedId,
                    executionContexts.get(managedId).apply(valuesTmp)
            ));
        }
        return executeInParalel.size() - 1;
    }

    // @Override
    /*
    private boolean isExecute(int managedId) {
        if (!(managedId >= 0 && managedId < countManagedExecutionContexts()))
            return false;
        return executeInParalel.contains(managedId);
    }
    */

    @Override
    public boolean isThreadActive(long threadId) {
        // return threadId >= 0 && threadId < executeInParalel.size();
        return false;
    }

    @Override
    public List<IAction> getMessagesFromExecuted(int managedId) {
        /*
        if (isExecute(managedId))
            return Collections.EMPTY_LIST; //Optional.empty();
        */

        if (managedId < 0 || countManagedExecutionContexts() <= managedId)
            throw new ModuleException("managedId");
        return filter(List.of(executionContextsOutput.get(managedId)), ActionType.EXECUTE, MessageType.DATA);//.stream().findFirst();
    }

    @Override
    public List<IAction> getMessagesFromExecuted(long threadId, int managedId) {
        if (managedId < 0 || countManagedExecutionContexts() <= managedId)
            throw new ModuleException("managedId");
        return filter(List.of(executionContextsOutput.get(managedId)), ActionType.EXECUTE, MessageType.DATA);//.stream().findFirst();
    }

    @Override
    public List<ICommand> getCommandsFromExecuted(int managedId) {
        /*
        if (isExecute(managedId))
            return Collections.EMPTY_LIST; //Optional.empty();
        */

        if (managedId < 0 || countManagedExecutionContexts() <= managedId)
            throw new ModuleException("managedId");
        return List.of(new Command(
                filter(List.of(executionContextsOutput.get(managedId)), null, null),
                CommandType.EXECUTE));
    }

    @Override
    public List<ICommand> getCommandsFromExecuted(long threadId, int managedId) {
        if (managedId < 0 || countManagedExecutionContexts() <= managedId)
            throw new ModuleException("managedId");
        return List.of(new Command(
                filter(List.of(executionContextsOutput.get(managedId)), null, null),
                CommandType.EXECUTE));
    }

    // @Override
    /*
    private boolean isError(int managedId) {
        // if (isExecute(managedId))
        //     return false;

        if (managedId < 0 || countManagedExecutionContexts() <= managedId)
            throw new ModuleException("managedId");
        return hasError(executionContextsOutput.get(managedId));
    }
    */

    @Override
    public void releaseThread(long threadId) {
        executeInParalel.remove(threadId);
    }

    @Override
    public void releaseThreadCache(long threadId) {
        executeInParalel.remove(threadId);
    }

    /*
    // @Override
    private boolean isProcessReady(int managedId) {
        return false;
    }

    // @Override
    private boolean isProcessExist(int managedId) {
        return false;
    }
    */

    @Override
    public ConfigurationControlTool getConfigurationControlTool() {
        return this;
    }

    @Override
    public FlowControlTool getFlowControlTool() {
        return this;
    }

    @Override
    public boolean isNeedStop() {
        return false;
    }


    @Override
    public List<IModule> getModules() {
        return modules;
    }

    @Override
    public Optional<IConfigurationManaged> getManagedConfiguration(int id) {
        if (id < 0 || countManagedConfigurations() <= id)
            throw new ModuleException("id");
        return Optional.ofNullable(managedConfigurations.get(id));
    }

    @Override
    public IConfigurationManaged createConfiguration(int id, IContainer container, IModule module, String name) {
        if (id < 0 || countManagedConfigurations() < id)
            throw new ModuleException("id");
        Configuration configuration = new Configuration(this, null, module, name, "");
        configuration.setContainer((Container) container);
        managedConfigurations.add(id, configuration);
        add(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_CREATE, configuration.getName());
        return configuration;
    }

    @Override
    public void removeManagedConfiguration(int id) {
        if (id < 0 || countManagedConfigurations() <= id)
            throw new ModuleException("id");
        Configuration configuration = managedConfigurations.remove(id);
        configuration.setContainer(null);
        add(MessageType.CONFIGURATION_CONTROL_CONFIGURATION_CREATE, configuration.getName());
    }

    @Override
    public int countCommands(IExecutionContextManaged executionContext) {
        return 0;
    }

    @Override
    public List<ICommand> getCommands(IExecutionContextManaged executionContext, int fromIndex, int toIndex) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public IConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public int getMaxWorkInterval() {
        return -1;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    public List<Configuration> getManagedConfigurations() {
        return managedConfigurations;
    }

    @Override
    public Optional<IExecutionContext> getManagedExecutionContext(int id) {
        return Optional.empty();
    }

    @Override
    public String getType() {
        return "";
    }

}
