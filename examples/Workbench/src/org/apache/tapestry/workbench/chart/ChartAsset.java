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

package org.apache.tapestry.workbench.chart;

import java.io.InputStream;
import java.util.Locale;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AbstractAsset;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 *  An asset used with the {@link ChartService}.  
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.10
 * 
 **/

public class ChartAsset extends AbstractAsset
{
    private IEngineService _chartService;
    private IComponent _chartProvider;

    public ChartAsset(IRequestCycle cycle, IComponent chartProvider)
    {
    	super(null, null);
    	
        IEngine engine = cycle.getEngine();

        _chartService = engine.getService(ChartService.SERVICE_NAME);
        _chartProvider = chartProvider;
    }

    public String buildURL(IRequestCycle cycle)
    {
        ILink l = _chartService.getLink(cycle, _chartProvider, null);

        return l.getURL();
    }

    public InputStream getResourceAsStream(IRequestCycle cycle) 
    {
        return null;
    }

    public InputStream getResourceAsStream(IRequestCycle cycle, Locale locale)
    {
        return null;
    }

}