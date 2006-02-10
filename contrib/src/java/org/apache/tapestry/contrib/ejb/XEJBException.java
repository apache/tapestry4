// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.contrib.ejb;

import javax.ejb.EJBException;

/**
 * Extended version of {@link EJBException} that includes a root cause.
 * {@link EJBException} doesn't have quite the right constructor for this ... it
 * has an equivalent to the rootCause property, (causedByException), but doesn't
 * have a constructor that allows us to set a custom message.
 * 
 * @author Howard Lewis Ship
 */

public class XEJBException extends EJBException
{

    private static final long serialVersionUID = 3712108893575174833L;

    private final Throwable rootCause;

    public XEJBException(String message)
    {
        this(message, null);
    }

    public XEJBException(String message, Throwable cause)
    {
        super(message);

        this.rootCause = cause;
    }

    public XEJBException(Throwable cause)
    {
        super(cause.getMessage());

        this.rootCause = cause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}
