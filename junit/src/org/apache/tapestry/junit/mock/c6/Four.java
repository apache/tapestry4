/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
