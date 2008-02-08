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

import org.apache.commons.io.IOUtils;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.text.Format;

/**
 * Used to insert some text (from a parameter) into the HTML. [ <a
 * href="../../../../../components/general/insert.html">Component Reference
 * </a>]
 *
 * @author Howard Lewis Ship
 */

public abstract class Insert extends AbstractComponent
{

    /**
     * Prints its value parameter, possibly formatted by its format parameter.
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (cycle.isRewinding())
            return;

        Object value = getValue();

        if (value == null)
            return;

        String insert = null;

        Format format = getFormat();

        if (format == null)
        {
            insert = value.toString();
        }
        else
        {
            try
            {
                insert = format.format(value);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(ComponentMessages
                        .unableToFormat(this, value, ex), this, getBinding(
                        "format").getLocation(), ex);
            }
        }

        boolean render = getRenderTag() || isParameterBound("class");

        if (render)
        {
            writer.begin(getTemplateTagName());

            renderInformalParameters(writer, cycle);

            if (getId() != null && !isParameterBound("id"))
                renderIdAttribute(writer, cycle);
        }

        printText(writer, cycle, insert);

        if (render)
            writer.end();
    }

    protected void printText(IMarkupWriter writer, IRequestCycle cycle, String value)
    {
        if (getMode() == null)
        {
            writer.print(value, getRaw());
            return;
        }

        StringReader reader = null;
        LineNumberReader lineReader = null;
        InsertMode mode = getMode();
        boolean raw = getRaw();

        try {
            reader = new StringReader(value);
            lineReader = new LineNumberReader(reader);

            int lineNumber = 0;

            while(true)
            {
                String line = lineReader.readLine();

                // Exit loop at end of file.

                if (line == null)
                    break;

                mode.writeLine(lineNumber, line, writer, raw);

                lineNumber++;
            }

        } catch (IOException ex)
        {
            throw new ApplicationRuntimeException(ComponentMessages.textConversionError(ex), this, null, ex);
        } finally
        {
            IOUtils.closeQuietly(lineReader);
            IOUtils.closeQuietly(reader);
        }
    }

    public abstract Object getValue();

    public abstract Format getFormat();

    public abstract boolean getRaw();

    public abstract boolean getRenderTag();

    public abstract InsertMode getMode();
}
