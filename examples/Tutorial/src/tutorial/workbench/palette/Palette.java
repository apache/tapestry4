/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
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

package tutorial.workbench.palette;

import java.util.*;
import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.valid.*;
import com.primix.tapestry.util.*;
import java.math.*;
import net.sf.tapestry.contrib.palette.*;

/**
 *  @version $Id$
 *  @author Howard Ship
 *
 */ 

public class Palette extends BasePage
{
	private List selectedColors;

	/**
	 *  Set the true when the user clicks the "continue" button.
	 *
	 */
	
	private boolean advance;
	
	private SortMode sort = SortMode.USER;

	private IPropertySelectionModel sortModel;
		
	public void detach()
	{
		sort = SortMode.USER;
		advance = false;
		selectedColors = null;
		
		super.detach();
	}
	
			
	public void formSubmit(IRequestCycle cycle)
	{
		if (advance)
		{
			Results results = (Results)cycle.getPage("palette.Results");
		
			results.setSelectedColors(selectedColors);
		
			cycle.setPage(results);
		}
	}
	
	private IPropertySelectionModel colorModel;
	
	private String[] colors = 
	{
		"Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet"
	};
	
	public IPropertySelectionModel getColorModel()
	{
		if (colorModel == null)
			colorModel = new StringPropertySelectionModel(colors);
		
		return colorModel;
	}
	
	public void setSort(SortMode value)
	{
		sort = value;
		
		fireObservedChange("sort", value);
	}
	
	public SortMode getSort()
	{
		return sort;
	}
	
	public IPropertySelectionModel getSortModel()
	{
		if (sortModel == null)
		{
			ResourceBundle bundle = 
				ResourceBundle.getBundle("tutorial.workbench.palette.SortModeStrings",
					getLocale());
			
			Enum[] options = new Enum[]
			{
				SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER
			};
			
			sortModel = new EnumPropertySelectionModel(options, bundle);
		}
	
		return sortModel;
	}
	
	public List getSelectedColors()
	{
		if (selectedColors == null)
			selectedColors = new ArrayList();
		
		return selectedColors;
	}
	
	public boolean getAdvance()
	{
		return advance;
	}
	
	public void setAdvance(boolean value)
	{
		advance = value;
	}
}
