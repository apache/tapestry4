//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.components;

import java.util.Iterator;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RenderOnlyPropertyException;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Repeatedly renders its wrapped contents while iterating through
 *  a list of values.
 *
 *  [<a href="../../../../../ComponentReference/Foreach.html">Component Reference</a>]
 *
 *  <p>
 *  While the component is rendering, the property
 *  {@link #getValue() value} (accessed as
 *  <code>components.<i>foreach</i>.value</code>
 *  is set to each successive value from the source,
 *  and the property
 *  {@link #getIndex() index} is set to each successive index
 *  into the source (starting with zero).
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Foreach extends AbstractComponent
{
    private Object _source;
    private IBinding _valueBinding;
    private IBinding _indexBinding;
	private String _element;

    private Object _value;
    private int _index;
    private boolean _rendering;

    public IBinding getIndexBinding()
    {
        return _indexBinding;
    }

    public void setIndexBinding(IBinding value)
    {
        _indexBinding = value;
    }


    /**
     *  Gets the source binding and returns an {@link Iterator}
     *  representing
     *  the values identified by the source.  Returns an empty {@link Iterator}
     *  if the binding, or the binding value, is null.
     *
     *  <p>Invokes {@link Tapestry#coerceToIterator(Object)} to perform
     *  the actual conversion.
     *
     **/

    protected Iterator getSourceData()
    {
 		if (_source == null)
 			return null;
 		
        return Tapestry.coerceToIterator(_source);
    }

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    /**
     *  Gets the source binding and iterates through
     *  its values.  For each, it updates the value binding and render's its wrapped elements.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        Iterator dataSource = getSourceData();

        // The dataSource was either not convertable, or was empty.

        if (dataSource == null)
            return;


        try
        {
            _rendering = true;
            _value = null;
            _index = 0;

            boolean hasNext = dataSource.hasNext();

            while (hasNext)
            {
                _value = dataSource.next();
                hasNext = dataSource.hasNext();

                if (_indexBinding != null)
                    _indexBinding.setInt(_index);

                if (_valueBinding != null)
                    _valueBinding.setObject(_value);

                if (_element != null)
                {
                    writer.begin(_element);
                    generateAttributes(writer, cycle);
                }

                renderBody(writer, cycle);

                if (_element != null)
                    writer.end();

                _index++;
            }
        }
        finally
        {
            _value = null;
            _rendering = false;
        }
    }

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    /**
     *  Returns the most recent value extracted from the source parameter.
     *
     *  @throws RenderOnlyPropertyException if the Foreach is not currently rendering.
     *
     **/

    public Object getValue()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "value");

        return _value;
    }

    public String getElement()
    {
        return _element;
    }

    public void setElement(String element)
    {
        _element = element;
    }

    public Object getSource()
    {
        return _source;
    }

    public void setSource(Object source)
    {
        _source = source;
    }

    /**
     *  The index number, within the {@link #getSource() source}, of the
     *  the current value.
     * 
     *  @throws RenderOnlyPropertyException if the Foreach is not currently rendering.
     *
     *  @since 2.2
     * 
     **/
    
    public int getIndex()
    {
        if (!_rendering)
            throw new RenderOnlyPropertyException(this, "index");
        
        return _index;
    }

}