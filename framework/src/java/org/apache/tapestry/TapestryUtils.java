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

import java.util.ArrayList;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.Defense;

/**
 * Constants and static methods.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
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

    public static final String PAGE_RENDER_SUPPORT_ATTRIBUTE = "org.apache.tapestry.PageRenderSupport";

    public static final String FORM_ATTRIBUTE = "org.apache.tapestry.Form";

    /**
     * Stores the support object using {@link #storeUniqueAttribute(IRequestCycle, String, Object)}.
     */

    public static void storePageRenderSupport(IRequestCycle cycle, PageRenderSupport support)
    {
        storeUniqueAttribute(cycle, PAGE_RENDER_SUPPORT_ATTRIBUTE, support);
    }

    /**
     * Store the IForm instance using {@link #storeUniqueAttribute(IRequestCycle, String, Object)}.
     */

    public static void storeForm(IRequestCycle cycle, IForm form)
    {
        storeUniqueAttribute(cycle, FORM_ATTRIBUTE, form);
    }

    /**
     * Gets the previously stored {@link org.apache.tapestry.PageRenderSupport}&nbsp;object.
     * 
     * @param cycle
     *            the request cycle storing the support object
     * @param component
     *            the component which requires the support (used to report exceptions)
     * @throws ApplicationRuntimeException
     *             if no support object has been stored
     */

    public static PageRenderSupport getPageRenderSupport(IRequestCycle cycle, IComponent component)
    {
        Defense.notNull(component, "component");

        PageRenderSupport result = getOptionalPageRenderSupport(cycle);
        if (result == null)
            throw new ApplicationRuntimeException(TapestryMessages.noPageRenderSupport(component),
                    component.getLocation(), null);

        return result;
    }

    /**
     * Gets the previously stored {@link IForm} object.
     * 
     * @param cycle
     *            the request cycle storing the support object
     * @param component
     *            the component which requires the form (used to report exceptions)
     * @throws ApplicationRuntimeException
     *             if no form object has been stored
     */
    public static IForm getForm(IRequestCycle cycle, IComponent component)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(component, "component");

        IForm result = (IForm) cycle.getAttribute(FORM_ATTRIBUTE);

        if (result == null)
            throw new ApplicationRuntimeException(TapestryMessages.noForm(component), component
                    .getLocation(), null);

        return result;
    }

    public static void removePageRenderSupport(IRequestCycle cycle)
    {
        cycle.removeAttribute(PAGE_RENDER_SUPPORT_ATTRIBUTE);
    }

    public static void removeForm(IRequestCycle cycle)
    {
        cycle.removeAttribute(FORM_ATTRIBUTE);
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

    /**
     * Splits a string using the default delimiter of ','.
     */

    public static String[] split(String input)
    {
        return split(input, ',');
    }

    /**
     * Splits a single string into an array of strings, using a specific delimiter character.
     */

    public static String[] split(String input, char delimiter)
    {
        if (HiveMind.isBlank(input))
            return new String[0];

        List strings = new ArrayList();

        char[] buffer = input.toCharArray();

        int start = 0;
        int length = 0;

        for (int i = 0; i < buffer.length; i++)
        {
            if (buffer[i] != delimiter)
            {
                length++;
                continue;
            }

            // Consecutive delimiters will result in a sequence
            // of empty strings.

            String token = new String(buffer, start, length);
            strings.add(token);

            start = i + 1;
            length = 0;
        }

        // If the string contains no delimiters, then
        // wrap it an an array and return it.

        if (start == 0 && length == buffer.length)
        {
            return new String[]
            { input };
        }

        // The final token.
        String token = new String(buffer, start, length);
        strings.add(token);

        return (String[]) strings.toArray(new String[strings.size()]);
    }
}