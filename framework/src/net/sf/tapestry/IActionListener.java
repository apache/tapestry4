package net.sf.tapestry;

/**
 *  Defines a listener to an {@link IAction} component, which is way to
 *  get behavior when the component's URL is triggered (or the form
 *  containing the component is submitted).  Certain form elements 
 *  ({@link net.sf.tapestry.form.Hidden})
 *  also make use of this interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IActionListener
{

    /**
     *  Method invoked by the component (an {@link net.sf.tapestry.link.ActionLink} or 
     *  {@link net.sf.tapestry.form.Form}, when its URL is triggered.
     *
     *  @param action The component which was "triggered".
     *  @param cycle The request cycle in which the component was triggered.
     *
     **/

    public void actionTriggered(IComponent component, IRequestCycle cycle)
        throws RequestCycleException;
}