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

package org.apache.tapestry;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *  Implementation of the {@link org.apache.tapestry.ILocation} interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class Location implements ILocation
{
    private IResourceLocation _resourceLocation;
    private int _lineNumber = -1;
    private int _columnNumber = -1;

    public Location(IResourceLocation location)
    {
        _resourceLocation = location;
    }

    public Location(IResourceLocation location, int lineNumber)
    {
        this(location);

        _lineNumber = lineNumber;
    }

    public Location(IResourceLocation location, int lineNumber, int columnNumber)
    {
        this(location);

        _lineNumber = lineNumber;
        _columnNumber = columnNumber;
    }

    public IResourceLocation getResourceLocation()
    {
        return _resourceLocation;
    }

    public int getLineNumber()
    {
        return _lineNumber;
    }

    public int getColumnNumber()
    {
        return _columnNumber;
    }

    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(237, 53);

        builder.append(_resourceLocation);
        builder.append(_lineNumber);
        builder.append(_columnNumber);

        return builder.toHashCode();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ILocation))
            return false;

        ILocation l = (ILocation) other;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(_lineNumber, l.getLineNumber());
        builder.append(_columnNumber, l.getColumnNumber());
        builder.append(_resourceLocation, l.getResourceLocation());

        return builder.isEquals();
    }

    public String toString()
    {
        if (_lineNumber <= 0 && _columnNumber <= 0)
            return _resourceLocation.toString();
        StringBuffer buffer = new StringBuffer(_resourceLocation.toString());
        if (_lineNumber > 0)
        {
            buffer.append(", line ");
            buffer.append(_lineNumber);
        }

        if (_columnNumber > 0)
        {
            buffer.append(", column ");
            buffer.append(_columnNumber);
        }

        return buffer.toString();
    }
}
