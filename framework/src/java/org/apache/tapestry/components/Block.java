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

package org.apache.tapestry.components;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Prevents its contents from being rendered until triggered by an {@link RenderBlock} component. [<a
 * href="../../../../../ComponentReference/Block.html">Component Reference</a>]
 * <p>
 * Block and {@link RenderBlock} are used to build a certain class of complicated component that
 * can't be assembled using the normal wrapping containment. Such a super component would have two
 * or more sections that need to be supplied by the containing page (or component).
 * <p>
 * Using Blocks, the blocks can be provided as parameters to the super component.
 * <p>
 * The invoker property gives the components inside the block access to the component (typically an
 * {@link RenderBlock}) that rendered the block. More often, the {@link #getParameter(String)}
 * method is used to get parameters <em>of the invoking component</em>.
 * 
 * @author Howard Lewis Ship
 * @since 0.2.9
 */

public abstract class Block extends AbstractComponent
{
    private IComponent _invoker;

    /**
     * Provides access to the invoking component's parameters.
     * 
     * @since 4.0
     */

    public Object getParameter(String name)
    {
        return _invoker.getBinding(name).getObject();
    }

    public void renderForComponent(IMarkupWriter writer, IRequestCycle cycle, IComponent invoker)
    {
        Defense.notNull(invoker, "invoker");

        IComponent oldInvoker = _invoker;

        try
        {
            _invoker = invoker;
            renderBody(writer, cycle);
        }
        finally
        {
            _invoker = oldInvoker;

        }
    }

    /**
     * Does nothing; the idea of a Block is to defer the rendering of the body of the block until an
     * {@link RenderBlock} forces it out.
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // Nothing!
    }

    /**
     * @deprecated Use {@link #getInvoker()} instead.
     */
    public IComponent getInserter()
    {
        return _invoker;
    }

    /**
     * Returns the object which invoked this Block's
     * {@link #renderForComponent(IMarkupWriter, IRequestCycle, IComponent)} method. This is often
     * used to access the informal parameters of a {@link RenderBlock} component.
     * 
     * @since 4.0
     */
    public IComponent getInvoker()
    {
        return _invoker;
    }

    /**
     * Used for testing only.
     * 
     * @since 4.0
     */

    void setInvoker(IComponent invoker)
    {
        _invoker = invoker;
    }
}