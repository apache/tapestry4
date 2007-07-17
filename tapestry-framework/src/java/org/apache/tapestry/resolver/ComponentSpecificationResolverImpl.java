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

package org.apache.tapestry.resolver;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.LocationImpl;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.services.ClassFinder;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Utility class that understands the rules of component types (which may optionally have a library
 * prefix) and can resolve the type to a {@link org.apache.tapestry.INamespace}and a
 * {@link org.apache.tapestry.spec.IComponentSpecification}.
 * <p>
 * Like {@link org.apache.tapestry.resolver.PageSpecificationResolver}, if the component is not
 * defined explicitly in the namespace, a search may occur: Performs the tricky work of resolving a
 * page name to a page specification. The search for pages in the application namespace is the most
 * complicated, since Tapestry searches for pages that aren't explicitly defined in the application
 * specification. The search, based on the <i>simple-name </i> of the page, goes as follows:
 * <ul>
 * <li>As declared in the application specification
 * <li><i>type</i>.jwc in the same folder as the application specification
 * <li><i>type</i> jwc in the WEB-INF/ <i>servlet-name </i> directory of the context root
 * <li><i>type</i>.jwc in WEB-INF
 * <li><i>type</i>.jwc in the application root (within the context root)
 * <li>By searching the framework namespace
 * <li>By searching for a named class file within the org.apache.tapestry.component-class-packages
 * property (defined within the namespace)
 * </ul>
 *
 * The search for components in library namespaces is more abbreviated:
 * <ul>
 * <li>As declared in the library specification
 * <li><i>type </i>.jwc in the same folder as the library specification
 * <li>By searching the framework namespace
 * </ul>
 *
 * @since 3.0
 */

public class ComponentSpecificationResolverImpl extends AbstractSpecificationResolver implements ComponentSpecificationResolver
{
    /** Set by container. */
    private Log _log;

    /** Set by resolve(). */
    private String _type;

    private ClassFinder _classFinder;

    private ClassResolver _classResolver;

    protected void reset()
    {
        _type = null;

        super.reset();
    }

    /**
     * Passed the namespace of a container (to resolve the type in) and the type to resolve,
     * performs the processing. A "bare type" (without a library prefix) may be in the
     * containerNamespace, or the framework namespace (a search occurs in that order).
     *
     * @param cycle
     *            current request cycle
     * @param containerNamespace
     *            namespace that may contain a library referenced in the type
     * @param type
     *            the component specification to find, either a simple name, or prefixed with a
     *            library id (defined for the container namespace)
     * @see #getNamespace()
     * @see #getSpecification()
     */

    public void resolve(IRequestCycle cycle, INamespace containerNamespace, String type, Location location)
    {
        int colonx = type.indexOf(':');

        if (colonx > 0)
        {
            String libraryId = type.substring(0, colonx);
            String simpleType = type.substring(colonx + 1);

            resolve(cycle, containerNamespace, libraryId, simpleType, location);
        }
        else
            resolve(cycle, containerNamespace, null, type, location);

        IComponentSpecification spec = getSpecification();

        if (spec.isDeprecated())
            _log.warn(ResolverMessages.componentIsDeprecated(type, location));
    }

    /**
     * Like
     * {@link #resolve(org.apache.tapestry.IRequestCycle, org.apache.tapestry.INamespace, java.lang.String, Location)},
     * but used when the type has already been parsed into a library id and a simple type.
     *
     * @param cycle
     *            current request cycle
     * @param containerNamespace
     *            namespace that may contain a library referenced in the type
     * @param libraryId
     *            the library id within the container namespace, or null
     * @param type
     *            the component specification to find as a simple name (without a library prefix)
     * @param location
     *            of reference to be resolved
     * @throws ApplicationRuntimeException
     *             if the type cannot be resolved
     */

