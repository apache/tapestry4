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

package org.apache.tapestry.pageload;

/**
 * Part of the scheme to defer connection of bindings until after all components (including
 * implicit components loaded from templates, and component contained within those, etc.)
 * are loaded.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
interface IQueuedInheritedBinding
{
    void connect();
}