package org.sa.rainbow.core.adaptation;

import org.sa.rainbow.core.error.RainbowException;

public interface IEvaluable {
    /**
     * Evaluates the adaptation stored in the scope; return a result if applicable.
     * 
     * @param incoming
     *            arguments
     * @return <code>Object</code> if evaluation has result, null otherwise
     */
    Object evaluate (Object[] argsIn) throws RainbowException;

    /**
     * Calculates the estimated average time duration required to perform this evaluable construct.
     * 
     * @return long estimated upper-bound time cost in milliseconds
     * @deprecated
     */
    @Deprecated
    long estimateAvgTimeCost ();


}
