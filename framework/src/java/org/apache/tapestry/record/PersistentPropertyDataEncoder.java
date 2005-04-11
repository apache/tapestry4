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

package org.apache.tapestry.record;

import java.util.List;

/**
 * Responsible for encoding {@link org.apache.tapestry.record.PropertyChange}s into and out of plain
 * strings.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface PersistentPropertyDataEncoder
{
    /**
     * Encodes a (possibly empty) list of {@link PropertyChange}s into a string representation that
     * can later be decoded.
     * 
     * @returns encoded string (possibly empty, but not null)
     */
    public String encodePageChanges(List changes);

    /**
     * Takes a string with an encoded set of page changes, and converts it back into a list of
     * {@link org.apache.tapestry.record.PropertyChange}s.
     */

    public List decodePageChanges(String encoded);

}