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

package org.apache.tapestry.junit.mock.c9;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 *  One giant test page to test all kinds of persistent properties.  Eight
 *  scalar types and an object type.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 **/

public abstract class Seven extends BasePage
{
    abstract public boolean getBooleanValue();
    abstract public byte getByteValue();
    abstract public char getCharValue();
    abstract public double getDoubleValue();
    abstract public float getFloatValue();
    abstract public int getIntValue();
    abstract public long getLongValue();
    abstract public short getShortValue();
    abstract public String getStringValue();
    abstract public void setBooleanValue(boolean booleanValue);
    abstract public void setByteValue(byte byteValue);
    abstract public void setCharValue(char charValue);
    abstract public void setDoubleValue(double doubleValue);
    abstract public void setFloatValue(float floatValue);
    abstract public void setIntValue(int intValue);
    abstract public void setLongValue(long longValue);
    abstract public void setShortValue(short shortValue);
    abstract public void setStringValue(String stringValue);

    public void finishLoad()
    {
        setBooleanValue(true);
        setByteValue((byte) 'A');
        setShortValue((short) 97);
        setCharValue('Z');
        setDoubleValue(3.2);
        setFloatValue(-22.7f);
        setIntValue(100);
        setLongValue(32000000);
        setStringValue("Magic");
    }

    public void update(IRequestCycle cycle)
    {
        setBooleanValue(false);
        setByteValue((byte) 'Q');
        setShortValue((short) 21);
        setCharValue('f');
        setDoubleValue(9.87);
        setFloatValue(-202.2f);
        setIntValue(3097);
        setLongValue(132000001);
        setStringValue("Marker");
    }

}
