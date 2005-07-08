package org.apache.tapestry.services.impl;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.services.ClassFinder;

/**
 * Implemenation of service tapestry.ClassFinder.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ClassFinderImpl implements ClassFinder
{
    private ClassResolver _classResolver;

    public Class findClass(String packageList, String className)
    {
        String[] packages = TapestryUtils.split(packageList);

        for (int i = 0; i < packages.length; i++)
        {
            String fullName = packages[i].trim() + "." + className;

            Class result = _classResolver.checkForClass(fullName);

            if (result != null)
                return result;
        }

        return _classResolver.checkForClass(className);
    }

    public void setClassResolver(ClassResolver classResolver)
    {
        _classResolver = classResolver;
    }

}
