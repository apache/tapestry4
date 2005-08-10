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
public abstract class ForBean extends AbstractFormComponent
{
    private static final char DESC_VALUE = 'V';

    private static final char DESC_PRIMARY_KEY = 'P';

    private static final String PARAMETER_SOURCE = "source";

    private static final String PARAMETER_FULL_SOURCE = "fullSource";

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

    public abstract Map getSourceIteratorMap();

    public abstract void setSourceIteratorMap(Map sourceIteratorMap);

    // injects
    public abstract DataSqueezer getDataSqueezer();

    public abstract ValueConverter getValueConverter();

    public abstract ExpressionEvaluator getExpressionEvaluator();

    private Object _value;

    private int _index;

    private boolean _rendering;

    /**
     * Gets the source binding and iterates through its values. For each, it updates the value
     * binding and render's its wrapped elements.
     */

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
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
     * Returns a list with the values to be iterated upon. The list is obtained in different ways: -
     * If the component is not located in a form or 'volatile' is set to true, then the simply the
     * values passed to 'source' are returned (same as Foreach) - If the component is in a form, and
     * the form is rewinding, the values stored in the form are returned -- rewind is then always
     * the same as render. - If the component is in a form, and the form is being rendered, the
     * values are stored in the form as Hidden fields.
     * 
     * @param cycle
     *            The current request cycle
     * @param form
     *            The form within which the component is located (if any)
     * @return An iterator with the values to be cycled upon
     */
    private Iterator getData(IRequestCycle cycle, IForm form)
    {
        if (form == null || getVolatile())
            return getSource(PARAMETER_SOURCE);

        String name = form.getElementId(this);
        if (cycle.isRewinding())
            return getStoredData(cycle, name);

        return storeSourceData(form, name);
    }

    /**
     * Returns an {@link Iterator} containing the values provided by the identified source binding.
     * 
     * @param parameter
     *            The name of the source binding
     * @return an iterator with the bound values. null if the parameter is not bound or the
     *         conversion cannot be performed
     */
    protected Iterator getSource(String parameter)
    {
        IBinding binding = getBinding(parameter);
        if (binding == null)
            return null;

        Object data = binding.getObject();
        return (Iterator) getValueConverter().coerceValue(data, Iterator.class);
    }

    /**
     * Returns a list of the values stored as Hidden fields in the form. A conversion is performed
     * if the primary key of the value is stored.
     * 
     * @param cycle
     *            The current request cycle
     * @param name
     *            The name of the HTTP parameter whether the values
     * @return an iterator with the values stored in the provided Hidden fields
     */
    protected Iterator getStoredData(IRequestCycle cycle, String name)
    {
        String[] submittedPrimaryKeys = cycle.getParameters(name);
        String pkDesc = submittedPrimaryKeys[0];

        // unsqueeze data
        List data = new ArrayList(submittedPrimaryKeys.length - 1);
        List pks = new ArrayList(submittedPrimaryKeys.length - 1);
        for (int i = 1; i < submittedPrimaryKeys.length; i++)
        {
            String stringRep = submittedPrimaryKeys[i];
            try
            {
                Object value = getDataSqueezer().unsqueeze(stringRep);
                data.add(value);
                if (i <= pkDesc.length() && pkDesc.charAt(i - 1) == DESC_PRIMARY_KEY)
                    pks.add(value);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(Tapestry.format(
                        "For.unable-to-convert-string",
                        stringRep), this, null, ex);
            }
        }

        // update the binding with the list of primary keys
        IBinding primaryKeysBinding = getBinding("primaryKeys");
        if (primaryKeysBinding != null)
            primaryKeysBinding.setObject(pks);

        // convert from primary keys to data
        for (int i = 0; i < data.size(); i++)
        {
            if (i <= pkDesc.length() && pkDesc.charAt(i) == DESC_PRIMARY_KEY)
            {
                Object pk = data.get(i);
                Object value = getValueFromPrimaryKey(pk);
                data.set(i, value);
            }
        }

        return data.iterator();
    }

