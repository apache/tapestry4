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
 * Implements {@link IRender}&nbsp;for a particular object by delegating to a
 * {@link org.apache.tapestry.describe.RenderStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class RenderBridge implements IRender
{
    private Object _object;

    private RenderStrategy _strategy;

    public RenderBridge(Object object, RenderStrategy strategy)
    {
        Defense.notNull(strategy, "strategy");

        _object = object;
        _strategy = strategy;
    }

    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        _strategy.renderObject(_object, writer, cycle);
    }
}