package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IModule;
import ru.smcsystem.api.dto.ObjectElement;

import java.util.List;

public class Module implements IModule {
    private String name;
    private ObjectElement info;

    public static class ModuleType {
        public String name;
        public int minCountSources;
        public int maxCountSources;
        public int minCountExecutionContexts;
        public int maxCountExecutionContexts;
        public int minCountManagedConfigurations;
        public int maxCountManagedConfigurations;

        public ModuleType(String name, int minCountSources, int maxCountSources, int minCountExecutionContexts, int maxCountExecutionContexts, int minCountManagedConfigurations, int maxCountManagedConfigurations) {
            this.name = name;
            this.minCountSources = minCountSources;
            this.maxCountSources = maxCountSources;
            this.minCountExecutionContexts = minCountExecutionContexts;
            this.maxCountExecutionContexts = maxCountExecutionContexts;
            this.minCountManagedConfigurations = minCountManagedConfigurations;
            this.maxCountManagedConfigurations = maxCountManagedConfigurations;
        }
    }

    private List<ModuleType> types;

    public Module(String name, List<ModuleType> types) {
        this.name = name;
        this.types = types;
        info = new ObjectElement();
    }

    public Module(String name) {
        this(name, List.of(new ModuleType("default", 0, -1, 0, -1, 0, -1)));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModuleType> getTypes() {
        return types;
    }

    public void setTypes(List<ModuleType> types) {
        this.types = types;
    }

    @Override
    public int countTypes() {
        return types.size();
    }

    @Override
    public String getTypeName(int i) {
        return types.get(i).name;
    }

    @Override
    public int getMinCountSources(int i) {
        return types.get(i).minCountSources;
    }

    @Override
    public int getMaxCountSources(int i) {
        return types.get(i).maxCountSources;
    }

    @Override
    public int getMinCountExecutionContexts(int i) {
        return types.get(i).minCountExecutionContexts;
    }

    @Override
    public int getMaxCountExecutionContexts(int i) {
        return types.get(i).maxCountExecutionContexts;
    }

    @Override
    public int getMinCountManagedConfigurations(int i) {
        return types.get(i).minCountManagedConfigurations;
    }

    @Override
    public int getMaxCountManagedConfigurations(int i) {
        return types.get(i).maxCountManagedConfigurations;
    }

    @Override
    public ObjectElement getInfo() {
        return null;
    }

    public void setInfo(ObjectElement info) {
        this.info = info;
    }
}
