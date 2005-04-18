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

import java.net.URL;

import org.apache.hivemind.Location;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.LocationImpl;
import org.apache.hivemind.util.URLResource;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.describe.LocationRenderStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestLocationRenderStrategy extends BaseDescribeTestCase
{
    private Resource newResource(URL url)
    {
        MockControl control = newControl(Resource.class);
        Resource resource = (Resource) control.getMock();

        resource.getResourceURL();
        control.setReturnValue(url);

        return resource;
    }

    private Location newLocation(String file, int lineNumber)
    {
        URL url = getClass().getResource(file);

        Resource resource = new URLResource(url);

        return new LocationImpl(resource, lineNumber);
    }

    private void train(IMarkupWriter writer, int startLine, int lineNumber, String[] lines)
    {
        writer.beginEmpty("br");
        writer.begin("table");
        writer.attribute("class", "location-content");

        for (int i = 0; i < lines.length; i++)
        {
            int currentLine = startLine + i;

            writer.begin("tr");

            if (currentLine == lineNumber)
                writer.attribute("class", "target-line");

            writer.begin("td");
            writer.attribute("class", "line-number");
            writer.print(currentLine);
            writer.end();

            writer.begin("td");
            writer.print(lines[i]);
            writer.end("tr");
            writer.println();
        }

        writer.end("table");
    }

    public void testNoURL()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        Resource resource = newResource(null);
        MockControl lc = newControl(Location.class);
        Location l = (Location) lc.getMock();

        l.getResource();
        lc.setReturnValue(resource);

        writer.print(l.toString());

        replayControls();

        new LocationRenderStrategy().renderObject(l, writer, cycle);

        verifyControls();
    }

    /**
     * Test when the highlight line is close to the end of the document.
     */
    public void testShortContent()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        Location l = newLocation("Short.txt", 7);

        writer.print(l.toString());

        train(writer, 2, 7, new String[]
        { "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" });

        replayControls();

        new LocationRenderStrategy().renderObject(l, writer, cycle);

        verifyControls();
    }

    /**
     * Test when the highlight line is close to the end of the document.
     */
    public void testLongContent()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        Location l = newLocation("Long.txt", 3);

        writer.print(l.toString());

        train(writer, 1, 3, new String[]
        { "Line One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight" });

        replayControls();

        new LocationRenderStrategy().renderObject(l, writer, cycle);

        verifyControls();
    }
}