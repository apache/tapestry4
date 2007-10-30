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

import org.apache.hivemind.Location;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import java.io.*;
import java.net.URL;

/**
 * Adapter for displaying {@link org.apache.hivemind.Location}&nbsp;objects as
 * HTML. This may include showing the content of the
 * {@link org.apache.hivemind.Resource}, with the line indicated in the
 * Location highlighted.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class LocationRenderStrategy implements RenderStrategy
{

    /**
     * Lines before and after the actual location to display.
     */
    private static final int RANGE = 5;

    public void renderObject(Object object, IMarkupWriter writer,
            IRequestCycle cycle)
    {
        Location l = (Location) object;

        // Always print out the location as a string.

        writer.print(l.toString());

        int lineNumber = l.getLineNumber();

        if (lineNumber < 1)
            return;

        URL url = l.getResource().getResourceURL();

        if (url == null)
            return;

        writeResourceContent(writer, url, lineNumber);
    }

    private void writeResourceContent(IMarkupWriter writer, URL url, int lineNumber)
    {
        LineNumberReader reader = null;

        try
        {
            reader = new LineNumberReader(new BufferedReader( new InputStreamReader(url.openStream())));

            writer.beginEmpty("br");
            writer.begin("table");
            writer.attribute("class", "location-content");
            writer.attribute("cellspacing", "0");
            writer.attribute("cellpadding", "0");

            while(true)
            {
                String line = reader.readLine();

                if (line == null)
                    break;

                int currentLine = reader.getLineNumber();

                if (currentLine > lineNumber + RANGE)
                    break;

                if (currentLine < lineNumber - RANGE)
                    continue;

                writer.begin("tr");

                if (currentLine == lineNumber)
                    writer.attribute("class", "target-line");
                
                writer.begin("td");
                writer.attribute("class", "line-number");
                writer.print(currentLine);
                writer.end();

                writer.begin("td");

                // pretty print tabs and spaces properly
                
                String spacers = extractWhitespaceStart(line);

                if (spacers != null && spacers.length() > 0)
                {
                    writer.printRaw(spacers);
                }

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

    /**
     * Finds any tab or whitespace characters in the beginning of this string - up
     * to the first occurrence of normal character data and returns it.
     *
     * @param input The string to extract whitespace/tab characters from.
     *
     * @return The whitespace/tab characters found, or null if none found.
     */
    String extractWhitespaceStart(String input)
    {
        if (input == null || input.length() < 1)
            return null;

        char[] vals = input.toCharArray();
        StringBuffer ret = new StringBuffer();
        
        for (int i=0; i < vals.length; i++)
        {
            if (Character.isWhitespace(vals[i]))
            {
                ret.append("&nbsp;");
                continue;
            }

            if (vals[i] == '\t')
            {
                ret.append("&nbsp;&nbsp;");
                continue;
            }

            break;
        }
        
        return ret.toString();
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
