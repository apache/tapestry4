// Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.IBinding;

/**
 * Implements a component that manages an HTML &lt;input type=text&gt; or &lt;input
 * type=password&gt; form element. [ <a
 * href="../../../../../ComponentReference/TextField.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class TextField extends AbstractTextField
{
    /** @since 3.1 */
    public abstract String getValue();

    /** @since 3.1 */
    public abstract void setValue(String value);

    public String readValue()
    {
        return getValue();
    }

    public void updateValue(String value)
    {
        setValue(value);
    }
}