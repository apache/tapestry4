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

package org.apache.tapestry.services.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.lib.DefaultImplementationBuilder;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * An implementation of {@link org.apache.hivemind.ServiceImplementationFactory}
 * that looks for a service implementation provided as an
 * {@link org.apache.tapestry.spec.ILibrarySpecification#getExtension(String) application
 * extension}. If no such extension exists, then a
 * {@link org.apache.hivemind.lib.DefaultImplementationBuilder default implementation}
 * is constructed and returned instead. This allows compatibility with Tapestry 3.0 and
 * earlier application extensions (though those will be phased out in the future).
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class ExtensionLookupFactory implements ServiceImplementationFactory
{
    private IApplicationSpecification _specification;
    private DefaultImplementationBuilder _defaultBuilder;

    public Object createCoreServiceImplementation(
        String serviceId,
        Class serviceInterface,
        Log serviceLog,
        Module invokingModule,
        List parameters)
    {
        ExtensionLookupParameter p = (ExtensionLookupParameter) parameters.get(0);

        String extensionName = p.getExtensionName();

        try
        {
            if (_specification.checkExtension(extensionName))
                return _specification.getExtension(extensionName, serviceInterface);

            return _defaultBuilder.buildDefaultImplementation(serviceInterface, invokingModule);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), p.getLocation(), ex);
        }
    }

    public void setDefaultBuilder(DefaultImplementationBuilder builder)
    {
        _defaultBuilder = builder;
    }

    public void setSpecification(IApplicationSpecification specification)
    {
        _specification = specification;
    }

}
