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

package org.apache.tapestry.html;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  Used to insert an image.  To create a rollover image, use the
 *  {@link Rollover} class, which integrates a link with the image assets
 *  used with the button.
 *
 *  [<a href="../../../../../ComponentReference/Image.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class Image extends AbstractComponent
{
    /**
     *  Renders the &lt;img&gt; element.
     *
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // Doesn't contain a body so no need to do anything on rewind (assumes no
        // sideffects to accessor methods via bindings).

        if (cycle.isRewinding())
            return;

        IAsset imageAsset = getImage();

        if (imageAsset == null)
            throw Tapestry.createRequiredParameterException(this, "image");

        String imageURL = imageAsset.buildURL(cycle);

        writer.beginEmpty("img");

        writer.attribute("src", imageURL);

        writer.attribute("border", getBorder());

        renderInformalParameters(writer, cycle);

        writer.closeTag();

    }

    public abstract IAsset getImage();

    public abstract int getBorder();
}