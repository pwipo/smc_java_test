package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.*;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.ObjectType;
import ru.smcsystem.api.exceptions.ModuleException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Container implements IContainerManaged {

    private ExecutionContextToolImpl executionContextTool;
    private String name;
    private Boolean enable;
    public List<IContainer> containers;
    public List<IConfiguration> configurations;
    private String smclText;
    private ObjectArray shapes;

    public Container(ExecutionContextToolImpl executionContextTool, String name, List<IContainer> containers, List<IConfiguration> configurations) {
        this.name = name;
        this.enable = true;
        this.containers = containers != null ? new ArrayList<>(containers) : new ArrayList<>();
        this.configurations = configurations != null ? new ArrayList<>(configurations) : new ArrayList<>();
        setExecutionContextTool(executionContextTool);
        smclText = "";
        shapes = new ObjectArray();
    }

    public void setExecutionContextTool(ExecutionContextToolImpl executionContextTool) {
        this.executionContextTool = executionContextTool;
    }

    @Override
    public int countConfigurations() {
        return configurations.size();
    }

    @Override
    public Optional<IConfiguration> getConfiguration(int id) {
        return configurations.size() > id && id >= 0 ? Optional.of(configurations.get(id)) : Optional.empty();
    }

    private List<Configuration> getManagedConfigurations() {
        List<Configuration> managedConfigurations = executionContextTool.getManagedConfigurations();
        return configurations.stream()
                .filter(managedConfigurations::contains)
                .map(c -> (Configuration) c)
                .collect(Collectors.toList());
    }

    @Override
    public int countManagedConfigurations() {
        return getManagedConfigurations().size();
    }

    @Override
    public Optional<IConfigurationManaged> getManagedConfiguration(int id) {
        List<Configuration> moduleConfigurations = getManagedConfigurations();
        if (id < 0 || moduleConfigurations.size() <= id)
            return Optional.empty();
        Configuration moduleConfiguration = moduleConfigurations.get(id);
        int i = configurations.indexOf(moduleConfiguration);
        return executionContextTool.getManagedConfiguration(i);
    }

    @Override
    public int countContainers() {
        return containers.size();
    }

    @Override
    public Optional<IContainer> getContainer(int id) {
        return containers.size() > id && id >= 0 ? Optional.of(containers.get(id)) : Optional.empty();
    }

    @Override
    public IContainerManaged createContainer(String name) {
        Container container = new Container(executionContextTool, name, null, null);
        containers.add(container);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_CONTAINER_CREATE, container.getName());
        return container;
    }

    @Override
    public void removeContainer(int id) {
        Optional<IContainer> optionalIContainer = getContainer(id);
        if (optionalIContainer.isEmpty())
            throw new ModuleException("id");
        Container container = (Container) optionalIContainer.get();
        if (container.countConfigurations() > 0)
            throw new ModuleException("container has child configurations");
        if (container.countContainers() > 0)
            throw new ModuleException("container has child containers");
        containers.remove(id);
        executionContextTool.add(MessageType.CONFIGURATION_CONTROL_CONTAINER_REMOVE, container.getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public Optional<IConfigurationManaged> getConfigurationManaged(int id) {
        return configurations.size() > id && id >= 0 ? Optional.of((IConfigurationManaged) configurations.get(id)) : Optional.empty();
    }

    @Override
    public Optional<IContainerManaged> getContainerManaged(int id) {
        return containers.size() > id && id >= 0 ? Optional.of((IContainerManaged) containers.get(id)) : Optional.empty();
    }

    @Override
    public String getSmcl() {
        return smclText;
    }

    @Override
    public boolean saveSmcl(String text) {
        smclText = text;
        return true;
    }

    @Override
    public ObjectArray getShapes() {
        return shapes;
    }

    @Override
    public ObjectArray getDecorationShapes() {
        if (shapes.size() == 0)
            return shapes;
        return new ObjectArray(
                Stream.iterate(0, i -> i + 1)
                        .limit(shapes.size())
                        .map(n -> (ObjectElement) shapes.get(n))
                        .filter(e -> e.findField("type")
                                .map(f -> f.getValue().toString())
                                .filter(s -> !s.equals("application") && !s.equals("container") && !s.equals("configuration"))
                                .isPresent())
                        .collect(Collectors.toList()), ObjectType.OBJECT_ELEMENT);
    }

    public void setShapes(ObjectArray shapes) {
        this.shapes = shapes;
    }

}
