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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;

import org.apache.hivemind.Location;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Adapter for displaying {@link org.apache.hivemind.Location}objects as HTML.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class LocationRenderableAdapter implements RenderableAdapter
{
    /**
     * Lines before and after the actual location to display.
     */
    private static final int RANGE = 5;

    public void renderObject(Object object, IMarkupWriter writer, IRequestCycle cycle)
    {
        Location l = (Location) object;

        // Always print out the location as a string.

        writer.print(l.toString());

        URL url = l.getResource().getResourceURL();

        if (url == null)
            return;

        writeResourceContent(writer, url, l.getLineNumber());
    }

    private void writeResourceContent(IMarkupWriter writer, URL url, int lineNumber)
    {
        LineNumberReader reader = null;

        try
        {
            reader = new LineNumberReader(new BufferedReader(
                    new InputStreamReader(url.openStream())));

            writer.beginEmpty("br");
            writer.begin("table");
            writer.attribute("class", "location-content");

            while (true)
            {
                String line = reader.readLine();

                if (line == null)
                    break;

                int currentLine = reader.getLineNumber();

                if (currentLine > lineNumber + RANGE)
                    break;

                if (currentLine < lineNumber - RANGE) continue;

                writer.begin("tr");
                
                if (currentLine == lineNumber)
                    writer.attribute("class", "target-line");
                
                writer.begin("td");
                writer.attribute("class", "line-number");
                writer.print(currentLine);
                writer.end();
                
                writer.begin("td");
                writer.print(line);
                writer.end("tr");
                writer.println();                
            }

            reader.close();
            reader = null;
        }
        catch (Exception ex)
        {
            // Ignore it.
        }
        finally
        {
            writer.end("table");
            close(reader);
        }
    }

    private void close(Reader reader)
    {
        try
        {
            if (reader != null)
                reader.close();
        }
        catch (IOException ex)
        {
            // Ignore
        }
    }

}