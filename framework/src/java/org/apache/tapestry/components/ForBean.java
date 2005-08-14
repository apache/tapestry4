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

package org.apache.tapestry.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.ExpressionEvaluator;

/**
 * @author mb
 */
public abstract class ForBean extends AbstractFormComponent {
    private static final char DESC_VALUE = 'V';
	private static final char DESC_PRIMARY_KEY = 'P';
	
	// parameters
	public abstract Object getSource();
	public abstract Object getFullSource();
    public abstract String getElement();
    public abstract boolean getVolatile();
    public abstract Object getDefaultValue();
    public abstract String getPrimaryKey();
    public abstract IPrimaryKeyConverter getConverter();
    public abstract String getKeyExpression();

    // properties
    public abstract Map getPrimaryKeyMap();
    public abstract void setPrimaryKeyMap(Map primaryKeys);

    public abstract List getSourcePrimaryKeys();
    public abstract void setSourcePrimaryKeys(List sourcePrimaryKeys);
    
    public abstract List getSavedSourceData();
    public abstract void setSavedSourceData(List sourceData);
    
    public abstract Iterator getFullSourceIterator();
    public abstract void setFullSourceIterator(Iterator fullSourceIterator);
    
    // injects
    public abstract DataSqueezer getDataSqueezer();
    public abstract ValueConverter getValueConverter();
    public abstract ExpressionEvaluator getExpressionEvaluator();
    
    
    private Object _value;
    private int _index;
    private boolean _rendering;

    /**
     *  Gets the source binding and iterates through
     *  its values.  For each, it updates the value binding and render's its wrapped elements.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    	// Clear the cache between rewind and render.
    	// This allows the value of 'source' to be changed by the form listeners.
    	setSavedSourceData(null);
    	
        // form may be null if component is not located in a form
        IForm form = (IForm) cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).
        boolean cycleRewinding = cycle.isRewinding();
        if (cycleRewinding && form != null && !form.isRewinding())
            return;

        // Get the data to be iterated upon. Store in form if needed.
        Iterator dataSource = getData(cycle, form);
        
        // Do not iterate if dataSource is null. 
        // The dataSource was either not convertable to Iterator, or was empty. 
        if (dataSource == null)
            return;

        String element = getElement();

        // Perform the iterations
        try
        {
            _index = 0;
            _rendering = true;

            while (dataSource.hasNext())
            {
            	// Get current value
                _value = dataSource.next();

                // Update output component parameters
                updateOutputParameters();
            	
                // Render component
                if (element != null)
                {
                    writer.begin(element);
                    renderInformalParameters(writer, cycle);
                }

                renderBody(writer, cycle);

                if (element != null)
                    writer.end();

                _index++;
            }
        }
        finally
        {
            _rendering = false;
            _value = null;
        }
    }
    
    
    /**
     * Returns a list with the values to be iterated upon.
     * 
     * The list is obtained in different ways:
     * - If the component is not located in a form or 'volatile' is set to true, 
     *   then the simply the values passed to 'source' are returned (same as Foreach)
     * - If the component is in a form, and the form is rewinding, the values stored 
     *    in the form are returned -- rewind is then always the same as render.
     * - If the component is in a form, and the form is being rendered, the values
     *   are stored in the form as Hidden fields. 
     * 
     * @param cycle The current request cycle
     * @param form The form within which the component is located (if any)
     * @return An iterator with the values to be cycled upon
     **/
    private Iterator getData(IRequestCycle cycle, IForm form) {
        if (form == null || getVolatile())
        	return getSourceData().iterator();
        
        String name = form.getElementId(this);
        if (cycle.isRewinding())
        	return getStoredData(cycle, name);
       	return storeSourceData(form, name);
    }
    
    /**
     *  Returns a {@link java.util.List} containing the values provided 
     *  by the identified source binding.
     *
     *  @return a list with the values to iterate upon. 
     *  null if conversion cannot be performed. 
     **/
    protected List getSourceData()
    {
    	List sourceData = getSavedSourceData();
    	if (sourceData == null) {
    		Object source = getSource();
    		sourceData = (List) getValueConverter().coerceValue(source, List.class);
    		setSavedSourceData(sourceData);
    	}
    	return sourceData;
    }
    
