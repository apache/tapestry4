// Copyright 2009 The Apache Software Foundation
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

import org.apache.tapestry.IMarkupWriter;

/**
 * MarkupUtils includes some useful functions related to {@link IMarkupWriter}.
 *
 * @author Andreas Andreou
 */
public class MarkupUtils {
    /* defeat instantiation */
    private MarkupUtils() { }

    public static void beginConditionalComment(IMarkupWriter writer, String condition)
    {
        writer.println();
        writer.printRaw("<!--[if " + condition + "]>");
        writer.println();
    }

    public static void endConditionalComment(IMarkupWriter writer)
    {
        writer.println();
        writer.printRaw("<![endif]-->");
        writer.println();
    }
}
