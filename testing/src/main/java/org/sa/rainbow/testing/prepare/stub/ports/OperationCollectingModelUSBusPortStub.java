package org.sa.rainbow.testing.prepare.stub.ports;

import org.sa.rainbow.core.models.IModelInstance;
import org.sa.rainbow.core.models.ModelReference;
import org.sa.rainbow.core.models.commands.IRainbowOperation;
import org.sa.rainbow.core.ports.IModelUSBusPort;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This is stubbed AbstractModelUSBusPortStub that stores all operations from a gauge.
 */
public class OperationCollectingModelUSBusPortStub implements IModelUSBusPort {

    private BlockingQueue<IRainbowOperation> operations = new LinkedBlockingQueue<>();

    /**
     * Wait and retrieve the next action from the gauge.
     *
     * @return the next operation
     */
    public IRainbowOperation takeOperation() throws InterruptedException {
        return operations.take();
    }

    /**
     * Wait and retrieve the next action from the gauge, with timeout.
     *
     * @param milliseconds timeout in millisecond
     * @return the next operation, or null if timed-out
     */
    public IRainbowOperation takeOperation(long milliseconds) throws InterruptedException {
        return operations.poll(milliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Is used to update the model. On the Model manager side, it calls the model manager to request an update to the
     * model. On the model client side, it calls this method to request the updated <br>
     * NOTE: This is a pure publish model - clients do not care about whether the update succeeded
     *
     * @param command The command to use to update the model
     */
    @Override
    public void updateModel(IRainbowOperation command) {
        try {
            operations.put(command);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Is used to update the model with a list of commands. The commands may be executed as a transaction (i.e., failure
     * of a command results in no change to the model.)
     *
     * @param commands    The list of commands to update the model
     * @param transaction Whether this should be run as a transaction
     */
    @Override
    public void updateModel(List<IRainbowOperation> commands, boolean transaction) {
        for (IRainbowOperation command : commands) {
            updateModel(command);
        }
    }

    @Override
    public <T> IModelInstance<T> getModelInstance(ModelReference modelReference) {
        return null;
    }

    @Override
    public void dispose() {
        // Do nothing
    }
}
