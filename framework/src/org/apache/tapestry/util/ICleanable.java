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

package org.apache.tapestry.util;

/**
 *  An interface implemented by objects that can be
 *  cleaned up, which is to say, can release unneeded
 *  object references.  This is useful for many classes which
 *  provide a pooling or caching function.  Over time, 
 *  some pooled or cached objects may no longer be useful
 *  to keep and can be released. 
 *  references to unneeded objects.
 *  This interface is the bridge between
 *  the {@link JanitorThread} class and an object that
 *  wishes to be periodically told to "clean up".
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.5
 *
 **/

public interface ICleanable
{
    /**
     *  Invoked periodically by the {@link JanitorThread}
     *  to perform whatever memory cleanups are reasonable.
     *
     **/

    public void executeCleanup();
}