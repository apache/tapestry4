package tutorial.portal;

import com.primix.tapestry.*;
import com.primix.tapestry.valid.*;


/**
 *  A base page for pages that contain an error property.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class ErrorPage
    extends BasePage
{
    private String error;
    private IValidationDelegate validationDelegate;
    
    private class MyDelegate extends BaseValidationDelegate
    {
        public void invalidField(IValidatingTextField field,
                                 ValidationConstraint constraint,
                                 String defaultErrorMessage)
        {
            if (error == null)
                error = defaultErrorMessage;
        }
    }
    
    public IValidationDelegate getValidationDelegate()
    {
        if (validationDelegate == null)
            validationDelegate = new MyDelegate();
        
        return validationDelegate;
    }
    
    /**
     *  Marks a particular {@link IValidatingTextField} as in error,
     *  and sets the page's error property, if not already
     *  set.
     *
     */
    
    protected void setErrorField(String idPath, String fieldError)
    {
        IValidatingTextField field = (IValidatingTextField)getNestedComponent(idPath);
        
        field.setError(true);
        
        if (error == null)
            error = fieldError;
    }
    
    public void detach()
    {
        error = null;
        
        super.detach();
    }
    
    public String getError()
    {
        return error;
    }
    
    public void setError(String value)
    {
        error = value;
    }
    
}
