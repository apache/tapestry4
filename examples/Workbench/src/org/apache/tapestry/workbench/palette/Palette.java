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

package org.apache.tapestry.workbench.palette;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.EnumPropertySelectionModel;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.commons.lang.enum.Enum;

/**
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class Palette extends BasePage
{
    private List _selectedColors;

    private SortMode _sort = SortMode.USER;

    private IPropertySelectionModel _sortModel;

    public void initialize()
    {
        _sort = SortMode.USER;
        _selectedColors = null;
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
        // Since Palette and palette.Results come from
        // a library now, we need to make sure
        // the namespace id is part of the name.

        PaletteResults results = (PaletteResults) cycle.getPage("PaletteResults");

        results.setSelectedColors(_selectedColors);

        cycle.activate(results);
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
        _sort = value;

        fireObservedChange("sort", value);
    }

    public SortMode getSort()
    {
        return _sort;
    }

    public IPropertySelectionModel getSortModel()
    {
        if (_sortModel == null)
        {
            String packageName = getClass().getPackage().getName();
            
            ResourceBundle bundle =
                ResourceBundle.getBundle(packageName + ".SortModeStrings", getLocale());

            Enum[] options =
                new Enum[] { SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER };

            _sortModel = new EnumPropertySelectionModel(options, bundle);
        }

        return _sortModel;
    }

    public List getSelectedColors()
    {
        return _selectedColors;
    }

    public void setSelectedColors(List selectedColors)
    {
        _selectedColors = selectedColors;
    }

}