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

package org.apache.tapestry.contrib.palette;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * Used to hold options editable by a Palette component, so that they may
 * be sorted into an appropriate order.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class PaletteOption implements IRender
{
    private String _value;
    private String _label;

    public PaletteOption(String value, String label)
    {
        _value = value;
        _label = label;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        writer.begin("option");
        writer.attribute("value", _value);
        writer.print(_label);
        writer.end(); // <option>
        writer.println();
    }

    public String getLabel()
    {
        return _label;
    }

    public String getValue()
    {
        return _value;
    }

}
