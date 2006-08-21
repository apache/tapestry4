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

package org.apache.tapestry.html;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.html.Image} component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestImage extends BaseComponentTestCase
{
    public void testRewinding()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        trainIsRewinding(cycle, true);

        Image image = (Image) newInstance(Image.class);

        replay();

        image.renderComponent(writer, cycle);

        verify();
    }

    public void testNoImage()
    {
        Location l = newLocation();
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IPage page = newPage();

        trainIsRewinding(cycle, false);

        trainGetPageName(page, "Fred");
        trainGetIdPath(page, null);

        Image image = newInstance(Image.class, new Object[]
        { "location", l, "id", "barney", "page", page, "container", page });

        replay();

        try
        {
            image.renderComponent(writer, cycle);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Value for parameter 'image' in component Fred/barney is null, and a non-null value is required.",
                    ex.getMessage());
            assertSame(image, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testRender()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle(false);
        
        IAsset asset = newAsset();
        trainBuildURL(asset, cycle, "/foo.gif");
        
        writer.beginEmpty("img");
        writer.attribute("src", "/foo.gif");
        
        IComponentSpecification spec = newSpec("border", null);
        IBinding informal = newBinding("0");
        
        writer.attribute("border", "0");

        writer.closeTag();
        
        replay();
        
        Image image = newInstance(Image.class, new Object[]
        { "image", asset, "specification", spec });
        
        image.setBinding("border", informal);

        image.renderComponent(writer, cycle);

        verify();
    }
}
