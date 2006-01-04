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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.util.ContentType;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class MarkupWriterSourceImpl implements MarkupWriterSource
{
    private Log _log;

    private MarkupFilter _defaultFilter = new AsciiMarkupFilter();

    private Map _contributions;

    public void setContributions(Map contributions)
    {
        _contributions = contributions;
    }

    public IMarkupWriter newMarkupWriter(PrintWriter writer, ContentType contentType)
    {
        Defense.notNull(writer, "writer");
        Defense.notNull(contentType, "contentType");

        MarkupFilter filter = findFilter(contentType);

        return new MarkupWriterImpl(contentType.toString(), writer, filter);
    }

    private MarkupFilter findFilter(ContentType contentType)
    {
        // Look for an exact match (caseless).

        String key = contentType.toString().toLowerCase();

        MarkupFilter result = (MarkupFilter) _contributions.get(key);

        if (result == null)
            result = (MarkupFilter) _contributions.get(contentType.getMimeType());

        if (result == null)
        {
            _log.error(MarkupMessages.noFilterMatch(contentType));

            result = _defaultFilter;
        }

        return result;
    }

    public void setLog(Log log)
    {
        _log = log;
    }
}