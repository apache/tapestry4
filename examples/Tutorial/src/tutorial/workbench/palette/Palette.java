package tutorial.workbench.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.enum.Enum;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.palette.SortMode;
import net.sf.tapestry.form.EnumPropertySelectionModel;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.form.StringPropertySelectionModel;
import net.sf.tapestry.html.BasePage;

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
            ResourceBundle bundle = ResourceBundle.getBundle("tutorial.workbench.palette.SortModeStrings", getLocale());

            Enum[] options = new Enum[] { SortMode.NONE, SortMode.LABEL, SortMode.VALUE, SortMode.USER };

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