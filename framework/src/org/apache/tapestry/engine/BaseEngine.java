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

package org.apache.tapestry.engine;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.record.SessionPageRecorder;

/**
 *  Concrete implementation of {@link org.apache.tapestry.IEngine} used for ordinary
 *  applications.  All page state information is maintained in
 *  the {@link javax.servlet.http.HttpSession} using
 *  instances of {@link org.apache.tapestry.record.SessionPageRecorder}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class BaseEngine extends AbstractEngine
{
    private static final long serialVersionUID = -7051050643746333380L;

    private final static int MAP_SIZE = 3;

    private transient Map _recorders;

    private transient Set _activePageNames;

    /**
     *  Removes all page recorders that contain no changes, or
     *  are marked for discard.  Subclasses
     *  should invoke this implementation in addition to providing
     *  thier own.
     *
     **/

    protected void cleanupAfterRequest(IRequestCycle cycle)
    {
        if (Tapestry.isEmpty(_recorders))
            return;

		boolean markDirty = false;
        Iterator i = _recorders.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String pageName = (String) entry.getKey();
            IPageRecorder recorder = (IPageRecorder) entry.getValue();

            if (!recorder.getHasChanges() || recorder.isMarkedForDiscard())
            {
                recorder.discard();

                i.remove();

                _activePageNames.remove(pageName);
      	
      			markDirty = true;
            }
        }
        
        if (markDirty)
        	markDirty();
    }

    public void forgetPage(String name)
    {
        if (_recorders == null)
            return;

        IPageRecorder recorder = (IPageRecorder) _recorders.get(name);
        if (recorder == null)
            return;

        if (recorder.isDirty())
            throw new ApplicationRuntimeException(
                Tapestry.format("BaseEngine.recorder-has-uncommited-changes", name));

        recorder.discard();
        _recorders.remove(name);
        _activePageNames.remove(name);
        
        markDirty();
    }

    /**
     *  Returns an unmodifiable {@link Collection} of the page names for which
     *  {@link IPageRecorder} instances exist.
     * 
     *
     **/

    public Collection getActivePageNames()
    {
        if (_activePageNames == null)
            return Collections.EMPTY_LIST;

        return Collections.unmodifiableCollection(_activePageNames);
    }

    public IPageRecorder getPageRecorder(String pageName, IRequestCycle cycle)
    {
        if (_activePageNames == null || !_activePageNames.contains(pageName))
            return null;

        IPageRecorder result = null;

        if (_recorders != null)
            return result = (IPageRecorder) _recorders.get(pageName);

        // So the page is active, but not in the cache of page recoders,
        // so (re-)create the page recorder.

        if (result == null)
            result = createPageRecorder(pageName, cycle);

        return result;
    }

    public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle)
    {
        if (_recorders == null)
            _recorders = new HashMap(MAP_SIZE);
        else
        {
            if (_recorders.containsKey(pageName))
                throw new ApplicationRuntimeException(
                    Tapestry.format("BaseEngine.duplicate-page-recorder", pageName));
        }

        // Force the creation of the HttpSession

        cycle.getRequestContext().createSession();
        setStateful();
       

        IPageRecorder result = new SessionPageRecorder();
        result.initialize(pageName, cycle);

        _recorders.put(pageName, result);

        if (_activePageNames == null)
            _activePageNames = new HashSet();

        _activePageNames.add(pageName);
        
        markDirty();

        return result;
    }

    /**
     *  Reconstructs the list of active page names
     *  written by {@link #writeExternal(ObjectOutput)}.
     * 
     **/

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);

        int count = in.readInt();

        if (count > 0)
            _activePageNames = new HashSet(count);

        for (int i = 0; i < count; i++)
        {
            String name = in.readUTF();

            _activePageNames.add(name);
        }

    }

    /**
     *  Writes the engine's persistent state; this is simply the list of active page
     *  names.  For efficiency, this is written as a count followed by each name
     *  as a UTF String.
     * 
     **/

    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);

        if (Tapestry.isEmpty(_activePageNames))
        {
            out.writeInt(0);
            return;
        }

        int count = _activePageNames.size();

        out.writeInt(count);

        Iterator i = _activePageNames.iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();

            out.writeUTF(name);
        }
    }

    public void extendDescription(ToStringBuilder builder)
    {
		builder.append("activePageNames", _activePageNames);
    }

}