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

package org.apache.tapestry.asset;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;

/**
 *  Base class for {@link org.apache.tapestry.IAsset} implementations.  Provides
 *  the location property.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class AbstractAsset implements IAsset
{
	private IResourceLocation _resourceLocation;
    private ILocation _location;

    protected AbstractAsset(IResourceLocation resourceLocation, ILocation location)
    {
    	_resourceLocation = resourceLocation;
        _location = location;
    }

    public ILocation getLocation()
    {
        return _location;
    }
    
    public IResourceLocation getResourceLocation()
    {
    	return _resourceLocation;
    }
    
    public String toString()
    {
    	ToStringBuilder builder = new ToStringBuilder(this);
    	
    	builder.append("resourceLocation", _resourceLocation);
    	builder.append("location", _location);
    	
    	return builder.toString();
    }
}
