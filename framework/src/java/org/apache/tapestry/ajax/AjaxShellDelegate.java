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
package org.apache.tapestry.ajax;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;


/**
 * The default rendering delegate responseible for include the 
 * dojo sources into the {@link Shell} component.
 *
 * @author jkuhnert
 */
public class AjaxShellDelegate implements IRender
{
    protected IAsset _dojoSource;
    
    /**
     * {@inheritDoc}
     */
    public void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        //first configure dojo, has to happen before package include
        StringBuffer str = new StringBuffer("<script type=\"text/javascript\">");
        str.append("djConfig = { isDebug: false,")
        .append(" baseRelativePath:\"").append(_dojoSource.getResourceLocation().getPath())
        .append("\", preventBackButtonFix: false };")
        .append(" </script>\n\n ");
        
        //include the js package
        str.append("<script type=\"text/javascript\" src=\"")
        .append(_dojoSource.getResourceLocation().getPath()).append("\"></script>");
        
        writer.printRaw(str.toString());
    }
    
    /**
     * Sets a valid path to the base dojo javascript installation
     * directory.
     * @param dojoSource
     */
    public void setDojoSource(IAsset dojoSource)
    {
        _dojoSource = dojoSource;
    }
}
