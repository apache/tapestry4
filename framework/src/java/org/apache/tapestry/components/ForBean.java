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
 * @since 4.0
 * @see org.apache.tapestry.components.IPrimaryKeyConverter
 * @see org.apache.tapestry.util.DefaultPrimaryKeyConverter
 */
public abstract class ForBean extends AbstractFormComponent
{
    // constants

    /**
     * Prefix on the hidden value stored into the field to indicate the the actual value is stored
     * (this is used when there is no primary key converter). The remainder of the string is a
     * {@link DataSqueezer squeezed} representation of the value.
     */
    private static final char DESC_VALUE = 'V';

    /**
     * Prefix on the hidden value stored into the field that indicates the primary key of the
     * iterated value is stored; the remainder of the string is a {@link DataSqueezer squeezed}
     * representation of the primary key. The {@link IPrimaryKeyConverter converter} is used to
     * obtain the value from this key.
     */
    private static final char DESC_PRIMARY_KEY = 'P';

    private final RepSource COMPLETE_REP_SOURCE = new CompleteRepSource();

    private final RepSource KEY_EXPRESSION_REP_SOURCE = new KeyExpressionRepSource();

    // parameters
    public abstract String getElement();

    public abstract String getKeyExpression();

    public abstract IPrimaryKeyConverter getConverter();

    public abstract Object getDefaultValue();

    public abstract boolean getMatch();

    public abstract boolean getVolatile();

    // injects
    public abstract DataSqueezer getDataSqueezer();

    public abstract ValueConverter getValueConverter();

    public abstract ExpressionEvaluator getExpressionEvaluator();

    // intermediate members
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
     * Returns the most recent value extracted from the source parameter.
     * 
     * @throws org.apache.tapestry.ApplicationRuntimeException
     *             if the For is not currently rendering.
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
     *             if the For is not currently rendering.
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

    /**
     * Updates the index and value output parameters if bound.
     */
    protected void updateOutputParameters()
    {
        IBinding indexBinding = getBinding("index");
        if (indexBinding != null)
            indexBinding.setObject(new Integer(_index));

        IBinding valueBinding = getBinding("value");
        if (valueBinding != null)
            valueBinding.setObject(_value);
    }

    /**
     * Updates the primaryKeys parameter if bound.
     */
    protected void updatePrimaryKeysParameter(String[] stringReps)
    {
        IBinding primaryKeysBinding = getBinding("primaryKeys");
        if (primaryKeysBinding == null)
            return;

        DataSqueezer squeezer = getDataSqueezer();

        int repsCount = stringReps.length;
        List primaryKeys = new ArrayList(repsCount);
        for (int i = 0; i < stringReps.length; i++)
        {
            String rep = stringReps[i];
            if (rep.length() == 0 || rep.charAt(0) != DESC_PRIMARY_KEY)
                continue;
            Object primaryKey = squeezer.unsqueeze(rep.substring(1));
            primaryKeys.add(primaryKey);
        }

        primaryKeysBinding.setObject(primaryKeys);
    }

    // Do nothing in those methods, but make the JVM happy
    protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
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
            return evaluateSourceIterator();

        String name = form.getElementId(this);
        if (cycle.isRewinding())
            return getStoredData(cycle, name);
        return storeSourceData(form, name);
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
        String[] stringReps = cycle.getParameters(name);
        if (stringReps == null)
            return null;

        updatePrimaryKeysParameter(stringReps);

