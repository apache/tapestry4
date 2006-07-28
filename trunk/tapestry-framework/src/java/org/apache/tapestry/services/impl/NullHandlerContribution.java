// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import ognl.NullHandler;

import org.apache.hivemind.impl.BaseLocatable;

/**
 * A contribution to the <code>tapestry.ognl.NullHandlers</code> configuration point; this
 * provides the Class and {@link ognl.NullHandler}that will be passed to
 * {@link ognl.OgnlRuntime#setNullHandler(java.lang.Class, ognl.NullHandler)}.
 * 
 * @author Andreas Andreou
 * @since 4.1
 */
public class NullHandlerContribution extends BaseLocatable
{
    private Class _subjectClass;

    private NullHandler _handler;

    public NullHandler getHandler()
    {
        return _handler;
    }

    public void setHandler(NullHandler handler)
    {
        _handler = handler;
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
