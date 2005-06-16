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

package org.apache.tapestry.form.validator;

import org.apache.tapestry.valid.ValidationStrings;

/**
 * {@link Validator} implementation that validates email addresses.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class EmailValidator extends RegExValidator
{
    /**
     * @see org.apache.tapestry.form.validator.RegExValidator#defaultExpression()
     */
    protected String defaultExpression()
    {
        return "^\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3}$";
    }
    
    /**
     * @see org.apache.tapestry.form.validator.RegExValidator#getMessageKey()
     */
    protected String getMessageKey()
    {
        return ValidationStrings.INVALID_EMAIL;
    }
}
