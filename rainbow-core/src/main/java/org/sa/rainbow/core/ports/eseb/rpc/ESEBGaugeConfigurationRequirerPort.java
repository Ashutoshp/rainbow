/*
 * The MIT License
 *
 * Copyright 2014 CMU ABLE Group.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.sa.rainbow.core.ports.eseb.rpc;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.sa.rainbow.core.gauges.IGaugeIdentifier;
import org.sa.rainbow.core.ports.eseb.ESEBProvider;
import org.sa.rainbow.core.util.TypedAttributeWithValue;

import edu.cmu.cs.able.eseb.participant.ParticipantException;

public class ESEBGaugeConfigurationRequirerPort extends AbstractESEBDisposableRPCPort implements
IESEBGaugeConfigurationRemoteInterface {
    static Logger                                  LOGGER = Logger.getLogger (ESEBGaugeConfigurationRequirerPort.class);
    private IESEBGaugeConfigurationRemoteInterface m_stub;

    public ESEBGaugeConfigurationRequirerPort (IGaugeIdentifier gauge) throws IOException, ParticipantException {

        super (ESEBProvider.getESEBClientHost (), ESEBProvider.getESEBClientPort (),
                gauge.id ());
        m_stub = getConnectionRole().createRemoteStub (IESEBGaugeConfigurationRemoteInterface.class,
                gauge.id () + IESEBGaugeConfigurationRemoteInterface.class.getSimpleName ());

    }

    @Override
    public boolean configureGauge (List<TypedAttributeWithValue> configParams) {
        return m_stub.configureGauge (configParams);
    }

    @Override
    public boolean reconfigureGauge () {
        return m_stub.reconfigureGauge ();
    }


}
