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

package org.apache.tapestry;

/**
 * A writer that is created by, and nested within, another markup writer. This is used by many
 * components that need to render their bodies to fully determine how to write the markup before
 * their bodies; the markup is stored inside the nested markup writer until needed.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 * @see org.apache.tapestry.IMarkupWriter#getNestedWriter()
 */
public interface NestedMarkupWriter extends IMarkupWriter
{
    /**
     * Returns any makrup so far accumulated by the nested markup writer. When the nested markup
     * writer is closed, it invokes {@link org.apache.tapestry.IMarkupWriter#printRaw(String)},
     * with this content, on its parent markup writer.
     */

    public String getBuffer();
}