    /**
     * Stores the provided data in the form and then returns the data as an iterator. If the primary
     * key of the value can be determined, then that primary key is saved instead.
     * 
     * @param form
     *            The form where the data will be stored
     * @param name
     *            The name under which the data will be stored
     * @return an iterator with the bound values stored in the form
     */
    protected Iterator storeSourceData(IForm form, String name)
    {
        Iterator iteratorSource = getSource(PARAMETER_SOURCE);
        if (iteratorSource == null)
            return null;

        // extract primary keys from data
        StringBuffer pkDesc = new StringBuffer();
        List data = new ArrayList();
        List pks = new ArrayList();
        while (iteratorSource.hasNext())
        {
            Object value = iteratorSource.next();
            data.add(value);

            Object pk = getPrimaryKeyFromValue(value);
            if (pk == null)
            {
                pk = value;
                pkDesc.append(DESC_VALUE);
            }
            else
                pkDesc.append(DESC_PRIMARY_KEY);
            pks.add(pk);
        }

        // store primary keys
        form.addHiddenValue(name, pkDesc.toString());
        for (Iterator it = pks.iterator(); it.hasNext();)
        {
            Object pk = it.next();
            try
            {
                String stringRep = getDataSqueezer().squeeze(pk);
                form.addHiddenValue(name, stringRep);
            }
            catch (Exception ex)
            {
                throw new ApplicationRuntimeException(Tapestry.format(
                        "For.unable-to-convert-value",
                        pk), this, null, ex);
            }
        }

        return data.iterator();
    }

    /**
     * Returns the primary key of the given value. Uses the 'keyExpression' or the 'converter' (if
     * either is provided).
     * 
     * @param value
     *            The value from which the primary key should be extracted
     * @return The primary key of the value, or null if such cannot be extracted.
     */
    private Object getPrimaryKeyFromValue(Object value)
    {
        Object primaryKey = null;

        String keyExpression = getKeyExpression();
        if (keyExpression != null)
            primaryKey = getExpressionEvaluator().read(value, keyExpression);

        if (primaryKey == null)
        {
            IPrimaryKeyConverter converter = getConverter();
            if (converter != null)
                primaryKey = converter.getPrimaryKey(value);
        }

        return primaryKey;
    }

    /**
     * Returns a value that corresponds to the provided primary key. Uses either the 'keyExpression'
     * if provided
     * 
     * @param primaryKey
     * @return
     */
    private Object getValueFromPrimaryKey(Object primaryKey)
    {
        Object value = null;

        // if keyExpression is defined, try to get the object in that way
        String keyExpression = getKeyExpression();
        if (keyExpression != null)
            value = getValueFromExpressionPrimaryKeys(keyExpression, primaryKey);

        if (value == null)
        {
            IPrimaryKeyConverter converter = getConverter();
            if (converter != null)
                value = converter.getValue(primaryKey);
        }

        if (value == null)
            value = getDefaultValue();

        return value;
    }

    private Object getValueFromExpressionPrimaryKeys(String keyExpression, Object primaryKey)
    {
        Object value = null;

        Map primaryKeys = getPrimaryKeyMap();
        if (primaryKeys == null)
            primaryKeys = new HashMap();
        else
        {
            value = primaryKeys.get(primaryKey);
            if (value != null)
                return value;
        }

        // Iterate over the elements in 'source' and 'fullSource' until a primary key match is found
        value = findPrimaryKeyMatch(primaryKey, PARAMETER_SOURCE);
        if (value == null)
            value = findPrimaryKeyMatch(primaryKey, PARAMETER_FULL_SOURCE);

        return value;
    }

    private Object findPrimaryKeyMatch(Object primaryKey, String parameter)
    {
        ExpressionEvaluator evaluator = getExpressionEvaluator();
        String keyExpression = getKeyExpression();

        Map primaryKeys = getPrimaryKeyMap();
        if (primaryKeys == null)
            primaryKeys = new HashMap();

        Map sourceIteratorMap = getSourceIteratorMap();
        if (sourceIteratorMap == null)
            sourceIteratorMap = new HashMap();

        Iterator it = (Iterator) sourceIteratorMap.get(parameter);
        if (it == null)
        {
            it = getSource(parameter);
            if (it == null)
                it = Collections.EMPTY_LIST.iterator();
        }

        try
        {
            while (it.hasNext())
            {
                Object sourceValue = it.next();
                Object sourcePrimaryKey = evaluator.read(sourceValue, keyExpression);
                if (sourcePrimaryKey != null)
                    primaryKeys.put(sourcePrimaryKey, sourceValue);

                if (primaryKey.equals(sourcePrimaryKey))
                {
                    return sourceValue;
                }
            }

            return null;
        }
        finally
        {
            sourceIteratorMap.put(parameter, it);
            setSourceIteratorMap(sourceIteratorMap);
            setPrimaryKeyMap(primaryKeys);
        }
    }

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
     * Returns the most recent value extracted from the source parameter.
     * 
     * @throws org.apache.tapestry.ApplicationRuntimeException
     *             if the Foreach is not currently rendering.
     */

    public final Object getValue()
    {
        if (!_rendering)
            throw Tapestry.createRenderOnlyPropertyException(this, "value");

        return _value;
    }

    /**
     * The index number, within the {@link #getSource() source}, of the the current value.
     * 
     * @throws org.apache.tapestry.ApplicationRuntimeException
     *             if the Foreach is not currently rendering.
     * @since 2.2
     */

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
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    }
}
