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

package org.apache.tapestry.pages;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.describe.RenderableAdapterFactory;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.util.exception.ExceptionDescription;
import org.apache.tapestry.web.WebUtils;

/**
 * Default exception reporting page.
 * 
 * @author Howard Lewis Ship
 */

public abstract class Exception extends BasePage implements PageDetachListener
{
    /** Transient property */
    public abstract void setExceptions(ExceptionDescription[] exceptions);

    public void setException(Throwable value)
    {
        ExceptionAnalyzer analyzer = new ExceptionAnalyzer();

        ExceptionDescription[] exceptions = analyzer.analyze(value);

        setExceptions(exceptions);
    }

    public void renderSystemProperties(IMarkupWriter writer)
    {

        Properties p = System.getProperties();

        String pathSeparator = p.getProperty("path.separator");

        writer.begin("div");
        writer.attribute("class", "described-object-title");
        writer.print("JVM System Properties");
        writer.end();
        writer.println();

        writer.begin("table");
        writer.attribute("class", "described-object");

        Iterator i = WebUtils.toSortedList(p.keys()).iterator();

        while (i.hasNext())
        {
            String key = (String) i.next();
            String value = p.getProperty(key);

            // TODO: Split things that look like paths.

            renderKeyAndValue(writer, key, value, pathSeparator);
        }

        writer.end();
    }

    private void renderKeyAndValue(IMarkupWriter writer, String key, String value,
            String pathSeparator)
    {
        String[] values = split(key, value, pathSeparator);

        for (int i = 0; i < values.length; i++)
        {
            writer.begin("tr");
            writer.begin("th");

            if (i == 0)
                writer.print(key);

            writer.end();
            writer.begin("td");
            writer.print(values[i]);
            writer.end("tr");
            writer.println();
        }
    }

    private String[] split(String key, String value, String pathSeparator)
    {
        if (!key.endsWith(".path"))
            return new String[]
            { value };

        StringTokenizer tokenizer = new StringTokenizer(value, pathSeparator);
        List values = Collections.list(tokenizer);

        return (String[]) values.toArray(new String[values.size()]);
    }

    public IRender getSystemPropertiesRenderer()
    {
        return new IRender()
        {
            public void render(IMarkupWriter writer, IRequestCycle cycle)
            {
                renderSystemProperties(writer);
            }
        };
    }

    public abstract RenderableAdapterFactory getAdapterFactory();
}