package ru.smcsystem.test.emulate;

import ru.smcsystem.api.dto.IModule;

public class Module implements IModule {

    private final String name;
    private final int minCountSources;
    private final int maxCountSources;
    private final int minCountExecutionContexts;
    private final int maxCountExecutionContexts;
    private final int minCountManagedConfigurations;
    private final int maxCountManagedConfigurations;

    public Module(
            String name
            , int minCountSources
            , int maxCountSources
            , int minCountExecutionContexts
            , int maxCountExecutionContexts
            , int minCountManagedConfigurations
            , int maxCountManagedConfigurations
    ) {
        this.name = name;
        this.minCountSources = minCountSources;
        this.maxCountSources = maxCountSources;
        this.minCountExecutionContexts = minCountExecutionContexts;
        this.maxCountExecutionContexts = maxCountExecutionContexts;
        this.minCountManagedConfigurations = minCountManagedConfigurations;
        this.maxCountManagedConfigurations = maxCountManagedConfigurations;
    }

    public Module(String name) {
        this(name, -1, -1, -1, -1, -1, -1);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMinCountSources() {
        return minCountSources;
    }

    @Override
    public int getMaxCountSources() {
        return maxCountSources;
    }

    @Override
    public int getMinCountExecutionContexts() {
        return minCountExecutionContexts;
    }

    @Override
    public int getMaxCountExecutionContexts() {
        return maxCountExecutionContexts;
    }

    @Override
    public int getMinCountManagedConfigurations() {
        return minCountManagedConfigurations;
    }

    @Override
    public int getMaxCountManagedConfigurations() {
        return maxCountManagedConfigurations;
    }

}
