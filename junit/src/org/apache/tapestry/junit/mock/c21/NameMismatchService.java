package org.apache.tapestry.junit.mock.c21;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IEngineServiceView;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;

public class NameMismatchService implements IEngineService
{

    public ILink getLink(IRequestCycle cycle, IComponent component, Object[] parameters)
    {
        return null;
    }

    public boolean service(
        IEngineServiceView engine,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        return false;
    }

    public String getName()
    {
        return "IncorrectName";
    }

}
