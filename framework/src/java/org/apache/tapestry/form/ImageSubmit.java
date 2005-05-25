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

package org.apache.tapestry.form;

import java.awt.Point;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 * Used to create an image button inside a {@link Form}. Although it is occasionally useful to know
 * the {@link Point}on the image that was clicked (i.e., use the image as a kind of image map,
 * which was the original intent of the HTML element), it is more commonly used to provide a graphic
 * image for the user to click, rather than the rather plain &lt;input type=submit&gt;. [ <a
 * href="../../../../../ComponentReference/ImageSubmit.html">Component Reference </a>]
 * 
 * @author Howard Lewis Ship
 */

public abstract class ImageSubmit extends Submit
{
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        if (form.wasPrerendered(writer, this))
            return;

        boolean rewinding = form.isRewinding();

        String nameOverride = getNameOverride();

        String name = nameOverride == null ? form.getElementId(this) : form.getElementId(
                this,
                nameOverride);

        if (rewinding)
        {
            // If disabled, do nothing.

            if (isDisabled())
                return;

            // Image clicks get submitted as two request parameters:
            // foo.x and foo.y

            String parameterName = name + ".x";

            String value = cycle.getParameter(parameterName);

            if (value != null)
                handleClick(cycle, form);

            return;
        }

        // Not rewinding, do the real render

        boolean disabled = isDisabled();
        IAsset disabledImage = getDisabledImage();

        IAsset finalImage = (disabled && disabledImage != null) ? disabledImage : getImage();

        String imageURL = finalImage.buildURL(cycle);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", name);

        if (disabled)
            writer.attribute("disabled", "disabled");

        // NN4 places a border unless you tell it otherwise.
        // IE ignores the border attribute and never shows a border.

        writer.attribute("border", 0);

        writer.attribute("src", imageURL);

        renderInformalParameters(writer, cycle);

        writer.closeTag();
    }

    void handleClick(IRequestCycle cycle, IForm form)
    {
        // The point parameter is not really used, unless the
        // ImageButton is used for its original purpose (as a kind
        // of image map). In modern usage, we only care about
        // whether the user clicked on the image (and thus submitted
        // the form), not where in the image the user actually clicked.

        if (isParameterBound("point"))
        {
            int x = Integer.parseInt(cycle.getParameter(getName() + ".x"));
            int y = Integer.parseInt(cycle.getParameter(getName() + ".y"));

            setPoint(new Point(x, y));
        }

        super.handleClick(cycle, form);
    }

    /** parameter */
    public abstract IAsset getDisabledImage();

    /** parameter */
    public abstract IAsset getImage();

    /** parameter */
    public abstract String getNameOverride();

    /** parameter */
    public abstract void setPoint(Point point);

    protected void prepareForRender(IRequestCycle cycle)
    {
        super.prepareForRender(cycle);

        if (getImage() == null)
            throw Tapestry.createRequiredParameterException(this, "image");
    }

}