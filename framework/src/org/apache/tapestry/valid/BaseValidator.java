/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IScript;
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
     *  @see #processValidatorScript(String, IRequestCycle, IFormComponent, Map)
     * 
     *  @since 2.2
     * 
     **/

    public static final String FIELD_SYMBOL = "field";

    /**
     *  Input symbol used to represent the validator itself to the script.
     * 
     *  @see #processValidatorScript(String, IRequestCycle, IFormComponent, Map)
     * 
     *  @since 2.2
     * 
     **/

    public static final String VALIDATOR_SYMBOL = "validator";

    /**
     *  Input symbol used to represent the {@link IForm} containing the field
     *  to the script.
     * 
     *  @see #processValidatorScript(String, IRequestCycle, IFormComponent, Map)
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
     *  @see #processValidatorScript(String, IRequestCycle, IFormComponent, Map)
     * 
     *  @since 2.2
     * 
     **/

    public static final String FUNCTION_SYMBOL = "function";

    private boolean _required;

    /** @since 3.0 */

    private String _requiredMessage;

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
     * Gets a pattern, either as the default value, or as a localized key.
     * If override is null, then the key from the
     * <code>org.apache.tapestry.valid.ValidationStrings</code>
     * {@link ResourceBundle} (in the specified locale) is used.
     * The pattern can then be used with {@link #formatString(String, Object[])}.
     * 
     * <p>Why do we not just lump these strings into TapestryStrings.properties?  
     * because TapestryStrings.properties is localized to the server's locale, which is fine
     * for the logging, debugging and error messages it contains.  For field validation, whose errors
     * are visible to the end user normally, we want to localize to the page's locale.
     *  
     * @param override The override value for the localized string from the bundle.
     * @param key used to lookup pattern from bundle, if override is null.
     * @param locale used to get right localization of bundle.
     * @since 3.0
     */

    protected String getPattern(String override, String key, Locale locale)
    {
        if (override != null)
            return override;

        ResourceBundle strings =
            ResourceBundle.getBundle("org.apache.tapestry.valid.ValidationStrings", locale);

        return strings.getString(key);
    }

    /**
     * Gets a string from the standard resource bundle.  The string in the bundle
     * is treated as a pattern for {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * 
     * @param pattern string the input pattern to be used with
     * {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * It may contain replaceable parameters, {0}, {1}, etc.
     * @param args the arguments used to fill replaceable parameters {0}, {1}, etc.
     * 
     * @since 3.0
     * 
     **/

    protected String formatString(String pattern, Object[] args)
    {
        return MessageFormat.format(pattern, args);
    }

    /**
     *  Convienience method for invoking {@link #formatString(String, Object[])}.
     * 
     *  @since 3.0
     * 
     **/

    protected String formatString(String pattern, Object arg)
    {
        return formatString(pattern, new Object[] { arg });
    }

    /**
     *  Convienience method for invoking {@link #formatString(String, Object[])}.
     * 
     *  @since 3.0
     * 
     **/

    protected String formatString(String pattern, Object arg1, Object arg2)
    {
        return formatString(pattern, new Object[] { arg1, arg2 });
    }

    /**
     *  Invoked to check if the value is null.  If the value is null (or empty),
     *  but the required flag is set, then this method throws a {@link ValidatorException}.
     *  Otherwise, returns true if the value is null.
     * 
     **/

    protected boolean checkRequired(IFormComponent field, String value) throws ValidatorException
    {
        boolean isEmpty = Tapestry.isBlank(value);

        if (_required && isEmpty)
            throw new ValidatorException(
                buildRequiredMessage(field),
                ValidationConstraint.REQUIRED);

        return isEmpty;
    }

    /** 
     * Builds an error message indicating a value for a required
     * field was not supplied.
     * 
     * @since 3.0
     */

    protected String buildRequiredMessage(IFormComponent field)
    {
        String pattern =
            getPattern(_requiredMessage, "field-is-required", field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName());
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
    {
    }

    /**
     *  Invoked (from sub-class
     *  implementations of {@link #renderValidatorContribution(IFormComponent, IMarkupWriter, IRequestCycle)}
     *  to process a standard validation script.  This expects that:
     *  <ul>
     *  <li>The {@link IFormComponent} is (ultimately) wrapped by a {@link Body}
     *  <li>The script generates a symbol named "function" (as per {@link #FUNCTION_SYMBOL})
     *  </ul>
     * 
     *  @param scriptPath the resource path of the script to execute
     *  @param cycle The active request cycle
     *  @param field The field to be validated
     *  @param symbols a set of input symbols needed by the script.  These symbols
     *  are augmented with symbols for the field, form and validator.  symbols may be
     *  null, but will be modified if not null.
     *  @throws ApplicationRuntimeException if there's an error processing the script.
     * 
     *  @since 2.2
     * 
     **/

    protected void processValidatorScript(
        String scriptPath,
        IRequestCycle cycle,
        IFormComponent field,
        Map symbols)
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

        Body body = Body.get(cycle);

        if (body == null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("ValidField.must-be-contained-by-body"),
                field,
                null,
                null);

        script.execute(cycle, body, finalSymbols);

        String functionName = (String) finalSymbols.get(FUNCTION_SYMBOL);

        form.addEventHandler(FormEventType.SUBMIT, functionName);
    }

    /**
     *  Returns true if client scripting is enabled.  Some validators are
     *  capable of generating client-side scripting to perform validation
     *  when the form is submitted.  By default, this flag is false and
     *  subclasses should check it 
     *  (in {@link #renderValidatorContribution(IFormComponent, IMarkupWriter, IRequestCycle)})
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

    public String getRequiredMessage()
    {
        return _requiredMessage;
    }

    /**
     * Overrides the <code>field-is-required</code> bundle key.
     * Parameter {0} is the display name of the field.
     * 
     * @since 3.0
     */

    public void setRequiredMessage(String string)
    {
        _requiredMessage = string;
    }

}