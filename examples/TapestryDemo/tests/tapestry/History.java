package tests.tapestry;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.sesstrack.*;
import java.rmi.*;
import java.util.*;

public class History extends BasePage
{
    private ServerHit hit;

    public History(IApplication application, ComponentSpecification specification)
    {
        super(application, specification);
    }

    /**
     *  Returns the URLs stored by the session tracker as an array.
     *
     */

    public Object[] getHits()
    {
        ISessionTracker tracker;
        List list;

        tracker = ((DemoApplication)application).getSessionTracker();

        try
        {
            list = tracker.getHits();

        }
        catch (RemoteException e)
        {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        
        return list.toArray();
    }

    public void setHit(ServerHit value)
    {
        hit = value;
    }

    public ServerHit getHit()
    {
        return hit;
    }

    public void detachFromApplication()
    {
        super.detachFromApplication();

        hit = null;
    }
}
