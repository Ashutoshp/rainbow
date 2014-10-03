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
package org.sa.rainbow.core.ports;

import java.util.Map;

/**
 * The API through which probes report their lifeculce (from creation, configuration, to deletion)
 * 
 * @author Bradley Schmerl: schmerl
 * 
 */
public interface IProbeLifecyclePort extends IDisposablePort {

    public String PROBE_CREATED      = "probeCreated";
    public String PROBE_ID           = "probeId";
    public String PROBE_LOCATION     = "probeLocation";
    public String PROBE_NAME         = "probeName";
    public String PROBE_DELETED      = "probeDeleted";
    public String PROBE_CONFIGURED   = "probeConfigured";
    public String CONFIG_PARAM_NAME  = "probeConfigParamName";
    public String CONFIG_PARAM_VALUE = "probeConfigParamValue";
    public String PROBE_DEACTIVATED  = "probeDeactivated";
    public String PROBE_ACTIVATED    = "probeActivated";

    /**
     * Reports that a probes has been created, giving it's id, type, and associated model
     * 
     */
    public void reportCreated ();

    /**
     * Reports that a probes has been deleted, giving it's id, type, and associated model
     * 
     */
    public void reportDeleted ();

    /**
     * Reports that a gaugprobese has been configured, along with the configuration parameters
     * 
     * @param configParams
     *            The parameters with which it was configured
     */
    public void reportConfigured (Map<String, Object> configParams);

    public void reportDeactivated ();

    public void reportActivated ();


}
