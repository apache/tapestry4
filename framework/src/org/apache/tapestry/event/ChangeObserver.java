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

package org.apache.tapestry.event;

/**
 * May observe changes in an object's properties.  This is a "weak" variation
 * on JavaBean's style bound properties.  It is used when there will be at most
 * a single listener on property changes, and that the listener is not interested
 * in the old value.
 *
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public interface ChangeObserver
{
    /**
     *  Sent when the observed object changes a property.  The event identifies
     *  the object, the property and the new value.
     *
     **/

    public void observeChange(ObservedChangeEvent event);
}