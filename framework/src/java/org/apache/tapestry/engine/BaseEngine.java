// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.IRequestCycle;

/**
 * Concrete implementation of {@link org.apache.tapestry.IEngine}used for ordinary applications.
 * All page state information is maintained in the {@link javax.servlet.http.HttpSession}using
 * instances of {@link org.apache.tapestry.record.SessionPageRecorder}.
 * 
 * @author Howard Lewis Ship
 */

public class BaseEngine extends AbstractEngine
{
    private static final long serialVersionUID = -7051050643746333380L;

    /**
     * Removes all page recorders that contain no changes, or are marked for discard. Subclasses
     * should invoke this implementation in addition to providing thier own.
     */

    protected void cleanupAfterRequest(IRequestCycle cycle)
    {
        //        if (Tapestry.isEmpty(_recorders))
        //            return;
        //
        //		boolean markDirty = false;
        //        Iterator i = _recorders.entrySet().iterator();
        //
        //        while (i.hasNext())
        //        {
        //            Map.Entry entry = (Map.Entry) i.next();
        //            String pageName = (String) entry.getKey();
        //            IPageRecorder recorder = (IPageRecorder) entry.getValue();
        //
        //            if (!recorder.getHasChanges() || recorder.isMarkedForDiscard())
        //            {
        //                recorder.discard();
        //
        //                i.remove();
        //
        //                _activePageNames.remove(pageName);
        //      	
        //      			markDirty = true;
        //            }
        //        }
        //        
        //        if (markDirty)
        //        	markDirty();
    }

    public void forgetPage(String name)
    {
        throw new UnsupportedOperationException("forgetPage()");

        //        if (_recorders == null)
        //            return;
        //
        //        IPageRecorder recorder = (IPageRecorder) _recorders.get(name);
        //        if (recorder == null)
        //            return;
        //
        //        if (recorder.isDirty())
        //            throw new ApplicationRuntimeException(
        //                Tapestry.format("BaseEngine.recorder-has-uncommited-changes", name));
        //
        //        recorder.discard();
        //        _recorders.remove(name);
        //        _activePageNames.remove(name);
        //        
        //        markDirty();
    }

    /**
     * Returns an unmodifiable {@link Collection}of the page names for which {@link IPageRecorder}
     * instances exist.
     */

    public Collection getActivePageNames()
    {
        return Collections.EMPTY_LIST;

    }

    public IPageRecorder getPageRecorder(String pageName, IRequestCycle cycle)
    {
        throw new UnsupportedOperationException("getPageRecorder()");
    }

    public IPageRecorder createPageRecorder(String pageName, IRequestCycle cycle)
    {
        throw new UnsupportedOperationException("createPageRecorder()");
    }

    /**
     * Reconstructs the list of active page names written by {@link #writeExternal(ObjectOutput)}.
     */

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        super.readExternal(in);
    }

    /**
     * Writes the engine's persistent state; this is simply the list of active page names. For
     * efficiency, this is written as a count followed by each name as a UTF String.
     */

    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
    }
}