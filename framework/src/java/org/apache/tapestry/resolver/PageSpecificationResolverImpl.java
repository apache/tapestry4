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
import org.apache.hivemind.Resource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageNotFoundException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Performs the tricky work of resolving a page name to a page specification. The search for pages
 * in the application namespace is the most complicated, since Tapestry searches for pages that
 * aren't explicitly defined in the application specification. The search, based on the
 * <i>simple-name </i> of the page, goes as follows:
 * <ul>
 * <li>As declared in the application specification
 * <li><i>simple-name </i>.page in the same folder as the application specification
 * <li><i>simple-name </i> page in the WEB-INF/ <i>servlet-name </i> directory of the context root
 * <li><i>simple-name </i>.page in WEB-INF
 * <li><i>simple-name </i>.page in the application root (within the context root)
 * <li><i>simple-name </i>.html as a template in the application root, for which an implicit
 * specification is generated
 * <li>By searching the framework namespace
 * <li>By invoking
 * {@link org.apache.tapestry.resolver.ISpecificationResolverDelegate#findPageSpecification(IRequestCycle, INamespace, String)}
 * </ul>
 * <p>
 * Pages in a component library are searched for in a more abbreviated fashion:
 * <ul>
 * <li>As declared in the library specification
 * <li><i>simple-name </i>.page in the same folder as the library specification
 * <li>By searching the framework namespace
 * <li>By invoking
 * {@link org.apache.tapestry.resolver.ISpecificationResolverDelegate#findPageSpecification(IRequestCycle, INamespace, String)}
 * </ul>
 * 
 * @see org.apache.tapestry.engine.IPageSource
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class PageSpecificationResolverImpl extends AbstractSpecificationResolver implements
        PageSpecificationResolver
{
    /** set by container */
    private Log _log;

    /** Set by resolve() */
    private String _simpleName;

    /** @since 4.0 * */
    private INamespace _applicationNamespace;

    /** @since 4.0 * */
    private INamespace _frameworkNamespace;

    /** @since 4.0 */

    private ComponentPropertySource _componentPropertySource;

    public void initializeService()
    {
        _applicationNamespace = getSpecificationSource().getApplicationNamespace();
        _frameworkNamespace = getSpecificationSource().getFrameworkNamespace();

        super.initializeService();
    }

    protected void reset()
    {
        _simpleName = null;

        super.reset();
    }

    /**
     * Resolve the name (which may have a library id prefix) to a namespace (see
     * {@link #getNamespace()}) and a specification (see {@link #getSpecification()}).
     * 
     * @throws ApplicationRuntimeException
     *             if the name cannot be resolved
     */

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
                namespace = _frameworkNamespace;
            else
                namespace = _applicationNamespace.getChildNamespace(namespaceId);
        }
        else
        {
            _simpleName = prefixedName;

            namespace = _applicationNamespace;
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
            throw new PageNotFoundException(ResolverMessages.noSuchPage(_simpleName, namespace));
    }

    public String getSimplePageName()
    {
        return _simpleName;
    }

    private void searchForPage(IRequestCycle cycle)
    {
        INamespace namespace = getNamespace();

        if (_log.isDebugEnabled())
            _log.debug(ResolverMessages.resolvingPage(_simpleName, namespace));

        String expectedName = _simpleName + ".page";

        Resource namespaceLocation = namespace.getSpecificationLocation();

        // See if there's a specification file in the same folder
        // as the library or application specification that's
        // supposed to contain the page.

        if (found(namespaceLocation.getRelativeResource(expectedName)))
            return;

        if (namespace.isApplicationNamespace())
        {

            // The application namespace gets some extra searching.

            if (found(getWebInfAppLocation().getRelativeResource(expectedName)))
                return;

            if (found(getWebInfLocation().getRelativeResource(expectedName)))
                return;

            if (found(getContextRoot().getRelativeResource(expectedName)))
                return;

            // The wierd one ... where we see if there's a template in the application root
            // location.

            String templateName = _simpleName + "." + getTemplateExtension();

            Resource templateResource = getContextRoot().getRelativeResource(templateName);

            if (_log.isDebugEnabled())
                _log.debug(ResolverMessages.checkingResource(templateResource));

            if (templateResource.getResourceURL() != null)
            {
                setupImplicitPage(templateResource);
                return;
            }

            // Not found in application namespace, so maybe its a framework page.

            if (_frameworkNamespace.containsPage(_simpleName))
            {
                if (_log.isDebugEnabled())
                    _log.debug(ResolverMessages.foundFrameworkPage(_simpleName));

                setNamespace(_frameworkNamespace);

                // Note: This implies that normal lookup rules don't work
                // for the framework! Framework pages must be
                // defined in the framework library specification.

                setSpecification(_frameworkNamespace.getPageSpecification(_simpleName));
                return;
            }
        }

        // Not found by any normal rule, so its time to
        // consult the delegate.

        IComponentSpecification specification = getDelegate().findPageSpecification(
                cycle,
                namespace,
                _simpleName);

        setSpecification(specification);
    }

    private void setupImplicitPage(Resource resource)
    {
        if (_log.isDebugEnabled())
            _log.debug(ResolverMessages.foundHTMLTemplate(resource));

        // TODO The SpecFactory in Specification parser should be used in some way to
        // create an IComponentSpecification!

        IComponentSpecification specification = new ComponentSpecification();
        specification.setPageSpecification(true);
        specification.setSpecificationLocation(resource);

        setSpecification(specification);

        install();
    }

    private boolean found(Resource resource)
    {
        if (_log.isDebugEnabled())
            _log.debug(ResolverMessages.checkingResource(resource));

        if (resource.getResourceURL() == null)
            return false;

        setSpecification(getSpecificationSource().getPageSpecification(resource));

        install();

        return true;
    }

    private void install()
    {
        INamespace namespace = getNamespace();
        IComponentSpecification specification = getSpecification();

        if (_log.isDebugEnabled())
            _log.debug(ResolverMessages.installingPage(_simpleName, namespace, specification));

        namespace.installPageSpecification(_simpleName, specification);
    }

    /**
     * If the namespace defines the template extension (as property
     * {@link Tapestry#TEMPLATE_EXTENSION_PROPERTY}, then that is used, otherwise the default is
     * used.
     */

    private String getTemplateExtension()
    {
        return _componentPropertySource.getNamespaceProperty(
                getNamespace(),
                Tapestry.TEMPLATE_EXTENSION_PROPERTY);
    }

    /** @since 4.0 */

    public void setLog(Log log)
    {
        _log = log;
    }

    /** @since 4.0 */
    public void setComponentPropertySource(ComponentPropertySource componentPropertySource)
    {
        _componentPropertySource = componentPropertySource;
    }
}