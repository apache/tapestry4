package net.sf.tapestry.contrib.inspector;

import org.apache.commons.lang.enum.Enum;

/**
 *  Identifies different views for the inspector.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class View extends Enum
{
    /**
     *  View that displays the basic specification information, plus
     *  formal and informal parameters (and related bindings), and 
     *  assets.
     *
     **/

    public static final View SPECIFICATION = new View("SPECIFICATION");

    /**
     *  View that displays the HTML template for the component, if one
     *  exists.
     *
     **/

    public static final View TEMPLATE = new View("TEMPLATE");

    /**
     *  View that shows the persistent properties of the page containing
     *  the inspected component.
     *
     **/

    public static final View PROPERTIES = new View("PROPERTIES");

    /**
     *  View that shows information about the 
     *  {@link net.sf.tapestry.IEngine}.
     *
     **/

    public static final View ENGINE = new View("ENGINE");


    private View(String name)
    {
        super(name);
    }

}