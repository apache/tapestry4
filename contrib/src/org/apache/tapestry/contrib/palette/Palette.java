/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.contrib.palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.valid.IValidationDelegate;

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
 *  {@link org.apache.tapestry.contrib.form.MultiplePropertySelection} component
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
 *  <td>Works, as with a {@link org.apache.tapestry.form.PropertySelection} component, to define the
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
 *  <td>in</td> 
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
 *  <td>in</td> 
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
 * <p>A Palette requires some CSS entries to render correctly ... especially
 * the middle column, which contains the two or four buttons for moving selections
 * between the two columns.  The width and alignment of this column must be set
 * using CSS.  Additionally, CSS is commonly used to give the Palette columns
 * a fixed width, and to dress up the titles.  Here is an example of some CSS
 * you can use to format the palette component:
 * 
 * <pre>
 * TABLE.tapestry-palette TH
 * {
 *   font-size: 9pt;
 *   font-weight: bold;
 *   color: white;
 *   background-color: #330066;
 *   text-align: center;
 * }
 *
 * TD.available-cell SELECT
 * {
 *   font-weight: normal;
 *   background-color: #FFFFFF;
 *   width: 200px;
 * }
 * 
 * TD.selected-cell SELECT
 * {
 *   font-weight: normal;
 *   background-color: #FFFFFF;
 *   width: 200px;
 * }
 * 
 * TABLE.tapestry-palette TD.controls
 * {
 *   text-align: center;
 *   vertical-align: middle;
 *   width: 60px;
 * }
 *  </pre>
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Palette extends BaseComponent implements IFormComponent
{
    private static final int DEFAULT_ROWS = 10;
    private static final int MAP_SIZE = 7;
    private static final String DEFAULT_TABLE_CLASS = "tapestry-palette";

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

    protected void finishLoad()
    {
        setSelectedTitleBlock((Block) getComponent("defaultSelectedTitleBlock"));
        setAvailableTitleBlock((Block) getComponent("defaultAvailableTitleBlock"));

        setSelectImage(getAsset("Select"));
        setSelectDisabledImage(getAsset("SelectDisabled"));
        setDeselectImage(getAsset("Deselect"));
        setDeselectDisabledImage(getAsset("DeselectDisabled"));
        setUpImage(getAsset("Up"));
        setUpDisabledImage(getAsset("UpDisabled"));
        setDownImage(getAsset("Down"));
        setDownDisabledImage(getAsset("DownDisabled"));

        setTableClass(DEFAULT_TABLE_CLASS);
        setRows(DEFAULT_ROWS);
        setSort(SortMode.NONE);
    }

    public abstract String getName();
    public abstract void setName(String name);

    public abstract IForm getForm();
    public abstract void setForm(IForm form);

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = Form.get(getPage().getRequestCycle());

        if (form == null)
            throw new ApplicationRuntimeException(
                "Palette component must be wrapped by a Form.",
                this,
                null,
                null);

        setForm(form);

        IValidationDelegate delegate = form.getDelegate();

        if (delegate != null)
            delegate.setFormComponent(this);

        setName(form.getElementId(this));

        if (form.isRewinding())
            handleSubmission(cycle);

        // Don't do any additional work if rewinding
        // (some other action or form on the page).

        if (!cycle.isRewinding())
        {
            // Lots of work to produce JavaScript and HTML for this sucker.

            _symbols = new HashMap(MAP_SIZE);

            runScript(cycle);

            // Output symbol 'formSubmitFunctionName' is the name
            // of a JavaScript function to execute when the form
            // is submitted.  This is also key to the operation
            // of the PropertySelection.

            form.addEventHandler(
                FormEventType.SUBMIT,
                (String) _symbols.get("formSubmitFunctionName"));

            // Buffer up the HTML for the left and right selects (the available
            // items and the selected items).

            bufferSelects(writer);
        }

        super.renderComponent(writer, cycle);
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _availableWriter = null;
        _selectedWriter = null;
        _symbols = null;

        super.cleanupAfterRender(cycle);
    }

    /**
     *  Executes the associated script, which generates all the JavaScript to
     *  support this Palette.
     *
     **/

    private void runScript(IRequestCycle cycle)
    {
        // Get the script, if not already gotten.  Scripts are re-entrant, so it is
        // safe to share this between instances of Palette.

        if (_script == null)
        {
            IEngine engine = getPage().getEngine();
            IScriptSource source = engine.getScriptSource();

            IResourceLocation scriptLocation =
                getSpecification().getSpecificationLocation().getRelativeLocation("Palette.script");

            _script = source.getScript(scriptLocation);
        }

        Body body = Body.get(cycle);
        if (body == null)
            throw new ApplicationRuntimeException(
                "Palette component must be wrapped by a Body.",
                this,
                null,
                null);

        setImage(body, cycle, "selectImage", getSelectImage());
        setImage(body, cycle, "selectDisabledImage", getSelectDisabledImage());
        setImage(body, cycle, "deselectImage", getDeselectImage());
        setImage(body, cycle, "deselectDisabledImage", getDeselectDisabledImage());

        if (isSortUser())
        {
            setImage(body, cycle, "upImage", getUpImage());
            setImage(body, cycle, "upDisabledImage", getUpDisabledImage());
            setImage(body, cycle, "downImage", getDownImage());
            setImage(body, cycle, "downDisabledImage", getDownDisabledImage());
        }

        _symbols.put("palette", this);

        _script.execute(cycle, body, _symbols);
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

        List selected = getSelected();

        Set selectedSet = selected == null ? Collections.EMPTY_SET : new HashSet(selected);

        _selectedWriter = writer.getNestedWriter();
        _availableWriter = writer.getNestedWriter();

        _selectedWriter.begin("select");
        _selectedWriter.attribute("multiple", "multiple");
        _selectedWriter.attribute("size", getRows());
        _selectedWriter.attribute("name", getName());
        _selectedWriter.println();

        _availableWriter.begin("select");
        _availableWriter.attribute("multiple", "multiple");
        _availableWriter.attribute("size", getRows());
        _availableWriter.attribute("name", (String) _symbols.get("availableName"));
        _availableWriter.println();

        // Each value specified in the model will go into either the selected or available
        // lists.

        IPropertySelectionModel model = getModel();

        int count = model.getOptionCount();
        for (int i = 0; i < count; i++)
        {
            IMarkupWriter w = _availableWriter;

            Object optionValue = model.getOption(i);

            if (selectedSet.contains(optionValue))
                w = _selectedWriter;

            w.begin("option");
            w.attribute("value", model.getValue(i));
            w.print(model.getLabel(i));
            w.end(); // <option>
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
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                if (_availableWriter != null)
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
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                if (_selectedWriter != null)
                    _selectedWriter.close();
                _selectedWriter = null;
            }
        };
    }

    private void handleSubmission(IRequestCycle cycle)
    {

        RequestContext context = cycle.getRequestContext();
        String[] values = context.getParameters(getName());

        int count = Tapestry.size(values);

        if (count == 0)
            return;

        List selected = new ArrayList(count);
        IPropertySelectionModel model = getModel();

        for (int i = 0; i < count; i++)
        {
            String value = values[i];
            Object option = model.translateValue(value);

            selected.add(option);
        }

        setSelected(selected);
    }

    public boolean isSortUser()
    {
        return getSort() == SortMode.USER;
    }

    /**
     *  Returns null, but may make sense to implement a displayName parameter.
     * 
     **/

    public String getDisplayName()
    {
        return null;
    }

    public abstract Block getAvailableTitleBlock();

    public abstract void setAvailableTitleBlock(Block availableTitleBlock);

    public abstract IAsset getDeselectDisabledImage();

    public abstract void setDeselectDisabledImage(IAsset deselectDisabledImage);

    public abstract IAsset getDeselectImage();

    public abstract void setDeselectImage(IAsset deselectImage);

    public abstract IAsset getDownDisabledImage();

    public abstract void setDownDisabledImage(IAsset downDisabledImage);

    public abstract IAsset getDownImage();

    public abstract void setDownImage(IAsset downImage);

    public abstract IPropertySelectionModel getModel();

    public abstract int getRows();

    public abstract void setRows(int rows);

    public abstract IAsset getSelectDisabledImage();

    public abstract void setSelectDisabledImage(IAsset selectDisabledImage);

    public abstract Block getSelectedTitleBlock();

    public abstract void setSelectedTitleBlock(Block selectedTitleBlock);

    public abstract IAsset getSelectImage();

    public abstract void setSelectImage(IAsset selectImage);

    public abstract SortMode getSort();

    public abstract void setSort(SortMode sort);

    public abstract void setTableClass(String tableClass);

    public abstract IAsset getUpDisabledImage();

    public abstract void setUpDisabledImage(IAsset upDisabledImage);

    public abstract IAsset getUpImage();

    public abstract void setUpImage(IAsset upImage);

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

    public abstract List getSelected();

    /**  @since 2.2 **/

    public abstract void setSelected(List selected);

}