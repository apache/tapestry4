//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
public class Part4 extends BasePage
{
	public void enterDetails(IRequestCycle cycle) throws RequestCycleException
	{
		// Submission has been performed. Validate the fields
		ValidationDelegate delegate = (ValidationDelegate) getBeans().getBean("delegate");
		if (delegate.getHasErrors())
		{
			// there are errors
			return;
		}

		cycle.setPage("Success");
	}

	/**
	 * Returns a set of colours that the user may choose from.
	 */
	public static IPropertySelectionModel getColourModel()
	{
		return new StringPropertySelectionModel(
				new String[]{"Black", "Fiji Blue", "Green", "Red", "Yellow"});
	}
}
