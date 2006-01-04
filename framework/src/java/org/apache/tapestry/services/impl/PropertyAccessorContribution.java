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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.impl.BaseLocatable;

import ognl.PropertyAccessor;

/**
 * A contribution to the <code>tapestry.ognl.PropertyAccessors</code> configuration point; this
 * provides the Class and {@link ognl.PropertyAccessor}that will be passed to
 * {@link ognl.OgnlRuntime#setPropertyAccessor(java.lang.Class, ognl.PropertyAccessor)}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PropertyAccessorContribution extends BaseLocatable
{
    private Class _subjectClass;

    private PropertyAccessor _accessor;

    public PropertyAccessor getAccessor()
    {
        return _accessor;
    }

    public void setAccessor(PropertyAccessor accessor)
    {
        _accessor = accessor;
    }

    public Class getSubjectClass()
    {
        return _subjectClass;
    }

    public void setSubjectClass(Class subjectClass)
    {
        _subjectClass = subjectClass;
    }
}