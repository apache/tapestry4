/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.contrib.palette;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

import net.sf.tapestry.*;
import net.sf.tapestry.components.*;
import net.sf.tapestry.form.*;
import net.sf.tapestry.html.*;
import net.sf.tapestry.spec.*;

import com.primix.tapestry.html.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.spec.*;
import java.util.*;

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
 *  <p><table border=1>
 * <tr>
 *    <td>Parameter</td>
 *    <td>Type</td>
 *    <td>Read / Write </td>
 *    <td>Required</td>
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *    <td>selected</td>
 *  <td>{@link List}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>A list of selected values.  Possible selections are defined by the model; this
 *  should be a subset of the possible values.  The Set must be writable; when
 *  the form is submitted, the set will be cleared and filled with the
 *  values selected by the user.
 *
 *  <p>The order may be set by the user, as well, depending on the
 *  sortMode parameter.</td> </tr>
 *
 * <tr>
 * <td>model</td>
 *  <td>{@link IPropertySelectionModel}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>Works, as with a {@link PropertySelection} component, to define the
 *  possible values.
 *  </td> </tr>
 *
 * <tr>
 *  <td>sort</td> <td>{@link SortMode}</td> <td>R</td>
 * <td>no</td> <td>{@link SortMode#NONE}</td>
 *  <td>
 *  Controls automatic sorting of the options. </td>
 * </tr>
 *
 * <tr>
 *  <td>rows</td>
 * <td>int</td> <td>R</td> <td>no</td> <td>10</td>
 * <td>The number of rows that should be visible in the Pallete's &lt;select&gt;
 *  elements.
 *  </td> </tr>
 *
 * <tr>
 *  <td>tableClass</td>
 *  <td>{@link String}</td> <td>R</td>
 *  <td>no</td> <td>tapestry-palette</td>
 *  <td>The CSS class for the table which surrounds the other elements of
 *  the Palette.</td> </tr>
 *
 * <tr>
 *  <td>selectedTitleBlock</td>
 *  <td>{@link Block}</td>
 *  </td>R</td> <td>no</td> <td>"Selected"</td>
 *  <td>If specified, allows a {@link Block} to be placed within
 *  the &lt;th&gt; reserved for the title above the selected items
 *  &lt;select&gt; (on the right).  This allows for images or other components to
 * be placed there.  By default, the simple word <code>Selected</code>
 *  is used.</td> </tr>
 *
 * <tr>
 *  <td>availableTitleBlock</td>
 *  <td>{@link Block}</td>
 *  </td>R</td> <td>no</td> <td>"Available"</td>
 *  <td>As with selectedTitleBlock, but for the left column, of items
 *  which are available to be selected.  The default is the word
 *  <code>Available</code>. </td> </tr>
 *
 * <tr>
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
 *  <td>R</td>
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
 *  @author Howard Ship
 *  @version $Id$
 */

public class Palette extends BaseComponent implements IFormComponent
{
	private static final int DEFAULT_ROWS = 10;
	private static final int MAP_SIZE = 7;
	private static final String DEFAULT_TABLE_CLASS = "tapestry-palette";

	private IBinding tableClassBinding;
	private String staticTableClass;

	private IBinding selectedTitleBlockBinding;
	private IBinding availableTitleBlockBinding;

	private IBinding selectedBinding;
	private IBinding modelBinding;

	private IBinding rowsBinding;
	private int staticRows;

	private IBinding sortBinding;
	private SortMode staticSort;
	private SortMode sort;

	private Block defaultSelectedTitleBlock;
	private Block defaultAvailableTitleBlock;

	/**
	 *  {@link IForm} which is currently wrapping the Palette.
	 *
	 */

	private IForm form;

	/**
	 *  The element name assigned to this usage of the Palette by the Form.
	 *
	 */

	private String name;

	/**
	 *  A set of symbols produced by the Palette script.  This is used to
	 *  provide proper names for some of the HTML elements (&lt;select&gt; and
	 *  &lt;button&gt; elements, etc.).
	 *
	 */

	private Map symbols;

	/**
	 *  Contains the text for the second &lt;select&gt; element, that provides
	 *  the available elements.
	 *
	 */

	private IMarkupWriter availableWriter;

	/**
	 *  Contains the text for the first &lt;select&gt; element, that
	 *  provides the selected elements.
	 *
	 */

	private IMarkupWriter selectedWriter;

	/**
	 *  A cached copy of the script used with the component.
	 *
	 */

	private IScript script;

	public void finishLoad(IPageLoader loader, ComponentSpecification spec)
		throws PageLoaderException
	{
		defaultSelectedTitleBlock = (Block) getComponent("defaultSelectedTitleBlock");
		defaultAvailableTitleBlock = (Block) getComponent("defaultAvailableTitleBlock");

		super.finishLoad(loader, spec);
	}

	public void setTableClassBinding(IBinding value)
	{
		tableClassBinding = value;

		if (value.isStatic())
			staticTableClass = value.getString();
	}

	public IBinding getTableClassBinding()
	{
		return tableClassBinding;
	}

	public void setSelectedTitleBlockBinding(IBinding value)
	{
		selectedTitleBlockBinding = value;
	}

	public IBinding getSelectedTitleBlockBinding()
	{
		return selectedTitleBlockBinding;
	}

	public void setAvailableTitleBlockBinding(IBinding value)
	{
		availableTitleBlockBinding = value;
	}

	public IBinding getAvailableTitleBlockBinding()
	{
		return availableTitleBlockBinding;
	}

	public void setSelectedBinding(IBinding value)
	{
		selectedBinding = value;
	}

	public IBinding getSelectedBinding()
	{
		return selectedBinding;
	}

	public void setModelBinding(IBinding value)
	{
		modelBinding = value;
	}

	public IBinding getModelBinding()
	{
		return modelBinding;
	}

	public void setRowsBinding(IBinding value)
	{
		rowsBinding = value;

		if (rowsBinding.isStatic())
			staticRows = value.getInt();
	}

	public IBinding getRowsBinding()
	{
		return rowsBinding;
	}

	public int getRows()
	{
		int result = staticRows;

		if (result == 0 && rowsBinding != null)
			result = rowsBinding.getInt();

		if (result == 0)
			result = DEFAULT_ROWS;

		return result;
	}

	public String getTableClass()
	{
		if (staticTableClass != null)
			return staticTableClass;

		if (tableClassBinding == null)
			return DEFAULT_TABLE_CLASS;

		String result = tableClassBinding.getString();

		if (result == null)
			return DEFAULT_TABLE_CLASS;
		else
			return result;
	}

	public void setSortBinding(IBinding value)
	{
		sortBinding = value;

		if (value.isStatic())
			staticSort = (SortMode) value.getObject("sort", SortMode.class);
	}

	public IBinding getSortBinding()
	
	{
		return sortBinding;
	}

	public SortMode getSort()
	{
		return sort;
	}

	/**
	 *  Returns the name used for the selected (right column) &lt;select&gt; element.
	 */

	public String getName()
	{
		return name;
	}

	public IForm getForm()
	{
		return form;
	}

	public void render(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		form = Form.get(page.getRequestCycle());

		if (form == null)
			throw new RequestCycleException(
				"Palette component must be wrapped by a Form.",
				this);

		name = form.getElementId(this);

		if (form.isRewinding())
		{
			handleSubmission(cycle);
			return;
		}

		// Don't do any additional work if rewinding
		// (some other action or form on the page).

		if (cycle.isRewinding())
			return;

		sort = staticSort;

		if (sort == null && sortBinding != null)
			sort = (SortMode) sortBinding.getObject("sort", SortMode.class);

		if (sort == null)
			sort = SortMode.NONE;

		// Lots of work to produce JavaScript and HTML for this sucker.

		String formName = form.getName();

		if (symbols == null)
			symbols = new HashMap(MAP_SIZE);
		else
			symbols.clear();

		symbols.put("formName", formName);
		symbols.put("name", name);

		runScript(cycle, symbols);

		// Output symbol 'formSubmitFunctionName' is the name
		// of a JavaScript function to execute when the form
		// is submitted.  This is also key to the operation
		// of the PropertySelection.

		form.addEventHandler(
			FormEventType.SUBMIT,
			(String) symbols.get("formSubmitFunctionName"));

		// Buffer up the HTML for the left and right selects (the available
		// items and the selected items).

		bufferSelects(writer);

		try
		
			{
			super.render(writer, cycle);
		}
		finally
		{
			availableWriter = null;
			selectedWriter = null;
			form = null;
			sort = null;

			symbols.clear();
		}
	}

	/**
	 *  Executes the associated script, which generates all the JavaScript to
	 *  support this Palette.
	 *
	 */

	private void runScript(IRequestCycle cycle, Map symbols)
		throws RequestCycleException
	{
		ScriptSession session;

		// Get the script, if not already gotten.  Scripts are re-entrant, so it is
		// safe to share this between instances of Palette.

		if (script == null)
		{
			IEngine engine = getPage().getEngine();
			IScriptSource source = engine.getScriptSource();

			try
			{
				script = source.getScript("/net/sf/tapestry/contrib/palette/Palette.script");
			}
			catch (ResourceUnavailableException ex)
			{
				throw new RequestCycleException(this, ex);
			}
		}

		Body body = Body.get(cycle);
		if (body == null)
			throw new RequestCycleException(
				"Palette component must be wrapped by a Body.",
				this);

		setImage(symbols, body, cycle, "selectImage", "Select");
		setImage(symbols, body, cycle, "selectDisabledImage", "Select-dis");
		setImage(symbols, body, cycle, "deselectImage", "Deselect");
		setImage(symbols, body, cycle, "deselectDisabledImage", "Deselect-dis");

		if (sort == SortMode.LABEL)
			symbols.put("sortLabel", Boolean.TRUE);

		if (sort == SortMode.VALUE)
			symbols.put("sortValue", Boolean.TRUE);

		if (sort == SortMode.USER)
		{
			symbols.put("sortUser", Boolean.TRUE);
			setImage(symbols, body, cycle, "upImage", "Up");
			setImage(symbols, body, cycle, "upDisabledImage", "Up-dis");
			setImage(symbols, body, cycle, "downImage", "Down");
			setImage(symbols, body, cycle, "downDisabledImage", "Down-dis");
		}

		try
		{
			session = script.execute(symbols);
		}
		catch (ScriptException ex)
		{
			throw new RequestCycleException(this, ex);
		}

		body.process(session);

	}

	private IAsset getAsset(String symbolName, String assetName)
	{
		IAsset result = null;
		IBinding binding = getBinding(symbolName);

		if (binding != null)
			result = (IAsset) binding.getObject(symbolName, IAsset.class);

		if (result == null)
			result = getAsset(assetName);

		return result;
	}

	/**
	 *  Gets the asset (first looking for a parameter with the symbolName, then
	 *  using the default asset), extracts its URL, sets it up for
	 *  preloading, and assigns the preload reference as a script symbol.
	 *
	 */

	private void setImage(
		Map symbols,
		Body body,
		IRequestCycle cycle,
		String symbolName,
		String assetName)
	
	{
		IAsset asset = getAsset(symbolName, assetName);

		String URL = asset.buildURL(cycle);
		String reference = body.getPreloadedImageReference(URL);

		symbols.put(symbolName, reference);
	}

	public Map getSymbols()
	{
		return symbols;
	}

	/**
	 *  Buffers the two &lt;select&gt;s, each in its own nested {@link IMarkupWriter}.
	 *  The idea is to run through the property selection model just once, assigning
	 *  each item to one or the other &lt;select&gt;.
	 *
	 */

	private void bufferSelects(IMarkupWriter writer)
	{
		List selected = (List) selectedBinding.getObject("selected", List.class);
		IPropertySelectionModel model =
			(IPropertySelectionModel) modelBinding.getObject(
				"model",
				IPropertySelectionModel.class);

		int rows = getRows();

		// Build a Set around the list of selected items.

		Set selectedSet = new HashSet(selected);

		selectedWriter = writer.getNestedWriter();
		availableWriter = writer.getNestedWriter();

		selectedWriter.begin("select");
		selectedWriter.attribute("multiple");
		selectedWriter.attribute("size", rows);
		selectedWriter.attribute("name", name);
		selectedWriter.println();

		availableWriter.begin("select");
		availableWriter.attribute("multiple");
		availableWriter.attribute("size", rows);
		availableWriter.attribute("name", (String) symbols.get("availableName"));
		availableWriter.println();

		// Each value specified in the model will go into either the selected or available
		// lists.

		int count = model.getOptionCount();
		for (int i = 0; i < count; i++)
		
			{
			IMarkupWriter w = availableWriter;

			Object optionValue = model.getOption(i);

			if (selectedSet.contains(optionValue))
				w = selectedWriter;

			w.beginEmpty("option");
			w.attribute("value", model.getValue(i));
			w.print(model.getLabel(i));
			w.println();
		}

		// Close the <select> tags

		selectedWriter.end();
		availableWriter.end();
	}

	/**
	 *  Renders the available select by closing the nested writer for the available
	 *  selects.
	 *
	 */

	public IRender getAvailableSelectDelegate()
	{
		return new IRender()
		{
			public void render(IMarkupWriter writer, IRequestCycle cycle)
				throws RequestCycleException
			{
				availableWriter.close();
				availableWriter = null;
			}
		};
	}

	/**
	 *  Like {@link #getAvailableSelectDelegate()}, but for the right, selected, column.
	 *
	 */

	public IRender getSelectedSelectDelegate()
	{
		return new IRender()
		{
			public void render(IMarkupWriter writer, IRequestCycle cycle)
				throws RequestCycleException
			{
				selectedWriter.close();
				selectedWriter = null;
			}
		};
	}

	private void handleSubmission(IRequestCycle cycle) throws RequestCycleException
	{
		List selected = (List) selectedBinding.getObject("selected", List.class);
		IPropertySelectionModel model =
			(IPropertySelectionModel) modelBinding.getObject(
				"model",
				IPropertySelectionModel.class);

		// Remove any values currently in the List
		selected.clear();

		RequestContext context = cycle.getRequestContext();
		String[] values = context.getParameters(name);

		if (values == null || values.length == 0)
			return;

		for (int i = 0; i < values.length; i++)
		
			{
			String value = values[i];
			Object option = model.translateValue(value);

			selected.add(option);
		}
	}

	public boolean isSortUser()
	{
		return sort == SortMode.USER;
	}

	public IAsset getSelectImage()
	{
		return getAsset("selectImage", "Select");
	}

	public IAsset getDeselectImage()
	{
		return getAsset("deselectImage", "Deselect");
	}

	public IAsset getUpImage()
	{
		return getAsset("upImage", "Up");
	}

	public IAsset getDownImage()
	{
		return getAsset("downImage", "Down");
	}

	/**
	 *  Returns null, but may make sense to implement a displayName parameter.
	 * 
	 **/
	
	public String getDisplayName()
	{
		return null;
	}

}