package com.primix.tapestry.components;

import com.primix.tapestry.*;
import java.util.*;

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

/**
 *  Adapts the selection model used by the {@link Select} component
 *  (in single selection mode) and the {@link RadioGroup}
 *  (which is always in single selection)
 *  to the typical application model, where the components are being
 *  used to update a single property (usually a String) to
 *  a value selected by the user.
 *
 *  <p>A <code>SelectionAdaptor</code> works with a {@link Foreach} to
 * provide values for the {@link Option} or {@link Radio} components.  The following table
 * shows the bindings:
 *
 *  <table border=1>
 *  <tr> <th> {@link Foreach} parameter</th>
 *		 <th> <code>SelectionAdaptor</code> property </th> </tr>
 *  <tr> <td> source </td> <td> options </td> </tr>
 *  <tr> <td> value </td> <td> currentSelection </td> </tr>
 *  <tr> <td> first </td> <td> first </td> </tr>
 *  <tr> <td> last </td> <td> last </td> </tr>
 *  </table>
 *
 *  <p>In addition, the <code>selected</code> property 
 *  of the {@link Option} or {@link Radio} should
 *  by bound to the <code>SelectionAdaptor</code>'s 
 *  <code>selected</code> property.
 *
 *
 * <p>This whole mechanism exists for compatibility and for very special cases.
 *  More generally, a {@link PropertySelection} component is used instead.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public abstract class SelectionAdaptor
{
	private Object previousSelection;
	private Object currentSelection;
	private Object newSelection;

	private boolean first;
	private boolean last;

	public Object getCurrentSelection()
	{
		return currentSelection;
	}

	/**
	 *  Implemented by subclasses to provide a list of unique values.
	 *
	 */
	 
	abstract public List getOptions();

	/**
	 *  Implemented by subclasses to provide a string label for whatever
	 *  the current selection is.  The subclass is responsible for ensuring
	 *  that the return value is properly localized.
	 *
	 */
	 
	abstract public String getCurrentSelectionLabel();
	
	/**
	*  Invoked from {@link #setFirst(boolean)} to find the initially selected
	*  option.  Subclasses should implement this method and return the
	*  current selection, or null if there is not current selection.
	*
	*/

	protected abstract Object getSelection();

	public boolean isFirst()
	{
		return first;
	}

	public boolean isLast()
	{
		return last;
	}

	public boolean isSelected()
	{
		if (previousSelection == null)
			return false;

		return previousSelection.equals(currentSelection);
	}

	public void setCurrentSelection(Object value)
	{
		currentSelection = value;
	}

	/**
	*  Invoked by the {@link Foreach} to indicate when the first
	*  element in the options list is about to be processed.
	*
	*  <p>On the first pass (when value is true), we get the previous selection
	*  using {@link #getSelection()} and clear the newSelection.
	*
	*/

	public void setFirst(boolean value)
	{
		first = value;

		if (first)
		{
			newSelection = null;
			previousSelection = getSelection();
		}
	}

	public void setLast(boolean value)
	{
		last = value;
	}

	/**
	*  Invoked by the {@link Option} or {@link RadioGroup} to indicate whether the
	*  element corresponding to the currentSelection is selected or not.  We rely
	*  on the {@link Foreach} having set the first and/or last properties before
	*  this method gets invoked.
	*
	*  <p>On the last iteration, we invoke {@link #updateSelection(Object)} to carry the
	*  new selection forward.
	*
	*/

	public void setSelected(boolean value)
	{
		if (value)
			newSelection = currentSelection;

		if (last)
			updateSelection(newSelection);
	}

	/**
	*  Invoked from {@link #setSelected(boolean)} on the last iteration, at which point
	*  the new selection will be known.  Subclasses should implement this method
	*  to do what is appropriate to react to the new selection.
	*
	*  @param newSelection The newly selected string, or null if no
	*  selection.
	*
	*/

	protected abstract void updateSelection(Object newSelection);
}
