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

package org.apache.tapestry.enhance;

import java.util.List;
import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Location;
import org.apache.hivemind.Messages;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.internal.ServiceModelFactory;
import org.apache.hivemind.internal.ServicePoint;
import org.apache.hivemind.schema.Translator;

/**
 * Fake implementation of {@link org.apache.hivemind.internal.Module} used to interface
 * with {@link org.apache.hivemind.service.ClassFactory#newClass(java.lang.String, java.lang.Class, org.apache.hivemind.internal.Module)}.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class FalseModule implements Module
{
    private ClassResolver _classResolver;

    public FalseModule(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

    public String getModuleId()
    {
        return "FALSE-MODULE";
    }

    public Object getService(String serviceId, Class serviceInterface)
    {
        return null;
    }

    public Object getService(Class serviceInterface)
    {
        return null;
    }

    public ServicePoint getServicePoint(String serviceId)
    {
        return null;
    }

    public List getConfiguration(String configurationId)
    {
        return null;
    }

    public ClassResolver getClassResolver()
    {
        return _classResolver;
    }

    public Messages getMessages()
    {
        return null;
    }

    public Translator getTranslator(String translator)
    {
        return null;
    }

    public ServiceModelFactory getServiceModelFactory(String name)
    {
        return null;
    }

    public Locale getLocale()
    {
        return null;
    }

    public String expandSymbols(String input, Location location)
    {
        return null;
    }

    public ErrorHandler getErrorHandler()
    {
        return null;
    }

    public Location getLocation()
    {
        return null;
    }

    public String valueForSymbol(String name)
    {
        return null;
    }

}
