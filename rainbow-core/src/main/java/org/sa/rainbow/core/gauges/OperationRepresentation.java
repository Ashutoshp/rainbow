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
package org.sa.rainbow.core.gauges;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sa.rainbow.core.models.commands.IRainbowOperation;
import org.sa.rainbow.core.util.TypedAttribute;
import org.sa.rainbow.util.HashCodeUtil;

public class OperationRepresentation implements IRainbowOperation, Cloneable {

    private String[] m_parameters;
    private String   m_target;
    private String   m_operationName;
    private String   m_modelName;
    private String   m_modelType;
    private String   m_origin;

    @Override
    public String toString () {
        return MessageFormat.format ("O[{0}:{1}/{2}.{3}({4}){5}]", m_modelName, m_modelType, m_operationName, m_target,
                m_parameters == null ? "" : Arrays.toString (m_parameters), m_origin == null ? "" : ("<" + m_origin));
    }

    public OperationRepresentation (IRainbowOperation cmd) {
        m_parameters = new String[cmd.getParameters ().length];
        String[] parameters = cmd.getParameters ();
        for (int i = 0; i < parameters.length; i++) {
            m_parameters[i] = parameters[i];
        }
        m_target = cmd.getTarget ();
        m_operationName = cmd.getName ();
        m_modelName = cmd.getModelName ();
        m_modelType = cmd.getModelType ();
        m_origin = cmd.getOrigin ();

    }

    public OperationRepresentation (String name, String modelName, String modelType, String target,
            String... parameters) {
        m_operationName = name;
        m_modelName = modelName;
        m_modelType = modelType;
        m_target = target;
        m_parameters = parameters;

    }


    @Override
    public String[] getParameters () {
        return m_parameters;
    }

    @Override
    public String getTarget () {
        return m_target;
    }

    @Override
    public String getName () {
        return m_operationName;
    }

    @Override
    public String getModelName () {
        return m_modelName;
    }

    @Override
    public String getModelType () {
        return m_modelType;
    }

    @Override
    protected OperationRepresentation clone () throws CloneNotSupportedException {
        return (OperationRepresentation )super.clone ();
    }

    @Override
    public boolean equals (Object obj) {
        if (obj != this) {
            if (obj instanceof OperationRepresentation) {
                OperationRepresentation cr = (OperationRepresentation )obj;
                return (cr.getModelType () == getModelType () || (getModelType () != null && getModelType ().equals (
                        cr.getModelType ())))
                        && (cr.getName () == getName () || (getName () != null && getName ()
                        .equals (cr.getName ())))

                        && (cr.getTarget () == getTarget () || (getTarget () != null && getTarget ().equals (
                                cr.getTarget ()))) && Arrays.equals (getParameters (), cr.getParameters ());
            }
            return false;
        }
        return true;
    }

    @Override
    public int hashCode () {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash (result, getModelType ());
        result = HashCodeUtil.hash (result, getModelName ());
        result = HashCodeUtil.hash (result, getName ());
        result = HashCodeUtil.hash (result, getTarget ());
        result = HashCodeUtil.hash (result, getParameters ());
        return result;
    }

    void setModel (String name, String type) {
        m_modelName = name;
        m_modelType = type;
    }

    void setModel (TypedAttribute modelRef) {
        m_modelName = modelRef.getName ();
        m_modelType = modelRef.getType ();
    }

    @Override
    public void setOrigin (String o) {
        m_origin = o;
    }

    static Pattern pattern = Pattern
            .compile ("\\\"?(([\\w\\$\\<\\>\\\"\\.]+)\\.)?(\\w+)\\s*\\(([\\w, \\.{}\\$\\<\\>\\\"]*)\\)\\\"?");

    public static OperationRepresentation parseCommandSignature (String commandSignature) {
        Matcher matcher = pattern.matcher (commandSignature);
        if (matcher.find ()) {
            String target = matcher.group (2);
            String commandName = matcher.group (3);
            String unprocessedParams = matcher.group (4);
            String[] parameters = new String[0];
            if (unprocessedParams != null) {
                parameters = unprocessedParams.split ("\\s*,\\s*");
            }
            OperationRepresentation rep = new OperationRepresentation (commandName, null, null, target,
                    parameters);
            return rep;
        }
        return null;
    }

    @Override
    public String getOrigin () {
        return m_origin;
    }

}
