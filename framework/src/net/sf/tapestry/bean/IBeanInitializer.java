package net.sf.tapestry.bean;

import net.sf.tapestry.IBeanProvider;

/**
 *  Interface for a set of classes used to initialize helper beans.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 * 
 **/

public interface IBeanInitializer
{
    /**
     *  Invoked by the {@link IBeanProvider} to initialize
     *  a property of the bean.
     *
     **/

    public void setBeanProperty(IBeanProvider provider, Object bean);

    /**
     *  Returns the name of the property this initializer
     *  will set.
     *
     **/

    public String getPropertyName();
}