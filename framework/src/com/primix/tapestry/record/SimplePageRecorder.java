package com.primix.tapestry.record;

import com.primix.tapestry.*;
import java.util.*;
import com.primix.foundation.*;
import java.io.Serializable;
import com.primix.tapestry.app.SimpleApplication;

/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Simple implementation of {@link IPageRecorder}
 *  that stores page changes in-memory using
 *  collections.
 *
 * <p>The recorder must be made session persistant, either by being stored
 *  directly in the session, or being referenced from a session-persistant
 *  object.  {@link SimpleApplication} simply stores a {@link Map} of
 *  these page recorders.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class SimplePageRecorder extends PageRecorder
{
	private final static int MAP_SIZE = 7;

	/**
	*  Dictionary of changes, keyed on an instance of ChangeKey
	*  (which enapsulates component path and property name).  The
	*  value is the new value for the object.
	*
	*/

	private Map changes;

	/**
	*  Simply clears the dirty flag, because there is no external place
	*  to store changed page properties.
	*
	*/

	public void commit()
	throws PageRecorderCommitException
	{
		dirty = false;
	}

	/**
	 *  Returns true if the recorder has any changes recorded.
	 *
	 */
	 
	public boolean getHasChanges()
	{
		if (changes == null)
			return false;
		
		return (changes.size() > 0);	
	}

	public Collection getChanges()
	{
    	Collection result;
        Iterator i;
        Map.Entry entry;
        PageChange change;
        int count;
        ChangeKey key;
        Object value;
        
		if (changes == null)
			return Collections.EMPTY_LIST;

		count = changes.size();
        result = new ArrayList(count);
        
        i = changes.entrySet().iterator();
        while (i.hasNext())
        {
        	entry = (Map.Entry)i.next();
            
            key = (ChangeKey)entry.getKey();
            
            value = entry.getValue();
            
            change = new PageChange(key.componentPath, key.propertyName, value);
            
            result.add(change);
        }

		return result;
	}

	protected void recordChange(String componentPath, String propertyName, Serializable newValue)
	{
		ChangeKey key;
		Object oldValue;

		key = new ChangeKey(componentPath, propertyName);

		if (changes == null)
			changes = new HashMap(MAP_SIZE);

		// Check the prior value.  If this is not an actual change,
		// then don't bother recording it, or marking this page recorder
		// dirty.

		oldValue = changes.get(key);
		if (newValue == oldValue)
			return;

		if (oldValue != null &&
			oldValue.equals(newValue))
			return;

		dirty = true;

		changes.put(key, newValue);
	}
}

