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

/**
 *  Squeezes a String (which is pretty simple, most of the time).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class StringAdaptor implements ISqueezeAdaptor
{
    private static final String PREFIX = "S";

    public void register(DataSqueezer squeezer)
    {
        squeezer.register(PREFIX, String.class, this);
    }

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        String string = (String) data;

        return PREFIX + string;
    }

    /**
     *  Strips the prefix from the string.  This method is only
     *  invoked by the {@link DataSqueezer} if the string leads
     *  with its normal prefix (an 'S').
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        if (string.length() == 1)
            return "";

        return string.substring(1);
    }
}