package com.primix.tapestry.components.html;

import com.primix.tapestry.*;
import com.primix.foundation.*;

public abstract class InsertTextMode
extends Enum
{
    /**
     *  Mode where each line (after the first) is preceded by a &lt;br&gt; tag.
     *
     */

    public static final InsertTextMode BREAK = new BreakMode();

    /**
     *  Mode where each line is wrapped with a &lt;p&gt; element.
     *
     */

    public static final InsertTextMode PARAGRAPH = new ParagraphMode();

    protected InsertTextMode(String enumerationId)
    {
        super(enumerationId);
    }

    /**
    *  Invoked by the {@link InsertText} component to write the next line.
    *
    *  @param lineNumber the line number of the line, starting with 0 for the first line.
    *  @param line the String for the current line.
    *  @param writer the {@link IResponseWriter} to send output to.
    */

    public abstract void writeLine(int lineNumber, String line, IResponseWriter writer);

    private static class BreakMode
    extends InsertTextMode
    {
        private BreakMode()
        {
            super("BREAK");
        }

        public void writeLine(int lineNumber, String line, IResponseWriter writer)
        {
            if (lineNumber > 0)
                writer.beginEmpty("br");

            writer.print(line);
        }

        private Object readResolve()
        {
            return getSingleton();
        }
    }

    private static class ParagraphMode
    extends InsertTextMode
    {
        private ParagraphMode()
        {
            super("PARAGRAPH");
        }

        public void writeLine(int lineNumber, String line, IResponseWriter writer)
        {
            writer.begin("p");

            writer.print(line);

            writer.end();
        }

        private Object readResolve()
        {
            return getSingleton();
        }
    }

}

