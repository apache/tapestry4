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
package net.sf.tapestry.html;

import org.apache.commons.lang.enum.Enum;

import net.sf.tapestry.IMarkupWriter;

/**
 *  Defines a number of ways to format multi-line text for proper
 *  renderring.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public abstract class InsertTextMode extends Enum
{
    /**
     *  Mode where each line (after the first) is preceded by a &lt;br&gt; tag.
     *
     **/

    public static final InsertTextMode BREAK = new BreakMode();

    /**
     *  Mode where each line is wrapped with a &lt;p&gt; element.
     *
     **/

    public static final InsertTextMode PARAGRAPH = new ParagraphMode();

    protected InsertTextMode(String name)
    {
        super(name);
    }

    /**
     *  Invoked by the {@link InsertText} component to write the next line.
     *
     *  @param lineNumber the line number of the line, starting with 0 for the first line.
     *  @param line the String for the current line.
     *  @param writer the {@link IMarkupWriter} to send output to.
     **/

    public abstract void writeLine(
        int lineNumber,
        String line,
        IMarkupWriter writer);

    private static class BreakMode extends InsertTextMode
    {
        private BreakMode()
        {
            super("BREAK");
        }

        public void writeLine(int lineNumber, String line, IMarkupWriter writer)
        {
            if (lineNumber > 0)
                writer.beginEmpty("br");

            writer.print(line);
        }
    }

    private static class ParagraphMode extends InsertTextMode
    {
        private ParagraphMode()
        {
            super("PARAGRAPH");
        }

        public void writeLine(int lineNumber, String line, IMarkupWriter writer)
        {
            writer.begin("p");

            writer.print(line);

            writer.end();
        }
    }

}