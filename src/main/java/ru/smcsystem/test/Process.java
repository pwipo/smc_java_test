package ru.smcsystem.test;

import ru.smcsystem.api.dto.IMessage;
import ru.smcsystem.api.enumeration.MessageType;
import ru.smcsystem.api.enumeration.ValueType;
import ru.smcsystem.api.module.Module;
import ru.smcsystem.api.tools.ConfigurationTool;
import ru.smcsystem.test.emulate.ConfigurationToolImpl;
import ru.smcsystem.test.emulate.ExecutionContextToolImpl;
import ru.smcsystem.test.emulate.Message;
import ru.smcsystem.test.emulate.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Process {

    private final ConfigurationToolImpl configurationTool;

    private final Module module;

    public Process(ConfigurationToolImpl configurationTool, Module module) {
        this.configurationTool = configurationTool;
        this.module = module;
    }

    public ConfigurationTool getConfigurationTool() {
        return configurationTool;
    }

    public List<IMessage> fullLifeCycle(ExecutionContextToolImpl executionContextTool) {
        List<IMessage> result = new ArrayList<>();
        result.addAll(start());
        result.addAll(execute(executionContextTool));
        result.addAll(update());
        result.addAll(execute(executionContextTool));
        result.addAll(stop());
        return result;
    }

    synchronized public List<IMessage> start() {
        List<IMessage> result = new ArrayList<>();
        if (module == null)
            return result;

        result.add(new Message(MessageType.ACTION_START, new Date(), new Value(ValueType.INTEGER, 1)));

        try {
            /*
            if (module instanceof Internal) {
                start((Internal) module);
            } else if (module instanceof External) {
                start((External) module);
            } else if (module instanceof FlowController) {
                start((FlowController) module);
            } else if (module instanceof ConfigurationController) {
                start((ConfigurationController) module);
            }
            */
            module.start(configurationTool);
        } catch (Exception e) {
            result.add(new Message(MessageType.ACTION_ERROR, new Date(), new Value(ValueType.STRING, "error " + e.getMessage())));
        }

        result.add(new Message(MessageType.ACTION_STOP, new Date(), new Value(ValueType.INTEGER, 1)));

        return result;
    }

    // synchronized private void start(Internal module) {
    //     module.start((InternalConfigurationTool) configurationTool);
    // }

    // synchronized private void start(External module) {
    //     module.start((ExternalConfigurationTool) configurationTool);
    // }

    // synchronized private void start(FlowController module) {
    //     module.start((FlowControllerConfigurationTool) configurationTool);
    // }

    // synchronized private void start(ConfigurationController module) {
    //     module.start((ConfigurationControllerConfigurationTool) configurationTool);
    // }

    public List<IMessage> execute(ExecutionContextToolImpl executionContextTool) {
        List<IMessage> result = new ArrayList<>();
        if (module == null)
            return result;

        configurationTool.init(executionContextTool);
        executionContextTool.init(configurationTool);
        result.add(new Message(MessageType.ACTION_START, new Date(), new Value(ValueType.INTEGER, 1)));

        try {
            List<IMessage> output = new ArrayList<>(executionContextTool.getOutput());
            executionContextTool.getOutput().clear();
            /*
            if (module instanceof Internal) {
                execute((Internal) module, (InternalExecutionContextTool) executionContextTool);
            } else if (module instanceof External) {
                execute((External) module, (ExternalExecutionContextTool) executionContextTool);
            } else if (module instanceof FlowController) {
                execute((FlowController) module, (FlowControlTool) executionContextTool);
            } else if (module instanceof ConfigurationController) {
                execute((ConfigurationController) module, (ConfigurationControlTool) executionContextTool);
            }
            */
            module.process(
                    configurationTool,
                    executionContextTool
            );
            result.addAll(executionContextTool.getOutput());
            executionContextTool.getOutput().addAll(0, output);
        } catch (Exception e) {
            result.add(new Message(MessageType.ACTION_ERROR, new Date(), new Value(ValueType.STRING, "error " + e.getMessage())));
        }

        result.add(new Message(MessageType.ACTION_STOP, new Date(), new Value(ValueType.INTEGER, 1)));

        return result;
    }

    /*
    synchronized private void execute(Internal module, InternalExecutionContextTool executionContextTool) {
        module.process(
                (InternalConfigurationTool) configurationTool,
                executionContextTool
        );
    }

    synchronized private void execute(External module, ExternalExecutionContextTool executionContextTool) {
        module.process(
                (ExternalConfigurationTool) configurationTool,
                executionContextTool
        );
    }

    synchronized private void execute(FlowController module, FlowControlTool executionContextTool) {
        module.process(
                (FlowControllerConfigurationTool) configurationTool,
                executionContextTool
        );
    }

    synchronized private void execute(ConfigurationController module, ConfigurationControlTool executionContextTool) {
        module.process(
                (ConfigurationControllerConfigurationTool) configurationTool,
                executionContextTool
        );
    }
    */

    synchronized public List<IMessage> update() {
        List<IMessage> result = new ArrayList<>();
        if (module == null)
            return result;

        result.add(new Message(MessageType.ACTION_START, new Date(), new Value(ValueType.INTEGER, 1)));

        try {
            /*
            if (module instanceof Internal) {
                update((Internal) module);
            } else if (module instanceof External) {
                update((External) module);
            } else if (module instanceof FlowController) {
                update((FlowController) module);
            } else if (module instanceof ConfigurationController) {
                update((ConfigurationController) module);
            }
            */
            module.update(configurationTool);
        } catch (Exception e) {
            result.add(new Message(MessageType.ACTION_ERROR, new Date(), new Value(ValueType.STRING, "error " + e.getMessage())));
        }

        result.add(new Message(MessageType.ACTION_STOP, new Date(), new Value(ValueType.INTEGER, 1)));

        return result;
    }

    /*
    synchronized private void update(Internal module) {
        module.update((InternalConfigurationTool) configurationTool);
    }

    synchronized private void update(External module) {
        module.update((ExternalConfigurationTool) configurationTool);
    }

    synchronized private void update(FlowController module) {
        module.update((FlowControllerConfigurationTool) configurationTool);
    }

    synchronized private void update(ConfigurationController module) {
        module.update((ConfigurationControllerConfigurationTool) configurationTool);
    }
    */

    synchronized public List<IMessage> stop() {
        List<IMessage> result = new ArrayList<>();
        if (module == null)
            return result;

        result.add(new Message(MessageType.ACTION_START, new Date(), new Value(ValueType.INTEGER, 1)));

        try {
            /*
            if (module instanceof Internal) {
                stop((Internal) module);
            } else if (module instanceof External) {
                stop((External) module);
            } else if (module instanceof FlowController) {
                stop((FlowController) module);
            } else if (module instanceof ConfigurationController) {
                stop((ConfigurationController) module);
            }
            */
            module.stop(configurationTool);
        } catch (Exception e) {
            result.add(new Message(MessageType.ACTION_ERROR, new Date(), new Value(ValueType.STRING, "error " + e.getMessage())));
        }

        result.add(new Message(MessageType.ACTION_STOP, new Date(), new Value(ValueType.INTEGER, 1)));

        return result;
    }

    /*
    synchronized private void stop(Internal module) {
        module.stop((InternalConfigurationTool) configurationTool);
    }

    synchronized private void stop(External module) {
        module.stop((ExternalConfigurationTool) configurationTool);
    }

    synchronized private void stop(FlowController module) {
        module.stop((FlowControllerConfigurationTool) configurationTool);
    }

    synchronized private void stop(ConfigurationController module) {
        module.stop((ConfigurationControllerConfigurationTool) configurationTool);
    }
    */

}
