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

package org.apache.tapestry.vlib.pages;

import org.apache.tapestry.pages.Exception;
import org.apache.tapestry.vlib.IErrorProperty;

/**
 *  A page only displayed when the application is unavailable
 *  (typically because of repeated {@link java.rmi.RemoteException}s
 *  or {@link javax.naming.NamingException}s accessing EJBs.
 * 
 *  @see org.apache.tapestry.vlib.VirtualLibraryEngine#rmiFailure(String, java.rmi.RemoteException, int)
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 *
 **/

public abstract class ApplicationUnavailable extends Exception
implements IErrorProperty
{
	
    public void activate(String message, Throwable ex)
    {
        setError(message);
        
        setException(ex);
    }
}
