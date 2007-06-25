// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.junit.mock.c11;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 * Used to test the Select and Option elements.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public abstract class SelectPage extends BasePage
{
    public abstract boolean isAnimal();

    public abstract boolean isMineral();

    public abstract boolean isVegetable();

    public abstract void setAnimal(boolean animal);

    public abstract void setMineral(boolean mineral);

    public abstract void setVegetable(boolean vegetable);

    public void formSubmit(IRequestCycle cycle)
    {
        StringBuffer buffer = new StringBuffer("Selections: ");
        boolean needComma = false;

        if (isAnimal())
        {
            buffer.append("animal");
            needComma = true;
        }

        if (isVegetable())
        {
            if (needComma)
                buffer.append(", ");

            buffer.append("vegetable");

            needComma = true;
        }

        if (isMineral())
        {
            if (needComma)
                buffer.append(", ");

            buffer.append("mineral");

            needComma = true;
        }

        if (!needComma)
            buffer.append("none");

        buffer.append(".");

        Result result = (Result) cycle.getPage("Result");

        String message = buffer.toString();

        result.setMessage(message);

        cycle.activate(result);
    }
}