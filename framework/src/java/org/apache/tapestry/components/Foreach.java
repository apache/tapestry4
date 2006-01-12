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

package org.apache.tapestry.components;

import java.util.Iterator;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Repeatedly renders its wrapped contents while iterating through a list of
 * values. [ <a href="../../../../../ComponentReference/Foreach.html">Component
 * Reference </a>]
 * <p>
 * While the component is rendering, the property {@link #getValue() value}(accessed
 * as <code>components.<i>foreach</i>.value</code> is set to each successive
 * value from the source, and the property {@link #getIndex() index}is set to
 * each successive index into the source (starting with zero).
 * 
 * @author Howard Lewis Ship
 */

public abstract class Foreach extends AbstractComponent
{

    private Object _value;

    private int _index;

    /**
     * Gets the source binding and returns an {@link Iterator}representing the
     * values identified by the source. Returns an empty {@link Iterator}if the
     * binding, or the binding value, is null.
     * <p>
     * Invokes {@link Tapestry#coerceToIterator(Object)}to perform the actual
     * conversion.
     */

    protected Iterator getSourceData()
    {
        Object source = null;

        IBinding sourceBinding = getBinding("source");
        if (sourceBinding != null) source = sourceBinding.getObject();

        if (source == null) return null;

        return (Iterator)getValueConverter().coerceValue(source, Iterator.class);
    }

    protected void prepareForRender(IRequestCycle cycle)
    {
        _value = null;
        _index = 0;
    }

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        _value = null;
    }

    /**
     * Gets the source binding and iterates through its values. For each, it
     * updates the value binding and render's its wrapped elements.
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Iterator dataSource = getSourceData();

        // The dataSource was either not convertable, or was empty.

        if (dataSource == null) return;

        boolean indexBound = isParameterBound("index");
        boolean valueBound = isParameterBound("value");

        String element = getElement();

        boolean hasNext = dataSource.hasNext();

        while(hasNext)
        {
            _value = dataSource.next();
            hasNext = dataSource.hasNext();

            if (indexBound) setIndexParameter(_index);

            if (valueBound) setValueParameter(_value);

            if (element != null)
            {
                writer.begin(element);
                renderInformalParameters(writer, cycle);
            }

            renderBody(writer, cycle);

            if (element != null) writer.end();

            _index++;
        }

    }

    /**
     * Returns the most recent value extracted from the source parameter.
     * 
     * @throws org.apache.tapestry.ApplicationRuntimeException
     *             if the Foreach is not currently rendering.
     */

    public Object getValue()
    {
        if (!isRendering()) throw Tapestry.createRenderOnlyPropertyException(this, "value");

        return _value;
    }

    /**
     * The index number, within the {@link #getSource() source}, of the the
     * current value.
     * 
     * @throws org.apache.tapestry.ApplicationRuntimeException
     *             if the Foreach is not currently rendering.
     * @since 2.2
     */

    public int getIndex()
    {
        if (!isRendering()) throw Tapestry.createRenderOnlyPropertyException(this, "index");

        return _index;
    }

    public abstract String getElement();

    /** @since 4.0 */
    public abstract void setIndexParameter(int value);

    /** @since 4.0 */
    public abstract void setValueParameter(Object value);

    /** @since 4.0 */
    public abstract ValueConverter getValueConverter();
}
