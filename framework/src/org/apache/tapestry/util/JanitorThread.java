/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.Tapestry;

/**
 *  A basic kind of janitor, an object that periodically invokes
 *  {@link ICleanable#executeCleanup()} on a set of objects.
 *
 *  <p>The JanitorThread holds a <em>weak reference</em> to
 *  the objects it operates on.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 **/

public class JanitorThread extends Thread
{
    /**
     * Default number of seconds between janitor runs, about 30 seconds.
     *
     **/

    public static final long DEFAULT_INTERVAL_MILLIS = 30 * 1024;

    private long interval = DEFAULT_INTERVAL_MILLIS;
    private boolean lockInterval = false;

    private static JanitorThread shared = null;

    /**
     *  A {@link List} of {@link WeakReference}s to {@link IJanitor} instances.
     *
     **/

    private List references = new ArrayList();

    /**
     *  Creates a new daemon Janitor.
     *
     **/

    public JanitorThread()
    {
        this(null);
    }

    /**
     *  Creates new Janitor with the given name.  The thread
     *  will have minimum priority and be a daemon.
     *
     **/

    public JanitorThread(String name)
    {
        super(name);

        setDaemon(true);
        setPriority(MIN_PRIORITY);
    }

    /**
     *  Returns a shared instance of JanitorThread.  In most cases,
     *  the shared instance should be used, rather than creating
     *  a new instance; the exception being when particular
     *  scheduling is of concern.  It is also bad policy to
     *  change the sleep interval on the shared janitor
     *  (though nothing prevents this, either).
     *
     **/

    public synchronized static JanitorThread getSharedJanitorThread()
    {
        if (shared == null)
        {
            shared = new JanitorThread("Shared-JanitorThread");
            shared.lockInterval = true;

            shared.start();
        }

        return shared;
    }

    public long getInterval()
    {
        return interval;
    }

    /**
     *  Updates the property, then interrupts the thread.
     *
     *  @param the interval, in milliseconds, between sweeps.
     *
     *  @throws IllegalStateException always, if the receiver is the shared JanitorThread
     *  @throws IllegalArgumentException if value is less than 1
     **/

    public void setInterval(long value)
    {
        if (lockInterval)
            throw new IllegalStateException(Tapestry.getString("JanitorThread.interval-locked"));

        if (value < 1)
            throw new IllegalArgumentException(Tapestry.getString("JanitorThread.illegal-interval"));

        interval = value;

        interrupt();
    }

    /**
     *  Adds a new cleanable object to the list of references.  Care should be taken that
     *  objects are not added multiple times; they will be
     *  cleaned too often.
     *
     **/

    public void add(ICleanable cleanable)
    {
        WeakReference reference = new WeakReference(cleanable);

        synchronized (references)
        {
            references.add(reference);
        }
    }

    /**
     *  Runs through the list of targets and invokes
     *  {@link ICleanable#executeCleanup()}
     *  on each of them.  {@link WeakReference}s that have been invalidated
     *  are weeded out.
     *
     **/

    protected void sweep()
    {
        synchronized (references)
        {
            Iterator i = references.iterator();

            while (i.hasNext())
            {
                WeakReference ref = (WeakReference) i.next();

                ICleanable cleanable = (ICleanable) ref.get();

                if (cleanable == null)
                    i.remove();
                else
                    cleanable.executeCleanup();
            }
        }
    }

    /**
     *  Waits for the next run, by sleeping for the desired period.
     *
     *
     **/

    protected void waitForNextPass()
    {
        try
        {
            sleep(interval);
        }
        catch (InterruptedException ex)
        {
            // Ignore.
        }
    }

    /**
     *  Alternates between {@link #waitForNextPass()} and
     *  {@link #sweep()}.
     *
     **/

    public void run()
    {
        while (true)
        {
            waitForNextPass();

            sweep();
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("JanitorThread@");
        buffer.append(Integer.toHexString(hashCode()));

        buffer.append("[interval=");
        buffer.append(interval);

        buffer.append(" count=");

        synchronized (references)
        {
            buffer.append(references.size());
        }

        buffer.append(']');

        return buffer.toString();
    }
}