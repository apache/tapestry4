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

import java.text.Format;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Used to insert some text (from a parameter) into the HTML.
 *
 *  [<a href="../../../../../ComponentReference/Insert.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Insert extends AbstractComponent
{
    private Object _value;
    private Format _format;

    // The class parameter is connected to the styleClass property
    private String _styleClass;
    private boolean _raw;

    /**
     *  Prints its value parameter, possibly formatted by its format parameter.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;

        if (_value == null)
            return;

        String insert = null;

        if (_format == null)
        {
            insert = _value.toString();
        }
        else
        {
            try
            {
                insert = _format.format(_value);
            }
            catch (Exception ex)
            {
                throw new RequestCycleException(Tapestry.getString("Insert.unable-to-format", _value), this, ex);
            }
        }

        if (_styleClass != null)
        {
            writer.begin("span");
            writer.attribute("class", _styleClass);
        }

        if (_raw)
            writer.printRaw(insert);
        else
            writer.print(insert);

        if (_styleClass != null)
            writer.end(); // <span>
    }

    public Object getValue()
    {
        return _value;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

    public Format getFormat()
    {
        return _format;
    }

    public void setFormat(Format format)
    {
        _format = format;
    }

    public String getStyleClass()
    {
        return _styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public boolean getRaw()
    {
        return _raw;
    }

    public void setRaw(boolean raw)
    {
        _raw = raw;
    }

}