package tests.tapestry;

/*
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
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
