package net.sf.tapestry.event;

/**
 * May observe changes in an object's properties.  This is a "weak" variation
 * on JavaBean's style bound properties.  It is used when there will be at most
 * a single listener on property changes, and that the listener is not interested
 * in the old value.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public interface ChangeObserver
{
    /**
     *  Sent when the observed object changes a property.  The event identifies
     *  the object, the property and the new value.
     *
     **/

    public void observeChange(ObservedChangeEvent event);
}