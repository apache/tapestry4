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
 *  Squeezes a {@link Integer}.  This adaptor claims all the digits as prefix
 *  characters, so its the very simplest conversion of all!
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

class IntegerAdaptor implements ISqueezeAdaptor
{
    /**
     *  Registers this adaptor using all nine digits and the minus sign.
     *
     **/

    public void register(DataSqueezer squeezer)
    {
        squeezer.register("-0123456789", Integer.class, this);
    }

    /**
     *  Simply invokes <code>toString()</code> on the data,
     *  which is actually type {@link Integer}.
     *
     **/

    public String squeeze(DataSqueezer squeezer, Object data)
    {
        return data.toString();
    }

    /**
     *  Constructs an {@link Integer} from the string.
     *
     **/

    public Object unsqueeze(DataSqueezer squeezer, String string)
    {
        return new Integer(string);
    }

}