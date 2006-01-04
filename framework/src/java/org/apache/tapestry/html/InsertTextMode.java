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

package org.apache.tapestry.html;

import org.apache.tapestry.IMarkupWriter;

/**
 * Defines a number of ways to format multi-line text for proper renderring.
 * 
 * @author Howard Lewis Ship
 */

public abstract class InsertTextMode
{
    /**
     * Mode where each line (after the first) is preceded by a &lt;br&gt; tag.
     */

    public static final InsertTextMode BREAK = new BreakMode();

    /**
     * Mode where each line is wrapped with a &lt;p&gt; element.
     */

    public static final InsertTextMode PARAGRAPH = new ParagraphMode();

    private final String _name;

    protected InsertTextMode(String name)
    {
        _name = name;
    }

    public String toString()
    {
        return "InsertTextMode[" + _name + "]";
    }

    /**
     * Invoked by the {@link InsertText} component to write the next line.
     * 
     * @param lineNumber
     *            the line number of the line, starting with 0 for the first line.
     * @param line
     *            the String for the current line.
     * @param writer
     *            the {@link IMarkupWriter} to send output to.
     * @param raw
     *            if true, then the output should be unfiltered
     */

    public abstract void writeLine(int lineNumber, String line, IMarkupWriter writer, boolean raw);

    private static class BreakMode extends InsertTextMode
    {
        private BreakMode()
        {
            super("BREAK");
        }

        public void writeLine(int lineNumber, String line, IMarkupWriter writer, boolean raw)
        {
            if (lineNumber > 0)
                writer.beginEmpty("br");

            writer.print(line, raw);
        }
    }

    private static class ParagraphMode extends InsertTextMode
    {
        private ParagraphMode()
        {
            super("PARAGRAPH");
        }

        public void writeLine(int lineNumber, String line, IMarkupWriter writer, boolean raw)
        {
            writer.begin("p");

            writer.print(line, raw);

            writer.end();
        }
    }

}