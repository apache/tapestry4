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

package org.apache.tapestry.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.Tapestry;

/**
 * A utility class often used with the {@link org.apache.tapestry.form.ListEdit}component. A
 * ListEditMap is loaded with data objects before the ListEdit renders, and again before the
 * ListEdit rewinds. This streamlines the synchronization of the form against data on the server. It
 * is most useful when the set of objects is of a manageable size (say, no more than a few hundred
 * objects).
 * <p>
 * The map stores a list of keys, and relates each key to a value. It also tracks a deleted flag for
 * each key.
 * <p>
 * Usage: <br>
 * The page or component should implement {@link org.apache.tapestry.event.PageRenderListener}and
 * implement
 * {@link org.apache.tapestry.event.PageRenderListener#pageBeginRender(org.apache.tapestry.event.PageEvent)}
 * to initialize the map.
 * <p>
 * The external data (from which keys and values are obtained) is queried, and each key/value pair
 * is {@link #add(Object, Object) added}to the map, in the order that items should be presented.
 * <p>
 * The {@link org.apache.tapestry.form.ListEdit}'s source parameter should be bound to the map's
 * {@link #getKeys() keys}property. The value parameter should be bound to the map's
 * {@link #setKey(Object) key}property.
 * <p>
 * The {@link org.apache.tapestry.form.ListEdit}'s listener parameter should be bound to a listener
 * method to synchronize a property of the component from the map.
 * 
 * <pre><code>
 * 
 *  
 *   
 *     public void synchronize({@link org.apache.tapestry.IRequestCycle} cycle)
 *     {
 *        ListEditMap map = ...;
 *        &lt;i&gt;Type&lt;/i&gt; object = (&lt;i&gt;Type&lt;/i&gt;)map.getValue();
 *    
 *        if (object == null)
 *          ...
 *    
 *        set&lt;i&gt;Property&lt;/i&gt;(object);
 *     }
 *     
 *   
 *  
 * </code></pre>
 * 
 * <p>
 * You may also connect a {@link org.apache.tapestry.form.Checkbox}'s selected parameter to the
 * map's {@link #isDeleted() deleted}property.
 * <p>
 * You may track inclusion in other sets by subclasses and implementing new boolean properties. The
 * accessor method should be a call to {@link #checkSet(Set)}and the mutator method should be a
 * call to {@link #updateSet(Set, boolean)}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class ListEditMap
{
    private Map _map = new HashMap();

    private List _keys = new ArrayList();

    private Set _deletedKeys;

    private Object _currentKey;

    /**
     * Records the key and value into this map. The keys may be obtained, in the order in which they
     * are added, using {@link #getKeys()}. This also sets the current key (so that you may invoke
     * {@link #setDeleted(boolean)}, for example).
     */

    public void add(Object key, Object value)
    {
        _currentKey = key;

        _keys.add(_currentKey);
        _map.put(_currentKey, value);
    }

    /**
     * Returns a List of keys, in the order that keys were added to the map (using
     * {@link #add(Object, Object)}. The caller must not modify the List.
     */

    public List getKeys()
    {
        return _keys;
    }

    /**
     * Sets the key for the map. This defines the key used with the other methods:
     * {@link #getValue()},{@link #isDeleted()},{@link #setDeleted(boolean)}.
     */

    public void setKey(Object key)
    {
        _currentKey = key;
    }

    /**
     * Returns the current key within the map.
     */

    public Object getKey()
    {
        return _currentKey;
    }

    /**
     * Returns the value for the key (set using {@link #setKey(Object)}). May return null if no
     * such key has been added (this can occur if a data object is deleted between the time a form
     * is rendered and the time a form is submitted).
     */

    public Object getValue()
    {
        return _map.get(_currentKey);
    }

    /**
     * Returns true if the {@link #setKey(Object) current key}is in the set of deleted keys.
     */

    public boolean isDeleted()
    {
        return checkSet(_deletedKeys);
    }

    /**
     * Returns true if the set contains the {@link #getKey() current key}. Returns false is the set
     * is null, or doesn't contain the current key.
     */

    protected boolean checkSet(Set set)
    {
        if (set == null)
            return false;

        return set.contains(_currentKey);
    }

    /**
     * Adds or removes the {@link #setKey(Object) current key}from the set of deleted keys.
     */

    public void setDeleted(boolean value)
    {
        _deletedKeys = updateSet(_deletedKeys, value);
    }

    /**
     * Updates the set, adding or removing the {@link #getKey() current key}from it. Returns the
     * set passed in. If the value is true and the set is null, an new instance of {@link HashSet}
     * is created and returned.
     */

    protected Set updateSet(Set set, boolean value)
    {
        if (value)
        {
            if (set == null)
                set = new HashSet();

            set.add(_currentKey);
        }
        else
        {
            if (set != null)
                set.remove(_currentKey);
        }

        return set;
    }

    /**
     * Returns the deleted keys in an unspecified order. May return an empty list.
     */

    public List getDeletedKeys()
    {
        return convertSetToList(_deletedKeys);
    }

    protected List convertSetToList(Set set)
    {
        if (Tapestry.isEmpty(set))
            return Collections.EMPTY_LIST;

        return new ArrayList(set);
    }

    /**
     * Returns all the values stored in the map, in the order in which values were added to the map
     * using {@link #add(Object, Object)}.
     */

    public List getAllValues()
    {
        int count = _keys.size();
        List result = new ArrayList(count);

        for (int i = 0; i < count; i++)
        {
            Object key = _keys.get(i);
            Object value = _map.get(key);

            result.add(value);
        }

        return result;
    }

    /**
     * Returns all the values stored in the map, excluding those whose id has been marked deleted,
     * in the order in which values were added to the map using {@link #add(Object, Object)}.
     */

    public List getValues()
    {
        int deletedCount = Tapestry.size(_deletedKeys);

        if (deletedCount == 0)
            return getAllValues();

        int count = _keys.size();

        List result = new ArrayList(count - deletedCount);

        for (int i = 0; i < count; i++)
        {
            Object key = _keys.get(i);

            if (_deletedKeys.contains(key))
                continue;

            Object value = _map.get(key);
            result.add(value);
        }

        return result;
    }

}