        return new ReadSourceDataIterator(stringReps);
    }

    /**
     * Pulls data from successive strings (posted by client-side hidden fields); each string
     * representation may be either a value or a primary key.
     */
    private class ReadSourceDataIterator implements Iterator
    {
        private final Iterator _sourceIterator = evaluateSourceIterator();

        private final Iterator _fullSourceIterator = evaluateFullSourceIterator();

        private final String[] _stringReps;

        private int _index = 0;

        private final Map _repToValueMap = new HashMap();

        ReadSourceDataIterator(String[] stringReps)
        {
            _stringReps = stringReps;
        }

        public boolean hasNext()
        {
            return _index < _stringReps.length;
        }

        public Object next()
        {
            String rep = _stringReps[_index++];

            return getValueFromStringRep(_sourceIterator, _fullSourceIterator, _repToValueMap, rep);
        }

        public void remove()
        {
            throw new UnsupportedOperationException("remove()");
        }

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
        return new StoreSourceDataIterator(form, name, evaluateSourceIterator());
    }

    /**
     * Iterates over a set of values, using {@link ForBean#getStringRepFromValue(Object)} to obtain
     * the correct client-side string representation, and working with the form to store each
     * successive value into the form.
     */
    private class StoreSourceDataIterator implements Iterator
    {
        private final IForm _form;

        private final String _name;

        private final Iterator _delegate;

        StoreSourceDataIterator(IForm form, String name, Iterator delegate)
        {
            _form = form;
            _name = name;
            _delegate = delegate;
        }

        public boolean hasNext()
        {
            return _delegate.hasNext();
        }

        public Object next()
        {
            Object value = _delegate.next();

            String rep = getStringRepFromValue(value);

            _form.addHiddenValue(_name, rep);

            return value;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("remove()");
        }
    }

    /**
     * Returns the string representation of the value. The first letter of the string representation
     * shows whether a value or a primary key is being described.
     * 
     * @param value
     * @return
     */
    protected String getStringRepFromValue(Object value)
    {
        String rep;
        DataSqueezer squeezer = getDataSqueezer();

        // try to extract the primary key from the value
        Object pk = getPrimaryKeyFromValue(value);
        if (pk != null)
            // Primary key was extracted successfully.
            rep = DESC_PRIMARY_KEY + squeezer.squeeze(pk);
        else
            // primary key could not be extracted. squeeze value.
            rep = DESC_VALUE + squeezer.squeeze(value);

        return rep;
    }

    /**
     * Returns the primary key of the given value. Uses the 'keyExpression' or the 'converter' (if
     * either is provided).
     * 
     * @param value
     *            The value from which the primary key should be extracted
     * @return The primary key of the value, or null if such cannot be extracted.
     */
    protected Object getPrimaryKeyFromValue(Object value)
    {
        if (value == null)
            return null;

        Object primaryKey = getKeyExpressionFromValue(value);
        if (primaryKey == null)
            primaryKey = getConverterFromValue(value);

        return primaryKey;
    }

    /**
     * Uses the 'keyExpression' parameter to determine the primary key of the given value
     * 
     * @param value
     *            The value from which the primary key should be extracted
     * @return The primary key of the value as defined by 'keyExpression', or null if such cannot be
     *         extracted.
     */
    protected Object getKeyExpressionFromValue(Object value)
    {
        String keyExpression = getKeyExpression();
        if (keyExpression == null)
            return null;

        Object primaryKey = getExpressionEvaluator().read(value, keyExpression);
        return primaryKey;
    }

    /**
     * Uses the 'converter' parameter to determine the primary key of the given value
     * 
     * @param value
     *            The value from which the primary key should be extracted
     * @return The primary key of the value as provided by the converter, or null if such cannot be
     *         extracted.
     */
    protected Object getConverterFromValue(Object value)
    {
        IPrimaryKeyConverter converter = getConverter();
        if (converter == null)
            return null;

        Object primaryKey = converter.getPrimaryKey(value);
        return primaryKey;
    }

    /**
     * Determines the value that corresponds to the given string representation. If the 'match'
     * parameter is true, attempt to find a value in 'source' or 'fullSource' that generates the
     * same string representation. Otherwise, create a new value from the string representation.
     * 
     * @param rep
     *            the string representation for which a value should be returned
     * @return the value that corresponds to the provided string representation
     */
    protected Object getValueFromStringRep(Iterator sourceIterator, Iterator fullSourceIterator,
            Map repToValueMap, String rep)
    {
        Object value = null;
        DataSqueezer squeezer = getDataSqueezer();

        // Check if the string rep is empty. If so, just return the default value.
        if (rep == null || rep.length() == 0)
            return getDefaultValue();

        // If required, find a value with an equivalent string representation and return it
        boolean match = getMatch();
        if (match)
        {
            value = findValueWithStringRep(
                    sourceIterator,
                    fullSourceIterator,
                    repToValueMap,
                    rep,
                    COMPLETE_REP_SOURCE);
            if (value != null)
                return value;
        }

        // Matching of the string representation was not successful or was disabled.
        // Use the standard approaches to obtain the value from the rep.
        char desc = rep.charAt(0);
        String squeezed = rep.substring(1);
        switch (desc)
        {
            case DESC_VALUE:
                // If the string rep is just the value itself, unsqueeze it
                value = squeezer.unsqueeze(squeezed);
                break;

            case DESC_PRIMARY_KEY:
                // Perform keyExpression match if not already attempted
                if (!match && getKeyExpression() != null)
                    value = findValueWithStringRep(
                            sourceIterator,
                            fullSourceIterator,
                            repToValueMap,
                            rep,
                            KEY_EXPRESSION_REP_SOURCE);

                // If 'converter' is defined, try to perform conversion from primary key to value
                if (value == null)
                {
                    IPrimaryKeyConverter converter = getConverter();
                    if (converter != null)
                    {
                        Object pk = squeezer.unsqueeze(squeezed);
                        value = converter.getValue(pk);
                    }
                }
                break;
        }

        if (value == null)
            value = getDefaultValue();

        return value;
    }

    /**
     * Attempt to find a value in 'source' or 'fullSource' that generates the provided string
     * representation. Use the RepSource interface to determine what the string representation of a
     * particular value is.
     * 
     * @param rep
     *            the string representation for which a value should be returned
     * @param repSource
     *            an interface providing the string representation of a given value
     * @return the value in 'source' or 'fullSource' that corresponds to the provided string
     *         representation
     */
    protected Object findValueWithStringRep(Iterator sourceIterator, Iterator fullSourceIterator,
            Map repToValueMap, String rep, RepSource repSource)
    {
        Object value = repToValueMap.get(rep);
        if (value != null)
            return value;

        value = findValueWithStringRepInIterator(sourceIterator, repToValueMap, rep, repSource);
        if (value != null)
            return value;

        value = findValueWithStringRepInIterator(fullSourceIterator, repToValueMap, rep, repSource);
        return value;
    }

    /**
     * Attempt to find a value in the provided collection that generates the required string
     * representation. Use the RepSource interface to determine what the string representation of a
     * particular value is.
     * 
     * @param rep
     *            the string representation for which a value should be returned
     * @param repSource
     *            an interface providing the string representation of a given value
     * @param it
     *            the iterator of the collection in which a value should be searched
     * @return the value in the provided collection that corresponds to the required string
     *         representation
     */
    protected Object findValueWithStringRepInIterator(Iterator it, Map repToValueMap, String rep,
            RepSource repSource)
    {
        while (it.hasNext())
        {
            Object sourceValue = it.next();
            if (sourceValue == null)
                continue;

            String sourceRep = repSource.getStringRep(sourceValue);
            repToValueMap.put(sourceRep, sourceValue);

            if (rep.equals(sourceRep))
                return sourceValue;
        }

        return null;
    }

    /**
     * Returns a new iterator of the values in 'source'.
     * 
     * @return the 'source' iterator
     */
    protected Iterator evaluateSourceIterator()
    {
        Iterator it = null;
        Object source = null;

        IBinding sourceBinding = getBinding("source");
        if (sourceBinding != null)
            source = sourceBinding.getObject();

        if (source != null)
            it = (Iterator) getValueConverter().coerceValue(source, Iterator.class);

        if (it == null)
            it = Collections.EMPTY_LIST.iterator();

        return it;
    }

    /**
     * Returns a new iterator of the values in 'fullSource'.
     * 
     * @return the 'fullSource' iterator
     */
    protected Iterator evaluateFullSourceIterator()
    {
        Iterator it = null;
        Object fullSource = null;

        IBinding fullSourceBinding = getBinding("fullSource");
        if (fullSourceBinding != null)
            fullSource = fullSourceBinding.getObject();

        if (fullSource != null)
            it = (Iterator) getValueConverter().coerceValue(fullSource, Iterator.class);

        if (it == null)
            it = Collections.EMPTY_LIST.iterator();

        return it;
    }

    /**
     * An interface that provides the string representation of a given value
     */
    protected interface RepSource
    {
        String getStringRep(Object value);
    }

    /**
     * An implementation of RepSource that provides the string representation of the given value
     * using all methods.
     */
    protected class CompleteRepSource implements RepSource
    {
        public String getStringRep(Object value)
        {
            return getStringRepFromValue(value);
        }
    }

    /**
     * An implementation of RepSource that provides the string representation of the given value
     * using just the 'keyExpression' parameter.
     */
    protected class KeyExpressionRepSource implements RepSource
    {
        public String getStringRep(Object value)
        {
            Object pk = getKeyExpressionFromValue(value);
            return DESC_PRIMARY_KEY + getDataSqueezer().squeeze(pk);
        }
    }

    /**
     * For component can not take focus.
     */
    protected boolean getCanTakeFocus()
    {
        return false;
    }

    public String getClientId()
    {
        return null;
    }

    public String getDisplayName()
    {
        return null;
    }

}
