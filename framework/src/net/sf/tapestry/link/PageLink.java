package net.sf.tapestry.link;

import net.sf.tapestry.IEngineService;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  A component for creating a navigation link to another page, 
 *  using the page service.
 *
 *  [<a href="../../../../../ComponentReference/PageLink.html">Component Reference</a>]
 *
 * @author Howard Ship
 * @version $Id$
 *
 **/

public class PageLink extends GestureLink
{
    private String _targetPage;

    /** @since 2.2 **/

    private INamespace _targetNamespace;

    /**
     *  Returns {@link IEngineService#PAGE_SERVICE}.
     *
     **/

    protected String getServiceName()
    {
        return IEngineService.PAGE_SERVICE;
    }

    /**
     *  Returns a single-element String array; the lone element is the
     *  name of the page, retrieved from the 'page' parameter.
     *
     **/

    protected Object[] getServiceParameters(IRequestCycle cycle) throws RequestCycleException
    {
        String parameter = null;

        if (_targetNamespace == null)
            parameter = _targetPage;
        else
            parameter = _targetNamespace.constructQualifiedName(_targetPage);

        return new String[] { parameter };
    }

    public String getTargetPage()
    {
        return _targetPage;
    }

    public void setTargetPage(String targetPage)
    {
        _targetPage = targetPage;
    }

    /** @since 2.2 **/

    public INamespace getTargetNamespace()
    {
        return _targetNamespace;
    }

    /** @since 2.2 **/

    public void setTargetNamespace(INamespace targetNamespace)
    {
        _targetNamespace = targetNamespace;
    }

}