    /**
     *  Returns a list of the values stored as Hidden fields in the form.
     *  A conversion is performed if the primary key of the value is stored.
     *  
     *  @param cycle The current request cycle
     *  @param name The name of the HTTP parameter whether the values 
     *  @return an iterator with the values stored in the provided Hidden fields
     **/
    protected Iterator getStoredData(IRequestCycle cycle, String name)
    {
        String[] submittedPrimaryKeys = cycle.getParameters(name);
        String pkDesc = submittedPrimaryKeys[0];

        // unsqueeze data
        List data = new ArrayList(submittedPrimaryKeys.length-1);

        List pks = null;
    	IBinding primaryKeysBinding = getBinding("primaryKeys");
    	if (primaryKeysBinding != null)
    		pks = new ArrayList(submittedPrimaryKeys.length-1);
        for (int i = 1; i < submittedPrimaryKeys.length; i++) {
        	String stringRep = submittedPrimaryKeys[i];
			Object value = getDataSqueezer().unsqueeze(stringRep);
			data.add(value);
			if (primaryKeysBinding != null && i <= pkDesc.length() && pkDesc.charAt(i-1) == DESC_PRIMARY_KEY)
				pks.add(value);
		}

        // update the binding with the list of primary keys
    	if (primaryKeysBinding != null)
    		primaryKeysBinding.setObject(pks);
        
    	// convert from primary keys to data
        for (int i = 0; i < data.size(); i++) {
        	if (i <= pkDesc.length() && pkDesc.charAt(i) == DESC_PRIMARY_KEY) {
        		Object pk = data.get(i);
        		Object value = getValueFromPrimaryKey(pk);
        		data.set(i, value);
        	}
		}
        
        return data.iterator();
    }
    
    /**
     *  Stores the provided data in the form and then returns the data as an iterator.
     *  If the primary key of the value can be determined, 
     *  then that primary key is saved instead.
     *   
     *  @param form The form where the data will be stored
     *  @param name The name under which the data will be stored
     *  @return an iterator with the bound values stored in the form 
     **/
    protected Iterator storeSourceData(IForm form, String name)
    {
    	List sourceData = getSourceData();
		if (sourceData == null)
			return null;
		
    	List sourcePrimaryKeys = evaluateSourcePrimaryKeys();
		if (sourcePrimaryKeys == null)
			return null;
    	
		// store primary keys
        form.addHiddenValue(name, sourcePrimaryKeys.get(0).toString());
        for (int i = 1; i < sourcePrimaryKeys.size(); i++) {
			Object pk = sourcePrimaryKeys.get(i);
			String stringRep = getDataSqueezer().squeeze(pk);
			form.addHiddenValue(name, stringRep);
		}
        
    	return sourceData.iterator();
    }

    /**
     * Converts the values in the 'source' parameter to primary keys
     * and returns a list containing the primary keys or the values themselves
     * if a primary key cannot be extracted. The first element of the array is a
     * string that shows whether a particular element is a primary key or a value. 
     * The method also stores the evaluated primary keys in a map that can be used
     * to determine the value that a particular primary key represents.  
     *  
     * @return an array consisting of the primary keys of the source values or 
     * the values themselves if a primary key cannot be found. The first element
     * of the array is a string describing whether a particular element is
     * a primary key or a value.
     */
    private List evaluateSourcePrimaryKeys()
    {
    	// check if the result is already cached to avoid evaluating again
    	List sourcePrimaryKeys = getSourcePrimaryKeys();
    	if (sourcePrimaryKeys != null)
    		return sourcePrimaryKeys;
    	
    	List sourceData = getSourceData();
		if (sourceData == null)
			return null;
    	
		// extract primary keys from data
		StringBuffer pkDesc = new StringBuffer(sourceData.size());
		sourcePrimaryKeys = new ArrayList(sourceData.size()+1);
		sourcePrimaryKeys.add(pkDesc);
		for (Iterator it = sourceData.iterator(); it.hasNext();) {
			Object value = it.next();
			
			Object pk = getPrimaryKeyFromValue(value);
			if (pk == null) {
				pkDesc.append(DESC_VALUE);
				pk = value;
			}
			else {
				pkDesc.append(DESC_PRIMARY_KEY);
			}
			sourcePrimaryKeys.add(pk);
		}
    	
		setSourcePrimaryKeys(sourcePrimaryKeys);
		
		return sourcePrimaryKeys;
    }
    
    /**
     * Converts the values in the 'source' parameter to primary keys if possible.
     * Stores the evaluated primary keys in a map to determine the value 
     * that a particular primary key represents.  
     *  
     * @return the map from primary keys to their corresponding objects 
     */
    private Map fillSourcePrimaryKeysMap()
    {
    	// check if the result is already cached to avoid evaluating again
    	Map primaryKeyMap = getPrimaryKeyMap();
    	if (primaryKeyMap != null)
    		return primaryKeyMap;
    	
    	List sourceData = getSourceData();
		if (sourceData == null)
			return null;
    	
		// extract primary keys from data
		primaryKeyMap = new HashMap();
		for (Iterator it = sourceData.iterator(); it.hasNext();) {
			Object value = it.next();
			Object pk = getPrimaryKeyFromValue(value);
			if (pk != null)
				primaryKeyMap.put(pk, value);
		}
    	
		setPrimaryKeyMap(primaryKeyMap);
		
		return primaryKeyMap;
    }
    
