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

package org.apache.tapestry.workbench.chart;

import java.io.Serializable;

/**
 *  An single point of data in the plot.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 *
 **/

public class PlotValue implements Serializable
{
    private String name;
    private int value;

    public PlotValue()
    {
    }

    public PlotValue(String name, int value)
    {
        this.name = name;
        this.value = value;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("PlotValue@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append('[');
        buffer.append(name);
        buffer.append(' ');
        buffer.append(value);
        buffer.append(']');

        return buffer.toString();
    }
}