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
package znews_ss.operator;

import org.acmestudio.acme.ModelHelper;
import org.acmestudio.acme.element.IAcmeElement;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.stitch.adaptation.IGenericArchOperators;

import java.util.HashMap;
import java.util.Map;

public class EffectOp {
    // This needs to be changed to commands
    public static void blackhole (Object lb, String client) {
        Map<String, String> pairs = new HashMap<> ();
        pairs.put("client", client);
        IGenericArchOperators opProvider = (IGenericArchOperators )Rainbow.instance ().getRainbowMaster ()
                .adaptationExecutors().get (ModelHelper.getAcmeSystem (((IAcmeElement )lb)).getName () + ":Acme");
        opProvider.changeState ("blackhole", lb, pairs);
    }
}
