package net.sf.tapestry.link;

import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.engine.ExternalService;

/**
 *  A component for creating a link to {@link IExternalPage} using the 
 * {@link ExternalService}.
 *
 *  [<a href="../../../../../ComponentReference/ExternalLink.html">Component Reference</a>]
 *
 * @see IExternalPage
 * @see ExternalService
 *
 * @author Malcolm Edgar
 * @version $Id$
 *
 **/

public class ExternalLink extends GestureLink
{
    private Object _parameters;

    private String _targetPage;

    /**
     *  Returns {@link IEngineService#EXTERNAL_SERVICE}.
     *
     **/

    protected String getServiceName()
    {
        return IEngineService.EXTERNAL_SERVICE;
    }

    protected Object[] getServiceParameters(IRequestCycle cycle)
    {
        Object[] pageParameters = DirectLink.constructServiceParameters(_parameters);
        
        if (pageParameters == null)
        return new Object[] { _targetPage  };
        
        Object[] parameters = new Object[pageParameters.length + 1];
        
        parameters[0] = _targetPage;
        
        System.arraycopy(pageParameters, 0, parameters, 1, pageParameters.length);
        
        return parameters;
    }
        
    public Object getParameters()
    {
        return _parameters;
    }

    public void setParameters(Object parameters)
    {
        _parameters = parameters;
    }

    public String getTargetPage()
    {
        return _targetPage;
    }

    public void setTargetPage(String targetPage)
    {
        _targetPage = targetPage;
    }

}

