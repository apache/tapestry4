//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.record;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *  Used to identify a property change.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ChangeKey
{
    private int _hashCode = -1;
    private String _componentPath;
    private String _propertyName;

    public ChangeKey(String componentPath, String propertyName)
    {
        _componentPath = componentPath;
        _propertyName = propertyName;
    }

    public boolean equals(Object object)
    {
        if (object == null)
            return false;

        if (this == object)
            return true;

        if (!(object instanceof ChangeKey))
            return false;

        ChangeKey other = (ChangeKey) object;

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(_propertyName, other._propertyName);
        builder.append(_componentPath, other._componentPath);

        return builder.isEquals();
    }

    public String getComponentPath()
    {
        return _componentPath;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    /**
     *
     *  Returns a hash code computed from the
     *  property name and component path.
     *
     **/

    public int hashCode()
    {
        if (_hashCode == -1)
        {
            HashCodeBuilder builder = new HashCodeBuilder(257, 23); // Random

            builder.append(_propertyName);
            builder.append(_componentPath);

            _hashCode = builder.toHashCode();
        }

        return _hashCode;
    }
}