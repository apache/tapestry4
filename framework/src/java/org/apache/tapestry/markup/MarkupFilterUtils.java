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

package org.apache.tapestry.markup;

import java.io.PrintWriter;

import org.apache.tapestry.util.text.ICharacterTranslator;

/**
 * For the meantime, implemenatations of {@link org.apache.tapestry.markup.MarkupFilter}&nbsp;are
 * wrappers around {@link org.apache.tapestry.util.text.ICharacterTranslator}. This class provides
 * handy methods for doing the grunt work.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class MarkupFilterUtils
{
    public static void print(PrintWriter writer, char[] data, int offset, int length,
            boolean escapeQuotes, ICharacterTranslator translator)
    {
        StringBuffer buffer = new StringBuffer(length);

        for (int i = 0; i < length; i++)
        {
            char ch = data[offset + i];

            if (ch == '"' && !escapeQuotes)
            {
                buffer.append(ch);
                continue;
            }

            String translated = translator.translate(ch);

            if (translated == null)
            {
                buffer.append(ch);
                continue;
            }

            buffer.append(translated);
        }

        // We'll have to see if building up a buffer and then printing it in one go is the
        // most efficient route. It's hard to predict what will give the best performance,
        // but there's almost certainly a buffered writer between this code and the
        // character set encoder.

        writer.print(buffer.toString());
    }
}