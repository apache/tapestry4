/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.form;

import java.awt.Point;

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Used to create an image button inside a {@link Form}.  Although it
 *  is occasionally useful to know the {@link Point} on the image that was clicked
 *  (i.e., use the image as a kind of image map, which was the original intent
 *  of the HTML element), it is more commonly used to provide a graphic
 *  image for the user to click, rather than the rather plain &lt;input type=submit&gt;.
 *
 *  [<a href="../../../../../ComponentReference/ImageSubmit.html">Component Reference</a>]
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public abstract class ImageSubmit extends AbstractFormComponent
{

    private String _name;

    public abstract IBinding getPointBinding();

    public abstract IBinding getSelectedBinding();

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        String nameOverride = getNameOverride();

        if (nameOverride == null)
            _name = form.getElementId(this);
        else
            _name = form.getElementId(this, nameOverride);

        if (rewinding)
        {
            // If disabled, do nothing.

            if (isDisabled())
                return;

            RequestContext context = cycle.getRequestContext();

            // Image clicks get submitted as two request parameters: 
            // foo.x and foo.y

            String parameterName = _name + ".x";

            String value = context.getParameter(parameterName);

            if (value == null)
                return;

            // The point parameter is not really used, unless the
            // ImageButton is used for its original purpose (as a kind
            // of image map).  In modern usage, we only care about
            // whether the user clicked on the image (and thus submitted
            // the form), not where in the image the user actually clicked.

            IBinding pointBinding = getPointBinding();

            if (pointBinding != null)
            {
                int x = Integer.parseInt(value);

                parameterName = _name + ".y";
                value = context.getParameter(parameterName);

                int y = Integer.parseInt(value);

                pointBinding.setObject(new Point(x, y));
            }

            // Notify the application, by setting the select parameter
            // to the tag parameter.

            IBinding selectedBinding = getSelectedBinding();

            if (selectedBinding != null)
                selectedBinding.setObject(getTag());

            IActionListener listener = getListener();

            if (listener != null)
                listener.actionTriggered(this, cycle);

            return;
        }

        // Not rewinding, do the real render

        boolean disabled = isDisabled();
        IAsset disabledImage = getDisabledImage();

        IAsset finalImage = (disabled && disabledImage != null) ? disabledImage : getImage();

        String imageURL = finalImage.buildURL(cycle);

        writer.beginEmpty("input");
        writer.attribute("type", "image");
        writer.attribute("name", _name);

        if (disabled)
            writer.attribute("disabled");

        // NN4 places a border unless you tell it otherwise.
        // IE ignores the border attribute and never shows a border.

        writer.attribute("border", 0);

        writer.attribute("src", imageURL);

        generateAttributes(writer, cycle);

        writer.closeTag();
    }

    public abstract boolean isDisabled();

    public abstract IAsset getDisabledImage();

    public abstract IAsset getImage();

    public abstract IActionListener getListener();

    public String getName()
    {
        return _name;
    }

    public abstract Object getTag();

    public abstract String getNameOverride();

    protected void prepareForRender(IRequestCycle cycle) throws RequestCycleException
    {
        super.prepareForRender(cycle);

        if (getImage() == null)
            throw new RequiredParameterException(this, "image", getBinding("image"));
    }

}