// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.pageload;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Used by test cases for property initializing / re-initializing.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public abstract class ComponentFixture extends AbstractComponent
{
    private String _stringProperty;

    public String getStringProperty()
    {
        return _stringProperty;
    }

    public void setStringProperty(String stringProperty)
    {
        _stringProperty = stringProperty;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        //
    }
}