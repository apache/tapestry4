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

package org.apache.tapestry.markup;

import java.io.PrintWriter;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.util.ContentType;

/**
 * Service interface for <code>tapestry.markup.MarkupWriterSource</code> service. A factory
 * service that creates new instances of {@link org.apache.tapestry.IMarkupWriter}, configured for
 * different content types;
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface MarkupWriterSource
{
    /**
     * Creates and returns a new instance of {@link org.apache.tapestry.IMarkupWriter}. The content
     * type is used to find the proper implemenation. Any additional content type data (after a ';')
     * is ignored.
     * 
     * @param writer
     *            The {@link PrintWriter}&nbsp;to which the markup writer should send output.
     * @param contentType
     *            Used to locate the correct markup writer implementation (used to select a
     *            {@link MarkupFilter}.
     * @return The configured markup writer instance.
     */

    public IMarkupWriter newMarkupWriter(PrintWriter writer, ContentType contentType);
}