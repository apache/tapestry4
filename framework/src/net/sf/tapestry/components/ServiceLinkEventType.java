package net.sf.tapestry.components;

import org.apache.commons.lang.enum.Enum;

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
    private String _attributeName;

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