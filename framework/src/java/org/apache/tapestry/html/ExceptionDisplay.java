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

package org.apache.tapestry.html;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.bean.EvenOdd;
import org.apache.tapestry.describe.RenderableAdapterFactory;
import org.apache.tapestry.util.exception.ExceptionDescription;

/**
 * Component used to display an already formatted exception. [ <a
 * href="../../../../../ComponentReference/ExceptionDisplay.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class ExceptionDisplay extends BaseComponent
{
    public abstract ExceptionDescription[] getExceptions();

    private ExceptionDescription _exception;

    private EvenOdd _evenOdd;

    public abstract int getIndex();

    public abstract int getCount();

    public abstract void setCount(int count);

    /**
     * Each time the current exception is set, as a side effect, the evenOdd helper bean is reset to
     * even.
     */

    public void setException(ExceptionDescription value)
    {
        _exception = value;

        _evenOdd.setEven(true);
    }

    public ExceptionDescription getException()
    {
        return _exception;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        ExceptionDescription[] exceptions = getExceptions();

        setCount(exceptions.length);

        try
        {
            _evenOdd = (EvenOdd) getBeans().getBean("evenOdd");

            super.renderComponent(writer, cycle);
        }
        finally
        {
            _exception = null;
            _evenOdd = null;
        }
    }

    public boolean isLast()
    {
        return getIndex() == (getCount() - 1);
    }
}