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

package org.apache.tapestry;

/**
 * A set of localized message strings.  This is somewhat like
 * a {@link java.util.ResourceBundle}, but with more
 * flexibility about where the messages come from.  In addition,
 * it includes methods similar to {@link java.text.MessageFormat}
 * for formatting the strings.
 *
 * @see org.apache.tapestry.IComponent#getMessages
 * @see org.apache.tapestry.engine.IComponentMessagesSource
 * 
 * @author Howard Lewis Ship
 * @since 2.0.4
 *
 */

public interface IMessages
{
    /**
     * Searches for a localized string with the given key.
     * If not found, a modified version of the key
     * is returned (all upper-case and surrounded by square
     * brackets).
     * 
     */

    public String getMessage(String key);

    /**
     * Searches for a localized string with the given key.
     * If not found, then the default value (which should already
     * be localized) is returned.  Passing a default of null
     * is useful when trying to determine if the strings contains
     * a given key.
     *
     */

    public String getMessage(String key, String defaultValue);

    /**
     * Formats a string, using
     * {@link java.text.MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * 
     * <p>
     * In addition, special processing occurs for any of the arguments that
     * inherit from {@link Throwable}: such arguments are replaced with the Throwable's message
     * (if non blank), or the Throwable's class name (if the message is blank).
     *
     * @param key the key used to obtain a localized pattern using
     * {@link #getMessage(String)}
     * @param arguments passed to the formatter
     *
     * @since 3.0
     *
     */

    public String format(String key, Object[] arguments);

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     * @since 3.0
     *
     */
    public String format(String key, Object argument);

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     *
     * @since 3.0
     * 
     */

    public String format(String key, Object argument1, Object argument2);

    /**
     * Convienience method for invoking {@link #format(String, Object[])}.
     *
     * @since 3.0
     * 
     */

    public String format(String key, Object argument1, Object argument2, Object argument3);
}
