/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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

package net.sf.tapestry.contrib.palette;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.html.*;
import com.primix.tapestry.form.*;
import com.primix.tapestry.spec.*;
import java.util.*;

/**
 *  A component used to make a number of selections from a list.  The general look
 *  is a pair of &lt;select&gt; elements.  with a pair of buttons between them.
 *  The left element is a list of values that can be selected.  The buttons move
 *  values from the left column ("available") to the right column ("selected").
 *
 *  <p>This all takes a bit of JavaScript to accomplish (quite a bit), which means
 *  a {@link Body} component must wrap the Palette.  In addition, the Palette
 *  component is not compatible with Netscape Navigator 4.x.
 *
 *  <p><table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *	  <td>selected</td>
 *	<td>{@link List}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *	<td>&nbsp;</td>
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
 *	<td>{@link IPropertySelectionModel}</td>
 *  <td>R</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td>Works, as with a {@link PropertySelection} component, to define the
 *  possible values.
 *  </td> </tr>
 *
 * <tr>
 *	<td>sort</td> <td>{@link SortMode}</td> <td>R</td>
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
 *  <tr>
 *		<td>mirror</td>
 *		<td>bool</td>
 *		<td>R</td>
 *		<td>false</td>
 *		<td>If true, then the normal orientation of the
 *  Palette is changed, such that the list of available items
 *  is on the LEFT and the selected items are on the RIGHT.  This
 *  may involve selecting alternate images for the select and de-select
 *  buttons since they incorporate arrows. </td> </tr>
 *
 * <tr>
 *	<td>selectImage
 * <br>selectDisabledImage
 * <br>deselectImage
 * <br>deselectDisabledImage
 * <br>upImage
 * <br>upDisabledImage
 * <br>downImage
 * <br>downDisabledImage
 *  </td>
 *  <td>{@link IAsset}</td>
 *	<td>R</td>
 *	<td>no</td> <td>&nbsp;</td>
 *  <td>If any of these are provided then they override the default images provided
 *  with the component.  This allows the look and feel to be customized relatively easily.
 *  Note that for the first four (selectImage through deselectImage), the images provided
 *  must coordinate with the mirror parameter; that is, if mirror is true, then the images
 *  should have an arrow pointing right for select and left for deselect.  When using the
 *  default images, the component automatically selects an internal image appropriate
 *  for mirroring.
 *		</td> </tr>
 *
 * </table>
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Palette
	extends BaseComponent
	implements IFormComponent
{
	private static final int DEFAULT_ROWS= 10;
	private static final int MAP_SIZE = 7;
	private static final String SKIP_KEY = 
		"net.sf.tapestry.contrib.palette.SkipBaseFunctions";
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
	
	private IBinding mirrorBinding;
	private boolean staticMirror;
	private boolean mirrorValue;
	private boolean mirrored;
	
	private Block defaultSelectedTitleBlock;
	private Block defaultAvailableTitleBlock;
	
	/**
	 *  {@link Form} which is currently wrapping the Palette.
	 *
	 */
	
	private Form form;
	
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
	
	private IResponseWriter availableWriter;
	
	/**
	 *  Contains the text for the first &lt;select&gt; element, that
	 *  provides the selected elements.
	 *
	 */
	
	private IResponseWriter selectedWriter;
	
	/**
	 *  A cached copy if the script used with the component.
	 *
	 */
	
	private IScript script;
	
	public void finishLoad(IPageLoader loader, ComponentSpecification spec)
		throws PageLoaderException
	{
		defaultSelectedTitleBlock = (Block)getComponent("defaultSelectedTitleBlock");
		defaultAvailableTitleBlock = (Block)getComponent("defaultAvailableTitleBlock");
		
		super.finishLoad(loader, spec);
	}
	
	public void setMirrorBinding(IBinding value)
	{
		mirrorBinding = value;
		
		staticMirror = value.isStatic();
		if (staticMirror)
			mirrorValue = value.getBoolean();
	}
	
	public IBinding getMirrorBinding()
	{
		return mirrorBinding;
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
	
	public void setselectedBinding(IBinding value)
	{
		selectedBinding = value;
	}
	
	public IBinding getselectedBinding()
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
			staticSort = (SortMode)value.getObject("sort", SortMode.class);
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
	
	public Form getForm()
	{
		return form;
	}
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		form = Form.get(getPage().getRequestCycle());
		
		if (form == null)
			throw new RequestCycleException("Palette component must be wrapped by a Form.", this);
		
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
			sort = (SortMode)sortBinding.getObject("sort", SortMode.class);
		
		if (sort == null)
			sort = SortMode.NONE;
		
		mirrored = false;
		
		if (mirrorBinding != null)
		{
			if (staticMirror)
				mirrored = mirrorValue;
			else
				mirrored = mirrorBinding.getBoolean();
		}
		
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
		
		form.addEventHandler(FormEventType.SUBMIT, 
								(String)symbols.get("formSubmitFunctionName"));
		
		// Buffer up the HTML for the left and right selects (the selected
		// items and the available items).
		
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
			throw new RequestCycleException("Palette component must be wrapped by a Body.", this);
		
		
		setImage(symbols, body, cycle, "selectImage", "Select", "SelectM");
		setImage(symbols, body, cycle, "selectDisabledImage", "Select-dis", "SelectM-dis");
		setImage(symbols, body, cycle, "deselectImage", "Deselect", "DeselectM");
		setImage(symbols, body, cycle, "deselectDisabledImage", "Deselect-dis", "DeselectM-dis");
		
		if (cycle.getAttribute(SKIP_KEY) == null)
			symbols.put("includeBaseFunctions", Boolean.TRUE);
		
		if (sort == SortMode.LABEL)
			symbols.put("sortLabel", Boolean.TRUE);
		
		if (sort == SortMode.VALUE)
			symbols.put("sortValue", Boolean.TRUE);
		
		if (sort == SortMode.USER)
		{
			symbols.put("sortUser", Boolean.TRUE);
			setImage(symbols, body, cycle, "upImage", "Up", null);
			setImage(symbols, body, cycle, "upDisabledImage", "Up-dis", null);
			setImage(symbols, body, cycle, "downImage", "Down", null);
			setImage(symbols, body, cycle, "downDisabledImage", "Down-dis", null);
		}
		
		try
		{
			session = script.execute(symbols);
		}
		catch (ScriptException ex)
		{
			throw new RequestCycleException(this, ex);
		}
		
		body.addOtherScript(session.getBody());
		body.addOtherInitialization(session.getInitialization());
		
		
		// This marks that the base functions have been emitted, and should not be
		// emitted again if there's another Palette on the page somewhere.
		
		cycle.setAttribute(SKIP_KEY, Boolean.TRUE);
	}
	
	private IAsset getAsset(String symbolName, String assetName, String mirrorAssetName)
	{
		IAsset result = null;
		IBinding binding = getBinding(symbolName);
		
		if (binding != null)
			result = (IAsset)binding.getObject(symbolName, IAsset.class);
		
		if (result == null)
		{
			String finalAssetName = assetName;
			
			if (mirrored && mirrorAssetName != null)
				finalAssetName = mirrorAssetName;
			
			result = getAsset(finalAssetName);
		}	
		
		return result;
	}
	
	/**
	 *  Gets the asset (first looking for a parameter with the symbolName, then
	 *  using the default asset), extracts its URL, sets it up for 
	 *  preloading, and assigns the preload reference as a script symbol.
	 *
	 */
	
	private void setImage(Map symbols, Body body, IRequestCycle cycle, 
			String symbolName, String assetName, String mirrorAssetName)
	{
		IAsset asset = getAsset(symbolName, assetName, mirrorAssetName);
		
		String URL = asset.buildURL(cycle);
		String reference = body.getPreloadedImageReference(URL);
		
		symbols.put(symbolName, reference);
	}
	
	public Map getSymbols()
	{
		return symbols;
	}
	
	private Block getHeaderBlock(boolean selected)
	{
		Block result = null;
		String bindingName = null;
		IBinding binding = null;
		
		if (selected)
		{
			bindingName = "selectedTitleBlock";
			binding = selectedTitleBlockBinding;
		}
		else
		{
			bindingName = "availableTitleBlock";
			binding = availableTitleBlockBinding;
		}
		
		if (binding != null)
			result = (Block)binding.getObject(bindingName, Block.class);
		
		if (result == null)
		{
			return selected 
				? defaultSelectedTitleBlock
				: defaultAvailableTitleBlock;
		}
		
		return result;			
	}
	
	/**
	 *  Returns the selected header block if mirrored, or the available
	 *  header block normally.
	 *
	 */
	
	public Block getRightHeaderBlock()
	{
		return getHeaderBlock(mirrored);
	}
	
	/**
	 *  Returns the available header block if mirrored, or the selected
	 *  header block normally.
	 *
	 */
	
	public Block getLeftHeaderBlock()
	{
		// Return the selected block normally, or the available block if mirrored.
		return getHeaderBlock(!mirrored);
	}
	
	/**
	 *  Buffers the two &lt;select&gt;s, each in its own nested {@link IResponseWriter}.
	 *  The idea is to run through the property selection model just once, assigning
	 *  each item to one or the other &lt;select&gt;.
	 *
	 */
	
	private void bufferSelects(IResponseWriter writer)
	{
		List selected = (List)selectedBinding.getObject("selected", List.class);
		IPropertySelectionModel model = 
			(IPropertySelectionModel)modelBinding.getObject("model", IPropertySelectionModel.class);
		
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
		availableWriter.attribute("name", (String)symbols.get("availableName"));
		availableWriter.println();
		
		// Each value specified in the model will go into either the selected or available
		// lists.
		
		int count = model.getOptionCount();
		for (int i = 0; i < count; i++)
		{
			IResponseWriter w = availableWriter;
			
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
	
	public IRender getLeftSelectDelegate()
	{
		return new IRender()
		{
			public void render(IResponseWriter writer, IRequestCycle cycle)
				throws RequestCycleException
			{
				if (mirrored)
				{
					availableWriter.close();
					availableWriter = null;
				}
				else
				{
					selectedWriter.close();
					selectedWriter = null;
				}
			}
		};
	}
	
	public IRender getRightSelectDelegate()
	{
		return new IRender()
		{
			public void render(IResponseWriter writer, IRequestCycle cycle)
				throws RequestCycleException
			{
				if (mirrored)
				{
					selectedWriter.close();
					selectedWriter = null;
				}
				else
				{
					availableWriter.close();
					availableWriter = null;
				}
			}
		};
	}
	
	private void handleSubmission(IRequestCycle cycle)
		throws RequestCycleException
	{
		List selected = (List)selectedBinding.getObject("selected", List.class);
		IPropertySelectionModel model = 
			(IPropertySelectionModel)modelBinding.getObject("model", IPropertySelectionModel.class);
		
		// Remove any values currently in the List
		selected.clear();
		
		RequestContext context = cycle.getRequestContext();
		String[] values = context.getParameterValues(name);
		
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
		return getAsset("selectImage", "Select", "SelectM");
	}
	
	public IAsset getDeselectImage()
	{
		return getAsset("deselectImage", "Deselect", "DeselectM");
	}
	
	public IAsset getUpImage()
	{
		return getAsset("upImage", "Up", null);
	}
	
	public IAsset getDownImage()
	{
		return getAsset("downImage", "Down", null);
	}
}

