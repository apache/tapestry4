// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

import java.util.Map;

import org.apache.hivemind.Location;

/**
 *  A Factory used by {@link org.apache.tapestry.parse.TemplateParser} to create 
 *  {@link org.apache.tapestry.parse.TemplateToken} objects.
 * 
 *  <p>
 *  This class is extended by Spindle - the Eclipse Plugin for Tapestry.
 *  <p>
 *  @author glongman@intelligentworks.com
 *  @since 3.0
 */
public class TemplateTokenFactory
{
    public OpenToken createOpenToken(String tagName, String jwcId, String type, Location location)
    {
        return new OpenToken(tagName, jwcId, type, location);
    }

    public CloseToken createCloseToken(String tagName, Location location)
    {
        return new CloseToken(tagName, location);
    }

    public TextToken createTextToken(
        char[] templateData,
        int blockStart,
        int end,
        Location templateLocation)
    {
        return new TextToken(templateData, blockStart, end, templateLocation);
    }

    public LocalizationToken createLocalizationToken(
        String tagName,
        String localizationKey,
        boolean raw,
        Map attributes,
        Location startLocation)
    {
        return new LocalizationToken(tagName, localizationKey, raw, attributes, startLocation);
    }
}