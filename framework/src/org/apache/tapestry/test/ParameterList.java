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

package org.apache.tapestry.test;

import java.util.List;
import java.util.ArrayList;

/**
 * Contains a list of string query parameters for a 
 * {@link org.apache.tapestry.test.RequestDescriptor}.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class ParameterList
{
    /** List of String **/
    private List _params = new ArrayList();

    public void add(String value)
    {
        _params.add(value);
    }

    public String[] getValues()
    {
        return (String[]) _params.toArray(new String[_params.size()]);
    }
}
