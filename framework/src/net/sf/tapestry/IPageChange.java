package net.sf.tapestry;

/**
 *  Represents a change to a component on a page, this represents
 *  a datum of information stored by a {@link IPageRecorder}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IPageChange
{
    /**
     *  The path to the component on the page, or null if the property is a property
     *  of the page.
     *
     **/

    public String getComponentPath();

    /**
     *  The new value for the property, which may be null.
     *
     **/

    public Object getNewValue();

    /**
     *  The name of the property that changed.
     *
     **/

    public String getPropertyName();
}