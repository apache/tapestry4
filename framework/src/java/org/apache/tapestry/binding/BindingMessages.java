// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.binding;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
class BindingMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(BindingMessages.class,
            "BindingStrings");

    public static String convertObjectError(IBinding binding, Throwable cause)
    {
        return _formatter.format("convert-object-error", binding.getParameterName(), cause);
    }

    public static String readOnlyBinding(IBinding binding)
    {
        return _formatter.format("read-only-binding", binding);
    }

    public static String missingAsset(IComponent component, String assetName)
    {
        return _formatter.format("missing-asset", component.getExtendedId(), assetName);
    }
}