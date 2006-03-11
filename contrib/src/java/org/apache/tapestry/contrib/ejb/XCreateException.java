// Copyright 2004, 2005 The Apache Software Foundation
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

import javax.ejb.CreateException;


/**
 *  Extended version of {@link CreateException} that includes a root cause.
 *
 *  @author Howard Lewis Ship
 *
 **/

public class XCreateException extends CreateException
{
    private static final long serialVersionUID = 6807032467099102587L;
    
	private Throwable rootCause;

    public XCreateException(String message)
    {
        super(message);
    }

    public XCreateException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public XCreateException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}