    /**
     * Returns the primary key of the given value. 
     * Uses the 'keyExpression' or the 'converter' (if either is provided).
     * 
     * @param value The value from which the primary key should be extracted
     * @return The primary key of the value, or null if such cannot be extracted.
     */
    private Object getPrimaryKeyFromValue(Object value) {
    	if (value == null)
    		return null;
    	
    	Object primaryKey = null;
    	
		String keyExpression = getKeyExpression();
		if (keyExpression != null)
			primaryKey = getExpressionEvaluator().read(value, keyExpression);
	
		if (primaryKey == null) {
	    	IPrimaryKeyConverter converter = getConverter();
	    	if (converter != null)
	    		primaryKey = converter.getPrimaryKey(value);
		}

    	return primaryKey;
    }
    
    /**
     * Returns a value that corresponds to the provided primary key.
     * Uses the 'keyExpression' or the 'converter' (if either is provided).
     * If 'keyExpression' is defined, it extracts the primary keys of all values 
     * in 'source' until a match is found. If there is no match, it does the same 
     * with 'fullSource'. If that does not help either, 'converter' is used.
     * Finally, the 'defaultValue' is returned as a last resort.
     * 
     * @param primaryKey The primary key that identifies the value 
     * @return A value with an identical primary key, or null if such is not found.
     */
    private Object getValueFromPrimaryKey(Object primaryKey) {
    	Object value = null;

    	Map primaryKeyMap = fillSourcePrimaryKeysMap();
    	if (primaryKeyMap != null)
    		value = primaryKeyMap.get(primaryKey);
    	
    	if (value == null) {
	    	// if fullSource is defined, try to get the object in that way
			Object fullSource = getFullSource();
			if (fullSource != null)
	        	value = findPrimaryKeyMatchInFullSource(primaryKey, fullSource);
    	}
    	
    	if (value == null) {
	    	IPrimaryKeyConverter converter = getConverter();
	    	if (converter != null)
	    		value = converter.getValue(primaryKey);
    	}

    	if (value == null)
    		value = getDefaultValue();

    	return value;
    }
    
    /**
     * Iterates over the fullSource parameter until a value with a matching primary key is found.
     * The primary keys generated and the fullSource iterator are stored in properties 
     * to avoid repeated evaluation.
     * 
     * @param primaryKey the primary key to be matched
     * @param fullSource the provided list of objects
     * @return an object with a matching primary key, or null if such is not found
     */
    private Object findPrimaryKeyMatchInFullSource(Object primaryKey, Object fullSource)
    {
    	Map primaryKeyMap = getPrimaryKeyMap();
    	if (primaryKeyMap == null)
    		primaryKeyMap = new HashMap();
    	
		Iterator it = getFullSourceIterator();
		if (it == null) {
			it = (Iterator) getValueConverter().coerceValue(fullSource, Iterator.class);
			if (it == null)
				it = Collections.EMPTY_LIST.iterator();
		}
		
		try { 
			while (it.hasNext()) {
	    		Object sourceValue = it.next();
	    		if (sourceValue == null)
	    			continue;
	    		
	        	Object sourcePrimaryKey = getPrimaryKeyFromValue(sourceValue);
	        	if (sourcePrimaryKey != null)
	        		primaryKeyMap.put(sourcePrimaryKey, sourceValue);
	        	
	        	if (primaryKey.equals(sourcePrimaryKey)) {
	        		return sourceValue;
	        	}
			}
			
			return null;
		}
		finally {
			setFullSourceIterator(it);
			setPrimaryKeyMap(primaryKeyMap);
		}
    }
    
    /**
     * Updates the index and value output parameters if bound.
     */
    private void updateOutputParameters()
    {
    	IBinding indexBinding = getBinding("index");
    	if (indexBinding != null)
    		indexBinding.setObject(new Integer(_index));

    	IBinding valueBinding = getBinding("value");
    	if (valueBinding != null)
    		valueBinding.setObject(_value);
    }
    
    /**
     *  Returns the most recent value extracted from the source parameter.
     *
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the For is not currently rendering.
     *
     **/

    public final Object getValue()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "value");
  
        return _value;
    }

    /**
     *  The index number, within the {@link #getSource() source}, of the
     *  the current value.
     * 
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the For is not currently rendering.
     * 
     **/
    
    public int getIndex()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "index");
        
        return _index;
    }

    public boolean isDisabled()
    {
        return false;
    }

	// Do nothing in those methods, but make the JVM happy
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) { }
    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) { }
}
