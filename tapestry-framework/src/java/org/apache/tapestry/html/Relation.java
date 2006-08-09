// Copyright Aug 2, 2006 The Apache Software Foundation
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
package org.apache.tapestry.html;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * Works with the {@link Shell} component to define and append a 
 * relationship between documents (typically a stylesheet) to 
 * the HTML response. 
 * 
 * @author andyhot
 */
public abstract class Relation extends AbstractComponent
{
    /** 
     * {@inheritDoc}
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        if (!cycle.isRewinding())
        {
            Shell shell = Shell.get(cycle);

            if (shell == null)
                throw new ApplicationRuntimeException(
                    HTMLMessages.shellComponentRequired(),
                    this.getLocation(), null);
                    
            Object href = getHref();
            boolean ok = (href instanceof String) || (href instanceof IAsset);
            if (!ok)
                throw new ApplicationRuntimeException(
                    HTMLMessages.stringOrIAssetExpected(),
                    this.getLocation(), null); 
                    
            String url;
            if (href instanceof String)
            {
                url = (String) href;
            }
            else
            {
                url = ((IAsset)href).buildURL();
            }
                    
            RelationBean bean = new RelationBean();           
            bean.setHref(url);
            bean.setMedia(getMedia());
            bean.setRel(getRel());
            bean.setRev(getRev());
            bean.setTitle(getTitle());
            bean.setType(getType());
            shell.addRelation(bean);
        }          
    }
    
    public abstract Object getHref();

    public abstract String getRel();
    
    public abstract String getRev();  
    
    public abstract String getType();
    
    public abstract String getTitle();
        
    public abstract String getMedia();    

}
