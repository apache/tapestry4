/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
 * @version $Id$
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
