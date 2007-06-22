package org.apache.tapestry.internal.pageload;

import java.io.Serializable;
import java.util.Locale;

/**
 * Alterantive implementation of {@link org.apache.tapestry.util.MultiKey} that is specifically
 * intended to be used to store and retrieve pages from a pool.
 *
 */
public class PageKey implements Serializable {

    String _pageName;
    Locale _locale;

    /**
     * Constructs a new instance for the specified page / locale.
     * @param pageName
     *          The page.
     * @param locale
     *          Locale of the page.
     */
    public PageKey(String pageName, Locale locale)
    {
        _pageName = pageName;
        _locale = locale;
    }

    public String getPageName()
    {
        return _pageName;
    }

    public String toString()
    {
        return "PageKey[" +
               "_pageName='" + _pageName + '\'' +
               '\n' +
               ", _locale=" + _locale +
               '\n' +
               ']';
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageKey pageKey = (PageKey) o;

        if (_locale != null ? !_locale.equals(pageKey._locale) : pageKey._locale != null) return false;
        if (_pageName != null ? !_pageName.equals(pageKey._pageName) : pageKey._pageName != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (_pageName != null ? _pageName.hashCode() : 0);
        result = 31 * result + (_locale != null ? _locale.hashCode() : 0);
        return result;
    }
}
