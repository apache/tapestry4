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

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  Inserts formatted text (possibly collected using a {@link org.apache.tapestry.form.TextArea} 
 *  component.
 * 
 *  [<a href="../../../../../ComponentReference/InsertText.html">Component Reference</a>]
 *
 *  <p>To maintain the line breaks provided originally, this component will
 *  break the input into individual lines and insert additional
 *  HTML to make each line seperate.
 *
 *  <p>This can be down more simply, using the &lt;pre&gt; HTML element, but
 *  that usually renders the text in a non-proportional font.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class InsertText extends AbstractComponent
{
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        String value = getValue();

        if (value == null)
            return;

        StringReader reader = null;
        LineNumberReader lineReader = null;
        InsertTextMode mode = getMode();

        try
        {
            reader = new StringReader(value);

            lineReader = new LineNumberReader(reader);

            int lineNumber = 0;

            while (true)
            {
                String line = lineReader.readLine();

                // Exit loop at end of file.

                if (line == null)
                    break;

                mode.writeLine(lineNumber, line, writer);

                lineNumber++;
            }

        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("InsertText.conversion-error"),
                this,
                null,
                ex);
        }
        finally
        {
            close(lineReader);
            close(reader);
        }

    }

    private void close(Reader reader)
    {
        if (reader == null)
            return;

        try
        {
            reader.close();
        }
        catch (IOException e)
        {
        }
    }

    public abstract InsertTextMode getMode();

    public abstract void setMode(InsertTextMode mode);

    public abstract String getValue();

    /**
     * Sets the mode parameter property to its default,
     * {@link InsertTextMode#BREAK}.
     * 
     * @since 3.0
     */
    protected void finishLoad()
    {
        setMode(InsertTextMode.BREAK);
    }

}