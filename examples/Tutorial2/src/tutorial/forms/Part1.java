package tutorial.forms;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.html.BasePage;

/**
 * Example code for the forms part of Tutorial
 * @author Neil Clayton
 */
public class Part1 extends BasePage {
	public void enterDetails(IRequestCycle cycle) throws RequestCycleException {
		// Submission has been performed. Do something here.
		cycle.setPage("Success");
	}
	
}
