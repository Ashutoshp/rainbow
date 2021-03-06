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

import edu.cmu.cs.able.eseb.rpc.ParametersTypeMapping;
import edu.cmu.cs.able.eseb.rpc.ReturnTypeMapping;
import org.sa.rainbow.core.models.IModelInstance;
import org.sa.rainbow.core.models.ModelReference;
import org.sa.rainbow.core.ports.IModelsManagerPort;

import java.util.Collection;

public interface IESEBModelsManagerRemoteInterface extends IModelsManagerPort {

    String DEFAULT_ESEB_RPCNAME = "rainbow_models_manager";
    String MODEL_CONVERTER_CLASS = "org.sa.rainbow.model.converter.eseb.class";

    @Override
    @ReturnTypeMapping ("set<string>")
    Collection<? extends String> getRegisteredModelTypes ();

    @Override
    @ReturnTypeMapping ("rainbow_model")
    @ParametersTypeMapping ({ "model_reference" })
    <T> IModelInstance<T> getModelInstance (ModelReference modelRef);
    
    @Override
    @ReturnTypeMapping("bool")
    @ParametersTypeMapping({ "model_reference"})
    boolean isModelLocked(ModelReference modelRef);

}
