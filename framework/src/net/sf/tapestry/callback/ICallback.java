package net.sf.tapestry.callback;

import java.io.Serializable;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.util.IImmutable;

/**
 *  Defines a callback, an object which is used to invoke or reinvoke a method
 *  on an object or component in a later request cycle.  This is used to
 *  allow certain operations (say, submitting an order) to defer to other processes
 *  (say, logging in and/or registerring).
 *
 *  <p>Callbacks must be {@link Serializable}, to ensure that they can be stored
 *  between request cycles.
 * 
 *  <p>Callbacks must be immutable, to ensure that they can be treated efficiently
 *  by {@link net.sf.tapestry.record.IValuePersister}.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 0.2.9
 *
 **/

public interface ICallback extends Serializable, IImmutable
{
    /**
     *  Performs the call back.  Typical implementation will locate a particular
     *  page or component and invoke a method upon it, or 
     *  invoke a method on the {@link IRequestCycle cycle}.
     *
     **/

    public void performCallback(IRequestCycle cycle) throws RequestCycleException;
}