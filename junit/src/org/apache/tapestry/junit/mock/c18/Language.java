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

package org.apache.tapestry.junit.mock.c18;

import org.apache.commons.lang.enum.Enum;

/**
 * Identifies different computer languages.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 */
public class Language extends Enum
{
    public static final Language JAVA = new Language("JAVA");
    public static final Language C_SHARP = new Language("C_SHARP");
    public static final Language PYTHON = new Language("PYTHON");

    private Language(String name)
    {
        super(name);
    }
}