    public void resolve(IRequestCycle cycle, INamespace containerNamespace, String libraryId,
                        String type, Location location)
    {
        reset();
        _type = type;

        INamespace namespace;
        try
        {
            namespace = findNamespaceForId(containerNamespace, libraryId);
        }
        catch (ApplicationRuntimeException e)
        {
            throw new ApplicationRuntimeException(e.getMessage(), location, e);
        }

        setNamespace(namespace);

        if (namespace.containsComponentType(type))
        {
            setSpecification(namespace.getComponentSpecification(type));
            return;
        }

        IComponentSpecification spec = searchForComponent(cycle);

        // If not found after search, check to see if it's in
        // the framework instead.

        if (spec == null)
        {
            throw new ApplicationRuntimeException(ResolverMessages.noSuchComponentType(
              type,
              namespace), location, null);

        }

        setSpecification(spec);

        // Install it into the namespace, to short-circuit any future search.

        install();
    }

    // Hm. This could maybe go elsewhere, say onto ISpecificationSource

    private IComponentSpecification searchForComponent(IRequestCycle cycle)
    {
        IComponentSpecification result = null;
        INamespace namespace = getNamespace();

        if (_log.isDebugEnabled())
            _log.debug(ResolverMessages.resolvingComponent(_type, namespace));

        String expectedName = _type + ".jwc";
        Resource namespaceLocation = namespace.getSpecificationLocation();

        // Look for appropriate file in same folder as the library (or application)
        // specificaiton.

        result = check(namespaceLocation.getRelativeResource(expectedName));

        if (result != null)
            return result;

        if (namespace.isApplicationNamespace()) {

            // The application namespace gets some extra searching.

            result = check(getWebInfAppLocation().getRelativeResource(expectedName));

            if (result == null)
                result = check(getWebInfLocation().getRelativeResource(expectedName));

            if (result == null)
                result = check((getContextRoot().getRelativeResource(expectedName)));

            if (result != null)
                return result;
        }

        result = getDelegate().findComponentSpecification(cycle, namespace, _type);
        if (result != null)
            return result;

        result = searchForComponentClass(namespace, _type);

        if (result != null)
            return result;

        // Not in the library or app spec; does it match a component
        // provided by the Framework?

        INamespace framework = getSpecificationSource().getFrameworkNamespace();

        if (framework.containsComponentType(_type))
            return framework.getComponentSpecification(_type);

        return null;
    }

    IComponentSpecification searchForComponentClass(INamespace namespace, String type)
    {
        String packages = namespace.getPropertyValue("org.apache.tapestry.component-class-packages");

        String className = type.replace('/', '.');

        Class componentClass = _classFinder.findClass(packages, className);
        if (componentClass == null)
            return null;

        IComponentSpecification spec = new ComponentSpecification();

        Resource namespaceResource = namespace.getSpecificationLocation();
        Resource componentResource = namespaceResource.getRelativeResource(type + ".jwc");

        // try classpath relative if namespace relative doesn't resolve

        if (componentResource.getResourceURL() == null) {

            componentResource = new ClasspathResource(_classResolver, componentClass.getName().replace('.', '/'));
        }

        Location location = new LocationImpl(componentResource);

        spec.setLocation(location);
        spec.setSpecificationLocation(componentResource);
        spec.setComponentClassName(componentClass.getName());

        return spec;
    }

    private IComponentSpecification check(Resource resource)
    {
        if (_log.isDebugEnabled())
            _log.debug("Checking: " + resource);

        if (resource.getResourceURL() == null)
            return null;

        return getSpecificationSource().getComponentSpecification(resource);
    }

    private void install()
    {
        INamespace namespace = getNamespace();
        IComponentSpecification specification = getSpecification();

        if (_log.isDebugEnabled())
            _log.debug(ResolverMessages.installingComponent(_type, namespace, specification));

        namespace.installComponentSpecification(_type, specification);
    }

    public String getType()
    {
        return _type;
    }

    public void setLog(Log log)
    {
        _log = log;
    }

    public void setClassFinder(ClassFinder classFinder)
    {
        _classFinder = classFinder;
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }
}
