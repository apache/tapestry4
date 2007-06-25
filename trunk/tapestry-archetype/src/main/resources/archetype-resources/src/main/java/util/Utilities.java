package ${packageName}.util;

import java.io.Serializable;
import java.text.MessageFormat;


import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Useful object designed to help do things common to application development that might be useful.
 * <p/>
 * In this case: a standard mechanism to log. You might insert other helper methods here. If you can't reuse someone else's code,
 * it's at least worth factoring it out, right?
 */
public class Utilities implements Serializable {
    public void log(Throwable th) {
        log(ExceptionUtils.getFullStackTrace(th));
    }

    public void log(String msg, Object... params) {
        System.out.println(MessageFormat.format(msg, params));
    }
}