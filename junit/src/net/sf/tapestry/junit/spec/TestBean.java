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

package net.sf.tapestry.junit.spec;

/**
 *  Bean used to test extensions.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class TestBean
{
    private boolean _booleanProperty;
    private int _intProperty;
    private long _longProperty;
    private String _stringProperty;
    private double _doubleProperty;
    
    public boolean getBooleanProperty()
    {
        return _booleanProperty;
    }

    public double getDoubleProperty()
    {
        return _doubleProperty;
    }

    public int getIntProperty()
    {
        return _intProperty;
    }

    public long getLongProperty()
    {
        return _longProperty;
    }

    public String getStringProperty()
    {
        return _stringProperty;
    }

    public void setBooleanProperty(boolean booleanProperty)
    {
        _booleanProperty = booleanProperty;
    }

    public void setDoubleProperty(double doubleProperty)
    {
        _doubleProperty = doubleProperty;
    }

    public void setIntProperty(int intProperty)
    {
        _intProperty = intProperty;
    }

    public void setLongProperty(long longProperty)
    {
        _longProperty = longProperty;
    }

    public void setStringProperty(String stringProperty)
    {
        _stringProperty = stringProperty;
    }

}
