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

package net.sf.tapestry.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 *  A specialized component used to edit a list of items
 *  within a form; it is similar to a {@link net.sf.tapestry.components.Foreach} but leverages
 *  hidden inputs within the &lt;form&gt; to store the items in the list.
 *
 *  [<a href="../../../../../ComponentReference/ListEdit.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public class ListEdit extends AbstractComponent
{
    /**
     *  Interface that allows a ListEdit to treat an Object[] array or
     *  a {@link List} identically.
     *
     **/

    private interface ISource
    {
        public int getCount();

        public Object get(int index);
    }

    private static class ArraySource implements ISource
    {
        Object[] _array;

        ArraySource(Object[] array)
        {
            _array = array;
        }

        public int getCount()
        {
            return _array.length;
        }

        public Object get(int index)
        {
            return _array[index];
        }
    }

    private static class ListSource implements ISource
    {
        List _list;

        ListSource(List list)
        {
            _list = list;
        }

        public int getCount()
        {
            return _list.size();
        }

        public Object get(int index)
        {
            return _list.get(index);
        }
    }

    private static class EmptySource implements ISource
    {
        public int getCount()
        {
            return 0;
        }

        public Object get(int index)
        {
            throw new IndexOutOfBoundsException("ListEdit.EmptySource contains no values.");
        }
    }

    private IBinding _valueBinding;
    private IBinding _indexBinding;

    private Object _source;
    private String _element;

    /** @since 2.2 **/

    private IActionListener _listener;

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setIndexBinding(IBinding value)
    {
        _indexBinding = value;
    }

    public IBinding getIndexBinding()
    {
        return _indexBinding;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        ISource source = null;
        int count;
        RequestContext context = null;
        Object value = null;

        IForm form = Form.get(cycle);
        if (form == null)
            throw new RequestCycleException(Tapestry.getString("must-be-wrapped-by-form", "ListEdit"), this);

        boolean cycleRewinding = cycle.isRewinding();
        boolean formRewinding = form.isRewinding();

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).

        if (cycleRewinding && !form.isRewinding())
            return;

        String name = form.getElementId(this);

        if (!cycleRewinding)
        {
            source = getSourceData();
            count = source.getCount();

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", name);
            writer.attribute("value", count);
            writer.println();
        }
        else
        {
            context = cycle.getRequestContext();
            count = Integer.parseInt(context.getParameter(name));
        }

        for (int i = 0; i < count; i++)
        {
            if (_indexBinding != null)
                _indexBinding.setInt(i);

            if (cycleRewinding)
                value = extractValue(context, form.getElementId(this));
            else
            {
                value = source.get(i);
                writeValue(writer, form.getElementId(this), value);
            }

            _valueBinding.setObject(value);

            if (_listener != null)
                _listener.actionTriggered(this, cycle);

            if (_element != null)
            {
                writer.begin(_element);
                generateAttributes(writer, cycle);
            }

            renderWrapped(writer, cycle);

            if (_element != null)
                writer.end();
        }
    }

    private void writeValue(IMarkupWriter writer, String name, Object value) throws RequestCycleException
    {
        String externalValue;

        try
        {
            externalValue = getDataSqueezer().squeeze(value);
        }
        catch (IOException ex)
        {
            throw new RequestCycleException(Tapestry.getString("ListEdit.unable-to-convert-value", value), this, ex);
        }

        writer.beginEmpty("input");
        writer.attribute("type", "hidden");
        writer.attribute("name", name);
        writer.attribute("value", externalValue);
        writer.println();
    }

    private Object extractValue(RequestContext context, String name) throws RequestCycleException
    {
        String value = context.getParameter(name);

        try
        {
            return getDataSqueezer().unsqueeze(value);
        }
        catch (IOException ex)
        {
            throw new RequestCycleException(Tapestry.getString("ListEdit.unable-to-convert-string", value), this, ex);
        }
    }

    private ISource getSourceData() throws RequestCycleException
    {
        if (_source == null)
            return new EmptySource();

        if (_source instanceof List)
            return new ListSource((List) _source);

        if (_source.getClass().isArray())
            return new ArraySource((Object[]) _source);

        if (_source instanceof Iterator)
        {
            Iterator i = (Iterator) _source;
            List list = new ArrayList();
            while (i.hasNext())
                list.add(i.next());

            return new ListSource(list);
        }

        throw new RequestCycleException(Tapestry.getString("ListEdit.unable-to-convert-source", _source), this);
    }

    public String getElement()
    {
        return _element;
    }

    public void setElement(String element)
    {
        _element = element;
    }

    public void setSource(Object source)
    {
        _source = source;
    }

    public Object getSource()
    {
        return _source;
    }

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    /** @since 2.2 **/

    public IActionListener getListener()
    {
        return _listener;
    }

    /** @since 2.2 **/

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

}