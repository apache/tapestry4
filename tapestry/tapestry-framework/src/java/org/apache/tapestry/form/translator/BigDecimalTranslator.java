package org.apache.tapestry.form.translator;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Translator for {@link java.math.BigDecimal} objects. 
 */
public class BigDecimalTranslator extends AbstractTranslator {

    public BigDecimalTranslator()
    {
    }

    // Needed until HIVEMIND-134 fix is available
    public BigDecimalTranslator(String initializer)
    {
        super(initializer);
    }

    protected String formatObject(IFormComponent field, Locale locale, Object object)
    {
        if (!BigDecimal.class.isInstance(object))
            throw new ApplicationRuntimeException("BigDecimalTranslator translates values of type BigDecimal, not: " + object.getClass());
        
        return object.toString();
    }

    protected Object parseText(IFormComponent field, ValidationMessages messages, String text)
            throws ValidatorException
    {
        try {
            
            return new BigDecimal(text);
        }
        catch (NumberFormatException e) {
            throw new ValidatorException(buildMessage(messages, field, ValidationStrings.INVALID_NUMBER), ValidationConstraint.NUMBER_FORMAT);
        }
    }
}
