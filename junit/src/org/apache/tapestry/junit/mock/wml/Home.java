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

package org.apache.tapestry.junit.mock.wml;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.apache.tapestry.wml.Deck;

/**
 *  Used to test {@link org.apache.tapestry.wml} components.
 *
 *
 *  @author David Solis
 *  @version $Id$
 *  @since 3.0
 *
 **/

public abstract class Home extends Deck
{
    public static final IPropertySelectionModel MODEL = new StringPropertySelectionModel
            (new String[] { "option_one", "option_two", "option_three" });

    public void submit(IRequestCycle cycle)
    {
        Result deck = (Result) cycle.getPage("Result");
        deck.setU(getU());
        deck.setL(getL());
        cycle.activate(deck);
    }

    public abstract String getU();
    public abstract String getL();
}
