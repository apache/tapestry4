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
 *  Defines and interface for the configuration for a Tapestry application.  An ApplicationSpecification
 *  extends {@link ILibrarySpecification} by adding new properties 
 *  name and engineClassName.
 *
 *  @author Geoffrey Longman
 *  @version $Id$
 *
 **/

public interface IApplicationSpecification extends ILibrarySpecification
{
    /**
     *  Returns a "user friendly" name for the application (which is optional).
     * 
     **/
    
    public String getName();

    public void setEngineClassName(String value);
    
    /**
     *  Returns the name of the class (which implements {@link org.apache.tapestry.IEngine}).
     *  May return null, in which case a default is used.
     * 
     **/
    
    public String getEngineClassName();
    
    public void setName(String name);
}