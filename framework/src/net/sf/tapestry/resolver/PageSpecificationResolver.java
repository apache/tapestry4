/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  Performs the tricky work of resolving a page name to a page specification.
 *  The search for pages in the application namespace is the most complicated,
 *  since Tapestry searches for pages that aren't explicitly defined in the
 *  application specification.  The search, based on the <i>simple-name</i>
 *  of the page, goes as follows:
 * 
 *  <ul>
 *  <li><i>simple-name</i>.page in the same folder as the application specification
 *  <li><i>simple-name</i> page in the WEB-INF/<i>servlet-name</i> directory of the context root
 *  <li><i>simple-name</i>.page in WEB-INF
 *  <li><i>simple-name</i>.page in the application root (within the context root)
 *  <li><i>simple-name</i>.html as a template in the application root, 
 *      for which an implicit specification is generated
 *  </ul>
 * 
 *  <p>If none of these work out, then the page is searched for in the framework namespace.
 *  This is used to find default implementations of pages such as Exception and StaleLink.
 *
 *  @see net.sf.tapestry.IPageSource
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class PageSpecificationResolver extends AbstractSpecificationResolver
{
    private static final Log LOG = LogFactory.getLog(PageSpecificationResolver.class);

    private String _simpleName;

    public PageSpecificationResolver(IRequestCycle cycle)
    {
        super(cycle);
    }

    /**
     *  Resolve the name (which may have a library id prefix) to a namespace
     *  (see {@link #getNamespace()}) and a specification (see {@link #getSpecification()}).
     * 
     *  @throws ApplicationRuntimeException if the name cannot be resolved
     * 
     **/

    public void resolve(String prefixedName)
    {
        reset();

        INamespace namespace = null;

        int colonx = prefixedName.indexOf(':');

        if (colonx > 0)
        {
            _simpleName = prefixedName.substring(colonx + 1);
            String namespaceId = prefixedName.substring(0, colonx);

            if (namespaceId.equals(INamespace.FRAMEWORK_NAMESPACE))
                namespace = getSpecificationSource().getFrameworkNamespace();
            else
                namespace = getSpecificationSource().getApplicationNamespace().getChildNamespace(namespaceId);
        }
        else
        {
            _simpleName = prefixedName;

            namespace = getSpecificationSource().getApplicationNamespace();
        }

        setNamespace(namespace);

        if (namespace.containsPage(_simpleName))
        {
            setSpecification(namespace.getPageSpecification(_simpleName));
            return;
        }

        // Not defined in the specification, so it's time to hunt it down.

        searchForPage();

        if (getSpecification() == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("Namespace.no-such-page", _simpleName, namespace.getNamespaceId()));

    }

    public  String getSimplePageName()
    {
        return _simpleName;
    }

    private void searchForPage()
    {
        INamespace namespace = getNamespace();

        if (LOG.isDebugEnabled())
            LOG.debug("Resolving unknown page '" + _simpleName + "' in " + namespace);

        String expectedName = _simpleName + ".page";

        IResourceLocation namespaceLocation = namespace.getSpecificationLocation();

        // See if there's a specification file in the same folder
        // as the library or application specification that's
        // supposed to contain the page.

        if (found(namespaceLocation.getRelativeLocation(expectedName)))
            return;

        // If not for the (unnamed) application specification, then return ... which will
        // cause an exception.

        if (!namespace.isApplicationNamespace())
            return;

        // The application namespace gets some extra searching.

        if (found(getWebInfAppLocation().getRelativeLocation(expectedName)))
            return;

        if (found(getWebInfLocation().getRelativeLocation(expectedName)))
            return;

        if (found(getApplicationRootLocation().getRelativeLocation(expectedName)))
            return;

        // The wierd one ... where we see if there's an HTML file in the application root location.

        String templateName = _simpleName + ".html";

        IResourceLocation templateLocation = getApplicationRootLocation().getRelativeLocation(templateName);

        if (templateLocation.getResourceURL() != null)
        {
            setupImplicitPage(templateLocation);
            return;
        }

        // Not found in application namespace, so maybe its a framework page.

        INamespace framework = getSpecificationSource().getFrameworkNamespace();

        if (framework.containsPage(_simpleName))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Found " + _simpleName + " in framework namespace.");

            setNamespace(framework);

            // Note:  This implies that normal lookup rules don't work
            // for the framework!  Framework pages must be
            // defined in the framework library specification.

            setSpecification(framework.getPageSpecification(_simpleName));
        }

    }

    private void setupImplicitPage(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Found HTML template at " + location);

        ComponentSpecification specification = new ComponentSpecification();
        specification.setPageSpecification(true);
        specification.setSpecificationLocation(location);

        setSpecification(specification);

        install();
    }

    private boolean found(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Checking: " + location);

        if (location.getResourceURL() == null)
            return false;

        setSpecification(getSpecificationSource().getPageSpecification(location));

        install();

        return true;
    }

    private void install()
    {
        INamespace namespace = getNamespace();
        ComponentSpecification specification = getSpecification();

        if (LOG.isDebugEnabled())
            LOG.debug("Installing page " + _simpleName + " into " + namespace + " as " + specification);

        namespace.installPageSpecification(_simpleName, specification);
    }

}
