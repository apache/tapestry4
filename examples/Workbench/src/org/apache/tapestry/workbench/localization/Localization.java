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

package org.apache.tapestry.workbench.localization;

import java.util.Locale;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Localization extends BasePage
{
	private IPropertySelectionModel localeModel;

	public void formSubmit(IRequestCycle cycle)
	{
		cycle.activate("LocalizationChange");
	}

	public IPropertySelectionModel getLocaleModel()
	{
		if (localeModel == null)
			localeModel = buildLocaleModel();

		return localeModel;
	}

	private IPropertySelectionModel buildLocaleModel()
	{
		LocaleModel model = new LocaleModel(getLocale());

		model.add(Locale.ENGLISH);
		model.add(Locale.FRENCH);
		model.add(Locale.GERMAN);
		model.add(Locale.ITALIAN);

		return model;
	}

}