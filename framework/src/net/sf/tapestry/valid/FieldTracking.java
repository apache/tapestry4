package net.sf.tapestry.valid;

import net.sf.tapestry.IRender;
import net.sf.tapestry.form.IFormComponent;

/**
 *  Default implementation of {@link IFieldTracking}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class FieldTracking implements IFieldTracking
{
    private IFormComponent component;
    private String invalidInput;
    private IRender renderer;
    private String fieldName;
    private ValidationConstraint constraint;

    FieldTracking()
    {
    }

    FieldTracking(String fieldName, IFormComponent component)
    {
        this.fieldName = fieldName;
        this.component = component;
    }

    public IFormComponent getFormComponent()
    {
        return component;
    }

    public IRender getRenderer()
    {
        return renderer;
    }

    public void setRenderer(IRender value)
    {
        renderer = value;
    }

    public String getInvalidInput()
    {
        return invalidInput;
    }

    public void setInvalidInput(String value)
    {
        invalidInput = value;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public ValidationConstraint getConstraint()
    {
        return constraint;
    }

    public void setConstraint(ValidationConstraint constraint)
    {
        this.constraint = constraint;
    }

}