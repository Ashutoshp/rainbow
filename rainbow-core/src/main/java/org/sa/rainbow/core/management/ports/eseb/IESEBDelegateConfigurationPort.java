package org.sa.rainbow.core.management.ports.eseb;

import java.util.List;
import java.util.Properties;

import org.sa.rainbow.core.management.ports.IRainbowDelegateConfigurationPort;
import org.sa.rainbow.core.models.EffectorDescription.EffectorAttributes;
import org.sa.rainbow.core.models.ProbeDescription.ProbeAttributes;

import edu.cmu.cs.able.eseb.rpc.ParametersTypeMapping;

public interface IESEBDelegateConfigurationPort extends IRainbowDelegateConfigurationPort {
    @Override
    @ParametersTypeMapping ({ "map<string,string>", "list<probe_description>", "list<effector_description>" })
    public abstract void sendConfigurationInformation (Properties props,
            List<ProbeAttributes> probes,
            List<EffectorAttributes> effectors);
}
