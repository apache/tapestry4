package org.apache.tapestry.enhance;

/**
 *
 */
public class BasicObject {

    User _user;

    public String getName()
    {
        return "Henry the eith I am.";
    }

    public User getUser()
    {
        return _user;
    }

    public void setUser(User user)
    {
        _user = user;
    }
}
