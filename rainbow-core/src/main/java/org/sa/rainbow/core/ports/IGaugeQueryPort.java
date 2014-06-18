package org.sa.rainbow.core.ports;

import java.util.Collection;

import org.sa.rainbow.core.gauges.IGaugeState;
import org.sa.rainbow.core.models.commands.IRainbowOperation;

/**
 * The interface through which a gauge can be queried.
 * 
 * @author Bradley Schmerl: schmerl
 * 
 */
public interface IGaugeQueryPort extends IDisposablePort {
    /**
     * Returns the entire state of this Gauge via the supplied lists.
     * 
     * @return A gauge state representing all the setup and config params of the gauge, as well as issued commands
     * 
     */
    public IGaugeState queryGaugeState ();

    /**
     * Queries for a command identified by the command name.
     * 
     * @param commandName
     *            the name of the command to get information for
     * @return A representation of the command, including the model it affects, the target, and the parameters last
     *         issued
     */
    public IRainbowOperation queryCommand (String commandName);

    /**
     * Queries for all of the commands reported by this Gauge.
     * 
     * @return Collection<IRainbowModelCommandRepresentation> A collection of all the commands last issued, one per
     *         command command mapping, including the target and parameters used.
     */
    public Collection<IRainbowOperation> queryAllCommands ();


}
