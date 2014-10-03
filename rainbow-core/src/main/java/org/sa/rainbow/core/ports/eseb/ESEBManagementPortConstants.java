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
package org.sa.rainbow.core.ports.eseb;

public interface ESEBManagementPortConstants {

    public static final String SEND_CONFIGURATION_INFORMATION = "sendConfigurationInformationMsg";
    public static final String REQUEST_CONFIG_INFORMATION     = "requestConfigurationInformationMsg";
    public static final String RECEIVE_HEARTBEAT              = "receiveHeartBeatMsg";
    public static final String START_DELEGATE = "startDelegateMsg";
    public static final String TERMINATE_DELEGATE = "terminateDelegateMsg";
    public static final String PAUSE_DELEGATE = "pauseDelegateMsg";
    public static final String START_PROBES                   = "startProbesMsg";
    public static final String KILL_PROBES                    = "killProbesMsg";

}
