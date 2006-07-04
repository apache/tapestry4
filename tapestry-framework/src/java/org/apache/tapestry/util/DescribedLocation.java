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

package org.apache.tapestry.util;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.Defense;

/**
 * Implementation of {@link org.apache.hivemind.Location} that is used to describe a location within
 * a resource. This is used when the location within the resource can't be expressed as a line and
 * column. One example is for setting the location of an annotation. This is useful for line-precise
 * exception reporting of errors related to annotations.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class DescribedLocation implements Location
{
    private final Resource _resource;

    private final String _description;

    public DescribedLocation(Resource resource, String description)
    {
        Defense.notNull(resource, "resource");
        Defense.notNull(description, "description");

        _resource = resource;
        _description = description;
    }

    /**
     * Returns the description provided in the constructor.
     */

    public String toString()
    {
        return _description;
    }

    /**
     * Returns the resource provided in the constructor.
     */

    public Resource getResource()
    {
        return _resource;
    }

    /**
     * Always returns 0.
     */

    public int getLineNumber()
    {
        return 0;
    }

    /**
     * Always returns 0.
     */

    public int getColumnNumber()
    {
        return 0;
    }

    /**
     * A DescribedLocation is equal to another only if their resources are equal, and their
     * descriptions are equal.
     */
    public boolean equals(Object other)
    {
        if (other instanceof DescribedLocation)
        {
            DescribedLocation otherLocation = (DescribedLocation) other;

            return _resource.equals(otherLocation._resource)
                    && _description.equals(otherLocation._description);
        }

        return false;
    }
}
