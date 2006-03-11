// Copyright 2004, 2005 The Apache Software Foundation
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
 * Squeezes a {@link Byte}.
 * 
 * @author Howard Lewis Ship
 */

public class ByteAdaptor implements SqueezeAdaptor
{
    private static final String PREFIX = "b";

    public String getPrefix()
    {
        return PREFIX;
    }

    public Class getDataClass()
    {
        return Byte.class;
    }

    /**
     * Invoked <code>toString()</code> on data (which is type {@link Byte}), and prefixs the
     * result.
     */

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return PREFIX + data.toString();
    }

    /**
     * Constructs an {@link Byte} from the string, after stripping the prefix.
     */

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Byte(string.substring(1));
    }

}