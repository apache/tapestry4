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

package org.apache.tapestry.util.exception;

import java.io.Serializable;

/**
 *  Captures a name/value property pair from an exception.  Part of
 *  an {@link ExceptionDescription}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class ExceptionProperty implements Serializable
{
    /**
     *  @since 2.0.4
     * 
     **/
    
    private static final long serialVersionUID = -4598312382467505134L;
    private String name;
    private String value;

    public ExceptionProperty(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}