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

package org.apache.tapestry.util.io;

import java.io.IOException;

import org.apache.commons.lang.enum.Enum;
import org.apache.commons.lang.enum.EnumUtils;

/**
 *  Adaptor for {@link Enum} classes.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/
public class EnumAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "E";
    private static final char SEPARATOR = '@';

    public String squeeze(DataSqueezer squeezer, Object o) throws IOException
    {
        Enum e = (Enum) o;
        return PREFIX + e.getClass().getName() + SEPARATOR + e.getName();
    }

    public Object unsqueeze(DataSqueezer squeezer, String str) throws IOException
    {
        int pos = str.indexOf(SEPARATOR);

        String className = str.substring(1, pos);
        String name = str.substring(pos + 1, str.length());

		Class enumClass = squeezer.getResolver().findClass(className);
		
        try
        {        	
            return EnumUtils.getEnum(enumClass, name);
        }
        catch (IllegalArgumentException ex)
        {
            throw new IOException(ex.getMessage());
        }
    }

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, Enum.class, this);
    }

}
