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
 *  Exception thrown on a login failure.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public class LoginException extends Exception
{
    private boolean _passwordError;

    public LoginException(String message, boolean passwordError)
    {
        super(message);

        _passwordError = passwordError;
    }

    /**
     *  Returns true if the error is related to the password.  Otherwise,
     *  the error is related to the email address (either not found,
     *  or the user has been invalidated or otherwise locked out).
     *
     **/

    public boolean isPasswordError()
    {
        return _passwordError;
    }

}