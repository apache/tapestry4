package net.sf.tapestry.components;

import net.sf.tapestry.IComponent;

/**
 *  A component that renders an HTML &lt;a&gt; element.  It exposes some
 *  properties to the components it wraps.  This is basically to facilitate
 *  the {@link net.sf.tapestry.html.Rollover} component.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public interface IServiceLink extends IComponent
{
    /**
     *  The name of an {@link net.sf.tapestry.IRequestCycle} attribute in which the
     *  current service link is stored.  ServiceLink links do not nest.
     *
     **/

    public static final String ATTRIBUTE_NAME = "net.sf.tapestry.active.IServiceLink";

    /**
     *  Returns whether this service link component is enabled or disabled.
     *
     *  @since 0.2.9
     *
     **/

    public boolean isDisabled();

    /**
     *  Adds a new event handler.  When the event occurs, the JavaScript function
     *  specified is executed.  Multiple functions can be specified, in which case
     *  all of them are executed.
     *
     *  <p>This was created for use by
     *  {@link net.sf.tapestry.html.Rollover} to set mouse over and mouse out handlers on
     *  the {@link IServiceLink} that wraps it, but can be used for
     *  many other things as well.
     *
     *  @since 0.2.9
     **/

    public void addEventHandler(ServiceLinkEventType eventType, String functionName);
}