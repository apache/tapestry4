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

package org.apache.tapestry.engine;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IDirect;

/**
 * Parameter object used by {@link org.apache.tapestry.engine.DirectService}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DirectServiceParameter
{
    private IDirect _direct;

    private Object[] _serviceParameters;

    public DirectServiceParameter(IDirect direct)
    {
        this(direct, null);
    }

    public DirectServiceParameter(IDirect direct, Object[] serviceParameters)
    {
        Defense.notNull(direct, "direct");

        _direct = direct;
        _serviceParameters = serviceParameters;
    }

    public IDirect getDirect()
    {
        return _direct;
    }

    public Object[] getServiceParameters()
    {
        return _serviceParameters;
    }
}