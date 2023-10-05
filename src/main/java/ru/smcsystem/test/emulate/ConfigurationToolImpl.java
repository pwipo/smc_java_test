package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.exceptions.ModuleException;
import ru.smcsystem.api.tools.ConfigurationTool;
import ru.smcsystem.api.tools.FileTool;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigurationToolImpl implements ConfigurationTool {

    private final Configuration configuration;

    private final FileToolImpl homeFolder;

    private final Map<String, Boolean> variablesChangeFlag;

    private final String workDirectory;

    public ConfigurationToolImpl(Configuration configuration, String homeFolder, String workDirectory) {
        if (configuration == null)
            throw new ModuleException("name");

        if (homeFolder == null)
            homeFolder = System.getProperty("java.io.tmpdir");
        File file = new File(homeFolder);
        if (!file.exists())
            throw new ModuleException("homeFolder");

        this.configuration = configuration;
        this.homeFolder = new FileToolImpl(file);
        variablesChangeFlag = this.configuration.getAllVariables().entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), true)).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, e -> true));
        if (workDirectory == null)
            workDirectory = System.getProperty("java.io.tmpdir");
        this.workDirectory = workDirectory;
    }

    public ConfigurationToolImpl(String name, String description, Map<String, IValue> settings, String homeFolder, String workDirectory) {
        this(
                new Configuration(
                        null
                        , new Container(null, "rootContainer", null, null)
                        , new Module("Module")
                        , name
                        , description
                        , settings
                        , null
                        , null
                        , null
                )
                , homeFolder
                , workDirectory);
    }

    public void init(ExecutionContextToolImpl executionContextTool) {
        configuration.setExecutionContextTool(executionContextTool);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Map<String, Boolean> getVariablesChangeFlag() {
        return variablesChangeFlag;
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

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public String getDescription() {
        return configuration.getDescription();
    }

    @Override
    public Map<String, IValue> getAllSettings() {
        return configuration.getAllSettings();
    }

    @Override
    public Optional<IValue> getSetting(String key) {
        return configuration.getSetting(key);
    }

    @Override
    public Map<String, IValue> getAllVariables() {
        return configuration.getAllVariables();
    }

    @Override
    public Optional<IValue> getVariable(String key) {
        return configuration.getVariable(key);
    }

    @Override
    public void setVariable(String key, String value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, Byte value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, Short value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, Integer value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, Long value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, Float value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, Double value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, BigInteger value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, BigDecimal value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, byte[] value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public void setVariable(String key, ObjectArray value) {
        configuration.setVariable(key, value);
        variablesChangeFlag.put(key, false);
    }

    @Override
    public boolean isVariableChanged(String key) {
        return variablesChangeFlag.get(key);
    }

    @Override
    public void removeVariable(String key) {
        configuration.removeVariable(key);
        variablesChangeFlag.remove(key);
    }

    @Override
    public FileTool getHomeFolder() {
        return homeFolder;
    }

    @Override
    public String getWorkDirectory() {
        if (workDirectory == null)
            throw new ModuleException("storage not allowed");
        return workDirectory;
    }

    @Override
    public int countExecutionContexts() {
        return configuration.countExecutionContexts();
    }

    @Override
    public Optional<IExecutionContext> getExecutionContext(int id) {
        return configuration.getExecutionContext(id).map(ec -> ec);
    }

    @Override
    public IModule getModule() {
        return configuration.getModule();
    }

    @Override
    public long getBufferSize() {
        return configuration.getBufferSize();
    }

    @Override
    public long getThreadBufferSize() {
        return configuration.getThreadBufferSize();
    }

    @Override
    public boolean isEnable() {
        return configuration.isEnable();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public IContainerManaged getContainer() {
        return configuration.getContainer();
    }

    @Override
    public boolean hasLicense(int freeDays) {
        return true;
    }

    @Override
    public void loggerTrace(String text) {
        System.out.printf("%s: Log Cfg %d: %s%n", Instant.now(), 0, text);
    }

    @Override
    public void loggerDebug(String text) {
        System.out.printf("%s: Log Cfg %d: %s%n", Instant.now(), 0, text);
    }

    @Override
    public void loggerInfo(String text) {
        System.out.printf("%s: Log Cfg %d: %s%n", Instant.now(), 0, text);
    }

    @Override
    public void loggerWarn(String text) {
        System.out.printf("%s: Log Cfg %d: %s%n", Instant.now(), 0, text);
    }

    @Override
    public void loggerError(String text) {
        System.out.printf("%s: Log Cfg %d: %s%n", Instant.now(), 0, text);
    }
}
