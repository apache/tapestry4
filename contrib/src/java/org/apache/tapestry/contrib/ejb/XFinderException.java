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

import javax.ejb.FinderException;

/**
 * Extended version of {@link FinderException} that includes a root cause.
 * 
 * @author Howard Lewis Ship
 */

public class XFinderException extends FinderException
{

    private static final long serialVersionUID = -7761100160348957271L;

    private final Throwable rootCause;

    public XFinderException(String message)
    {
        this(message, null);
    }

    public XFinderException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public XFinderException(Throwable rootCause)
    {
        this(rootCause.getMessage(), rootCause);
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}
