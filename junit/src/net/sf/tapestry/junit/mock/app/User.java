package net.sf.tapestry.junit.mock.app;

/**
 *  Contains information about a user.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class User
{
    private String _firstName;
    private String _lastName;
    private boolean _male = true;
    private AgeRange _ageRange = AgeRange.ADULT;
    
    public AgeRange getAgeRange()
    {
        return _ageRange;
    }

    public String getFirstName()
    {
        return _firstName;
    }

    public String getLastName()
    {
        return _lastName;
    }

    public boolean isMale()
    {
        return _male;
    }

    public void setAgeRange(AgeRange ageRange)
    {
        _ageRange = ageRange;
    }

    public void setFirstName(String firstName)
    {
        _firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        _lastName = lastName;
    }

    public void setMale(boolean male)
    {
        _male = male;
    }

}
