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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.Defense;

/**
 * Constants and static methods.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TapestryUtils
{
    /**
     * Stores an attribute into the request cycle, verifying that no object with that key is already
     * present.
     * 
     * @param cycle
     *            the cycle to store the attribute into
     * @param key
     *            the key to store the attribute as
     * @param object
     *            the attribute value to store
     * @throws IllegalStateException
     *             if a non-null value has been stored into the cycle with the provided key.
     */

    public static void storeUniqueAttribute(IRequestCycle cycle, String key, Object object)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(key, "key");
        Defense.notNull(object, "object");

        Object existing = cycle.getAttribute(key);
        if (existing != null)
            throw new IllegalStateException(TapestryMessages.nonUniqueAttribute(
                    object,
                    key,
                    existing));

        cycle.setAttribute(key, object);
    }

    static final String PAGE_RENDER_SUPPORT_ATTRIBUTE = "org.apache.tapestry.PageRenderSupport";

    /**
     * Stores the support object using {@link #storeUniqueAttribute(IRequestCycle, String, Object)}.
     */

    public static void storeRenderPageSupport(IRequestCycle cycle, PageRenderSupport support)
    {
        storeUniqueAttribute(cycle, PAGE_RENDER_SUPPORT_ATTRIBUTE, support);
    }

    /**
     * Gets the previously stored {@link org.apache.tapestry.PageRenderSupport}object.
     * 
     * @param cycle
     *            the request cycle storing the support object
     * @param caller
     *            the caller of this method (used in exception messages)
     * @throws ApplicationRuntimeException
     *             if no support object has been stored
     */

    public static PageRenderSupport getPageRenderSupport(IRequestCycle cycle, Object caller)
    {
        Defense.notNull(cycle, "cycle");

        PageRenderSupport result = (PageRenderSupport) cycle
                .getAttribute(PAGE_RENDER_SUPPORT_ATTRIBUTE);

        if (result == null)
            throw new ApplicationRuntimeException(TapestryMessages.noPageRenderSupport(), HiveMind
                    .getLocation(caller), null);

        return result;
    }

    public static void removePageRenderSupport(IRequestCycle cycle)
    {
        cycle.removeAttribute(PAGE_RENDER_SUPPORT_ATTRIBUTE);
    }

    /**
     * Returns the {@link PageRenderSupport}&nbsp;object if previously stored, or null otherwise.
     * This is used in the rare case that a component wishes to adjust its behavior based on whether
     * the page render support services are avaiable (typically, adjust for whether enclosed by a
     * Body component, or not).
     */

    public static PageRenderSupport getOptionalPageRenderSupport(IRequestCycle cycle)
    {
        return (PageRenderSupport) cycle.getAttribute(PAGE_RENDER_SUPPORT_ATTRIBUTE);
    }
}