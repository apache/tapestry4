package tutorial.workbench.localization;

import java.util.Locale;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.html.BasePage;

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
		cycle.setPage("LocalizationChange");
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