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

/**
 *  General wrapper for any exception (normal or runtime) that may occur during
 *  runtime processing for the application.  This exception is used
 *  when the intent is to communicate a low-level failure to the user or
 *  developer; it is not expected to be caught.  The {@link #getCause() rootCause}
 *  property is a <em>nested</em> exception (Tapestry supported this concept
 *  long before the JDK did).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class ApplicationRuntimeException extends RuntimeException implements ILocatable
{
    private Throwable _rootCause;
    private transient ILocation _location;
    private transient Object _component;

    public ApplicationRuntimeException(Throwable rootCause)
    {
        this(rootCause.getMessage(), rootCause);
    }

    public ApplicationRuntimeException(String message)
    {
        this(message, null, null, null);
    }

    public ApplicationRuntimeException(String message, Throwable rootCause)
    {
        this(message, null, null, rootCause);
    }

    public ApplicationRuntimeException(
        String message,
        Object component,
        ILocation location,
        Throwable rootCause)
    {
        super(message);

        _rootCause = rootCause;
        _component = component;

        _location = Tapestry.findLocation(new Object[] { location, rootCause, component });
    }

    public ApplicationRuntimeException(String message, ILocation location, Throwable rootCause)
    {
        this(message, null, location, rootCause);
    }

    public Throwable getCause()
    {
        return _rootCause;
    }

    public ILocation getLocation()
    {
        return _location;
    }

    public Object getComponent()
    {
        return _component;
    }
}