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

package org.apache.tapestry.form.translator;

import org.apache.tapestry.form.IFormComponent;

/**
 * A trivial {@link Translator} implementation.  By default, empty text submissions
 * are interpretted as null.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class StringTranslator extends AbstractTranslator
{
    private String _empty = null;
    
    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#parseText(org.apache.tapestry.form.IFormComponent, java.lang.String)
     */
    protected Object parseText(IFormComponent field, String text)
    {
        return text;
    }

    /**
     * @see org.apache.tapestry.form.translator.AbstractTranslator#formatObject(org.apache.tapestry.form.IFormComponent, java.lang.Object)
     */
    protected String formatObject(IFormComponent field, Object object)
    {
        return object.toString();
    }
    
    public Object getEmpty()
    {
        return _empty;
    }
    
    public void setEmpty(String empty)
    {
        _empty = empty;
    }
}
