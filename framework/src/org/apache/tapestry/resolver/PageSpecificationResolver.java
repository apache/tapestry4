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
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Performs the tricky work of resolving a page name to a page specification.
 *  The search for pages in the application namespace is the most complicated,
 *  since Tapestry searches for pages that aren't explicitly defined in the
 *  application specification.  The search, based on the <i>simple-name</i>
 *  of the page, goes as follows:
 * 
 *  <ul>
 *  <li>As declared in the application specification
 *  <li><i>simple-name</i>.page in the same folder as the application specification
 *  <li><i>simple-name</i> page in the WEB-INF/<i>servlet-name</i> directory of the context root
 *  <li><i>simple-name</i>.page in WEB-INF
 *  <li><i>simple-name</i>.page in the application root (within the context root)
 *  <li><i>simple-name</i>.html as a template in the application root, 
 *      for which an implicit specification is generated
 *  <li>By searching the framework namespace
 *  <li>By invoking {@link org.apache.tapestry.resolver.ISpecificationResolverDelegate#findPageSpecification(IRequestCycle, INamespace, String)}
 *  </ul>
 * 
 *  <p>Pages in a component library are searched for in a more abbreviated fashion:
 *  <ul>
 *  <li>As declared in the library specification
 *  <li><i>simple-name</i>.page in the same folder as the library specification
 *  <li>By searching the framework namespace
 *  <li>By invoking {@link org.apache.tapestry.resolver.ISpecificationResolverDelegate#findPageSpecification(IRequestCycle, INamespace, String)}
 *  </ul>
 *
 *  @see org.apache.tapestry.engine.IPageSource
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
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

    public void resolve(IRequestCycle cycle, String prefixedName)
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
                namespace =
                    getSpecificationSource().getApplicationNamespace().getChildNamespace(
                        namespaceId);
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

        searchForPage(cycle);

        if (getSpecification() == null)
            throw new ApplicationRuntimeException(
                Tapestry.format(
                    "Namespace.no-such-page",
                    _simpleName,
                    namespace.getNamespaceId()));

    }

    public String getSimplePageName()
    {
        return _simpleName;
    }

    private void searchForPage(IRequestCycle cycle)
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

        if (namespace.isApplicationNamespace())
        {

            // The application namespace gets some extra searching.

            if (found(getWebInfAppLocation().getRelativeLocation(expectedName)))
                return;

            if (found(getWebInfLocation().getRelativeLocation(expectedName)))
                return;

            if (found(getApplicationRootLocation().getRelativeLocation(expectedName)))
                return;

            // The wierd one ... where we see if there's a template in the application root location.

            String templateName = _simpleName + "." + getTemplateExtension();

            IResourceLocation templateLocation =
                getApplicationRootLocation().getRelativeLocation(templateName);

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
                return;
            }
        }

        // Not found by any normal rule, so its time to
        // consult the delegate.

        IComponentSpecification specification =
            getDelegate().findPageSpecification(cycle, namespace, _simpleName);
            
        setSpecification(specification);
    }

    private void setupImplicitPage(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Found HTML template at " + location);
		// TODO The SpecFactory in Specification parser should be used in some way to create an IComponentSpecifciation!
        IComponentSpecification specification = new ComponentSpecification();
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
        IComponentSpecification specification = getSpecification();

        if (LOG.isDebugEnabled())
            LOG.debug(
                "Installing page " + _simpleName + " into " + namespace + " as " + specification);

        namespace.installPageSpecification(_simpleName, specification);
    }

    /**
     *  If the namespace defines the template extension (as property
     *  {@link Tapestry#TEMPLATE_EXTENSION_PROPERTY}, then that is used, otherwise
     *  the default is used.
     * 
     **/

    private String getTemplateExtension()
    {
        String extension =
            getNamespace().getSpecification().getProperty(Tapestry.TEMPLATE_EXTENSION_PROPERTY);

        if (extension == null)
            extension = Tapestry.DEFAULT_TEMPLATE_EXTENSION;

        return extension;
    }
    
    protected void reset()
    {
    	_simpleName = null;
    	
        super.reset();
    }

}
