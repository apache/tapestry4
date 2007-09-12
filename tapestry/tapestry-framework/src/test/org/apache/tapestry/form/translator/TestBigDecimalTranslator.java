package org.apache.tapestry.form.translator;

import org.apache.tapestry.form.FormComponentContributorTestCase;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Tests {@link BigDecimalTranslator}.
 */
@Test
public class TestBigDecimalTranslator extends FormComponentContributorTestCase {

    public void test_Default_Format()
    {
        BigDecimalTranslator translator = new BigDecimalTranslator();

        testFormat(translator, new BigDecimal("14.1"), "14.1");
    }

    public void test_Null_Format()
    {
        BigDecimalTranslator translator = new BigDecimalTranslator();

        testFormat(translator, null, "");
    }

    public void test_Parse_Null()
            throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMock(ValidationMessages.class);
        BigDecimalTranslator translator = new BigDecimalTranslator();

        replay();

        assert translator.parse(field, messages, null) == null;

        verify();
    }

    @Test(expectedExceptions = ValidatorException.class)
    public void test_Parse_Invalid()
            throws Exception
    {
        IFormComponent field = newField("fred");
        ValidationMessages messages = newValidationMessages(Locale.getDefault());
        BigDecimalTranslator translator = new BigDecimalTranslator();

        expect(messages.formatValidationMessage((String)eq(null), eq(ValidationStrings.INVALID_NUMBER), aryEq(new String[] { "fred"}))).andReturn("msg");

        replay();

        translator.parse(field, messages, "a23");

        verify();
    }

    public void test_Parse_Simple()
            throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMock(ValidationMessages.class);
        BigDecimalTranslator translator = new BigDecimalTranslator();

        replay();

        BigDecimal value = (BigDecimal) translator.parse(field, messages, "99999999.9999");

        verify();

        assertEquals(value, new BigDecimal("99999999.9999"));
    }

    private void testFormat(Translator translator, BigDecimal number, String expected)
    {
        IFormComponent field = newField();

        String result = translator.format(field, Locale.ENGLISH, number);

        assertEquals(expected, result);
    }
}
