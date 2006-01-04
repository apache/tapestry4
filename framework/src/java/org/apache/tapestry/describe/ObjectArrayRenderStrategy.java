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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Renders an object array as an unordered list; each element is recursively rendered.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ObjectArrayRenderStrategy implements RenderStrategy
{
    private RenderStrategy _renderStrategy;

    public void renderObject(Object object, IMarkupWriter writer, IRequestCycle cycle)
    {
        Object[] array = (Object[]) object;

        if (array.length == 0)
        {
            writer.begin("em");
            writer.print("empty list");
            writer.end();
            return;
        }

        writer.begin("ul");

        for (int i = 0; i < array.length; i++)
        {
            writer.begin("li");

            Object item = array[i];

            if (item == null)
            {
                writer.begin("em");
                writer.print("<NULL>");
                writer.end();
            }
            else
                _renderStrategy.renderObject(item, writer, cycle);

            writer.end();

        }

        writer.end();
    }

    public void setRenderStrategy(RenderStrategy renderStrategy)
    {
        _renderStrategy = renderStrategy;
    }

}
