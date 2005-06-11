// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.pageload;

/**
 * Determines the name of a page class based on information inside a
 * {@link org.apache.tapestry.pageload.ComponentClassProviderContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface ComponentClassProvider
{
    /**
     * Return the component class name, or null if this provider can't resolve it.
     */

    public String provideComponentClassName(ComponentClassProviderContext context);
}