package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.SourceGetType;
import ru.smcsystem.api.enumeration.SourceType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SourceList implements ISourceListManaged {

    private final ExecutionContextToolImpl executionContextTool;

    private final String configurationName;

    private final String executionContextName;

    private List<ISourceManaged> sources;

    public SourceList(
            ExecutionContextToolImpl executionContextTool
            , String configurationName
            , String executionContextName
            , List<ISourceManaged> sources
    ) {
        this.executionContextTool = executionContextTool;
        this.configurationName = configurationName;
        this.executionContextName = executionContextName;
        this.sources = sources != null ? sources : new ArrayList<>();
    }

    public List<ISourceManaged> getSources() {
        return sources;
    }

    @Override
    public int countSource() {
        return sources.size();
    }

    @Override
    public Optional<ISource> getSource(int id) {
        if (id <= 0 || countSource() <= id)
            throw new ModuleException("id");
        return Optional.ofNullable(sources.get(id));
    }

    @Override
    public ISourceManaged createSource(IConfiguration configuration, SourceGetType getType, int countLast, boolean eventDriven) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, configuration, eventDriven, countSource());
        sources.add(source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_CREATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    @Override
    public ISourceManaged createSource(IExecutionContext executionContext, SourceGetType getType, int countLast, boolean eventDriven) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, executionContext, eventDriven, countSource());
        sources.add(source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_CREATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    private ISourceManaged createSourceStatic(Object value) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, new Value(value), countSource());
        sources.add(source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_CREATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    @Override
    public ISourceManaged createSource(String value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Byte value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Short value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Integer value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Long value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Float value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Double value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(BigInteger value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(BigDecimal value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(byte[] value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(Boolean value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource(ObjectArray value) {
        return createSourceStatic(value);
    }

    @Override
    public ISourceManaged createSource() {
        Source source = new Source(executionContextTool, configurationName, executionContextName, (List<ISourceManaged>) null, countSource());
        sources.add(source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_CREATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    @Override
    public ISourceManaged createSource(ObjectArray value, List<String> fields) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, new Value(value), (List<ISourceManaged>) null, countSource(), fields);
        sources.add(source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_CREATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    @Override
    public ISourceManaged updateSource(int id, IConfiguration configuration, SourceGetType getType, int countLast, boolean eventDriven) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, configuration, eventDriven, countSource());
        sources.set(id, source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_UPDATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    @Override
    public ISourceManaged updateSource(int id, IExecutionContext executionContext, SourceGetType getType, int countLast, boolean eventDriven) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, executionContext, eventDriven, countSource());
        sources.set(id, source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_UPDATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    private ISourceManaged updateSourceStatic(int id, Object value) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, new Value(value), countSource());
        sources.set(id, source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_UPDATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    @Override
    public ISourceManaged updateSource(int id, String value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Byte value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Short value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Integer value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Long value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Float value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Double value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, BigInteger value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, BigDecimal value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, byte[] value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, Boolean value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, ObjectArray value) {
        return updateSourceStatic(id, value);
    }

    @Override
    public ISourceManaged updateSource(int id, ObjectArray value, List<String> fields) {
        Source source = new Source(executionContextTool, configurationName, executionContextName, new Value(value), (List<ISourceManaged>) null, countSource(), fields);
        sources.set(id, source);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_UPDATE, String.format("%s.%s.%d", configurationName, executionContextName, source.getOrder()));
        return source;
    }

    /*
    @Override
    public void changeOrderUp(int id) {
        if (id <= 0 || countSource() <= id)
            throw new ModuleException("id");
        Source iSource = (Source) sources.get(id);
        Source iSource1 = (Source) sources.get(id - 1);

        int order = iSource.getOrder();
        iSource.setOrder(iSource1.getOrder());
        iSource1.setOrder(order);
        sources.set(id, iSource1);
        sources.set(id - 1, iSource);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_UPDATE, String.format("%s.%s.%d", configurationName, executionContextName, iSource.getOrder()));
    }

    @Override
    public void changeOrderDown(int id) {
        if (id <= 0 || countSource() - 1 <= id)
            throw new ModuleException("id");
        Source iSource = (Source) sources.get(id);
        Source iSource1 = (Source) sources.get(id + 1);

        int order = iSource.getOrder();
        iSource.setOrder(iSource1.getOrder());
        iSource1.setOrder(order);
        sources.set(id, iSource1);
        sources.set(id + 1, iSource);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_UPDATE, String.format("%s.%s.%d", configurationName, executionContextName, iSource.getOrder()));
    }
    */

    @Override
    public void removeSource(int id) {
        if (id <= 0 || countSource() <= id)
            throw new ModuleException("id");
        ISource source = sources.remove(id);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_SOURCE_CONTEXT_CREATE, String.format("%s.%s.%d", configurationName, executionContextName, ((Source) source).getOrder()));
    }

    @Override
    public Optional<ISourceListManaged> getSourceListManaged(int id) {
        List<ISourceManaged> sources = getSources();
        if (id < 0 || sources.size() <= id)
            return Optional.empty();
        ISourceManaged contextSource = sources.get(id);
        if (contextSource.getType() != SourceType.MULTIPART)
            return Optional.empty();
        return Optional.of(new SourceList(executionContextTool, configurationName, executionContextName, List.of(contextSource)));
    }

    @Override
    public Optional<ISourceManaged> getSourceManaged(int id) {
        return Optional.ofNullable(id >= 0 && id < sources.size() ? sources.get(id) : null);
    }
}
