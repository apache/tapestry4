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

package org.apache.tapestry.resolver;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.INamespace;

/**
 * Messages for the resolver package.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
class ResolverMessages
{
    private static final MessageFormatter _formatter =
        new MessageFormatter(ResolverMessages.class, "ResolverStrings");

    public static String noSuchComponentType(String type, INamespace namespace)
    {
        return _formatter.format("no-such-component-type", type, namespace.getNamespaceId());
    }

    public static String noSuchPage(String name, INamespace namespace)
    {
        return _formatter.format("no-such-page", name, namespace.getNamespaceId());
    }
}
