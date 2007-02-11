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
package org.apache.tapestry.asset;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.pool.BasePoolableObjectFactory;


/**
 * Implementation of {@link BasePoolableObjectFactory} used to cache {@link DateFormat} objects.
 */
public class PoolableDateFormatFactory extends BasePoolableObjectFactory
{

    private String _format;
    
    private Locale _locale;
    
    /**
     * Creates a new pool factory that will create {@link DateFormat} objects by passing
     * in the format/locale arguments to that objects constructor.
     * 
     * @param format The date format type.
     * @param locale The locale of the format.
     */
    public PoolableDateFormatFactory(String format, Locale locale)
    {
        _format = format;
        _locale = locale;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object makeObject()
        throws Exception
    {
        return new SimpleDateFormat(_format, _locale);
    }

}
