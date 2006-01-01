// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.services;

import org.apache.hivemind.Messages;
import org.apache.tapestry.IComponent;

/**
 *  Defines an object that can provide a component with its
 *  {@link org.apache.hivemind.Messages}.
 *
 *  @author Howard Lewis Ship
 *  @since 2.0.4
 *
 */

public interface ComponentMessagesSource
{
    public Messages getMessages(IComponent component);
}
