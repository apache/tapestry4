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

package org.apache.tapestry.junit.mock.c6;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 *  Test a number of different property value types.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class Four extends BasePage
{
    private boolean _booleanValue;
    private byte _byteValue;
    private short _shortValue;
    private long _longValue;
    private float _floatValue;
    private double _doubleValue;
    private char _charValue;
    private int _intValue;
    private StringHolder _stringHolder;
    
    public void initialize()
    {
      _booleanValue = false;
      _byteValue = 0;
      _shortValue = 0;
      _longValue = 0;
      _floatValue = 0;
      _doubleValue = 0;
      _charValue = ' ';
      _intValue = 0;
      _stringHolder = null;
    }   
    
    public boolean getBooleanValue()
    {
        return _booleanValue;
    }

    public double getDoubleValue()
    {
        return _doubleValue;
    }

    public float getFloatValue()
    {
        return _floatValue;
    }

    public long getLongValue()
    {
        return _longValue;
    }

    public short getShortValue()
    {
        return _shortValue;
    }

    public void setBooleanValue(boolean booleanValue)
    {
        _booleanValue = booleanValue;
        
        fireObservedChange("booleanValue", booleanValue);
    }

    public void setDoubleValue(double doubleValue)
    {
        _doubleValue = doubleValue;
        
        fireObservedChange("doubleValue", _doubleValue);
    }

    public void setFloatValue(float floatValue)
    {
        _floatValue = floatValue;
        
        fireObservedChange("floatValue", _floatValue);
    }

    public void setLongValue(long longValue)
    {
        _longValue = longValue;
        
        fireObservedChange("longValue", _longValue);
    }

    public void setShortValue(short shortValue)
    {
        _shortValue = shortValue;
        
        fireObservedChange("shortValue", _shortValue);
    }

    public char getCharValue()
    {
        return _charValue;
    }

    public void setCharValue(char charValue)
    {
        _charValue = charValue;
        
        fireObservedChange("charValue", charValue);
    }

    public void change(IRequestCycle cycle)
    {
        setBooleanValue(true);
        setCharValue('H');
        setLongValue(1234567890123l);
        setFloatValue(-1.5f);
        setDoubleValue(22. / 7.);
        setShortValue((short)127);
        setByteValue((byte)27);
        setIntValue(23);
        setStringHolder(new StringHolder("Surprise!"));
    }

    public byte getByteValue()
    {
        return _byteValue;
    }

    public void setByteValue(byte byteValue)
    {
        _byteValue = byteValue;
        
        fireObservedChange("byteValue", byteValue);
    }

    public int getIntValue()
    {
        return _intValue;
    }

    public void setIntValue(int intValue)
    {
        _intValue = intValue;
        
        fireObservedChange("intValue", intValue);
    }

    public StringHolder getStringHolder()
    {
        return _stringHolder;
    }

    public void setStringHolder(StringHolder stringHolder)
    {
        _stringHolder = stringHolder;
        
        fireObservedChange("stringHolder", stringHolder);
    }

}
