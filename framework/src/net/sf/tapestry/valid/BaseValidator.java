//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.valid;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.tapestry.IEngine;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IScript;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ScriptException;
import net.sf.tapestry.ScriptSession;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.form.FormEventType;
import net.sf.tapestry.html.Body;

/**
 *  Abstract base class for {@link IValidator}.  Supports a required and locale property.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public abstract class BaseValidator implements IValidator
{
    /**
     *  Input Symbol used to represent the field being validated.
     * 
     *  @see #processValidatorScript(String, IRequestCycle, IField, Map)
     * 
     *  @since 2.2
     * 
     **/

    public static final String FIELD_SYMBOL = "field";

    /**
     *  Input symbol used to represent the validator itself to the script.
     * 
     *  @see #processValidatorScript(String, IRequestCycle, IField, Map)
     * 
     *  @since 2.2
     * 
     **/

    public static final String VALIDATOR_SYMBOL = "validator";

    /**
     *  Input symbol used to represent the {@link IForm} containing the field
     *  to the script.
     * 
     *  @see #processValidatorScript(String, IRequestCycle, IField, Map)
     *  
     *  @since 2.2
     **/

    public static final String FORM_SYMBOL = "form";

    /**
     *  Output symbol set by the script asthe name of the validator 
     *  JavaScript function.
     *  The function implemented must return true or false (true
     *  if the field is valid, false otherwise).
     *  After the script is executed, the function is added
     *  to the {@link IForm} as a {@link net.sf.tapestry.form.FormEventType#SUBMIT}.
     * 
     *  @see #processValidatorScript(String, IRequestCycle, IField, Map)
     * 
     *  @since 2.2
     * 
     **/

    public static final String FUNCTION_SYMBOL = "function";

    private boolean _required;

    /** 
     *  @since 2.2
     * 
     **/

    private boolean _clientScriptingEnabled = false;

    /**
     *  Standard constructor.  Leaves locale as system default and required as false.
     * 
     **/

    public BaseValidator()
    {
    }

    protected BaseValidator(boolean required)
    {
        _required = required;
    }

    public boolean isRequired()
    {
        return _required;
    }

    public void setRequired(boolean required)
    {
        _required = required;
    }

    /**
     *  Gets a string from the standard resource bundle.  The string in the bundle
     *  is treated as a pattern for {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * 
     *  <p>Why do we not just lump these strings into TapestryStrings.properties?  
     *  Because TapestryStrings.properties is localized to the server's locale, which is fine
     *  for the logging, debugging and error messages it contains.  For field validation, whose errors
     *  are visible to the end user normally, we want to localize to the page and engine's locale.
     * 
     *  <p>This will be more useful when we have actual localizations of ValidationStrings.properties.
     * 
     *  @since 2.1
     * 
     **/

    protected String getString(String key, Locale locale, Object[] args)
    {
        ResourceBundle strings = ResourceBundle.getBundle("net.sf.tapestry.valid.ValidationStrings", locale);

        String pattern = strings.getString(key);

        return MessageFormat.format(pattern, args);
    }

    /**
     *  Convienience method for invoking {@link #getString(String, Locale, Object[])}.
     * 
     *  @since 2.1
     * 
     **/

    protected String getString(String key, Locale locale, Object arg)
    {
        return getString(key, locale, new Object[] { arg });
    }

    /**
     *  Convienience method for invoking {@link #getString(String, Locale, Object[])}.
     * 
     *  @since 2.1
     * 
     **/

    protected String getString(String key, Locale locale, Object arg1, Object arg2)
    {
        return getString(key, locale, new Object[] { arg1, arg2 });
    }

    /**
     *  Invoked to check if the value is null.  If the value is null (or empty),
     *  but the required flag is set, then this method throws a {@link ValidatorException}.
     *  Otherwise, returns true if the value is null.
     * 
     **/

    protected boolean checkRequired(IField field, String value) throws ValidatorException
    {
        boolean isNull = Tapestry.isNull(value);

        if (_required && isNull)
            throw new ValidatorException(
                getString("field-is-required", field.getPage().getLocale(), field.getDisplayName()),
                ValidationConstraint.REQUIRED,
                null);

        return isNull;
    }

    /**
     *  This implementation does nothing.  Subclasses may supply their own implementation.
     * 
     *  @since 2.2
     * 
     **/

    public void renderValidatorContribution(IField field, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
    }

    /**
     *  Invoked (from sub-class
     *  implementations of {@link #renderValidatorContribution(IField, IMarkupWriter, IRequestCycle)}
     *  to process a standard validation script.  This expects that:
     *  <ul>
     *  <li>The {@link IField} is (ultimately) wrapped by a {@link Body}
     *  <li>The script generates a symbol named "function" (as per {@link #FUNCTION_SYMBOL})
     *  </ul>
     * 
     *  @param scriptPath the resource path of the script to execute
     *  @param cycle The active request cycle
     *  @param field The field to be validated
     *  @param symbols a set of input symbols needed by the script.  These symbols
     *  are augmented with symbols for the field, form and validator.  symbols may be
     *  null, but will be modified if not null.
     *  @throws RequestCycleException if there's an error processing the script.
     * 
     *  @since 2.2
     * 
     **/

    protected void processValidatorScript(String scriptPath, IRequestCycle cycle, IField field, Map symbols)
        throws RequestCycleException
    {
        IEngine engine = field.getPage().getEngine();
        IScriptSource source = engine.getScriptSource();
        IForm form = field.getForm();

        Map finalSymbols = (symbols == null) ? new HashMap() : symbols;

        finalSymbols.put(FIELD_SYMBOL, field);
        finalSymbols.put(FORM_SYMBOL, form);
        finalSymbols.put(VALIDATOR_SYMBOL, this);

        IScript script = source.getScript(scriptPath);

        ScriptSession session;

        try
        {
            session = script.execute(finalSymbols);
        }
        catch (ScriptException ex)
        {
            throw new RequestCycleException(ex.getMessage(), field, ex);
        }

        Body body = Body.get(cycle);

        if (body == null)
            throw new RequestCycleException(Tapestry.getString("ValidField.must-be-contained-by-body"), field);

        body.process(session);

        String functionName = (String) finalSymbols.get(FUNCTION_SYMBOL);

        form.addEventHandler(FormEventType.SUBMIT, functionName);
    }

    /**
     *  Returns true if client scripting is enabled.  Some validators are
     *  capable of generating client-side scripting to perform validation
     *  when the form is submitted.  By default, this flag is false and
     *  subclasses should check it 
     *  (in {@link #renderValidatorContribution(IField, IMarkupWriter, IRequestCycle)})
     *  before generating client side script.
     * 
     *  @since 2.2
     * 
     **/

    public boolean isClientScriptingEnabled()
    {
        return _clientScriptingEnabled;
    }

    public void setClientScriptingEnabled(boolean clientScriptingEnabled)
    {
        _clientScriptingEnabled = clientScriptingEnabled;
    }

}