package net.sf.tapestry.valid;

/**
 *  Thrown by a {@link IValidator} when submitted input is not valid.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class ValidatorException extends Exception
{
    private ValidationConstraint constraint;
    private String invalidInput;

    public ValidatorException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     *  Creates a new instance.
     *  @param errorMessage the default error message to be used (this may be
     *  overriden by the {@link IValidationDelegate}
     *  @param constraint a validation constraint that has been compromised, or
     *  null if no constraint is applicable
     *  @param invalidInput the input received by the component that was invalid; this
     *  is stored during the request cycle and will be used as the default value
     *  for the input field during the page render
     **/

    public ValidatorException(
        String errorMessage,
        ValidationConstraint constraint,
        String invalidInput)
    {
        super(errorMessage);

        this.constraint = constraint;
        this.invalidInput = invalidInput;
    }

    public ValidationConstraint getConstraint()
    {
        return constraint;
    }

    public String getInvalidInput()
    {
        return invalidInput;
    }
}