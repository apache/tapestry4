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

package org.apache.tapestry.bean;

/**
 *  Used to emit a stream of alterating string values: "even", "odd", etc.  This
 *  is often used in the Inspector pages to make the class of a &lt;tr&gt; alternate
 *  for presentation reasons.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class EvenOdd
{
    private boolean even = true;

    /**
     *  Returns "even" or "odd".  Whatever it returns on one invocation, it will
     *  return the opposite on the next.  By default, the first value
     *  returned is "even".
     *
     **/

    public String getNext()
    {
        String result = even ? "even" : "odd";

        even = !even;

        return result;
    }
    
    public boolean isEven()
    {
        return even;
    }

	/**
	 *  Overrides the even flag.
	 * 
	 **/
	
    public void setEven(boolean value)
    {
        even = value;
    }
}