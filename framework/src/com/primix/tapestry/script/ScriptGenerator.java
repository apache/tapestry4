package com.primix.tapestry.script;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import java.util.*;
import java.io.*;
import org.xml.sax.*;

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
 *  with a set of substitutions.  The template is an XML document (see the class
 *  {@link ScriptParser} for details on how it is implemented.
 *
 *  <p>A problem: this class has a dependency on class {@link Body}
 *  (from package com.primix.tapestry.components), but class {@link Rollover} (from the same
 *  package) has a dependency on this class.  That is somewhat icky (see
 *  Java 2 Performance and Idiom Guide for why) and needs to be fixed in some way.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class ScriptGenerator
{
    private ITemplateToken[] bodyTokens;
    private ITemplateToken[] initTokens; 
    private String resourcePath;

    /**
     *  Constructs a {@link InputSource} around the {@link InputStream} and
     *  invokes the standard constructor.
     *
     */

    public ScriptGenerator(InputStream stream, String resourcePath)
    throws ScriptParseException
    {
        this(new InputSource(stream), resourcePath);
    }

    /**
     * Standard constructor, takes an {@link InputSource} which will be parsed,
     * and the resourcePath (a String used to identify what the InputSource
     * is parsing, used in any error message).
     *
     */

    public ScriptGenerator(InputSource inputSource, String resourcePath)
    throws ScriptParseException
    {
        this.resourcePath = resourcePath;

        parseTemplate(inputSource, resourcePath);
    }

    private void parseTemplate(InputSource inputSource, String resourcePath)
    throws ScriptParseException
    {
        ScriptParser parser;

        parser = new ScriptParser(inputSource, resourcePath);

        parser.parse();

        bodyTokens = parser.getBodyTokens();
        initTokens = parser.getInitializationTokens();
    }

    /**
     *  Interacts with the {@link Body} component to generate the Script
     *  in the proper way.
     *
     */

    public void generateScript(Body body, Map symbols)
    {
        if (bodyTokens != null)
            body.addOtherScript(generateScript(bodyTokens, symbols));

        if (initTokens != null)
            body.addOtherInitialization(generateScript(initTokens, symbols));
    }

    /**
     *  Extracts the {@link Body} (via {@link Body#get(IRequestCycle)})
     *  and invokes {@link #generateScript(Body, Map)}.
     *
     */

    public void generateScript(IRequestCycle cycle, Map symbols)
    {
        Body body;

        body = Body.get(cycle);

        if (body == null)
            throw new ApplicationRuntimeException(
                "GenerateScript requires a Body component.");

        generateScript(body, symbols);
    }


    private String generateScript(ITemplateToken[] tokens, Map symbols)
    {
        StringWriter writer = null;

        try
        {
            // The trick would be to figure out the best initial size
            // for the StringWriter.

            writer = new StringWriter();

            for (int i = 0; i < tokens.length; i++)
                tokens[i].write(writer, symbols);

            return writer.toString();
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                "Unexpected exception processing script " + resourcePath + ".", ex);
        }
        finally
        {
            close(writer);
        }
    }

    private void close(Writer writer)
    {
        if (writer != null)
        {
            try
            {
                writer.close();
            }
            catch (IOException ex)
            {
                // Ignore.
            }
        }
    }
}