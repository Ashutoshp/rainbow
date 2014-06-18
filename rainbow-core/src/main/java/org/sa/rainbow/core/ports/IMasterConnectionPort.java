package org.sa.rainbow.core.ports;

import java.util.Properties;

import org.sa.rainbow.core.RainbowComponentT;
import org.sa.rainbow.core.error.RainbowConnectionException;

/**
 * This interface represents a port through which delegates connect to the master
 * 
 * @author Bradley Schmerl: schmerl
 * 
 */
public interface IMasterConnectionPort extends IDisposablePort {

    /** Message types for sending to the Rainbow Master, which could be displayed on the UI **/
    public enum ReportType {INFO, WARNING, ERROR, FATAL};

    /**
     * Connects a delegate to the master through the connection port
     * 
     * @param delegateID
     *            The id of the delegate being connected
     * @param connectionProperties
     *            The connection properties, representing information from the delegate that needs to be passed to the
     *            master.
     * 
     * @return A deployment port through which the delegate can be managed
     * @throws RainbowConnectionException TODO
     */
    public IDelegateManagementPort connectDelegate (String delegateID, Properties connectionProperties) throws RainbowConnectionException;

    /**
     * Disconnects the delegate from the master. The master will delete the delegate from its records. Any processing
     * that comes from a disconnected delegate will be logged as an error and not processed.
     * 
     * @param delegateId
     *            The delegate being disconnected
     */
    public void disconnectDelegate (String delegateId);

    public void report (String delegateID, ReportType type, RainbowComponentT compT, String msg);


}
