// Copyright 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.enhance;

import java.util.Iterator;
import java.util.Map;

import org.apache.hivemind.ErrorLog;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.InjectSpecification;

/**
 * Iterates over the {@link org.apache.tapestry.spec.InjectSpecification}s and locates and
 * delegates to a {@link org.apache.tapestry.enhance.InjectEnhancementWorker} for each one.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class DispatchToInjectWorker implements EnhancementWorker
{
    private ErrorLog _errorLog;

    private Map _injectWorkers;

    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        Iterator i = spec.getInjectSpecifications().iterator();

        while (i.hasNext())
        {
            InjectSpecification is = (InjectSpecification) i.next();

            invokeWorker(op, is);
        }
    }

    private void invokeWorker(EnhancementOperation op, InjectSpecification spec)
    {
        try
        {
            InjectEnhancementWorker worker = (InjectEnhancementWorker) _injectWorkers.get(spec
                    .getType());

            if (worker == null)
            {
                _errorLog.error(EnhanceMessages.unknownInjectType(spec.getProperty(), spec
                        .getType()), spec.getLocation(), null);
                return;
            }

            worker.performEnhancement(op, spec);

        }
        catch (Exception ex)
        {
            _errorLog.error(EnhanceMessages.errorAddingProperty(spec.getProperty(), op
                    .getBaseClass(), ex), spec.getLocation(), ex);
        }
    }

    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public void setInjectWorkers(Map injectWorkers)
    {
        _injectWorkers = injectWorkers;
    }
}
