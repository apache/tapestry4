package com.primix.tapestry.script;

import java.util.*;
import java.io.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  A class to assist in creating scripts (typically, JavaScript) from a template,
 *  with a set of substitutions.
 *
 *  <p>Scripts are very simple; just ASCII text.  Most of the script is passed through
 *  unchanged.  In the script, there can be substition constructs, a symbol name enclosed
 *  in dollar signs, ex: <code>$<i>name</i>$</code>.
 *
 * <p>When a script is generated, a symbol table (in the form of a {@link Map}) is
 *  passed in, to facilitate symbol subtitutions.
 *
 * <p>If we ever add any more complicated constructs (i.e., conditional and looping
 * operations), it may be time to scrap this and use an XML document, but for the
 * meantime, its lean-and-mean.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class ScriptGenerator
{
    private char[] template;
    private ITemplateToken[] tokens;

    /**
     *  Creates generator using the given String as the template.
     *
     */

    public ScriptGenerator(String template)
    {
        this.template = template.toCharArray();
    }

    /**
     *  Retains the character array as the template (note that the constructor
     *  does <em>not</em> make a copy).
     *
     */

    public ScriptGenerator(char[] template)
    {
        this.template = template;
    }

    /**
     *  Constructs a script using the contents of the reader as a template.
     *  The reader is read to the end, but not closed.
     *
     */

    public ScriptGenerator(Reader reader)
    throws IOException
    {
        readTemplate(reader);
    }

    /**
     *  Constructs a script using the contents of the stream as a template.
     *  The stream is read to the end, but not closed.  Uses an
     *  {@link InputStreamReader} wrapped by a {@link BufferedReader}.
     *
     */

    public ScriptGenerator(InputStream stream)
    throws IOException
    {
        readTemplate(stream);
    }

    private void readTemplate(Reader reader)
    throws IOException
    {
        char[] chunk = new char[1000];
        int charsRead;
        StringBuffer buffer = null;

        while (true)
        {
            charsRead = reader.read(chunk);
            if (charsRead < 0)
                break;

            if (buffer == null)
                buffer = new StringBuffer(charsRead);

            buffer.append(chunk, 0, charsRead);
        }

        // Now, copy the contents of the buffer
        // into the template char array.
                
        charsRead = buffer.length();
        template = new char[charsRead];

        buffer.getChars(0, charsRead, template, 0);
    }
    
    private void readTemplate(InputStream stream)
    throws IOException
    {
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try
        {
            isr = new InputStreamReader(stream);
            reader = new BufferedReader(isr);

            readTemplate(reader);
        }
        finally
        {
            close(reader);
            close(isr);
        }

    }


    /**
     *  Invoked to generate a script from the template.
     *  The symbols parameter is a {@link Map} of substitutions.
     *  Both the keys and values are Strings.
     *
     */

    public String generateScript(Map symbols)
    throws IOException
    {
        StringWriter writer = null;

        if (symbols == null)
            throw new NullPointerException(
                "ScriptGenerator.generateScript(): symbols parameter may not be null.");
        
        if (tokens == null)
            parseTokens();

        try
        {
            // The final string should be approximately the
            // same length as the template, if not a little bit
            // longer.

            writer = new StringWriter(template.length);

            for (int i = 0; i < tokens.length; i++)
                tokens[i].write(writer, symbols);

            return writer.toString();
        }   
        finally
        {
            close(writer);
        }
    }

    private void close(Writer writer)
    {
        try
        {
            if (writer != null)
                writer.close();
        }
        catch (IOException ex)
        {
            // Ignore.
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
            // Ignore.
        }
    }

    private static final int STATE_NORMAL = 0;
    private static final int STATE_COLLECT_NAME = 1;
    private static final int STATE_BACKSLASH = 2;

    private void parseTokens()
    {
        int start = 0;
        int blockLength = 0;
        int pos = 0;
        int length = template.length;
        char ch;
        int state = STATE_NORMAL;
        List list = new ArrayList();

        while (pos < length)
        {
            ch = template[pos++];

            switch (state)
            {
                case STATE_NORMAL:

                    if (ch == '$')
                    {
                        if (blockLength > 0)
                            list.add(buildStaticToken(start, blockLength));

                        // pos is now the first character in the symbol name.

                        start = pos;
                        blockLength = 0;

                        state = STATE_COLLECT_NAME;
                        continue;
                    }

                    if (ch == '\\')
                    {
                        // Terminate the current block (before the slash).

                        if (blockLength > 0)
                            list.add(buildStaticToken(start, blockLength));

                        // Start a new block with the character after
                        // the backslash.

                        start = pos;
                        blockLength = 0;
                        
                        state = STATE_BACKSLASH;
                        continue;
                    }

                    // An ordinary character, add it to the current block.
                    
                    blockLength++;
                    continue;

                case STATE_BACKSLASH:

                    // The character after a backslash is always added to the block,
                    // without interpretation.
                    
                    blockLength++;

                    state = STATE_NORMAL;
                    continue;

                default:
                    // STATE_COLLECT_NAME

                    if (ch == '$')
                    {
                        // error if start = end

                        list.add(buildSymbolToken(start, blockLength));

                        // Start a new static block just after
                        // the closing brace (though if that char
                        // is a '$', a new symbol will start instead).

                        start = pos;
                        blockLength = 0;
                        state = STATE_NORMAL;
                        continue;
                    }

                    // Non-'$', just collect it.
                    // TBD:  validate that the character is ok (i..e, alphabetic, numeric,
                    // etc.).

                    blockLength++;
                    continue;
            }
        }

        // Add an improperly terminated substitution as a static block.

        if (state != STATE_NORMAL)
            list.add(buildStaticToken(start - 1, blockLength + 1));
        else
            if (blockLength > 0)
                list.add(buildStaticToken(start, blockLength));

        length = list.size();
        tokens = new ITemplateToken[length];

        tokens = (ITemplateToken[])list.toArray(tokens);
    }

    private ITemplateToken buildStaticToken(int start, int length)
    {
        return new StaticToken(template, start, length);
    }

    private ITemplateToken buildSymbolToken(int start, int length)
    {
        String name;

        name = new String(template, start, length);

        return new SymbolToken(name);
    }
}