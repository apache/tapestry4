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

package tutorial.workbench.localization;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import java.util.*;


/**
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class LocaleModel
implements IPropertySelectionModel
{
    private Locale locale;
    private List locales = new ArrayList();

    public LocaleModel(Locale locale)
    {
        this.locale = locale;
    }

    public void add(Locale locale)
    {
        locales.add(locale);
    }

    private Locale get(int index)
    {
        return (Locale)locales.get(index);
    }

    public String getLabel(int index)
    {
        return get(index).getDisplayName(locale);
    }

    public int getOptionCount()
    {
        return locales.size();
    }


	public Object getOption(int index)
	{
		return locales.get(index);
	}

    /**
     *  Returns the String version of the integer index.
     *
     */

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public Object translateValue(String value)
    {
        int index = Integer.parseInt(value);

        return locales.get(index);
    }
}
