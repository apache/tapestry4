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

package org.apache.tapestry.wml;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;

/**
 *  The Input element specifies a text entry object.
 *
 *  @version $Id$
 *  @author David Solis
 *  @since 3.0
 *
 **/

public abstract class Input extends AbstractComponent
{

	/**
	 *  @see AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
	 **/

	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
	{
		boolean render = !cycle.isRewinding();

		if (render)
		{
			writer.beginEmpty("input");

			writer.attribute("type", isHidden() ? "password" : "text");

            writer.attribute("name", getName());

			String title = getTitle();
			if (Tapestry.isNonBlank(title))
				writer.attribute("title", title);

			String format = getFormat();
            if (Tapestry.isNonBlank(format))
				writer.attribute("format", format);

            boolean emptyok = isEmptyok();
            if (emptyok != false)
				writer.attribute("emptyok", emptyok);

			renderInformalParameters(writer, cycle);

			String value = readValue();
			if (Tapestry.isNonBlank(value))
				writer.attribute("value", value);

			writer.closeTag();
		}
	}

	public abstract String getTitle();

	public abstract String getName();

	public abstract String getFormat();

	public abstract boolean isHidden();

	public abstract boolean isEmptyok();

	public abstract IBinding getValueBinding();

	public String readValue()
    {
		return getValueBinding().getString();
	}

}
