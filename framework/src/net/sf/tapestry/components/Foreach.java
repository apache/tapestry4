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
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Foreach extends AbstractComponent
{
    private Object source;
    private IBinding valueBinding;
    private IBinding indexBinding;
	private String element;

    private Object value;
    private boolean rendering;

    public IBinding getIndexBinding()
    {
        return indexBinding;
    }

    public void setIndexBinding(IBinding value)
    {
        indexBinding = value;
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
 		if (source == null)
 			return null;
 		
        return Tapestry.coerceToIterator(source);
    }

    public IBinding getValueBinding()
    {
        return valueBinding;
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
            rendering = true;
            value = null;
            int i = 0;

            boolean hasNext = dataSource.hasNext();

            while (hasNext)
            {
                value = dataSource.next();
                hasNext = dataSource.hasNext();

                if (indexBinding != null)
                    indexBinding.setInt(i);

                if (valueBinding != null)
                    valueBinding.setObject(value);

                if (element != null)
                {
                    writer.begin(element);
                    generateAttributes(writer, cycle);
                }

                renderBody(writer, cycle);

                if (element != null)
                    writer.end();

                i++;
            }
        }
        finally
        {
            value = null;
            rendering = false;
        }
    }

    public void setValueBinding(IBinding value)
    {
        valueBinding = value;
    }

    /**
     *  Returns the most recent value extracted from the source parameter.
     *
     *  @throws RenderOnlyPropertyException if the Foreach is not currently rendering.
     *
     **/

    public Object getValue()
    {
        if (!rendering)
            throw new RenderOnlyPropertyException(this, "value");

        return value;
    }

    public String getElement()
    {
        return element;
    }

    public void setElement(String element)
    {
        this.element = element;
    }

    public Object getSource()
    {
        return source;
    }

    public void setSource(Object source)
    {
        this.source = source;
    }

}