/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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