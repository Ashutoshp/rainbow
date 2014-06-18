package org.sa.rainbow.core.ports.eseb;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.cmu.cs.able.eseb.bus.EventBus;
import edu.cmu.cs.able.eseb.conn.BusConnection;
import edu.cmu.cs.able.eseb.participant.ParticipantException;
import edu.cmu.cs.able.eseb.rpc.JavaRpcFactory;
import edu.cmu.cs.able.eseb.rpc.RpcEnvironment;
import edu.cmu.cs.able.typelib.jconv.TypelibJavaConversionRule;
import edu.cmu.cs.able.typelib.jconv.TypelibJavaConverter;

public class ESEBRPCConnector {
    public static Logger  LOGGER = Logger.getLogger (ESEBRPCConnector.class);
    private BusConnection m_client;
    private EventBus      m_srvr;

    private static class RPCInfo {
        RpcEnvironment m_env;
        String         participant_id;
    }

    private static Map<String, RPCInfo> m_infoMap = new HashMap<> ();

    private RPCInfo                     m_info;

    public ESEBRPCConnector (String remoteHost, short remotePort, String serverId) throws IOException,
    ParticipantException {
        setClient (remoteHost, remotePort);
        setUpEnvironment (serverId);
    }

    public ESEBRPCConnector (short port, String serverId) throws IOException, ParticipantException {
        m_srvr = ESEBProvider.getBusServer (port);
        ESEBProvider.useServer (m_srvr);
        setClient ("localhost", port);
        setUpEnvironment (serverId);

    }

    private void setUpEnvironment (String serverId) throws ParticipantException {
        synchronized (m_infoMap) {
            RPCInfo info = m_infoMap.get (serverId);

            if (info == null) {
                info = new RPCInfo ();
                info.participant_id = serverId;
                info.m_env = new RpcEnvironment (m_client, info.participant_id);
                m_infoMap.put (serverId, info);
                setupConverters (info);
            }
            m_info = info;
        }
    }

    private void setupConverters (RPCInfo info) {
        // Share the participant, environment, and then you can share the connection 
        TypelibJavaConverter converter = info.m_env.converter ();
        List<? extends TypelibJavaConversionRule> rules = ESEBProvider.getConversionRules ();
        for (TypelibJavaConversionRule r : rules) {
            converter.add (r);
        }

    }

    protected void setClient (String remoteHost, short remotePort) {
        LOGGER.info ("Getting RPC connector at " + remoteHost + ":" + remotePort);
        if (m_client != null) {
            if (!m_client.host ().equals (remoteHost) || m_client.port () != remotePort) {
                m_client.stop ();
                ESEBProvider.releaseClient (m_client);
                m_client = null;
            }
            else
                return;
        }
        m_client = ESEBProvider.getBusClient (remoteHost, remotePort);
        ESEBProvider.useClient (m_client);
    }

    public BusConnection getESEBConnection () {
        return m_client;
    }

    private String getParticipantId () {
        return m_info.participant_id;
    }

    private RpcEnvironment getRPCEnvironment () {
        return m_info.m_env;
    }

    public <T> T createRemoteStub (Class<T> cls, String obj_id) {
        LOGGER.info ("Creating RPC Requirer end for " + obj_id + " with participant " + getParticipantId ());
        return JavaRpcFactory.create_remote_stub (cls, this.getRPCEnvironment (), this.getParticipantId (), 5000,
                obj_id);
    }

    public <T> void createRegistryWrapper (Class<T> cls, T wrapped, String obj_id) {
        LOGGER.info ("Creating RPC Provider end for " + obj_id + " with participant " + getParticipantId ());
        JavaRpcFactory.create_registry_wrapper (cls, wrapped, getRPCEnvironment (), obj_id);
    }

    public void close () throws IOException {
        if (m_client != null) {
            ESEBProvider.releaseClient (m_client);
        }
        if (m_srvr != null) {
            ESEBProvider.releaseServer (m_srvr);
        }
    }

}
