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

package com.primix.tapestry.form;

import com.primix.tapestry.util.*;
import java.util.*;

/**
 *  Implementation of {@link IPropertySelectionModel} that wraps around
 *  a set of {@link Enum}s.  In addition to this, a {@link Locale} and
 *  some information needed to extract labels from a {@link ResourceBundle}
 *  are provided.
 *
 *  <p>Uses a simple index number as the value (used to represent the option).
 *
 *  <p>The resource bundle from which labels are extracted is usually
 *  a resource within the Tapestry application.  Since 
 *  {@link ResourceBundle#getBundle(String, Locale)} uses its caller's class loader,
 *  and that classloader will be the Tapestry framework's classloader, the application's
 *  resources won't be visible.  This requires that the application resolve
 *  the resource to a {@link ResourceBundle} before creating this model.
 *  
 *  @version $Id$
 *  @author Howard Ship
 */

public class EnumPropertySelectionModel implements IPropertySelectionModel
{
	private Enum[] options;
	private String[] labels;

	private String resourcePrefix;
	private ResourceBundle bundle;

	/**
	 * Standard constructor.
	 *
	 * <p>Labels for the options are extracted from a resource bundle.  resourceBaseName
	 * identifies the bundle.  Typically, the bundle will be a <code>.properties</code>
	 * file within the classpath.  Specify the fully qualified class name equivalent, i.e.,
	 * for file <code>/com/example/foo/LabelStrings.properties</code> use
	 * <code>com.example.foo.LabelStrings</code> as the resource base name.
	 *
	 * <p>Normally (when resourcePrefix is null), the keys used to extract labels
	 * matches the {@link Enum#getEnumerationId() enumeration id} of the option.  By
	 * convention, the enumeration id matches the name of the static variable.
	 *
	 * <p>To avoid naming conflicts when using a single resource bundle for multiple
	 * models, use a resource prefix.  This is a string which is prepended to
	 * the enumeration id (they prefix and enumeration id are seperated with a period).
	 *
	 * @param   options The list of possible values for this model, in the order they
	 * should appear. This exact array is retained (not copied).
	 *
	 * @param   locale The {@link Locale} for which labels should be generated.
	 *
	 * @param   bundle The {@link ResourceBundle} from which labels may be extracted.
	 *
	 * @param   resourcePrefix An optional prefix used when accessing keys within the bundle. 
	 */

	public EnumPropertySelectionModel(
		Enum[] options,
		ResourceBundle bundle,
		String resourcePrefix)
	{
		this.options = options;
		this.bundle = bundle;
		this.resourcePrefix = resourcePrefix;
	}

	/**
	 *  Simplified constructor using the default locale and no prefix.
	 *
	 */

	public EnumPropertySelectionModel(Enum[] options, ResourceBundle bundle)
	{
		this.options = options;
		this.bundle = bundle;
	}

	public int getOptionCount()
	{
		return options.length;
	}

	public Object getOption(int index)
	{
		return options[index];
	}

	public String getLabel(int index)
	{
		if (labels == null)
			readLabels();

		return labels[index];
	}

	public String getValue(int index)
	{
		return Integer.toString(index);
	}

	public Object translateValue(String value)
	{
		int index;

		index = Integer.parseInt(value);

		return options[index];
	}

	private void readLabels()
	{
		int i;
		String key;
		String enumerationId;

		labels = new String[options.length];

		for (i = 0; i < options.length; i++)
		{
			enumerationId = options[i].getEnumerationId();

			if (resourcePrefix == null)
				key = enumerationId;
			else
				key = resourcePrefix + "." + enumerationId;

			labels[i] = bundle.getString(key);
		}

	}
}