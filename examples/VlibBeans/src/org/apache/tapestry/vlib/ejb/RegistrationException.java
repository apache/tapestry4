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

package org.apache.tapestry.vlib.ejb;

/**
 *  Exception thrown by {@link IOperations#registerNewUser(String, String, String, String)}
 *  if the registration is not allowed (usually, because of a duplicate email or
 *  name).
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class RegistrationException extends Exception
{
    private Throwable rootCause;

    public RegistrationException(Throwable rootCause)
    {
        super(rootCause.getMessage());

        this.rootCause = rootCause;
    }

    public RegistrationException(String message, Throwable rootCause)
    {
        super(message);

        this.rootCause = rootCause;
    }

    public RegistrationException(String message)
    {
        super(message);
    }

    public Throwable getRootCause()
    {
        return rootCause;
    }
}