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

package org.apache.tapestry.record;

/**
 *  Represents a change to a component on a page, this represents
 *  a datum of information stored by a {@link org.apache.tapestry.engine.IPageRecorder}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 **/

public interface IPageChange
{
    /**
     *  The path to the component on the page, or null if the property is a property
     *  of the page.
     *
     **/

    public String getComponentPath();

    /**
     *  The new value for the property, which may be null.
     *
     **/

    public Object getNewValue();

    /**
     *  The name of the property that changed.
     *
     **/

    public String getPropertyName();
}