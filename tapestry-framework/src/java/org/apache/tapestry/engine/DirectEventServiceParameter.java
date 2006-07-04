// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IDirectEvent;

/**
 * Parameter object used by {@link org.apache.tapestry.engine.DirectEventService}.
 * 
 * @author jkuhnert
 * @since 4.1
 */
public class DirectEventServiceParameter
{
    protected IDirectEvent _direct;

    protected Object[] _serviceParameters;

    protected String[] _updateParts;
    
    protected boolean _json;
    
    public DirectEventServiceParameter(IDirectEvent direct)
    {
        this(direct, null, null, false);
    }

    public DirectEventServiceParameter(IDirectEvent direct, Object[] serviceParameters)
    {
        this(direct, serviceParameters, null, false);
    }
    
    /**
     * Creates a new direct service parameter map. 
     * 
     * @param direct The object implementing the direct triggerable interface
     * @param serviceParameters The parameters for the triggered object
     * @param updateParts The parts expected to be updated on any returned response
     * triggerd by this direct call.
     */
    public DirectEventServiceParameter(IDirectEvent direct, Object[] serviceParameters,
            String[] updateParts, boolean json)
    {
        Defense.notNull(direct, "direct");
        
        _direct = direct;
        _serviceParameters = serviceParameters;
        _updateParts = updateParts;
        _json = json;
    }

    public IDirectEvent getDirect()
    {
        return _direct;
    }

    public Object[] getServiceParameters()
    {
        return _serviceParameters;
    }
    
    public String[] getUpdateParts()
    {
        return _updateParts;
    }
    
    public boolean isJSON()
    {
        return _json;
    }
}
