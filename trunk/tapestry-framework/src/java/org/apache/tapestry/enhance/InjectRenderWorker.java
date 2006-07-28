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

import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.services.ComponentRenderWorker;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Injects the component's render worker chain as the
 * {@link org.apache.tapestry.IComponent#getRenderWorker() renderWorker}property.
 * 
 * @author jkuhnert
 * @since 4.1
 */
public class InjectRenderWorker implements EnhancementWorker
{

    public static final String PROPERTY_NAME = "renderWorker";
    
    private ErrorLog _errorLog;
    
    private ComponentRenderWorker _renderWorker;
    
    public void performEnhancement(EnhancementOperation op, IComponentSpecification spec)
    {
        try
        {
            injectRenderWorker(op, spec);
        }
        catch (Exception ex)
        {
            _errorLog.error(EnhanceMessages.errorAddingProperty(
                    PROPERTY_NAME, op.getBaseClass(), ex), spec
                    .getLocation(), ex);
        }
    }
    
    public void injectRenderWorker(EnhancementOperation op, IComponentSpecification spec)
    {
        Defense.notNull(op, "op");
        Defense.notNull(spec, "spec");
        
        op.claimReadonlyProperty(PROPERTY_NAME);
        
        String fieldName = op.addInjectedField("_$"
                + PROPERTY_NAME, ComponentRenderWorker.class,
                _renderWorker);
        
        EnhanceUtils.createSimpleAccessor(op, fieldName,
                PROPERTY_NAME, ComponentRenderWorker.class,
                spec.getLocation());
    }
    
    public void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }
    
    public void setRenderWorker(ComponentRenderWorker worker)
    {
        _renderWorker = worker;
    }
}
