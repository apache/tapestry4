package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.foundation.*;
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
  *  Implementation of {@link IPropertySelectionModel} that wraps around
  *  a set of {@link Enum}s.  In addition to this, a {@link Locale} and
  *  some information needed to extract labels from a {@link ResourceBundle}
  *  are provided.
  *
  *  @version $Id$
  *  @author Howard Ship
  */
  
public class EnumPropertySelectionModel
implements IPropertySelectionModel
{
	private Enum[] options;
	private String[] labels;
	
	private Locale locale;
	private String resourceBaseName;
	private String resourcePrefix;
	

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
	 * @param   resourceBaseName The base class name of a {@link ResourceBundle} from which
	 * labels will be loaded.
	 *
	 * @param   resourcePrefix An optional prefix used when accessing keys within the bundle. 
	 */
	 
	public EnumPropertySelectionModel(Enum[] options, Locale locale,
			String resourceBaseName, String resourcePrefix)
	{
		this.options = options;
		this.locale = locale;
		this.resourceBaseName = resourceBaseName;
		this.resourcePrefix = resourcePrefix;
	}

	/**
	 *  Simplified constructor using the default locale and no prefix.
	 *
	 */

	public EnumPropertySelectionModel(Enum[] options, String resourceBaseName)
	{
		this.options = options;
		this.resourceBaseName = resourceBaseName;
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
	
	private void readLabels()
	{
		ResourceBundle bundle;
		int i;
		String key;
		String enumerationId;
		
		bundle = ResourceBundle.getBundle(resourceBaseName, locale);
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