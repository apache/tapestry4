//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.spec;

/**
 *  Bean used to test extensions.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class PropertyBean
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
