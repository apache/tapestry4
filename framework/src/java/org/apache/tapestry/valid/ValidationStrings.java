//Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.valid;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Constants used for accessing validation message patterns.
 * 
 * @author Paul Ferraro
 */
public final class ValidationStrings
{
    public static final String REQUIRED_TEXT_FIELD = "field-is-required";
    public static final String REQUIRED_SELECT_FIELD = "select-field-is-required";
    public static final String REQUIRED_FILE_FIELD = "file-field-is-required";

    public static final String INVALID_DATE = "invalid-date-format";
    public static final String INVALID_NUMBER = "invalid-numeric-format";
    public static final String INVALID_EMAIL = "invalid-email-format";
    
    public static final String REGEX_MISMATCH = "regex-mismatch";
    
    public static final String VALUE_TOO_SHORT = "field-too-short";
    public static final String VALUE_TOO_LONG = "field-too-long";
    
    public static final String VALUE_TOO_SMALL = "number-too-small";
    public static final String VALUE_TOO_LARGE = "number-too-large";
    
    public static final String DATE_TOO_EARLY = "date-too-small";
    public static final String DATE_TOO_LATE = "date-too-large";
    
    public static final String INVALID_FIELD_EQUALITY = "invalid-field-equality";
    
    private static final String RESOURCE_BUNDLE = ValidationStrings.class.getName();
    
    /**
     * Fetches the appropriate validation message pattern from the appropriate localized resource.
     * This method should be called with the locale of the current request.
     */
    public static String getMessagePattern(String key, Locale locale)
    {
        return ResourceBundle.getBundle(RESOURCE_BUNDLE, locale).getString(key);
    }
    
    private ValidationStrings()
    {
        // Disable construction
    }
}
