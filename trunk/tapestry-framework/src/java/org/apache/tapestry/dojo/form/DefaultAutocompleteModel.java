// Copyright Jul 30, 2006 The Apache Software Foundation
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
package org.apache.tapestry.dojo.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;


/**
 * Default simple implementation of {@link IAutocompleteModel}. This class relies
 * on the java beans specification to resolve key fields of an incoming 
 * {@link List}. 
 * 
 * <p>
 *  If you had an object type of <code>User</code>, with the primary/unique id of 
 *  each <code>User</code> object stored as a member with a name of <code>id</code> 
 *  you would pass something like this into the model(don't forget that javabeans syntax
 *  requires a corresponding getId() for members):
 * </p>
 * 
 * <pre>
 *  IAutocompleteModel model = new DefaultAutocompleteModel(List users, "id", "name");
 * </pre>
 * 
 * @see "http://jakarta.apache.org/commons/beanutils/commons-beanutils-1.6.1/docs/api/org/apache/commons/beanutils/PropertyUtils.html"
 * @author jkuhnert
 */
public class DefaultAutocompleteModel implements IAutocompleteModel
{

    private List _values;
    
    private String _keyExpression;
    
    private String _labelExpression;
    
    /**
     * Create a new model using java beans syntax to access the key/label
     * for the list using the specified bean expressions.
     * 
     * @param values 
     *          The list of values to manage.
     * @param keyField 
     *          The java beans expression for getting the primary key of each object
     *          in the list. {@link #getPrimaryKey(Object)}.
     * @param labelField
     *          The java beans expression for getting the label of each object
     *          in the list. {@link #getLabelFor(Object)}.
     */
    public DefaultAutocompleteModel(List values, String keyField, String labelField) 
    {
        Defense.notNull(values, "Value list can't be null.");
        Defense.notNull(keyField, "Model keyField java beans expression can't be null.");
        Defense.notNull(labelField, "Model labelField java beans expression can't be null.");
        
        _values = values;
        _keyExpression = keyField;
        _labelExpression = labelField;
    }
    
    /**
     * {@inheritDoc}
     */
    public List getValues(String match)
    {
        List ret = new ArrayList();
        
        if (match == null)
            return ret;
        
        String filter = match.trim().toLowerCase();
        
        for (int i = 0; i < _values.size(); i++) {
            
            Object value = _values.get(i);
            String label = getLabelFor(value);
            
            if (label.toLowerCase().indexOf(filter) > -1)
                ret.add(value);
        }
        
        return ret;
    }
    
    /** 
     * {@inheritDoc}
     */
    public String getLabelFor(Object value)
    {
        try {
            
            return PropertyUtils.getProperty(value, _labelExpression).toString();
            
        } catch (Exception e) {
            throw new ApplicationRuntimeException(e);
        }
    }

    /** 
     * {@inheritDoc}
     */
    public Object getPrimaryKey(Object value)
    {
        try {
            
            return PropertyUtils.getProperty(value, _keyExpression);
            
        } catch (Exception e) {
            throw new ApplicationRuntimeException(e);
        }
    }

    /** 
     * {@inheritDoc}
     */
    public Object getValue(Object primaryKey)
    {
        for (int i = 0; i < _values.size(); i++) {
            
            Object value = _values.get(i);
            if (getPrimaryKey(value).toString().equals(primaryKey.toString()))
                return value;
        }
        
        return null;
    }

}
