// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * Base implementation of {@link org.apache.tapestry.SessionStoreOptimized}. Subclasses should
 * invoke {@link #markSessionStoreNeeded()} any time internal state changed.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class BaseSessionStoreOptimized implements SessionStoreOptimized, Serializable,
        HttpSessionBindingListener
{
    private static final long serialVersionUID = -2786704444616789831L;

    private transient boolean _dirty;

    /**
     * Clears the dirty flag.
     */
    public void valueBound(HttpSessionBindingEvent event)
    {
        _dirty = false;
    }

    /**
     * Does nothing.
     */
    public void valueUnbound(HttpSessionBindingEvent event)
    {
    }

    /**
     * Sets the dirty flag.
     */
    protected void markSessionStoreNeeded()
    {
        _dirty = true;
    }

    /**
     * Returns the dirty flag.
     */
    public boolean isStoreToSessionNeeded()
    {
        return _dirty;
    }

}
