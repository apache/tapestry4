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

package org.apache.tapestry.components;

import org.apache.commons.lang.enum.Enum;

/**
 *  Different types of JavaScript events that an {@link ILinkComponent}
 *  can provide handlers for.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 *
 **/

public class LinkEventType extends Enum
{
    private String _attributeName;

    /**
     *  Type for <code>onMouseOver</code>.  This may also be called "focus".
     *
     **/

    public static final LinkEventType MOUSE_OVER = new LinkEventType("MOUSE_OVER", "onMouseOver");

    /**
     * Type for <code>onMouseOut</code>.  This may also be called "blur".
     *
     **/

    public static final LinkEventType MOUSE_OUT = new LinkEventType("MOUSE_OUT", "onMouseOut");

    /**
     * Type for <code>onClick</code>.
     *
     * @since 1.0.1
     *
     **/

    public static final LinkEventType CLICK = new LinkEventType("CLICK", "onClick");

    /**
     * Type for <code>onDblClick</code>.
     *
     * @since 1.0.1
     *
     **/

    public static final LinkEventType DOUBLE_CLICK =
        new LinkEventType("DOUBLE_CLICK", "onDblClick");

    /**
     * Type for <code>onMouseDown</code>.
     *
     * @since 1.0.1.
     *
     **/

    public static final LinkEventType MOUSE_DOWN = new LinkEventType("MOUSE_DOWN", "onMouseDown");

    /**
     * Type for <code>onMouseUp</code>.
     *
     * @since 1.0.1
     *
     **/

    public static final LinkEventType MOUSE_UP = new LinkEventType("MOUSE_UP", "onMouseUp");

    /**
     *  Constructs a new type of event.  The name should match the
     *  static final variable (i.e., MOUSE_OVER) and the attributeName
     *  is the name of the HTML attribute to be managed (i.e., "onMouseOver").
     *
     *  <p>This method is protected so that subclasses can be created
     *  to provide additional managed event types.
     **/

    protected LinkEventType(String name, String attributeName)
    {
        super(name);

        _attributeName = attributeName;
    }

    /**
     *  Returns the name of the HTML attribute corresponding to this
     *  type.
     *
     **/

    public String getAttributeName()
    {
        return _attributeName;
    }
}