package org.apache.tapestry.services;

/**
 * Used to search for a class within a list of packages. Available as service tapestry.ClassFinder.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public interface ClassFinder
{
    /**
     * Searches for a class within a list of packages, which always includes the default package.
     * 
     * @param packageList
     *            a comma seperated list of package named (i.e., "java.lang,java.util")
     * @param className
     *            the name of the class to search for. This may be just a class name, or even a
     *            partial class name (i.e., "impl.Foo").
     * @return the class, if found, or null if no class could be found in any of the packages
     */

    public Class findClass(String packageList, String className);
}
