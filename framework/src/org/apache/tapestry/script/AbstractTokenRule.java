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

package org.apache.tapestry.script;

import org.apache.tapestry.ILocation;
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
     * and converts the text into a series of {@link org.apache.tapestry.script.StaticToken} and
     * {@link org.apache.tapestry.script.InsertToken}s.
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
    protected void addTextTokens(IScriptToken token, String text, ILocation location)
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
        ILocation location)
    {
        String literal = text.substring(blockStart, blockStart + blockLength);

        return new StaticToken(literal, location);
    }
}
