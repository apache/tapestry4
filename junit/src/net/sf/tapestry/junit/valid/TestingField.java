package net.sf.tapestry.junit.valid;

import java.util.Locale;

import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.AbstractFormComponent;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.IField;

/**
 *  Used as a stand-in for a real component when testing the 
 *  {@link net.sf.tapestry.valid.IValidator}
 *  implementations.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class TestingField extends AbstractFormComponent implements IField
{
	private String _displayName;
	private Class _valueType;
    private IForm _form;

    public TestingField(String displayName)
    {
        this(displayName, new TestingForm());
    }
    
    public TestingField(String displayName, Class type)
    {
        this(displayName, new TestingForm(), type);
    }

	public TestingField(String displayName, IForm form)
	{
	       this(displayName, form, String.class);
	}

	public TestingField(String displayName, IForm form, Class type)
	{
		_displayName = displayName;
        _form = form;
		_valueType = type;
        
        IPage page = new BasePage();
        page.setLocale(Locale.ENGLISH);
        page.addComponent(this);
        
        setPage(page);
	}
	
	public Class getValueType()
	{
		return _valueType;
	}

	public String getDisplayName()
	{
		return _displayName;
	}

	public String getName()
	{
		return _displayName;
	}


    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
    }

    public IForm getForm()
    {
        return _form;
    }

    public boolean isDisabled()
    {
        return false;
    }

}