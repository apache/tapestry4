package tutorial.events;

import java.text.DateFormat;
import java.util.Date;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;

/**
 * Example code for the event handling section of the tutorial
 * @author neil clayton
 */
public class Home extends BasePage {
	/**
	 * Called by both the "direct" and "action" components
	 */
	public void timeListener(IRequestCycle cycle) throws RequestCycleException {
		System.err.println("TIME LISTENER METHOD CALLED");
		pageTime = DateFormat.getDateTimeInstance().format(new Date());
	}
	
	public String getPageTime() {
		return pageTime;
	}
	
	/**
	 * @see net.sf.tapestry.AbstractPage#detach()
	 */
	public void detach() {
		pageTime = null;
	}

	private String pageTime;
}
