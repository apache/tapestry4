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

package org.apache.tapestry.param;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;

/**
 *  Identifies exceptions in connected parameters (parameters that
 *  are automatically assigned to component properties by the framework).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.3
 *
 **/

public class ConnectedParameterException extends ApplicationRuntimeException
{
    private String _parameterName;
    private String _propertyName;

    public ConnectedParameterException(
        String message,
        Object component,
        String parameterName,
        String propertyName,
        Throwable rootCause)
    {
        this(message, component, parameterName, propertyName, null, rootCause);
    }

    /** @since 3.0 **/

    public ConnectedParameterException(
        String message,
        Object component,
        String parameterName,
        String propertyName,
        ILocation location,
        Throwable rootCause)
    {
        super(message, location, rootCause);

        _parameterName = parameterName;
        _propertyName = propertyName;
    }

    public String getParameterName()
    {
        return _parameterName;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }
}