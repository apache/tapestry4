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

package org.apache.tapestry.spec;

/**
 *  Special subclass of {@link org.apache.tapestry.spec.BindingSpecification} used
 *  to encapsulate the additional information 
 *  specific to listener bindings.  In a ListenerBindingSpecification, the
 *  value property is the actual script (and is aliased as property script), 
 *  but an additional property,
 *  language, (which may be null) is needed.  This is the language
 *  the script is written in.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class ListenerBindingSpecification extends BindingSpecification implements IListenerBindingSpecification
{
    protected String _language;
    
    public ListenerBindingSpecification()
    {
    	setType(BindingType.LISTENER);
    }
    
    public String getLanguage()
    {
        return _language;
    }
    
    public String getScript()
    {
        return getValue();
    }
    
    public void setLanguage(String language)
    {
        _language = language;
    }

}
