package net.sf.tapestry.form;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  A base class for building components that correspond to HTML form elements.
 *  All such components must be wrapped (directly or indirectly) by
 *  a {@link Form} component.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *  @since 1.0.3
 * 
 **/

public abstract class AbstractFormComponent extends AbstractComponent implements IFormComponent
{
    /**
     *  Returns the {@link Form} wrapping this component.
     *
     *  @throws RequestCycleException if the component is not wrapped by a {@link Form}.
     *
     **/

    public IForm getForm(IRequestCycle cycle) throws RequestCycleException
    {
        IForm result = Form.get(cycle);

        if (result == null)
            throw new RequestCycleException(
                Tapestry.getString("AbstractFormComponent.must-be-contained-by-form"),
                this);

        return result;
    }

    public IForm getForm()
    {
        return Form.get(getPage().getRequestCycle());
    }

    abstract public String getName();

    /**
     *  Implemented in some subclasses to provide a display name (suitable
     *  for presentation to the user as a label or error message).  This implementation
     *  return null.
     * 
     **/

    public String getDisplayName()
    {
        return null;
    }
}