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

/**
 * Filter used with {@link org.apache.tapestry.markup.MarkupWriterImpl}to determine how to convert
 * the output into a format compatible with the content type. Typically, this means translating
 * certain characters into escape codes (for example, in HTML, convert '&lt;' to '&amp;lt;'.
 * <p>
 * Implementations should be stateless and thread safe.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface MarkupFilter
{
    /**
     * Print the value to the writer, escaping characters as necessary.
     * 
     * @param writer
     *            the write to which converted content should be output
     * @param data
     *            a character array containing the characters to be output
     * @param offset
     *            the offset within the array to begin output
     * @param length
     *            the number of characters to output
     * @param escapeQuotes
     *            if true, the value is being rendered as an attribute value and double quotes
     *            within the value should be escaped. If false, then then double quotes may pass
     *            through unchanged.
     */

    public void print(PrintWriter writer, char[] data, int offset, int length, boolean escapeQuotes);

}