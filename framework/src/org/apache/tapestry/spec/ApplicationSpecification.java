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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Defines the configuration for a Tapestry application.  An ApplicationSpecification
 *  extends {@link LibrarySpecification} by adding new properties
 *  name and engineClassName.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ApplicationSpecification
    extends LibrarySpecification
    implements IApplicationSpecification
{
    private String _name;
    private String _engineClassName;

    public String getName()
    {
        return _name;
    }

    public void setEngineClassName(String value)
    {
        _engineClassName = value;
    }

    public String getEngineClassName()
    {
        return _engineClassName;
    }

    public void setName(String name)
    {
        _name = name;
    }

    protected void extendDescription(ToStringBuilder builder)
    {
        builder.append("name", _name);
        builder.append("engineClassName", _engineClassName);
    }

}