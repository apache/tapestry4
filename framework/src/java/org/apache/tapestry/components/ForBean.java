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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
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
    
    // injects
    public abstract DataSqueezer getDataSqueezer();
    public abstract ValueConverter getValueConverter();
    public abstract ExpressionEvaluator getExpressionEvaluator();
    
    
    private Object _value;
    private int _index;
    private boolean _rendering;
	
    /**
     *  Gets the source binding and returns an {@link Iterator}
     *  representing
     *  the values identified by the source.  Returns an empty {@link Iterator}
     *  if the binding, or the binding value, is null.
     *
     *  <p>Invokes {@link Tapestry#coerceToIterator(Object)} to perform
     *  the actual conversion.
     *
     **/

    protected Iterator getSourceData()
    {
		Object source = getSource();
		if (source == null)
			return null;
    
    	Iterator iteratorSource = (Iterator) getValueConverter().coerceValue(source, Iterator.class);
    	
    	return iteratorSource;
    }

    protected Iterator storeSourceData(IForm form, String name)
    {
    	Iterator iteratorSource = getSourceData();
		if (iteratorSource == null)
			return null;
    	
		// extract primary keys from data
		StringBuffer pkDesc = new StringBuffer();
		List data = new ArrayList();
		List pks = new ArrayList();
		while (iteratorSource.hasNext()) {
			Object value = iteratorSource.next();
			data.add(value);
			
			Object pk = getPrimaryKeyFromValue(value);
			if (pk == null) {
				pk = value;
				pkDesc.append(DESC_VALUE);
			}
			else
				pkDesc.append(DESC_PRIMARY_KEY);
			pks.add(pk);
		}
		
		// store primary keys
        form.addHiddenValue(name, pkDesc.toString());
        for (Iterator it = pks.iterator(); it.hasNext();) {
			Object pk = it.next();
	    	try {
				String stringRep = getDataSqueezer().squeeze(pk);
				form.addHiddenValue(name, stringRep);
			} catch (IOException ex) {
	            throw new ApplicationRuntimeException(
	                    Tapestry.format("For.unable-to-convert-value", pk),
	                    this,
	                    null,
	                    ex);
			}
		}

    	return data.iterator();
    }

    protected Iterator getStoredData(IRequestCycle cycle, String name)
    {
        String[] submittedPrimaryKeys = cycle.getParameters(name);
        String pkDesc = submittedPrimaryKeys[0];

        // unsqueeze data
        List data = new ArrayList(submittedPrimaryKeys.length-1);
        List pks = new ArrayList(submittedPrimaryKeys.length-1);
        for (int i = 1; i < submittedPrimaryKeys.length; i++) {
        	String stringRep = submittedPrimaryKeys[i];
        	try {
				Object value = getDataSqueezer().unsqueeze(stringRep);
				data.add(value);
				if (i <= pkDesc.length() && pkDesc.charAt(i-1) == DESC_PRIMARY_KEY)
					pks.add(value);
			} catch (IOException ex) {
	            throw new ApplicationRuntimeException(
	                    Tapestry.format("For.unable-to-convert-string", stringRep),
	                    this,
	                    null,
	                    ex);
			}
		}

        // update the binding with the list of primary keys
    	IBinding primaryKeysBinding = getBinding("primaryKeys");
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
     *  Gets the source binding and iterates through
     *  its values.  For each, it updates the value binding and render's its wrapped elements.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // form may be null if component is not located in a form
        IForm form = (IForm) cycle.getAttribute(TapestryUtils.FORM_ATTRIBUTE);

        // If the cycle is rewinding, but not this particular form,
        // then do nothing (don't even render the body).
        boolean cycleRewinding = cycle.isRewinding();
        if (cycleRewinding && form != null && !form.isRewinding())
            return;

        boolean bInForm = (form != null && !getVolatile());
        
        String name = "";
        if (form != null)
            name = form.getElementId(this);

        // Get the data to be iterated upon. Store in form if needed.
        Iterator dataSource;
        if (!bInForm)
        	dataSource = getSourceData();
        else if (cycleRewinding)
        	dataSource = getStoredData(cycle, name);
        else 
        	dataSource = storeSourceData(form, name);
      	
        
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
            	IBinding indexBinding = getBinding("index");
            	if (indexBinding != null)
            		indexBinding.setObject(new Integer(_index));

            	IBinding valueBinding = getBinding("value");
            	if (valueBinding != null)
            		valueBinding.setObject(_value);
            	
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

    private Object restoreValue(IForm form, String name, Object primaryKey)
    {
    	return getValueFromPrimaryKey(primaryKey);
    }
    
    private void storeValue(IForm form, String name, Object value)
    {
    	Object convertedValue = getPrimaryKeyFromValue(value);
    	
        try
        {
        	String externalValue = getDataSqueezer().squeeze(convertedValue);
            form.addHiddenValue(name, externalValue);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.format("For.unable-to-convert-value", value),
                this,
                null,
                ex);
        }
    }

    private Object getPrimaryKeyFromValue(Object value) {
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
    
    private Object getValueFromPrimaryKey(Object primaryKey) {
    	Object value = null;

    	if (value == null && getKeyExpression() != null) {
    		String keyExpression = getKeyExpression();
    		if (keyExpression != null) {
        		Map primaryKeys = getPrimaryKeyMap();
            	if (primaryKeys == null)
            		primaryKeys = initializePrimaryKeysFromSource(keyExpression);
            	value = primaryKeys.get(primaryKeys);
    		}
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
    
    private Map initializePrimaryKeysFromSource(String keyExpression)
    {
    	Map primaryKeys = new HashMap();
    	
    	Object fullSource = getFullSource();
    	if (fullSource == null)
    		fullSource = getSource();
    	if (fullSource == null)
    		return primaryKeys;
    	
    	ExpressionEvaluator evaluator = getExpressionEvaluator();
    	
    	Iterator iteratorSource = (Iterator) getValueConverter().coerceValue(fullSource, Iterator.class);
    	while (iteratorSource.hasNext()) {
    		Object value = iteratorSource.next();
        	Object primaryKey = evaluator.read(value, keyExpression);
        	if (primaryKey != null)
        		primaryKeys.put(primaryKey, value);
    	}
    	
    	setPrimaryKeyMap(primaryKeys);
    	return primaryKeys;
    }
    
    /**
     *  Returns the most recent value extracted from the source parameter.
     *
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the Foreach is not currently rendering.
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
     *  @throws org.apache.tapestry.ApplicationRuntimeException if the Foreach is not currently rendering.
     *
     *  @since 2.2
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
