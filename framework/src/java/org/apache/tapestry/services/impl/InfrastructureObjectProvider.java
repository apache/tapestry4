// Copyright 2004 The Apache Software Foundation
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
import org.apache.commons.logging.Log;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.service.ObjectProvider;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.services.Infrastructure;

/**
 * An ObjectProvider that streamlines access to the 
 * central {@link org.apache.tapestry.services.Infrastructure} object.  The locator
 * for this provider is the name of a property of the Infrastructure. 
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */

public class InfrastructureObjectProvider implements ObjectProvider
{
    private Log _log;
    private ErrorHandler _errorHandler;
    private Infrastructure _infrastructure;

    public Object provideObject(
        Module contributingModule,
        Class propertyType,
        String locator,
        Location location)
    {
        try
        {
            return PropertyUtils.read(_infrastructure, locator);
        }
        catch (Throwable ex)
        {
            _errorHandler.error(
                _log,
                ImplMessages.unableToReadInfrastructureProperty(locator, _infrastructure, ex),
                location,
                ex);

            return null;
        }
    }

    public void setErrorHandler(ErrorHandler handler)
    {
        _errorHandler = handler;
    }

    public void setInfrastructure(Infrastructure infrastructure)
    {
        _infrastructure = infrastructure;
    }

    public void setLog(Log log)
    {
        _log = log;
    }

}
