//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.engine;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.record.SimplePageRecorder;

/**
 *  Concrete implementation of {@link net.sf.tapestry.IEngine} used for relatively
 *  small applications.  All page state information is maintained in memory.  Since
 *  the instance is stored within the {@link javax.servlet.http.HttpSession}, 
 *  all page state information
 *  will be carried along to other servers in the cluster.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class SimpleEngine extends AbstractEngine
{
    /**
     *  @since 2.0.4
     * 
     **/

    private static final long serialVersionUID = -1658741363570905534L;

    private final static int MAP_SIZE = 3;

    private Map recorders;

    /**
     *  Restores the object state as written by
     *  {@link #writeExternal(ObjectOutput)}.
     *
     **/

    public void readExternal(ObjectInput in)
        throws IOException, ClassNotFoundException
    {
        int i, count;
        String pageName;
        SimplePageRecorder recorder;

        super.readExternal(in);

        count = in.readInt();

        if (count == 0)
            return;

        recorders = new HashMap(MAP_SIZE);

        for (i = 0; i < count; i++)
        {
            pageName = in.readUTF();

            // Putting a cast here is not super-efficient, but keeps
            // us sane!

            recorder = (SimplePageRecorder) in.readObject();

            recorders.put(pageName, recorder);
        }
    }

    /**
     *  Invokes the superclass implementation, then
     *  writes the number of recorders as an int (may be zero).
     *
     *  <p>For each recorder, writes
     *  <ul>
     *  <li>page name ({@link String})
     *  <li>page recorder ({@link SimplePageRecorder})
     *  </ul>
     *
     **/

    public void writeExternal(ObjectOutput out) throws IOException
    {
        Iterator i;
        Map.Entry entry;

        super.writeExternal(out);

        if (recorders == null)
        {
            out.writeInt(0);
            return;
        }

        out.writeInt(recorders.size());

        i = recorders.entrySet().iterator();

        while (i.hasNext())
        {
            entry = (Map.Entry) i.next();

            out.writeUTF((String) entry.getKey());
            out.writeObject(entry.getValue());
        }

    }

    /**
     *  Removes all page recorders that contain no changes, or
     *  are marked for discard.  Subclasses
     *  should invoke this implementation in addition to providing
     *  thier own.
     *
     **/

    protected void cleanupAfterRequest(IRequestCycle cycle)
    {
        Iterator i;
        Map.Entry entry;
        IPageRecorder recorder;

        if (recorders == null)
            return;

        i = recorders.entrySet().iterator();

        while (i.hasNext())
        {
            entry = (Map.Entry) i.next();
            recorder = (IPageRecorder) entry.getValue();

            if (!recorder.getHasChanges() || recorder.isMarkedForDiscard())
                i.remove();
        }
    }

    public void forgetPage(String name)
    {
        IPageRecorder recorder;

        if (recorders == null)
            return;

        recorder = (IPageRecorder) recorders.get(name);
        if (recorder == null)
            return;

        if (recorder.isDirty())
            throw new ApplicationRuntimeException(
                Tapestry.getString("SimpleEngine.recorder-has-uncommited-changes", name));

        recorders.remove(name);
    }

    /**
     *  Returns an unmodifiable {@link Collection} of the page names for which
     *  {@link IPageRecorder} instances exist.
     *
     **/

    public Collection getActivePageNames()
    {
        if (recorders == null)
            return Collections.EMPTY_LIST;

        return Collections.unmodifiableCollection(recorders.keySet());
    }

    public IPageRecorder getPageRecorder(String pageName)
    {
        if (recorders == null)
            return null;

        return (IPageRecorder) recorders.get(pageName);
    }

    public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle)
    {
        IPageRecorder result;

        if (recorders == null)
            recorders = new HashMap(MAP_SIZE);
        else
        {
            if (recorders.containsKey(pageName))
                throw new ApplicationRuntimeException(
                    Tapestry.getString("SimpleEngine.duplicate-page-recorder", pageName));
        }

        // Here's the key thing that identifies SimpleApplication as simple.
        // It uses a SimplePageRecorder (that simply stores the page property changes
        // in the HttpSession).

        result = new SimplePageRecorder();

        recorders.put(pageName, result);

        // Force the creation of the HttpSession

        cycle.getRequestContext().createSession();

        setStateful();

        return result;
    }

}