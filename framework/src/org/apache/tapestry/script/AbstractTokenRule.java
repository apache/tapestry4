/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.script;

import org.apache.commons.hivemind.Location;
import org.apache.tapestry.util.xml.BaseRule;
import org.apache.tapestry.util.xml.RuleDirectedParser;

/**
 * Base class for the rules that build {@link org.apache.tapestry.script.IScriptToken}s.
 * Used with classes that can contain a mix of text and elements (those that
 * accept "full content").
 * 
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 **/

abstract class AbstractTokenRule extends BaseRule
{

    /**
     * Adds a token to its parent, the top object on the stack.
     */
    protected void addToParent(RuleDirectedParser parser, IScriptToken token)
    {
        IScriptToken parent = (IScriptToken) parser.peek();

        parent.addToken(token);
    }

    /**
     * Peeks at the top object on the stack (which must be a {@link IScriptToken}),
     * and converts the text into a series of {@link StaticToken} and
     * {@link InsertToken}s.
     */

    public void content(RuleDirectedParser parser, String content)
    {
        IScriptToken token = (IScriptToken) parser.peek();

        addTextTokens(token, content, parser.getLocation());
    }

    private static final int STATE_START = 0;
    private static final int STATE_DOLLAR = 1;
    private static final int STATE_COLLECT_EXPRESSION = 2;

    /**
     * Parses the provided text and converts it into a series of 
     */
    protected void addTextTokens(IScriptToken token, String text, Location location)
    {
        char[] buffer = text.toCharArray();
        int state = STATE_START;
        int blockStart = 0;
        int blockLength = 0;
        int expressionStart = -1;
        int expressionLength = 0;
        int i = 0;
        int braceDepth = 0;

        while (i < buffer.length)
        {
            char ch = buffer[i];

            switch (state)
            {
                case STATE_START :

                    if (ch == '$')
                    {
                        state = STATE_DOLLAR;
                        i++;
                        continue;
                    }

                    blockLength++;
                    i++;
                    continue;

                case STATE_DOLLAR :

                    if (ch == '{')
                    {
                        state = STATE_COLLECT_EXPRESSION;
                        i++;

                        expressionStart = i;
                        expressionLength = 0;
                        braceDepth = 1;

                        continue;
                    }

                    // The '$' was just what it was, not the start of a ${} expression
                    // block, so include it as part of the static text block.

                    blockLength++;

                    state = STATE_START;
                    continue;

                case STATE_COLLECT_EXPRESSION :

                    if (ch != '}')
                    {
                        if (ch == '{')
                            braceDepth++;

                        i++;
                        expressionLength++;
                        continue;
                    }

                    braceDepth--;

                    if (braceDepth > 0)
                    {
                        i++;
                        expressionLength++;
                        continue;
                    }

                    // Hit the closing brace of an expression.

                    // Degenerate case:  the string "${}".

                    if (expressionLength == 0)
                        blockLength += 3;

                    if (blockLength > 0)
                        token.addToken(constructStatic(text, blockStart, blockLength, location));

                    if (expressionLength > 0)
                    {
                        String expression =
                            text.substring(expressionStart, expressionStart + expressionLength);

                        token.addToken(new InsertToken(expression, location));
                    }

                    i++;
                    blockStart = i;
                    blockLength = 0;

                    // And drop into state start

                    state = STATE_START;

                    continue;
            }

        }

        // OK, to handle the end.  Couple of degenerate cases where
        // a ${...} was incomplete, so we adust the block length.

        if (state == STATE_DOLLAR)
            blockLength++;

        if (state == STATE_COLLECT_EXPRESSION)
            blockLength += expressionLength + 2;

        if (blockLength > 0)
            token.addToken(constructStatic(text, blockStart, blockLength, location));
    }

    private IScriptToken constructStatic(
        String text,
        int blockStart,
        int blockLength,
        Location location)
    {
        String literal = text.substring(blockStart, blockStart + blockLength);

        return new StaticToken(literal, location);
    }
}
