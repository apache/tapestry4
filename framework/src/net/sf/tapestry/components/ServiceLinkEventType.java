//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.components;

import net.sf.tapestry.util.Enum;

/**
 *  Different types of JavaScript events that an {@link IServiceLink}
 *  can provide handlers for.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 0.2.9
 *
 **/

public class ServiceLinkEventType extends Enum
{
    private String attributeName;

    /**
     *  Type for <code>onMouseOver</code>.  This may also be called "focus".
     *
     **/

    public static final ServiceLinkEventType MOUSE_OVER =
        new ServiceLinkEventType("MOUSE_OVER", "onMouseOver");

    /**
     * Type for <code>onMouseOut</code>.  This may also be called "blur".
     *
     **/

    public static final ServiceLinkEventType MOUSE_OUT =
        new ServiceLinkEventType("MOUSE_OUT", "onMouseOut");

    /**
     * Type for <code>onClick</code>.
     *
     * @since 1.0.1
     *
     **/

    public static final ServiceLinkEventType CLICK = new ServiceLinkEventType("CLICK", "onClick");

    /**
     * Type for <code>onDblClick</code>.
     *
     * @since 1.0.1
     *
     **/

    public static final ServiceLinkEventType DOUBLE_CLICK =
        new ServiceLinkEventType("DOUBLE_CLICK", "onDblClick");

    /**
     * Type for <code>onMouseDown</code>.
     *
     * @since 1.0.1.
     *
     **/

    public static final ServiceLinkEventType MOUSE_DOWN =
        new ServiceLinkEventType("MOUSE_DOWN", "onMouseDown");

    /**
     * Type for <code>onMouseUp</code>.
     *
     * @since 1.0.1
     *
     **/

    public static final ServiceLinkEventType MOUSE_UP =
        new ServiceLinkEventType("MOUSE_UP", "onMouseUp");

    /**
     *  Constructs a new type of event.  The name should match the
     *  static final variable (i.e., MOUSE_OVER) and the attributeName
     *  is the name of the HTML attribute to be managed (i.e., "onMouseOver").
     *
     *  <p>This method is protected so that subclasses can be created
     *  to provide additional managed event types.
     **/

    protected ServiceLinkEventType(String name, String attributeName)
    {
        super(name);

        this.attributeName = attributeName;
    }

    /**
     *  Returns the name of the HTML attribute corresponding to this
     *  type.
     *
     **/

    public String getAttributeName()
    {
        return attributeName;
    }
}