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

package org.apache.tapestry.form;

import java.util.ResourceBundle;

import org.apache.commons.lang.enum.Enum;

/**
 *  Implementation of {@link IPropertySelectionModel} that wraps around
 *  a set of {@link Enum}s.
 * 
 *  <p>Uses a simple index number as the value (used to represent the option).
 *
 *  <p>The resource bundle from which labels are extracted is usually
 *  a resource within the Tapestry application.  Since 
 *  {@link ResourceBundle#getBundle(String, java.util.Locale)} uses its caller's class loader,
 *  and that classloader will be the Tapestry framework's classloader, the application's
 *  resources won't be visible.  This requires that the application resolve
 *  the resource to a {@link ResourceBundle} before creating this model.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class EnumPropertySelectionModel implements IPropertySelectionModel
{
    private Enum[] _options;
    private String[] _labels;

    private String _resourcePrefix;
    private ResourceBundle _bundle;

    /**
     *  Standard constructor.
     *
     *  <p>Labels for the options are extracted from a resource bundle.  resourceBaseName
     *  identifies the bundle.  Typically, the bundle will be a <code>.properties</code>
     *  file within the classpath.  Specify the fully qualified class name equivalent, i.e.,
     *  for file <code>/com/example/foo/LabelStrings.properties</code> use
     *  <code>com.example.foo.LabelStrings</code> as the resource base name.
     *
     *  <p>Normally (when resourcePrefix is null), the keys used to extract labels
     *  matches the {@link Enum#getName() enumeration id} of the option.  By
     *  convention, the enumeration id matches the name of the static variable.
     *
     *  <p>To avoid naming conflicts when using a single resource bundle for multiple
     *  models, use a resource prefix.  This is a string which is prepended to
     *  the enumeration id (they prefix and enumeration id are seperated with a period).
     *
     *  @param   options The list of possible values for this model, in the order they
     *  should appear. This exact array is retained (not copied).
     *
     *  @param   bundle The {@link ResourceBundle} from which labels may be extracted.
     *
     *  @param   resourcePrefix An optional prefix used when accessing keys within the bundle. 
     *  Used to allow a single ResouceBundle to contain labels for multiple Enums.
     **/

    public EnumPropertySelectionModel(Enum[] options, ResourceBundle bundle, String resourcePrefix)
    {
        _options = options;
        _bundle = bundle;
        _resourcePrefix = resourcePrefix;
    }

    /**
     *  Simplified constructor using no prefix.
     *
     **/

    public EnumPropertySelectionModel(Enum[] options, ResourceBundle bundle)
    {
        this(options, bundle, null);
    }

    public int getOptionCount()
    {
        return _options.length;
    }

    public Object getOption(int index)
    {
        return _options[index];
    }

    public String getLabel(int index)
    {
        if (_labels == null)
            readLabels();

        return _labels[index];
    }

    public String getValue(int index)
    {
        return Integer.toString(index);
    }

    public Object translateValue(String value)
    {
        int index;

        index = Integer.parseInt(value);

        return _options[index];
    }

    private void readLabels()
    {
        _labels = new String[_options.length];

        for (int i = 0; i < _options.length; i++)
        {
            String enumerationId = _options[i].getName();

            String key;

            if (_resourcePrefix == null)
                key = enumerationId;
            else
                key = _resourcePrefix + "." + enumerationId;

            _labels[i] = _bundle.getString(key);
        }

    }
}