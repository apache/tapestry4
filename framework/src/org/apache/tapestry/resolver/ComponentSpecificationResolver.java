/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ITemplateSource;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.ParameterSpecification;

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
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ComponentSpecificationResolver extends AbstractSpecificationResolver
{
    private static final Log LOG = LogFactory.getLog(ComponentSpecificationResolver.class);

    private String _type;

    public ComponentSpecificationResolver(IRequestCycle cycle)
    {
        super(cycle);
    }

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
     **/

    public void resolve(
        IRequestCycle cycle,
        INamespace containerNamespace,
        String type,
        ILocation location)
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
     *  Like {@link #resolve(INamespace, String)}, but used when the type has already
     *  been parsed into a library id and a simple type.
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
     **/

    public void resolve(
        IRequestCycle cycle,
        INamespace containerNamespace,
        String libraryId,
        String type,
        ILocation location)
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
                Tapestry.format(
                    "Namespace.no-such-component-type",
                    type,
                    namespace.getNamespaceId()),
                location,
                null);

        }

        // This code is here because this is the centralized location that combines
        // execution flow for all kinds of templates (standard, by extension, etc)
        establishDefaultParameters();
    }

    private void searchForComponent(IRequestCycle cycle)
    {
        INamespace namespace = getNamespace();

        if (LOG.isDebugEnabled())
            LOG.debug("Resolving unknown component '" + _type + "' in " + namespace);

        String expectedName = _type + ".jwc";
        IResourceLocation namespaceLocation = namespace.getSpecificationLocation();

        // Look for appropriate file in same folder as the library (or application)
        // specificaiton.

        if (found(namespaceLocation.getRelativeLocation(expectedName)))
            return;

        if (namespace.isApplicationNamespace())
        {

            // The application namespace gets some extra searching.

            if (found(getWebInfAppLocation().getRelativeLocation(expectedName)))
                return;

            if (found(getWebInfLocation().getRelativeLocation(expectedName)))
                return;

            if (found(getApplicationRootLocation().getRelativeLocation(expectedName)))
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

    private boolean found(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Checking: " + location);

        if (location.getResourceURL() == null)
            return false;

        setSpecification(getSpecificationSource().getComponentSpecification(location));

        install();

        return true;
    }

    private void install()
    {
        INamespace namespace = getNamespace();
        IComponentSpecification specification = getSpecification();

        if (LOG.isDebugEnabled())
            LOG.debug(
                "Installing component type "
                    + _type
                    + " into "
                    + namespace
                    + " as "
                    + specification);

        namespace.installComponentSpecification(_type, specification);
    }
    
    
    private void establishDefaultParameters()
    {
        IComponentSpecification specification = getSpecification();

        if (specification.getParameter(ITemplateSource.TEMPLATE_TAG_PARAMETER_NAME) == null)
        {
            // Add a templateTag parameter
            IParameterSpecification parameter = new ParameterSpecification();
            parameter.setPropertyName(ITemplateSource.TEMPLATE_TAG_PARAMETER_NAME);
            parameter.setDirection(Direction.AUTO);
            parameter.setType("java.lang.String");
            parameter.setDefaultValue("null");
            parameter.setLocation(specification.getLocation());
            parameter.setRequired(false);
            
            specification.addParameter(ITemplateSource.TEMPLATE_TAG_PARAMETER_NAME, parameter);
        }
    }
}
