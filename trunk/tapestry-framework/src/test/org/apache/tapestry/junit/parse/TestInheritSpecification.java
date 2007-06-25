// Copyright Aug 27, 2006 The Apache Software Foundation
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
package org.apache.tapestry.junit.parse;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.Submit;


/**
 * Tests component specification inheritance.
 * 
 * @author jkuhnert
 */
public abstract class TestInheritSpecification extends Submit
{
    
    protected boolean isClicked(IRequestCycle cycle, String name)
    {
        return cycle.getParameter(name + "myclick") != null;
    }
    
}
