package net.sf.tapestry;

import net.sf.tapestry.form.FormEventType;
import net.sf.tapestry.valid.IValidationDelegate;

/**
 *  A generic way to access a component which defines an HTML form.  This interface
 *  exists so that the {@link IRequestCycle} can invoke the
 *  {@link #rewind(IMarkupWriter, IRequestCycle)} method (which is used to deal with
 *  a Form that uses the direct service).  In release 1.0.5, more responsibility
 *  for forms was moved here.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 **/

public interface IForm extends IAction
{

    /**
     *  Attribute name used with the request cycle; allows other components to locate
     *  the IForm.
     *
     *  @since 1.0.5
     * 
     **/

    public static final String ATTRIBUTE_NAME = "net.sf.tapestry.active.Form";

    /**
     *  Invoked by the {@link IRequestCycle} to allow a form that uses
     *  the direct service, to respond to the form submission.
     *
     **/

    public void rewind(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException;

    /**
     *  Adds an additional event handler.  The type determines when the
     *  handler will be invoked, {@link FormEventType#SUBMIT}
     *  is most typical.
     *
     * @since 1.0.5
     * 
     **/

    public void addEventHandler(FormEventType type, String functionName);

    /**
     *  Constructs a unique identifier (within the Form).  The identifier
     *  consists of the component's id, with an index number added to
     *  ensure uniqueness.
     *
     *  <p>Simply invokes {@link #getElementId(String)} with the component's id.
     *
     *
     *  @since 1.0.5
     * 
     **/

    public String getElementId(IComponent component);

    /**
     *  Constructs a unique identifier from the base id.  If possible, the
     *  id is used as-is.  Otherwise, a unique identifier is appended
     *  to the id.
     *
     *  <p>This method is provided simply so that some components
     *  ({@link net.sf.tapestry.form.ImageSubmit}) have more specific control over
     *  their names.
     *
     *  @since 1.0.5
     *
     **/

    public String getElementId(String baseId);

    /**
     * Returns the name of the form.
     *
     *  @since 1.0.5
     *
     **/

    public String getName();

    /**
     *  Returns true if the form is rewinding (meaning, the form was the subject
     *  of the request cycle).
     *
     *  @since 1.0.5
     *
     **/

    public boolean isRewinding();

    /**
     *  Returns the validation delegate for the form.
     * 
     *  @since 1.0.8
     * 
     **/

    public IValidationDelegate getDelegate() throws RequestCycleException;
}