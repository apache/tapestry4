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

package tutorial.pagelinking;

import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 * Provides the listeners and a place to store a simple 'result' string
 * that can be shown back to the user.
 */
public class SecondPage extends BasePage {
    public void linkListener(IRequestCycle cycle) throws RequestCycleException {
        result = "The link listener was called - which is a good thing.";
    }

    public void buttonListener(IRequestCycle cycle) throws RequestCycleException {
        result = "The submit listener was called - which is also a good thing.";
    }

    public String getResult() {
        return result;
    }

    /** Clean up */
    public void detach() {
        super.detach();
        result = null;
    }

    private String result;
}
