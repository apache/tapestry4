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