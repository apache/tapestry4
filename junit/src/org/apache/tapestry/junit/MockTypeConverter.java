package org.apache.tapestry.junit;

import ognl.DefaultTypeConverter;

import java.util.Map;
import java.util.Date;
import java.sql.Timestamp;

public class MockTypeConverter extends DefaultTypeConverter {
    public Object convertValue(Map context, Object value, Class toType) {
        Object result = null;

        if ((toType == Timestamp.class) && (value instanceof Date)) {
            return new Timestamp(((Date) value).getTime());
        } else {
            result = super.convertValue(context, value, toType);
        }
        return result;
    }
}
