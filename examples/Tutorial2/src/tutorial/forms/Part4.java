//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
