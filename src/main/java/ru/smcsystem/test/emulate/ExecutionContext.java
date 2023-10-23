package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.SourceGetType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.math.BigDecimal;
import java.math.BigInteger;
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
        this.executionContextTool = executionContextTool;
        this.configuration = configuration;
        this.name = name;
        this.executionContexts = executionContexts != null ? new ArrayList<>(executionContexts) : new ArrayList<>();
        this.managedConfigurations = managedConfigurations != null ? new ArrayList<>(managedConfigurations) : new ArrayList<>();
        this.sourceList = new SourceList(executionContextTool, configuration.getName(), name, sources != null ? new ArrayList<>(sources) : new ArrayList<>());
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
    public ISourceManaged createSource(String value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Byte value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Short value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Integer value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Long value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Float value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Double value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(BigInteger value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(BigDecimal value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(byte[] value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(Boolean value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource(ObjectArray value) {
        return sourceList.createSource(value);
    }

    @Override
    public ISourceManaged createSource() {
        return sourceList.createSource();
    }

    @Override
    public ISourceManaged createSource(ObjectArray value, List<String> fields) {
        return sourceList.createSource(value, fields);
    }

    @Override
    public ISourceManaged updateSource(int id, IConfiguration configuration, SourceGetType getType, int countLast, boolean eventDriven) {
        return sourceList.updateSource(id, configuration, getType, countLast, eventDriven);
    }

    @Override
    public ISourceManaged updateSource(int id, IExecutionContext executionContext, SourceGetType getType, int countLast, boolean eventDriven) {
        return sourceList.updateSource(id, executionContext, getType, countLast, eventDriven);
    }

    @Override
    public ISourceManaged updateSource(int id, String value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Byte value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Short value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Integer value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Long value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Float value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Double value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, BigInteger value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, BigDecimal value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, byte[] value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Boolean value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, ObjectArray value) {
        return sourceList.updateSource(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, ObjectArray value, List<String> fields) {
        return sourceList.updateSource(id, value, fields);
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
    public boolean isActive() {
        return false;
    }

    @Override
    public Optional<ISourceListManaged> getSourceListManaged(int id) {
        return sourceList.getSourceListManaged(id);
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
