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