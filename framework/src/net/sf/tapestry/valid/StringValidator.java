package net.sf.tapestry.valid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Simple validation of strings, to enforce required, and minimum length
 *  (maximum length is enforced in the client browser, by setting a maximum input
 *  length on the text field).
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class StringValidator extends BaseValidator
{
    private static final class StaticStringValidator extends StringValidator
    {
        private static final String UNSUPPORTED_MESSAGE = "Changes to property values are not allowed.";

        private StaticStringValidator(boolean required)
        {
            super(required);
        }

        /** @throws UnsupportedOperationException **/

        public void setMinimumLength(int minimumLength)
        {
            throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
        }

        /** @throws UnsupportedOperationException **/

        public void setRequired(boolean required)
        {
            throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
        }
        
        /** @throws UnsupportedOperationException **/
                
        public void setClientScriptingEnabled(boolean clientScriptingEnabled)
        {
            throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
        }
   }

    /**
     *  Returns a shared instance of a StringValidator with the required flag set.
     *  The instance is not modifiable.
     *
     **/

    public static final StringValidator REQUIRED = new StaticStringValidator(true);

    /**
     *  Returns a shared instance of a StringValidator with the required flag cleared.
     *  The instance is not modifiable.
     *
     **/

    public static final StringValidator OPTIONAL = new StaticStringValidator(false);

    private int _minimumLength;

    /** @since 2.2 **/
    
    private String _scriptPath = "/net/sf/tapestry/valid/StringValidator.script";

    public StringValidator()
    {
    }

    private StringValidator(boolean required)
    {
        super(required);
    }

    public String toString(IField field, Object value)
    {
        if (value == null)
            return null;

        return value.toString();
    }

    public Object toObject(IField field, String input) throws ValidatorException
    {
        if (checkRequired(field, input))
            return null;

        if (_minimumLength > 0 && input.length() < _minimumLength)
        {
            String errorMessage =
                getString(
                    "field-too-short",
                    field.getPage().getLocale(),
                    Integer.toString(_minimumLength),
                    field.getDisplayName());

            throw new ValidatorException(errorMessage, ValidationConstraint.MINIMUM_WIDTH, input);
        }

        return input;
    }

    public int getMinimumLength()
    {
        return _minimumLength;
    }

    public void setMinimumLength(int minimumLength)
    {
        _minimumLength = minimumLength;
    }

    /** 
     * 
     *  @since 2.2
     * 
     **/

    public void renderValidatorContribution(IField field, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (!isClientScriptingEnabled())
            return;

        if (!(isRequired() || _minimumLength > 0))
            return;

        Map symbols = new HashMap();

        Locale locale = field.getPage().getLocale();
        String displayName = field.getDisplayName();

        if (isRequired())
            symbols.put("requiredMessage", getString("field-is-required", locale, displayName));

        if (_minimumLength > 0)
            symbols.put(
                "minimumLengthMessage",
                getString("field-too-short", locale, Integer.toString(_minimumLength), displayName));

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }
    
    /**
     *  @since 2.2
     * 
     **/
    
    public String getScriptPath()
    {
        return _scriptPath;
    }

    /**
     *  Allows a developer to use the existing validation logic with a different client-side
     *  script.  This is often sufficient to allow application-specific error presentation
     *  (perhaps by using DHTML to update the content of a &lt;span&gt; tag, or to use
     *  a more sophisticated pop-up window than <code>window.alert()</code>).
     * 
     *  @since 2.2
     * 
     **/
    
    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }    

}