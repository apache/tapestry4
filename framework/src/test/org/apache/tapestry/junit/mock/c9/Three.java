// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock.c9;

import org.apache.tapestry.html.BasePage;

/**
 * Demonstration for a class that has abstract accessor methods and initializes the property in
 * finishLoad().
 * 
 * @author Howard Lewis Ship
 * @version $Id$
 * @since 3.0
 */

public abstract class Three extends BasePage
{
    public abstract String getWord();

    public abstract void setWord(String word);

    protected void finishLoad()
    {
        setWord("Tapestry");
    }

}