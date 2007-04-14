// Copyright May 8, 2006 The Apache Software Foundation.
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

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Various scripting utility methods.
 */
public final class ScriptUtils
{
    /**
     * XML cdata start.
     */
    public static final String BEGIN_COMMENT = "\n<script>\n//<![CDATA[\n";
    /**
     * XML character data end.
     */
    public static final String END_COMMENT = "\n//]]>\n</script>\n";
    /**
     * Regexp represenging javascript matches.
     */
    public static final Pattern SCRIPT_PATTERN =
        Pattern.compile("(?:<script.*?>)(.*?)(?:<\\/script>)",
            Pattern.COMMENTS | Pattern.DOTALL | Pattern.MULTILINE);
    
    /* defeat instantiation */
    private ScriptUtils() { }
    
    /**
     * Takes any <script>contents..</script> tags found in the specified
     * input string and replaces their contents into one large <script></script>
     * block (meaning if multiple script blocks are found, they will be turned into one),
     * with the addition of {@link #BEGIN_COMMENT} inserted before the logic block and
     * {@link #END_COMMENT} inserted after the logic block.
     *
     * @param input 
     *          The string to replace tags on
     * @return The properly formatted string, if any formatting needed to occur.
     */
    public static synchronized String ensureValidScriptTags(String input) {
        if (input == null)
            return null;
        
        Matcher matcher = SCRIPT_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer(input.length());
        
        boolean matched = false;
        int end = 0;
        while (matcher.find()) {
            
            matched = true;
            String str = matcher.group(1);
            int pos = matcher.start() - end;
            end = matcher.end();
            
            if (str == null || str.trim().equals(""))
                matcher.appendReplacement(buffer, "");
            else {
                // gather the text from the beggining to the match into a new buffer
                StringBuffer matchLocal = new StringBuffer();
                matcher.appendReplacement(matchLocal, BEGIN_COMMENT + "$1" + END_COMMENT);
                
                // the first part is always script-less, no need to remove comments from it.
                String curr =  matchLocal.toString();
                String prefix = curr.substring(0, pos);
                String suffix = curr.substring(pos);
                
                // the second part is in a script, so remove comments.
                suffix = StringUtils.replace(suffix, "<!--", "");
                suffix = StringUtils.replace(suffix, "// -->", "");
                buffer.append(prefix).append(suffix);
            }
        }
        
        if (!matched)
            buffer.append(input);
        else {
            //copies non matched character input, ie content after the last script.
            matcher.appendTail(buffer);
        }

        return buffer.toString();
    }
    
    /**
     * Utility that will attempt to generate a unique hash string
     * that is javascript client in a function name based on the inomcing
     * object's {@link Object#hashCode()} return value.
     * 
     * @param target The object to hash a string for.
     * @return A string hash value, not necessarily exactly the same thing that would
     *         be returned by {@link Object#hashCode()}.
     */
    public static String functionHash(Object target)
    {
        int hash = target.hashCode();
        if (hash < 0) // flip exponent if negative
            hash = hash*-1;
        
        return String.valueOf(hash);
    }
}
