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

package org.apache.tapestry.junit.mock.c11;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/**
 *  Used to test the Select and Option elements.
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 **/

public class SelectPage extends BasePage
{
	private boolean _animal;
	private boolean _mineral;
	private boolean _vegetable;
	
	public void initialize()
	{
		_animal = false;
		_mineral = false;
		_vegetable = false;
	}
	
    public boolean isAnimal()
    {
        return _animal;
    }

    public boolean isMineral()
    {
        return _mineral;
    }

    public boolean isVegetable()
    {
        return _vegetable;
    }

    public void setAnimal(boolean animal)
    {
        _animal = animal;
    }

    public void setMineral(boolean mineral)
    {
        _mineral = mineral;
    }

    public void setVegetable(boolean vegetable)
    {
        _vegetable = vegetable;
    }

	public void formSubmit(IRequestCycle cycle)
	{
		StringBuffer buffer = new StringBuffer("Selections: ");
		boolean needComma = false;
		
		if (_animal)
		{
			buffer.append("animal");
			needComma = true;
		}
		
		if (_vegetable)
		{
			if (needComma)
			buffer.append(", ");
			
			buffer.append("vegetable");
			
			needComma = true;
		}
		
		if (_mineral)
		{
			if (needComma) buffer.append(", ");
			
			buffer.append("mineral");
			
			needComma = true;
		}
			
			if (!needComma)
			buffer.append("none");
			
		buffer.append(".");
		
		Result result = (Result)cycle.getPage("Result");
		
		String message = buffer.toString();
		
		result.setMessage(message);
		
		cycle.activate(result);
	}
}
