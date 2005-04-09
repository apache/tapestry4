// Copyright 2005 The Apache Software Foundation
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

import org.apache.hivemind.HiveMind;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * Representation of hidden form field data, which is collected by the Form component as it renders
 * (and renders its body).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class HiddenFieldData
{
    private String _name;

    private String _value;

    private String _id;

    public HiddenFieldData(String name, String value)
    {
        this(name, null, value);
    }

    public HiddenFieldData(String name, String id, String value)
    {
        _name = name;
        _id = id;
        _value = value;
    }

    public String getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    public String getValue()
    {
        return _value;
    }
}