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

import org.apache.hivemind.Location;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class LiteralDeferredObject implements DeferredObject
{
    private String _value;

    private Location _location;

    public LiteralDeferredObject(String value, Location location)
    {
        _value = value;
        _location = location;
    }

    public Object getObject()
    {
        return _value;
    }

    public Location getLocation()
    {
        return _location;
    }

}
