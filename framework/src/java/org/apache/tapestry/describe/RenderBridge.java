// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.describe;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * Implements {@link IRender}for a particular object by delegating to a
 * {@link org.apache.tapestry.describe.RenderableAdapter}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RenderBridge implements IRender
{
    private Object _object;

    private RenderableAdapter _adapter;

    public RenderBridge(Object object, RenderableAdapter adapter)
    {
        Defense.notNull(adapter, "adapter");

        _object = object;
        _adapter = adapter;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        _adapter.renderObject(_object, writer, cycle);
    }
}