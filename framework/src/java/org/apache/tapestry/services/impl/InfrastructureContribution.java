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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * A contribution to the {@link org.apache.tapestry.services.Infrastructure}&nbsp;service. Defines
 * a property of Infrastructure and the value for that property. The infrastructure is setup in a
 * <em>mode</em> (currently, either "servlet" or "portlet"). Contributions that define a non-null
 * mode are ignored unless their mode matches the Infrastructure mode.
 * <p>
 * There are two configuration points that control Infrastructure:
 * <code>tapestry.Infrastructure</code> and <code>tapestry.InfrastructureOverride</code>.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class InfrastructureContribution extends BaseLocatable
{
    private String _property;

    private String _mode;

    private DeferredObject _deferredObject;

    public void setDeferredObject(DeferredObject deferredObject)
    {
        _deferredObject = deferredObject;
    }

    /**
     * The object which should be exposed as the given Infrastructure property.
     */
    public Object getObject()
    {
        return _deferredObject.getObject();
    }

    /**
     * The mode for which this contribution applies, or null if the contribution applies to all
     * modes.
     */

    public String getMode()
    {
        return _mode;
    }

    public void setMode(String mode)
    {
        _mode = mode;
    }

    /**
     * The property of the {@link org.apache.tapestry.services.Infrastructure}for which a value is
     * to be provided.
     */

    public String getProperty()
    {
        return _property;
    }

    public void setProperty(String property)
    {
        _property = property;
    }

    public boolean matchesMode(String mode)
    {
        // If our mode is null, then we only match null.

        if (_mode == mode)
            return true;

        // Otherwise, match our non-null model against their possibly-null mode.

        return _mode != null && _mode.equals(mode);
    }
}