package tutorial.portal;

import com.primix.tapestry.*;

/**
 *  This is much simpler than the others, and mostly is here to demonstrate
 *  how to include {@link com.primix.tapestry.link.Direct} links in a Portlet
 *  content.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Weather 
	extends BasePage
{
	private boolean weekend;
	
	public void detach()
	{
		weekend = false;
		
		super.detach();
	}
	
	public void setWeekend(boolean value)
	{
		weekend = value;
		fireObservedChange("weekend", value);
	}
	
	public boolean isWeekend()
	{
		return weekend;
	}
	
	public String getForecast()
	{
		if (weekend)
			return "Dismal, pelting rain, 47 - 52.  Enjoy your time off.";
		
		return "Sunny, bright, 76 - 82.  Now, get back to your cube.";
	}
	
	/**
	 *  The context is '1' for the 'weekend' link, or '0' otherwise.
	 *
	 */
	
	private void update(String context, IRequestCycle cycle)
	{
		setWeekend(context.equals("1"));
		
		// Here's the kicker; this page, Weather, should not render the response.
		// The response should be rendered by the Home page.  In fact, this page,
		// outside of the 'content' block, has not actual content.
		
		cycle.setPage("Home");
	}
	
	public IDirectListener getLinkListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IDirect direct, String[] context, 
					IRequestCycle cycle)
				throws RequestCycleException
			{
				update(context[0], cycle);
			}
		};
	}
	
	public String getWeekdayClass()
	{
		return weekend ? null : "selected";
	}
	
	public String getWeekendClass()
	{
		return weekend ? "selected" : null;
	}
}
