package tutorial.forms;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.form.StringPropertySelectionModel;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.valid.ValidationDelegate;

/**
 * Example code for the forms part of Tutorial
 * @author Neil Clayton
 */
public class Part4 extends BasePage {
	public void enterDetails(IRequestCycle cycle) throws RequestCycleException {
		// Submission has been performed. Validate the fields
		ValidationDelegate delegate = (ValidationDelegate) getBeans().getBean("delegate");
		if (delegate.getHasErrors()) {
			// there are errors
			return;
		}

		cycle.setPage("Success");
	}

	/**
	 * Returns a set of colours that the user may choose from.
	 */
	public static IPropertySelectionModel getColourModel() {
		return new StringPropertySelectionModel(
			new String[] { "Black", "Fiji Blue", "Green", "Red", "Yellow" });
	}
}
