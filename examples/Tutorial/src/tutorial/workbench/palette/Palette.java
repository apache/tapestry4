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

package tutorial.workbench.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.palette.SortMode;
import net.sf.tapestry.form.EnumPropertySelectionModel;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.form.StringPropertySelectionModel;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.util.Enum;

/**
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Palette extends BasePage
{
    private List selectedColors;

    private SortMode sort = SortMode.USER;

    private IPropertySelectionModel sortModel;

    public void detach()
    {
        sort = SortMode.USER;
        selectedColors = null;

        super.detach();
    }

    public void formSubmit(IRequestCycle cycle)
    {
        // Does nothing ... may be invoked because
        // the user changed the sort
    }

    /**
     *  Invoked before {@link #formSubmit(IRequestCycle)} if the
     *  user clicks the "advance" button.
     * 
     **/

    public void advance(IRequestCycle cycle)
    {
        Results results = (Results) cycle.getPage("palette.Results");

        results.setSelectedColors(selectedColors);

        cycle.setPage(results);
    }

    private IPropertySelectionModel colorModel;

    private String[] colors = { "Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet" };

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
                ResourceBundle.getBundle("tutorial.workbench.palette.SortModeStrings", getLocale());

            Enum[] options = new Enum[] { SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER };

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

}