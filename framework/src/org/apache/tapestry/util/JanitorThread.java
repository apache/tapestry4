//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.Tapestry;

/**
 * A basic kind of janitor, an object that periodically invokes {@link ICleanable#executeCleanup()}
 * on a set of objects.
 * <p>
 * The JanitorThread holds a <em>weak reference</em> to the objects it operates on.
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 1.0.5
 */

public class JanitorThread extends Thread
{
    /**
     * Default number of seconds between janitor runs, about 30 seconds.
     */

    public static final long DEFAULT_INTERVAL_MILLIS = 30 * 1024;

    private long interval = DEFAULT_INTERVAL_MILLIS;

    private boolean lockInterval = false;

    private static JanitorThread shared = null;

    /**
     * A {@link List}of {@link WeakReference}s to {@link ICleanable}instances.
     */

    private List references = new ArrayList();

    /**
     * Creates a new daemon Janitor.
     */

    public JanitorThread()
    {
        this(null);
    }

    /**
     * Creates new Janitor with the given name. The thread will have minimum priority and be a
     * daemon.
     */

    public JanitorThread(String name)
    {
        super(name);

        setDaemon(true);
        setPriority(MIN_PRIORITY);
    }

    /**
     * Returns a shared instance of JanitorThread. In most cases, the shared instance should be
     * used, rather than creating a new instance; the exception being when particular scheduling is
     * of concern. It is also bad policy to change the sleep interval on the shared janitor (though
     * nothing prevents this, either).
     */

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
     * Updates the property, which may not take effect until the next time the thread finishes
     * sleeping.
     * 
     * @param value
     *            the interval, in milliseconds, between sweeps.
     * @throws IllegalStateException
     *             always, if the receiver is the shared JanitorThread
     * @throws IllegalArgumentException
     *             if value is less than 1
     */

    public void setInterval(long value)
    {
        if (lockInterval)
            throw new IllegalStateException(Tapestry.getMessage("JanitorThread.interval-locked"));

        if (value < 1)
            throw new IllegalArgumentException(Tapestry
                    .getMessage("JanitorThread.illegal-interval"));

        interval = value;
    }

    /**
     * Adds a new cleanable object to the list of references. Care should be taken that objects are
     * not added multiple times; they will be cleaned too often.
     */

    public void add(ICleanable cleanable)
    {
        WeakReference reference = new WeakReference(cleanable);

        synchronized (references)
        {
            references.add(reference);
        }
    }

    /**
     * Runs through the list of targets and invokes {@link ICleanable#executeCleanup()}on each of
     * them. {@link WeakReference}s that have been invalidated are weeded out.
     */

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
     * Waits for the next run, by sleeping for the desired period. Returns true if the sleep was
     * successful, or false if the thread was interrupted (and should shut down).
     */

    protected void waitForNextPass()
    {
        try
        {
            sleep(interval);
        }
        catch (InterruptedException ex)
        {
            interrupt();
        }
    }

    /**
     * Alternates between {@link #waitForNextPass()}and {@link #sweep()}.
     */

    public void run()
    {
        while (!isInterrupted())
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