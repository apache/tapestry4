package tutorial.adder;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import java.util.*;

public class Home extends BasePage
{
	private List items;
	private String newValue;
	private String error;


	public Home(IApplication application, ComponentSpecification specification)
	{
		super(application, specification);
	}

	public List getItems()
	{
		return items;
	}

	public void setItems(List value)
	{
		items = value;

		fireObservedChange("items", value);
	}

	public void setNewValue(String value)
	{
		newValue = value;
	}
	
	public String getNewValue()
	{
		return newValue;
	}

	public void detachFromApplication()
	{
		items = null;
		newValue = null;
		error = null;

		super.detachFromApplication();
	}

	public void addItem(double value)
	{
		if (items == null)
		{
			items = new ArrayList();
			fireObservedChange("items", items);
		}

		items.add(new Double(value));

		fireObservedChange();
	}

	public double getTotal()
	{
		Iterator i;
		Double item;
		double result = 0.0;

		if (items != null)
		{
			i = items.iterator();
			while (i.hasNext())
			{
				item = (Double)i.next();
				result += item.doubleValue();
			}
		}

		return result;
	}

	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				try
				{
					double item = Double.parseDouble(newValue);
					addItem(item);
					
					newValue = null;
				}
				catch (NumberFormatException e)
				{
					error = "Please enter a valid number.";
				}
			}
		};

	}

	public String getError()
	{
		return error;
	}
}

