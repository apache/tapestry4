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
 *  An element that may be asked to render itself to an
 *  {@link IMarkupWriter} using a {@link IRequestCycle}.
 *
 *  <p>This primarily includes {@link IComponent} and {@link IPage},
 *  but also extends to other things, such as objects responsible for
 *  rendering static markup text.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public interface IRender
{
    /**
     *  The principal rendering/rewinding method.  This will cause
     *  the receiving component to render its top level elements (HTML
     *  text and components).
     *
     *  <p>Renderring and rewinding are the exact same process.  The
     *  same code that renders must be able to restore state by going
     *  through the exact same operations (even though the output is
     *  discarded).
     *
     **/

    public void render(IMarkupWriter writer, IRequestCycle cycle);
}