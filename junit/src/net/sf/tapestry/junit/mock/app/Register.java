package net.sf.tapestry.junit.mock.app;

import java.util.ResourceBundle;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.form.EnumPropertySelectionModel;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.IValidationDelegate;

/**
 *
 *  Page used to demonstate basic forms.  
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class Register extends BasePage
{
    private IPropertySelectionModel _ageRangeModel;

    public IPropertySelectionModel getAgeRangeModel()
    {
        if (_ageRangeModel == null)
            _ageRangeModel =
                new EnumPropertySelectionModel(
                    AgeRange.getAllValues(),
                    ResourceBundle.getBundle("net.sf.tapestry.junit.mock.app.AgeRangeStrings", getLocale()));

        return _ageRangeModel;
    }

    public void formSubmit(IRequestCycle cycle)
    {
        IValidationDelegate delegate = (IValidationDelegate)getBeans().getBean("delegate");
        
        if (delegate.getHasErrors())
            return;
            
        User user = (User)getBeans().getBean("user");
        
        RegisterConfirm page = (RegisterConfirm)cycle.getPage("RegisterConfirm");
        
        page.setUser(user);
        cycle.setPage(page);
    }
}
