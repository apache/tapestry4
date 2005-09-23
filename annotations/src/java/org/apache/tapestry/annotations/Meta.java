package org.apache.tapestry.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows meta-data information about the page or component to be specified.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Target(
{ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Meta {
    /**
     * Meta-data is specified as a series of key value pairs; each string is such a pair, with an
     * equals sign seperating the two.
     */
    String[] value();
}
