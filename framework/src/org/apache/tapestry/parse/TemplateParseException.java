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

package org.apache.tapestry.parse;

import org.apache.hivemind.Locatable;
import org.apache.hivemind.Location;

/**
 *  Exception thrown indicating a problem parsing an HTML template.
 *
 *  @author Howard Ship
 *  @version $Id$
 * 
 **/

public class TemplateParseException extends Exception implements Locatable
{
    private Location _location;
    private Throwable _rootCause;

    public TemplateParseException(String message)
    {
        this(message, null, null);
    }

    public TemplateParseException(String message, Location location)
    {
        this(message, location, null);
    }

    public TemplateParseException(String message, Location location, Throwable rootCause)
    {
        super(message);

        _location = location;

        _rootCause = rootCause;

    }

    public Location getLocation()
    {
        return _location;
    }

    public Throwable getRootCause()
    {
        return _rootCause;
    }

}