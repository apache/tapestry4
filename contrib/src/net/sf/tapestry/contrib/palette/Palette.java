package net.sf.tapestry.contrib.palette;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.components.Block;
import net.sf.tapestry.form.Form;
import net.sf.tapestry.form.FormEventType;
import net.sf.tapestry.form.IFormComponent;
import net.sf.tapestry.form.IPropertySelectionModel;
import net.sf.tapestry.html.Body;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  A component used to make a number of selections from a list.  The general look
 *  is a pair of &lt;select&gt; elements.  with a pair of buttons between them.
 *  The right element is a list of values that can be selected.  The buttons move
 *  values from the right column ("available") to the left column ("selected").
 *
 *  <p>This all takes a bit of JavaScript to accomplish (quite a bit), which means
 *  a {@link Body} component must wrap the Palette. If JavaScript is not enabled
 *  in the client browser, then the user will be unable to make (or change) any selections.
 *
 *  <p>Cross-browser compatibility is not perfect.  In some cases, the 
 *  {@link net.sf.tapestry.contrib.form.MultiplePropertySelection} component
 *  may be a better choice.
 * 
 *  <p><table border=1>
 * <tr>
 *    <td>Parameter</td>
 *    <td>Type</td>
 *    <td>Direction </td>
 *    <td>Required</td>
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>selected</td>
 *  <td>{@link List}</td>
 *  <td>in</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>A List of selected values.  Possible selections are defined by the model; this
 *  should be a subset of the possible values.  This may be null when the
 *  component is renderred.  When the containing form is submitted,
 *  this parameter is updated with a new List of selected objects.
 *
 *  <p>The order may be set by the user, as well, depending on the
 *  sortMode parameter.</td> </tr>
 *
 * <tr>
 * <td>model</td>
 *  <td>{@link IPropertySelectionModel}</td>
 *  <td>in</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>Works, as with a {@link net.sf.tapestry.form.PropertySelection} component, to define the
 *  possible values.
 *  </td> </tr>
 *
 *  <tr>
 *  <td>sort</td> 
 *  <td>{@link SortMode}</td> 
 *  <td>in</td>
 *  <td>no</td> 
 *  <td>{@link SortMode#NONE}</td>
 *  <td>
 *  Controls automatic sorting of the options. </td>
 *  </tr>
 *
 * <tr>
 *  <td>rows</td>
 *  <td>int</td> 
 *  <td>in</td> 
 *  <td>no</td> 
 *  <td>10</td>
 *  <td>The number of rows that should be visible in the Pallete's &lt;select&gt;
 *  elements.
 *  </td> </tr>
 *
 * <tr>
 *  <td>tableClass</td>
 *  <td>{@link String}</td> 
 *  <td>in</td>
 *  <td>no</td> 
 *  <td>tapestry-palette</td>
 *  <td>The CSS class for the table which surrounds the other elements of
 *  the Palette.</td> </tr>
 *
 * <tr>
 *  <td>selectedTitleBlock</td>
 *  <td>{@link Block}</td>
 *  </td>in</td> 
 *  <td>no</td> 
 *  <td>"Selected"</td>
 *  <td>If specified, allows a {@link Block} to be placed within
 *  the &lt;th&gt; reserved for the title above the selected items
 *  &lt;select&gt; (on the right).  This allows for images or other components to
 *  be placed there.  By default, the simple word <code>Selected</code>
 *  is used.</td> </tr>
 *
 * <tr>
 *  <td>availableTitleBlock</td>
 *  <td>{@link Block}</td>
 *  </td>in</td> 
 *  <td>no</td> 
 *  <td>"Available"</td>
 *  <td>As with selectedTitleBlock, but for the left column, of items
 *  which are available to be selected.  The default is the word
 *  <code>Available</code>. </td> </tr>
 *
 *  <tr>
 *  <td>selectImage
 * <br>selectDisabledImage
 * <br>deselectImage
 * <br>deselectDisabledImage
 * <br>upImage
 * <br>upDisabledImage
 * <br>downImage
 * <br>downDisabledImage
 *  </td>
 *  <td>{@link IAsset}</td>
 *  <td>in</td>
 *  <td>no</td> <td>&nbsp;</td>
 *  <td>If any of these are specified then they override the default images provided
 *  with the component.  This allows the look and feel to be customized relatively easily.
 *
 *  <p>The most common reason to replace the images is to deal with backgrounds.  The default
 *  images are anti-aliased against a white background.  If a colored or patterned background
 *  is used, the default images will have an ugly white fringe.  Until all browsers have full
 *  support for PNG (which has a true alpha channel), it is necessary to customize the images
 *  to match the background.
 *
 *      </td> </tr>
 *
 * </table>
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Palette extends BaseComponent implements IFormComponent
{
    private static final int DEFAULT_ROWS = 10;
    private static final int MAP_SIZE = 7;
    private static final String DEFAULT_TABLE_CLASS = "tapestry-palette";

    /** @since 2.2 **/
    private List _selected;
    private IPropertySelectionModel _model;
    private SortMode _sort = SortMode.NONE;
    private int _rows = DEFAULT_ROWS;
    private String _tableClass = DEFAULT_TABLE_CLASS;
    private Block _selectedTitleBlock;
    private Block _availableTitleBlock;
    private IAsset _selectImage;
    private IAsset _selectDisabledImage;
    private IAsset _deselectImage;
    private IAsset _deselectDisabledImage;
    private IAsset _upImage;
    private IAsset _upDisabledImage;
    private IAsset _downImage;
    private IAsset _downDisabledImage;

    /**
     *  {@link IForm} which is currently wrapping the Palette.
     *
     **/

    private IForm _form;

    /**
     *  The element name assigned to this usage of the Palette by the Form.
     *
     **/

    private String _name;

    /**
     *  A set of symbols produced by the Palette script.  This is used to
     *  provide proper names for some of the HTML elements (&lt;select&gt; and
     *  &lt;button&gt; elements, etc.).
     *
     **/

    private Map _symbols;

    /**
     *  Contains the text for the second &lt;select&gt; element, that provides
     *  the available elements.
     *
     **/

    private IMarkupWriter _availableWriter;

    /**
     *  Contains the text for the first &lt;select&gt; element, that
     *  provides the selected elements.
     *
     **/

    private IMarkupWriter _selectedWriter;

    /**
     *  A cached copy of the script used with the component.
     *
     **/

    private IScript _script;

    public void finishLoad()
    {
        _selectedTitleBlock = (Block) getComponent("defaultSelectedTitleBlock");
        _availableTitleBlock = (Block) getComponent("defaultAvailableTitleBlock");

        _selectImage = getAsset("Select");
        _selectDisabledImage = getAsset("SelectDisabled");
        _deselectImage = getAsset("Deselect");
        _deselectDisabledImage = getAsset("DeselectDisabled");
        _upImage = getAsset("Up");
        _upDisabledImage = getAsset("UpDisabled");
        _downImage = getAsset("Down");
        _downDisabledImage = getAsset("DownDisabled");
    }

    /**
     *  Returns the name used for the selected (right column) &lt;select&gt; element.
     **/

    public String getName()
    {
        return _name;
    }

    public IForm getForm()
    {
        return _form;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        _form = Form.get(getPage().getRequestCycle());

        if (_form == null)
            throw new RequestCycleException("Palette component must be wrapped by a Form.", this);

        _name = _form.getElementId(this);

        if (_form.isRewinding())
        {
            handleSubmission(cycle);
            return;
        }

        // Don't do any additional work if rewinding
        // (some other action or form on the page).

        if (cycle.isRewinding())
            return;

        // Lots of work to produce JavaScript and HTML for this sucker.

        String formName = _form.getName();

        _symbols = new HashMap(MAP_SIZE);

        runScript(cycle);

        // Output symbol 'formSubmitFunctionName' is the name
        // of a JavaScript function to execute when the form
        // is submitted.  This is also key to the operation
        // of the PropertySelection.

        _form.addEventHandler(FormEventType.SUBMIT, (String) _symbols.get("formSubmitFunctionName"));

        // Buffer up the HTML for the left and right selects (the available
        // items and the selected items).

        bufferSelects(writer);

        super.renderComponent(writer, cycle);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _availableWriter = null;
        _selectedWriter = null;
        _form = null;
        _symbols = null;

        super.cleanupAfterRender(cycle);
    }

    /**
     *  Executes the associated script, which generates all the JavaScript to
     *  support this Palette.
     *
     **/

    private void runScript(IRequestCycle cycle) throws RequestCycleException
    {
        ScriptSession session;

        // Get the script, if not already gotten.  Scripts are re-entrant, so it is
        // safe to share this between instances of Palette.

        if (_script == null)
        {
            IEngine engine = getPage().getEngine();
            IScriptSource source = engine.getScriptSource();

            _script = source.getScript("/net/sf/tapestry/contrib/palette/Palette.script");
        }

        Body body = Body.get(cycle);
        if (body == null)
            throw new RequestCycleException("Palette component must be wrapped by a Body.", this);

        setImage(body, cycle, "selectImage", _selectImage);
        setImage(body, cycle, "selectDisabledImage", _selectDisabledImage);
        setImage(body, cycle, "deselectImage", _deselectImage);
        setImage(body, cycle, "deselectDisabledImage", _deselectDisabledImage);

         if (_sort == SortMode.USER)
        {
            setImage(body, cycle, "upImage", _upImage);
            setImage(body, cycle, "upDisabledImage", _upDisabledImage);
            setImage(body, cycle, "downImage", _downImage);
            setImage(body, cycle, "downDisabledImage", _downDisabledImage);
        }

        _symbols.put("palette", this);

        try
        {
            session = _script.execute(_symbols);
        }
        catch (ScriptException ex)
        {
            throw new RequestCycleException(this, ex);
        }

        body.process(session);

    }

    /**
     *  Extracts its asset URL, sets it up for
     *  preloading, and assigns the preload reference as a script symbol.
     *
     **/

    private void setImage(Body body, IRequestCycle cycle, String symbolName, IAsset asset)
    {
        String URL = asset.buildURL(cycle);
        String reference = body.getPreloadedImageReference(URL);

        _symbols.put(symbolName, reference);
    }

    public Map getSymbols()
    {
        return _symbols;
    }

    /**
     *  Buffers the two &lt;select&gt;s, each in its own nested {@link IMarkupWriter}.
     *  The idea is to run through the property selection model just once, assigning
     *  each item to one or the other &lt;select&gt;.
     *
     **/

    private void bufferSelects(IMarkupWriter writer)
    {
        // Build a Set around the list of selected items.

        Set selectedSet = _selected == null ? Collections.EMPTY_SET : new HashSet(_selected);

        _selectedWriter = writer.getNestedWriter();
        _availableWriter = writer.getNestedWriter();

        _selectedWriter.begin("select");
        _selectedWriter.attribute("multiple");
        _selectedWriter.attribute("size", _rows);
        _selectedWriter.attribute("name", _name);
        _selectedWriter.println();

        _availableWriter.begin("select");
        _availableWriter.attribute("multiple");
        _availableWriter.attribute("size", _rows);
        _availableWriter.attribute("name", (String) _symbols.get("availableName"));
        _availableWriter.println();

        // Each value specified in the model will go into either the selected or available
        // lists.

        int count = _model.getOptionCount();
        for (int i = 0; i < count; i++)
        {
            IMarkupWriter w = _availableWriter;

            Object optionValue = _model.getOption(i);

            if (selectedSet.contains(optionValue))
                w = _selectedWriter;

            w.beginEmpty("option");
            w.attribute("value", _model.getValue(i));
            w.print(_model.getLabel(i));
            w.println();
        }

        // Close the <select> tags

        _selectedWriter.end();
        _availableWriter.end();
    }

    /**
     *  Renders the available select by closing the nested writer for the available
     *  selects.
     *
     **/

    public IRender getAvailableSelectDelegate()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
            {
                _availableWriter.close();
                _availableWriter = null;
            }
        };
    }

    /**
     *  Like {@link #getAvailableSelectDelegate()}, but for the right, selected, column.
     *
     **/

    public IRender getSelectedSelectDelegate()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
            {
                _selectedWriter.close();
                _selectedWriter = null;
            }
        };
    }

    private void handleSubmission(IRequestCycle cycle) throws RequestCycleException
    {

        RequestContext context = cycle.getRequestContext();
        String[] values = context.getParameters(_name);

        if (values == null || values.length == 0)
            return;

        _selected = new ArrayList(values.length);

        for (int i = 0; i < values.length; i++)
        {
            String value = values[i];
            Object option = _model.translateValue(value);

            _selected.add(option);
        }
    }

    public boolean isSortUser()
    {
        return _sort == SortMode.USER;
    }

    /**
     *  Returns null, but may make sense to implement a displayName parameter.
     * 
     **/

    public String getDisplayName()
    {
        return null;
    }

    public Block getAvailableTitleBlock()
    {
        return _availableTitleBlock;
    }

    public void setAvailableTitleBlock(Block availableTitleBlock)
    {
        _availableTitleBlock = availableTitleBlock;
    }

    public IAsset getDeselectDisabledImage()
    {
        return _deselectDisabledImage;
    }

    public void setDeselectDisabledImage(IAsset deselectDisabledImage)
    {
        _deselectDisabledImage = deselectDisabledImage;
    }

    public IAsset getDeselectImage()
    {
        return _deselectImage;
    }

    public void setDeselectImage(IAsset deselectImage)
    {
        _deselectImage = deselectImage;
    }

    public IAsset getDownDisabledImage()
    {
        return _downDisabledImage;
    }

    public void setDownDisabledImage(IAsset downDisabledImage)
    {
        _downDisabledImage = downDisabledImage;
    }

    public IAsset getDownImage()
    {
        return _downImage;
    }

    public void setDownImage(IAsset downImage)
    {
        _downImage = downImage;
    }

    public IPropertySelectionModel getModel()
    {
        return _model;
    }

    public void setModel(IPropertySelectionModel model)
    {
        _model = model;
    }

    public int getRows()
    {
        return _rows;
    }

    public void setRows(int rows)
    {
        _rows = rows;
    }

    public IAsset getSelectDisabledImage()
    {
        return _selectDisabledImage;
    }

    public void setSelectDisabledImage(IAsset selectDisabledImage)
    {
        _selectDisabledImage = selectDisabledImage;
    }

    public Block getSelectedTitleBlock()
    {
        return _selectedTitleBlock;
    }

    public void setSelectedTitleBlock(Block selectedTitleBlock)
    {
        _selectedTitleBlock = selectedTitleBlock;
    }

    public IAsset getSelectImage()
    {
        return _selectImage;
    }

    public void setSelectImage(IAsset selectImage)
    {
        _selectImage = selectImage;
    }

    public SortMode getSort()
    {
        return _sort;
    }

    public void setSort(SortMode sort)
    {
        _sort = sort;
    }

    public String getTableClass()
    {
        return _tableClass;
    }

    public void setTableClass(String tableClass)
    {
        _tableClass = tableClass;
    }

    public IAsset getUpDisabledImage()
    {
        return _upDisabledImage;
    }

    public void setUpDisabledImage(IAsset upDisabledImage)
    {
        _upDisabledImage = upDisabledImage;
    }

    public IAsset getUpImage()
    {
        return _upImage;
    }

    public void setUpImage(IAsset upImage)
    {
        _upImage = upImage;
    }

    /**
     *  Returns false.  Palette components are never disabled.
     * 
     *  @since 2.2
     * 
     **/

    public boolean isDisabled()
    {
        return false;
    }

    /** @since 2.2 **/

    public List getSelected()
    {
        return _selected;
    }

    /**  @since 2.2 **/

    public void setSelected(List selected)
    {
        _selected = selected;
    }

}