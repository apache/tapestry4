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

package org.apache.tapestry.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.asset.AssetSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.Namespace;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.parse.ISpecificationParser;
import org.apache.tapestry.services.NamespaceResources;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.LibrarySpecification;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 * Default implementation of {@link ISpecificationSource} that expects to use the normal class
 * loader to locate component specifications from within the classpath.
 * <p>
 * Caches specifications in memory forever, or until {@link #resetDidOccur()} is invoked.
 * 
 * @author Howard Lewis Ship
 */
public class SpecificationSourceImpl implements ISpecificationSource, ResetEventListener,
        ReportStatusListener
{
    private ClassResolver _classResolver;

    private IApplicationSpecification _specification;

    private ISpecificationParser _parser;

    private NamespaceResources _namespaceResources;

    private INamespace _applicationNamespace;

    private INamespace _frameworkNamespace;

    private AssetSource _assetSource;

    private String _serviceId;

    /**
     * Contains previously parsed component specifications.
     */

    private Map _componentCache = new HashMap();

    /**
     * Contains previously parsed page specifications.
     * 
     * @since 2.2
     */

    private Map _pageCache = new HashMap();

    /**
     * Contains previously parsed library specifications, keyed on specification resource path.
     * 
     * @since 2.2
     */

    private Map _libraryCache = new HashMap();

    /**
     * Contains {@link INamespace} instances, keyed on id (which will be null for the application
     * specification).
     */

    private Map _namespaceCache = new HashMap();

    public void reportStatus(ReportStatusEvent event)
    {
        event.title(_serviceId);

        event.property("page specification count", _pageCache.size());
        event.collection("page specifications", _pageCache.keySet());
        event.property("component specification count", _componentCache.size());
        event.collection("component specifications", _componentCache.keySet());
    }

    public void initializeService()
    {
        _namespaceResources = new NamespaceResourcesImpl(this, _assetSource);
    }

    /**
     * Clears the specification cache. This is used during debugging.
     */

    public synchronized void resetEventDidOccur()
    {
        _componentCache.clear();
        _pageCache.clear();
        _libraryCache.clear();
        _namespaceCache.clear();

        _applicationNamespace = null;
        _frameworkNamespace = null;
    }

    protected IComponentSpecification parseSpecification(Resource resource, boolean asPage)
    {
        IComponentSpecification result = null;

        try
        {
            if (asPage)
                result = _parser.parsePageSpecification(resource);
            else
                result = _parser.parseComponentSpecification(resource);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                    ImplMessages.unableToParseSpecification(resource), ex);
        }

        return result;
    }

    protected ILibrarySpecification parseLibrarySpecification(Resource resource)
    {
        try
        {
            return _parser.parseLibrarySpecification(resource);
        }
        catch (DocumentParseException ex)
        {
            throw new ApplicationRuntimeException(
                    ImplMessages.unableToParseSpecification(resource), ex);
        }

    }

    /**
     * Gets a component specification.
     * 
     * @param resourcePath
     *            the complete resource path to the specification.
     * @throws ApplicationRuntimeException
     *             if the specification cannot be obtained.
     */

    public synchronized IComponentSpecification getComponentSpecification(Resource resourceLocation)
    {
        IComponentSpecification result = (IComponentSpecification) _componentCache
                .get(resourceLocation);

        if (result == null)
        {
            result = parseSpecification(resourceLocation, false);

            _componentCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized IComponentSpecification getPageSpecification(Resource resourceLocation)
    {
        IComponentSpecification result = (IComponentSpecification) _pageCache.get(resourceLocation);

        if (result == null)
        {
            result = parseSpecification(resourceLocation, true);

            _pageCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized ILibrarySpecification getLibrarySpecification(Resource resourceLocation)
    {
        ILibrarySpecification result = (LibrarySpecification) _libraryCache.get(resourceLocation);

        if (result == null)
        {
            result = parseLibrarySpecification(resourceLocation);
            _libraryCache.put(resourceLocation, result);
        }

        return result;
    }

    public synchronized INamespace getApplicationNamespace()
    {
        if (_applicationNamespace == null)
            _applicationNamespace = new Namespace(null, null, _specification, _namespaceResources);

        return _applicationNamespace;
    }

    public synchronized INamespace getFrameworkNamespace()
    {
        if (_frameworkNamespace == null)
        {
            Resource resource = new ClasspathResource(_classResolver,
                    "/org/apache/tapestry/Framework.library");

            ILibrarySpecification ls = getLibrarySpecification(resource);

            _frameworkNamespace = new Namespace(INamespace.FRAMEWORK_NAMESPACE, null, ls,
                    _namespaceResources);
        }

        return _frameworkNamespace;
    }

    public void setParser(ISpecificationParser parser)
    {
        _parser = parser;
    }

    public void setClassResolver(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

    public void setSpecification(IApplicationSpecification specification)
    {
        _specification = specification;
    }

    public void setAssetSource(AssetSource assetSource)
    {
        _assetSource = assetSource;
    }

    public void setServiceId(String serviceId)
    {
        _serviceId = serviceId;
    }

}
