// Copyright Aug 4, 2006 The Apache Software Foundation
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
package org.apache.tapestry.util.text;


/**
 * Handles escaping of special characters as per the XML spec section 2.2.
 * 
 * @author lquijano
 */
public final class XmlCharacterTranslator extends MarkupCharacterTranslator {
    
    /** Default constructor. */
    public XmlCharacterTranslator() {
        super(true);
    }
    
    /**
     * Translates the character.
     * 
     * <p>
     *  XML spec section 2.2
     *  Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] |
     *  [#xE000-#xFFFD] |
     *  [#x10000-#x10FFFF]
     *  any Unicode character, excluding the surrogate blocks, FFFE, and FFFF.
     *  </p>
     */
    public String translate(char ch) {
        if (ch == 0x09 || ch == 0x0a || ch == 0x0d
                || (ch >= 0x20 && ch <= 0xd7ff)
                || (ch >= 0xe000 && ch <= 0xfffd)
                || (ch >= 0x10000 && ch <= 0x10ffff)) {
            return super.translate(ch);
        }

        return "";
    }
}
