package tutorial.locale;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.form.*;
import java.util.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class Home
extends BasePage
implements IActionListener
{
    private Locale selectedLocale;
    private IPropertySelectionModel localeModel;

    public void detach()
    {
        selectedLocale = null;
        
        super.detach();
    }

    public Locale getSelectedLocale()
    {
        return selectedLocale;
    }

    public void setSelectedLocale(Locale value)
    {
        selectedLocale = value;
    }

    public IActionListener getFormListener()
    {
        return this;
    }

    public void actionTriggered(IComponent component, IRequestCycle cycle)
    throws RequestCycleException
    {
        getEngine().setLocale(selectedLocale);

        cycle.setPage("Change");
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