package net.sf.tapestry.form;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implements a component that manages an HTML &lt;input type=button&gt; form element.
 *
 *  [<a href="../../../../../ComponentReference/Button.html">Component Reference</a>]
 *  
 *  <p>This component is useful for attaching JavaScript onclick event handlers.
 *
 *  @author Howard Lewis Ship
 *  @author Paul Geerts
 *  @author Malcolm Edgar
 *  @version $Id$
 **/
public class Button extends AbstractFormComponent
{
	private String _label;
	private boolean _disabled;
	private IBinding _selectedBinding;
	private String _name;

	public String getName()
	{
		return _name;
	}

	public void setSelectedBinding(IBinding value)
	{
		_selectedBinding = value;
	}

	public IBinding getSelectedBinding()
	{
		return _selectedBinding;
	}

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		IForm form = getForm(cycle);

		boolean rewinding = form.isRewinding();

		_name = form.getElementId(this);

		if (rewinding)
		{
			return;
		}

		writer.beginEmpty("input");
		writer.attribute("type", "button");
		writer.attribute("name", _name);

		if (_disabled)
		{
			writer.attribute("disabled");
		}
		if (_label != null)
		{
			writer.attribute("value", _label);
		}
		generateAttributes(writer, cycle);

		writer.closeTag();
	}

	public String getLabel()
	{
		return _label;
	}

	public void setLabel(String label)
	{
		_label = label;
	}

	public boolean isDisabled()
	{
		return _disabled;
	}

	public void setDisabled(boolean disabled)
	{
		_disabled = disabled;
	}
}
