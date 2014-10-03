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
package org.sa.rainbow.core.health;

/**
 * This interface defines common strings and commands required for the
 * Rainbow Health Protocol; participants in the protocol are humorously named
 * a "Rainbow Cloud."
 * <p>
 * Prescribed formats for Rainbow monitoring data, {x} is replaced by actual data:<ul>
 * <li> Timestamp in ISO8601 format: "[yyyy-MM-dd HH:mm:ss,SSS]"
 * <li> Rainbow init:  "=init="
 * <li> Rainbow term:  "=term="
 * <li> Rainbow memory usage:  "mem: {free} {total} {max}"
 * <li> Rainbow constraint eval begins:  "eval-begin"  
 * <li> Rainbow constraint eval ends:    "eval-end"
 * <li> Rainbow adaptation begins:  "adapt-begin"  
 * <li> Rainbow adaptation ends:    "adapt-end"
 * </ul>
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IRainbowHealthProtocol {

    public static enum CloudType {
        DELEGATE, PB_RELAY, GAUGE, PROBE, EFFECTOR
    }

    public static final String ID = "id";
    public static final String CLOUD_TYPE = "cloudT";
    public static final String LOCATION = "location";
    public static final String SERVICE_NAME = "svcName";
    public static final String EFFECTOR_KIND = "effK";
    public static final String PROBE_KIND = "probeK";
    public static final String TEXT = "txt";
    public static final String BEACON_PERIOD = "beaconPer";
    public static final String EXIT_VALUE = "exitVal";

    public static final String DATA_RAINBOW_INIT = "=init=";
    public static final String DATA_RAINBOW_TERM = "=term=";
    public static final String DATA_MEMORY_USE = "#mem: ";
    public static final String DATA_MODEL_PROPERTY = "#prop: ";
    public static final String DATA_CONSTRAINT_BEGIN = "#eval-begin";
    public static final String DATA_CONSTRAINT_END = "#eval-end";
    public static final String DATA_ADAPTATION_BEGIN = "#adapt-begin";
    public static final String DATA_ADAPTATION_SELECTION_BEGIN = "#adapt-select-begin";
    public static final String DATA_ADAPTATION_SELECTION_END   = "#adapt-select-end";
    public static final String DATA_ADAPTATION_STRATEGY_ATTR = "#adapt-aggAtt: ";
    public static final String DATA_ADAPTATION_STRATEGY_ATTR2 = "#adapt-aggAt': ";
    public static final String DATA_ADAPTATION_SCORE = "#adapt-score: ";
    public static final String DATA_ADAPTATION_STRATEGY = "#adapt-outcome: ";
    public static final String DATA_ADAPTATION_STAT = "#adapt-stat: ";
    public static final String DATA_ADAPTATION_END = "#adapt-end";

}
