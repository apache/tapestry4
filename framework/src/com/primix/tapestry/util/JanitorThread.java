/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.util;

import java.util.*;
import java.lang.ref.*;

/**
 *  A basic kind of janitor, an object that periodically invokes
 *  {@link ICleanable#executeJanitor()} on a set of objects.
 *
 *  <p>The JanitorThread holds a <em>weak reference</em> to
 *  the objects it operates on.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 */

public class JanitorThread extends Thread
{
	/**
	 * Default number of seconds between janitor runs, about 30 seconds.
	 *
	 */
	
	public static final long DEFAULT_INTERVAL_MILLIS = 30 * 1024;
	
	private long interval = DEFAULT_INTERVAL_MILLIS;
	private boolean lockInterval = false;
	
	private static JanitorThread shared = null;
	
	/**
	 *  A {@link List} of {@link WeakReference}s to {@link IJanitor} instances.
	 *
	 */
	
	private List references = new ArrayList();

	/**
	 *  Creates a new daemon Janitor.
	 *
	 */
	
	public JanitorThread()
	{
		this(null);
	}
	
	/**
	 *  Creates new Janitor with the given name.  The thread
	 *  will have minimum priority and be a daemon.
	 *
	 */
	
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
	 */
	
	public static JanitorThread getSharedJanitorThread()
	{
		if (shared == null)
		{
			synchronized(JanitorThread.class)
			{
				if (shared == null)
				{
					shared = new JanitorThread("Shared-JanitorThread");
					shared.lockInterval = true;
					
					shared.start();
				}
			}
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
	 */
	
	public void setInterval(long value)
	{
		if (lockInterval)
			throw new IllegalStateException("The interval for this janitor thread is locked.");
		
		if (value < 1)
			throw new IllegalArgumentException("The interval for a janitor thread may not be less than 1 millisecond.");
		
		interval = value;
		
		interrupt();
	}
	
	/**
	 *  Adds a new cleanable object to the list of references.  Care should be taken that
	 *  objects are not added multiple times; they will be
	 *  cleaned too often.
	 *
	 */
	
	public void add(ICleanable cleanable)
	{
		WeakReference reference = new WeakReference(cleanable);
		
		synchronized(references)
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
	 */
	
	protected void sweep()
	{
		synchronized(references)
		{
			Iterator i = references.iterator();
			
			while (i.hasNext())
			{
				WeakReference ref = (WeakReference)i.next();
				
				ICleanable cleanable = (ICleanable)ref.get();
				
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
	 */
	
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
	 */
	
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
		synchronized(references)
		{
			buffer.append(references.size());
		}
		
		buffer.append(']');
		
		return buffer.toString();
	}
}
