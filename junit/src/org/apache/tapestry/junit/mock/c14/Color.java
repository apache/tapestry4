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

package org.apache.tapestry.junit.mock.c14;

import org.apache.commons.lang.enum.Enum;

public class Color extends Enum
{
    public static final Color RED = new Color("RED");
    public static final Color GREEN = new Color("GREEN");
    public static final Color BLUE = new Color("BLUE");
    public static final Color PUECE = new Color("PUECE");
    public static final Color YELLOW = new Color("YELLOW");
    public static final Color BLACK = new Color("BLACK");
    public static final Color WHITE = new Color("WHITE");

    public static final Color[] ALL_COLORS = { RED, GREEN, BLUE, YELLOW, BLACK, WHITE, PUECE };

    private Color(String name)
    {
        super(name);
    }

}
