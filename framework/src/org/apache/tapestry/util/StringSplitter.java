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

package org.apache.tapestry.util;

/**
 *  Used to split a string into substrings based on a single character
 *  delimiter.  A fast, simple version of
 *  {@link java.util.StringTokenizer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class StringSplitter
{
    private char delimiter;

    public StringSplitter(char delimiter)
    {
        this.delimiter = delimiter;
    }

    public char getDelimiter()
    {
        return delimiter;
    }

    /**
     *  Splits a string on the delimter into an array of String
     *  tokens.  The delimiters are not included in the tokens.  Null
     *  tokens (caused by two consecutive delimiter) are reduced to an
     *  empty string. Leading delimiters are ignored.
     *
     **/

    public String[] splitToArray(String value)
    {
        char[] buffer;
        int i;
        String[] result;
        int resultCount = 0;
        int start;
        int length;
        String token;
        String[] newResult;
        boolean first = true;

        buffer = value.toCharArray();

        result = new String[3];

        start = 0;
        length = 0;

        for (i = 0; i < buffer.length; i++)
        {
            if (buffer[i] != delimiter)
            {
                length++;
                continue;
            }

            // This is used to ignore leading delimiter(s).

            if (length > 0 || !first)
            {
                token = new String(buffer, start, length);

                if (resultCount == result.length)
                {
                    newResult = new String[result.length * 2];

                    System.arraycopy(result, 0, newResult, 0, result.length);

                    result = newResult;
                }

                result[resultCount++] = token;

                first = false;
            }

            start = i + 1;
            length = 0;
        }

        // Special case:  if the string contains no delimiters
        // then it isn't really split.  Wrap the input string
        // in an array and return.  This is a little optimization
        // to prevent a new String instance from being
        // created unnecessarily.

        if (start == 0 && length == buffer.length)
        {
            result = new String[1];
            result[0] = value;
            return result;
        }

        // If the string is all delimiters, then this
        // will result in a single empty token.

        token = new String(buffer, start, length);

        newResult = new String[resultCount + 1];
        System.arraycopy(result, 0, newResult, 0, resultCount);
        newResult[resultCount] = token;

        return newResult;
    }
}