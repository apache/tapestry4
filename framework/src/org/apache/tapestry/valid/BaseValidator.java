/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.valid;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
import org.apache.tapestry.RequestCycleException;
import org.apache.tapestry.ScriptException;
import org.apache.tapestry.ScriptSession;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.form.FormEventType;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.html.Body;
import org.apache.tapestry.resource.ClasspathResourceLocation;

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
     *  to the {@link IForm} as a {@link org.apache.tapestry.form.FormEventType#SUBMIT}.
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
        ResourceBundle strings =
            ResourceBundle.getBundle("org.apache.tapestry.valid.ValidationStrings", locale);

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

    protected boolean checkRequired(IFormComponent field, String value) throws ValidatorException
    {
        boolean isNull = Tapestry.isNull(value);

        if (_required && isNull)
            throw new ValidatorException(
                getString("field-is-required", field.getPage().getLocale(), field.getDisplayName()),
                ValidationConstraint.REQUIRED);

        return isNull;
    }

    /**
     *  This implementation does nothing.  Subclasses may supply their own implementation.
     * 
     *  @since 2.2
     * 
     **/

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
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

    protected void processValidatorScript(
        String scriptPath,
        IRequestCycle cycle,
        IFormComponent field,
        Map symbols)
        throws RequestCycleException
    {
        IEngine engine = field.getPage().getEngine();
        IScriptSource source = engine.getScriptSource();
        IForm form = field.getForm();

        Map finalSymbols = (symbols == null) ? new HashMap() : symbols;

        finalSymbols.put(FIELD_SYMBOL, field);
        finalSymbols.put(FORM_SYMBOL, form);
        finalSymbols.put(VALIDATOR_SYMBOL, this);

        IResourceLocation location =
            new ClasspathResourceLocation(engine.getResourceResolver(), scriptPath);

        IScript script = source.getScript(location);

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
            throw new RequestCycleException(
                Tapestry.getString("ValidField.must-be-contained-by-body"),
                field);

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