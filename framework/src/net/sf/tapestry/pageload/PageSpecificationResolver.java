package net.sf.tapestry.pageload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.html.BasePage;
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
 *  <li><i>simple-name</i> page in the WEB-INF/pages/<i>servlet-name</i> directory of the context root
 *  <li><i>simple-name</i>.page in WEB-INF
 *  <li><i>simple-name</i>.page in the application root (within the context root)
 *  <li><i>simple-name</i>.html as a template, for which an implicit specification is generated
 *  </ul>
 * 
 *  <p>If none of these work out, then the page is searched for in the framework namespace.
 *  This is used to find default implementations of the
 *  pages such as Exception and StaleLink.
 *
 *  @see net.sf.tapestry.IPageSource
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class PageSpecificationResolver
{
    private static final Log LOG = LogFactory.getLog(PageSpecificationResolver.class);

    private ISpecificationSource _source;
    private String _simplePageName;
    private INamespace _namespace;
    private ComponentSpecification _specification;
    private IResourceLocation _applicationRootLocation;
    private IResourceLocation _webInfLocation;
    private IResourceLocation _webInfPagesLocation;

    public PageSpecificationResolver(IRequestCycle cycle)
    {
        IEngine engine = cycle.getEngine();

        _source = engine.getSpecificationSource();

        _applicationRootLocation = Tapestry.getApplicationRootLocation(cycle);

        String servletName = cycle.getRequestContext().getServlet().getServletConfig().getServletName();

        _webInfLocation = _applicationRootLocation.getRelativeLocation("/WEB-INF/");
        _webInfPagesLocation = _webInfLocation.getRelativeLocation("pages/" + servletName + "/");
    }

    /**
     *  Resolve the name (which may have a library id prefix) to a namespace
     *  (see {@link #getNamespace()}) and a specification (see {@link #getSpecification()}).
     * 
     *  @throws ApplicationRuntimeException if the name cannot be resolved
     * 
     **/
    
    public void resolve(String pageName)
    {
        _namespace = null;
        _specification = null;
        
        int colonx = pageName.indexOf(':');

        if (colonx > 0)
        {
            _simplePageName = pageName.substring(colonx + 1);
            String namespaceId = pageName.substring(0, colonx);

            if (namespaceId.equals(INamespace.FRAMEWORK_NAMESPACE))
                _namespace = _source.getFrameworkNamespace();
            else
                _namespace = _source.getApplicationNamespace().getChildNamespace(namespaceId);
        }
        else
        {
            _simplePageName = pageName;

            _namespace = _source.getApplicationNamespace();
        }

        if (_namespace.containsPage(_simplePageName))
            _specification = _namespace.getPageSpecification(_simplePageName);
        {

            searchForPage();

            if (_specification == null)
                throw new ApplicationRuntimeException(
                    Tapestry.getString("Namespace.no-such-page", _simplePageName, _namespace.getNamespaceId()));
        }

    }

    public INamespace getNamespace()
    {
        return _namespace;
    }

    public ComponentSpecification getSpecification()
    {
        return _specification;
    }

    private void searchForPage()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Resolving unknown page '" + _simplePageName + "' in " + _namespace);

        String expectedName = _simplePageName + ".page";

        IResourceLocation namespaceLocation = _namespace.getSpecificationLocation();

        if (found(namespaceLocation.getRelativeLocation(expectedName)))
            return;

        // If not for the (unnamed) application specification, then return ... which will
        // cause an exception.

        if (!_namespace.isApplicationNamespace())
            return;

        // The application namespace gets some extra searching.

        if (found(_webInfPagesLocation.getRelativeLocation(expectedName)))
            return;

        if (found(_webInfLocation.getRelativeLocation(expectedName)))
            return;

        if (found(_applicationRootLocation.getRelativeLocation(expectedName)))
            return;

        // The wierd one ... where we see if there's an HTML file in the application root location.

        String templateName = _simplePageName + ".html";

        IResourceLocation templateLocation = _applicationRootLocation.getRelativeLocation(templateName);

        if (templateLocation.getResourceURL() != null)
        {
            setupImplicitPage(templateLocation);
            return;
        }

        // Not found in application namespace, so maybe its a framework page.

        INamespace framework = _source.getFrameworkNamespace();

        if (framework.containsPage(_simplePageName))
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Found " + _simplePageName + " in framework namespace.");

            _namespace = framework;
            _specification = framework.getPageSpecification(_simplePageName);
        }

    }

    private void setupImplicitPage(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Found HTML template at " + location);

        _specification = new ComponentSpecification();
        _specification.setComponentClassName(BasePage.class.getName());
        _specification.setPageSpecification(true);
        _specification.setSpecificationLocation(location);

        install();
    }

    private boolean found(IResourceLocation location)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Checking: " + location);

        if (location.getResourceURL() == null)
            return false;

        _specification = _source.getPageSpecification(location);

        install();

        return true;
    }

    private void install()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Installing " + _simplePageName + " into " + _namespace + " as " + _specification);

        _namespace.installPageSpecification(_simplePageName, _specification);
    }

}
