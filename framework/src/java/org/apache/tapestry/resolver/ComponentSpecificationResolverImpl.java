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

package org.apache.tapestry.resolver;

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Utility class that understands the rules of component types (which
 *  may optionally have a library prefix) and can resolve 
 *  the type to a {@link org.apache.tapestry.INamespace} and a 
 *  {@link org.apache.tapestry.spec.IComponentSpecification}.
 * 
 *  <p>Like {@link org.apache.tapestry.resolver.PageSpecificationResolver},
 *  if the component is not defined explicitly in the namespace, a search
 *  may occur:
 * 
 *  Performs the tricky work of resolving a page name to a page specification.
 *  The search for pages in the application namespace is the most complicated,
 *  since Tapestry searches for pages that aren't explicitly defined in the
 *  application specification.  The search, based on the <i>simple-name</i>
 *  of the page, goes as follows:
 * 
 *  <ul>
 *  <li>As declared in the application specification
 *  <li><i>type</i>.jwc in the same folder as the application specification
 *  <li><i>type</i> jwc in the WEB-INF/<i>servlet-name</i> directory of the context root
 *  <li><i>type</i>.jwc in WEB-INF
 *  <li><i>type</i>.jwc in the application root (within the context root)
 *  <li>By searching the framework namespace
 *  </ul> 
 * 
 *  The search for components in library namespaces is more abbreviated:
 *  <li>As declared in the library specification
 *  <li><i>type</i>.jwc in the same folder as the library specification
 *  <li>By searching the framework namespace
 *  </ul>
 *
 * 
 *  @author Howard Lewis Ship
 *  @since 3.0
 *
 */

public class ComponentSpecificationResolverImpl
    extends AbstractSpecificationResolver
    implements ComponentSpecificationResolver
{
    /** Set by container */
    private Log _log;

    /** Set by resolve() */
    private String _type;

    protected void reset()
    {
        _type = null;

        super.reset();
    }

    /**
     *  Passed the namespace of a container (to resolve the type in)
     *  and the type to resolve, performs the processing.  A "bare type"
     *  (without a library prefix) may be in the containerNamespace,
     *  or the framework namespace
     *  (a search occurs in that order).
     * 
     *  @param cycle current request cycle
     *  @param containerNamespace namespace that may contain
     *  a library referenced in the type
     *  @param type the component specification
     *  to  find, either a simple name, or prefixed with a library id
     *  (defined for the container namespace)
     * 
     *  @see #getNamespace()
     *  @see #getSpecification()
     * 
     */

    public void resolve(
        IRequestCycle cycle,
        INamespace containerNamespace,
        String type,
        Location location)
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
    }

    /**
     *  Like {@link #resolve(org.apache.tapestry.IRequestCycle, org.apache.tapestry.INamespace, java.lang.String, org.apache.tapestry.ILocation)},
     *  but used when the type has already been parsed into a library id and a simple type.
     * 
     *  @param cycle current request cycle
     *  @param containerNamespace namespace that may contain
     *  a library referenced in the type
     *  @param libraryId the library id within the container namespace, or null
     *  @param type the component specification
     *  to  find as a simple name (without a library prefix)
     *  @param location of reference to be resolved
     *  @throws ApplicationRuntimeException if the type cannot be resolved
     * 
     */

    public void resolve(
        IRequestCycle cycle,
        INamespace containerNamespace,
        String libraryId,
        String type,
        Location location)
    {
        reset();
        _type = type;

        INamespace namespace = null;

        if (libraryId != null)
            namespace = containerNamespace.getChildNamespace(libraryId);
        else
            namespace = containerNamespace;

        setNamespace(namespace);

        if (namespace.containsComponentType(type))
            setSpecification(namespace.getComponentSpecification(type));
        else
            searchForComponent(cycle);

        // If not found after search, check to see if it's in
        // the framework instead.

        if (getSpecification() == null)
        {

            throw new ApplicationRuntimeException(
                ResolverMessages.noSuchComponentType(type, namespace),
                location,
                null);

        }
    }

    private void searchForComponent(IRequestCycle cycle)
    {
        INamespace namespace = getNamespace();

        if (_log.isDebugEnabled())
            _log.debug("Resolving unknown component '" + _type + "' in " + namespace);

        String expectedName = _type + ".jwc";
        Resource namespaceLocation = namespace.getSpecificationLocation();

        // Look for appropriate file in same folder as the library (or application)
        // specificaiton.

        if (found(namespaceLocation.getRelativeResource(expectedName)))
            return;

        if (namespace.isApplicationNamespace())
        {

            // The application namespace gets some extra searching.

            if (found(getWebInfAppLocation().getRelativeResource(expectedName)))
                return;

            if (found(getWebInfLocation().getRelativeResource(expectedName)))
                return;

            if (found(getApplicationRootLocation().getRelativeResource(expectedName)))
                return;
        }

        // Not in the library or app spec; does it match a component
        // provided by the Framework?

        INamespace framework = getSpecificationSource().getFrameworkNamespace();

        if (framework.containsComponentType(_type))
        {
            setSpecification(framework.getComponentSpecification(_type));
            return;
        }

        IComponentSpecification specification =
            getDelegate().findComponentSpecification(cycle, namespace, _type);

        setSpecification(specification);

        // If not found by here, an exception will be thrown.
    }

    private boolean found(Resource resource)
    {
        if (_log.isDebugEnabled())
            _log.debug("Checking: " + resource);

        if (resource.getResourceURL() == null)
            return false;

        setSpecification(getSpecificationSource().getComponentSpecification(resource));

        install();

        return true;
    }

    private void install()
    {
        INamespace namespace = getNamespace();
        IComponentSpecification specification = getSpecification();

        if (_log.isDebugEnabled())
            _log.debug(
                "Installing component type "
                    + _type
                    + " into "
                    + namespace
                    + " as "
                    + specification);

        namespace.installComponentSpecification(_type, specification);
    }

    public void setLog(Log log)
    {
        _log = log;
    }

}
