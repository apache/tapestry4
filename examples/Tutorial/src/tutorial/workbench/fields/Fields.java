package tutorial.workbench.fields;

import java.math.BigDecimal;

import tutorial.workbench.WorkbenchValidationDelegate;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.html.BasePage;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Fields extends BasePage
{
    public static final int INT_MIN = 5;
    public static final int INT_MAX = 20;
    public static final double DOUBLE_MIN = 3.14;
    public static final double DOUBLE_MAX = 27.5;
    public static final BigDecimal DECIMAL_MIN = new BigDecimal("2");

    public static final BigDecimal DECIMAL_MAX =
        new BigDecimal("100.123456234563456734563456356734567456784567456784567845675678456785678");

    public static final long LONG_MIN = 6;
    public static final long LONG_MAX = 21;

    public static final int STRING_MIN_LENGTH = 3;

    private boolean _clientValidationEnabled = true;
    
    public void detach()
    {
        _clientValidationEnabled = true;
        
        super.detach();
    }
       
    
    public void clientValidationChanged(IRequestCycle cycle)
    {
        // Do nothing.
    }

    public void formSubmit(IRequestCycle cycle)
    {

        WorkbenchValidationDelegate delegate = (WorkbenchValidationDelegate) getBeans().getBean("delegate");

        // If no error message, advance to the Results page,

        // otherwise, stay here and show the error message.

        if (!delegate.getHasErrors())
            cycle.setPage("FieldsResults");
    }

    public boolean isClientValidationEnabled()
    {
        return _clientValidationEnabled;
    }

    public void setClientValidationEnabled(boolean clientValidationEnabled)
    {
        _clientValidationEnabled = clientValidationEnabled;
        
        fireObservedChange("clientValidationEnabled", _clientValidationEnabled);
    }

}