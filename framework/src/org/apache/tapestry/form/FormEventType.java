/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.form;

import org.apache.commons.lang.enum.Enum;

/**
 *  Lists different types of JavaScript events that can be associated
 *  with a {@link Form} via {@link Form#addEventHandler(FormEventType, String)}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 **/

public class FormEventType extends Enum
{
    /**
     *  Form event triggered when the form is submitted.  Allows an event handler
     *  to perform any final changes before the results are posted to the server.
     *
     *  <p>The JavaScript method should return <code>true</code> or
     * <code>false</code>.  If there are multiple event handlers for the form
     * they will be combined using the binary and operator (<code>&amp;&amp;</code>).
     *
     **/

    public static final FormEventType SUBMIT = new FormEventType("SUBMIT", "onsubmit");

    /**
     *  Form event triggered when the form is reset; this allows an event handler
     *  to deal with any special cases related to resetting.
     *
     **/

    public static final FormEventType RESET = new FormEventType("RESET", "onreset");

    private String _propertyName;

    private FormEventType(String name, String propertyName)
    {
        super(name);

        _propertyName = propertyName;
    }

    /** 
     *  Returns the DOM property corresponding to event type (used when generating
     *  client-side scripting).
     *
     **/

    public String getPropertyName()
    {
        return _propertyName;
    }

    /**
     *  Returns true if multiple functions should be combined
     *  with the <code>&amp;&amp;</code> operator.  Otherwise,
     *  the event handler functions are simply invoked
     *  sequentially (as a series of JavaScript statements).
     *
     **/

    public boolean getCombineUsingAnd()
    {
        return this == FormEventType.SUBMIT;
    }
}