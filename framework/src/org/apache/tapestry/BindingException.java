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

package org.apache.tapestry;

/**
 *  A general exception describing an {@link IBinding}
 *  and an {@link IComponent}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class BindingException extends ApplicationRuntimeException
{
    private transient IBinding _binding;

    public BindingException(String message, IBinding binding)
    {
        this(message, binding, null);
    }

    public BindingException(String message, IBinding binding, Throwable rootCause)
    {
        this(message, null, null, binding, rootCause);
    }


    public BindingException(
        String message,
        Object component,
        ILocation location,
        IBinding binding,
        Throwable rootCause)
    {
        super(
            message,
            component,
            Tapestry.findLocation(new Object[] { location, binding, component }),
            rootCause);

        _binding = binding;
    }

    public IBinding getBinding()
    {
        return _binding;
    }
}