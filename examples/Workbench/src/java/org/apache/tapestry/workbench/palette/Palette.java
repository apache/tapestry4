// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;

/**
 * @author Howard Lewis Ship
 */

public abstract class Palette extends BasePage
{
    public abstract List getSelectedColors();

    public abstract String getSort();

    public abstract IValidationDelegate getDelegate();
    
    private IPropertySelectionModel _sortModel;

    /**
     * Invoked before {@link #formSubmit(IRequestCycle)} if the user clicks the "advance" button.
     */

    @InjectPage("PaletteResults")
    public abstract PaletteResults getResultsPage();

    public IPage advance()
    {
        if (getDelegate().getHasErrors()) return null;
        
        // Since Palette and palette.Results come from
        // a library now, we need to make sure
        // the namespace id is part of the name.

        PaletteResults results = getResultsPage();

        results.setSelectedColors(getSelectedColors());

        return results;
    }

    private IPropertySelectionModel colorModel;

    private String[] colors =
    { "Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet" };

    public IPropertySelectionModel getColorModel()
    {
        if (colorModel == null)
            colorModel = new StringPropertySelectionModel(colors);

        return colorModel;
    }

    public IPropertySelectionModel getSortModel()
    {
        if (_sortModel == null)
        {
            String[] options = new String[]
            { SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER };

            _sortModel = new StringPropertySelectionModel(options);
        }

        return _sortModel;
    }
}