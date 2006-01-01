// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.util.io;

import org.apache.tapestry.services.DataSqueezer;

/**
 * Squeezes a Character.
 * 
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class CharacterAdaptor implements SqueezeAdaptor
{
    private static final String PREFIX = "c";

    private static final char PREFIX_CH = 'c';

    public String getPrefix()
    {
        return PREFIX;
    }

    public Class getDataClass()
    {
        return Character.class;
    }

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        Character charData = (Character) data;
        char value = charData.charValue();

        char[] buffer = new char[]
        { PREFIX_CH, value };

        return new String(buffer);
    }

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Character(string.charAt(1));
    }

}
