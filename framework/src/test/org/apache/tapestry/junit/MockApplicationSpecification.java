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

package org.apache.tapestry.junit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IExtensionSpecification;

/**
 * @author Erik Hatcher
 * @version $Id$
 */
public class MockApplicationSpecification implements IApplicationSpecification
{
    private Map extensions = new HashMap();

    public String getLibrarySpecificationPath(String id)
    {
        return null;
    }

    public void setLibrarySpecificationPath(String id, String path)
    {
    }

    public List getLibraryIds()
    {
        return null;
    }

    public String getPageSpecificationPath(String name)
    {
        return null;
    }

    public void setPageSpecificationPath(String name, String path)
    {
    }

    public List getPageNames()
    {
        return null;
    }

    public void setComponentSpecificationPath(String type, String path)
    {
    }

    public String getComponentSpecificationPath(String type)
    {
        return null;
    }

    public List getComponentTypes()
    {
        return null;
    }

    public String getServiceClassName(String name)
    {
        return null;
    }

    public List getServiceNames()
    {
        return null;
    }

    public void setServiceClassName(String name, String className)
    {
    }

    public String getDescription()
    {
        return null;
    }

    public void setDescription(String description)
    {
    }

    public Map getExtensionSpecifications()
    {
        return null;
    }

    public void addExtensionSpecification(String name, IExtensionSpecification extension)
    {
    }

    public List getExtensionNames()
    {
        return null;
    }

    public IExtensionSpecification getExtensionSpecification(String name)
    {
        return null;
    }

    public Object getExtension(String name)
    {
        return getExtension(name, null);
    }

    /**
     * Ignore type constraint for now, just returns item from map from {@link #setExtensions(Map)}
     * the specified name.
     */
    public Object getExtension(String name, Class typeConstraint)
    {
        return extensions.get(name);
    }

    public boolean checkExtension(String name)
    {
        return extensions.get(name) != null;
    }

    public void setExtensions(Map extensions)
    {
        this.extensions = extensions;
    }

    public void instantiateImmediateExtensions()
    {
    }

    public ClassResolver getResourceResolver()
    {
        return null;
    }

    public void setResourceResolver(ClassResolver resolver)
    {
    }

    public String getPublicId()
    {
        return null;
    }

    public void setPublicId(String value)
    {
    }

    public Resource getSpecificationLocation()
    {
        return null;
    }

    public void setSpecificationLocation(Resource specificationLocation)
    {
    }

    public List getPropertyNames()
    {
        return null;
    }

    public void setProperty(String name, String value)
    {
    }

    public void removeProperty(String name)
    {
    }

    public String getProperty(String name)
    {
        return null;
    }

    public void setLocation(Location location)
    {
    }

    public Location getLocation()
    {
        return null;
    }

    public String getName()
    {
        return null;
    }

    public void setEngineClassName(String value)
    {
    }

    public String getEngineClassName()
    {
        return null;
    }

    public void setName(String name)
    {
    }
}