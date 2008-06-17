package ${packageName}.application;

import java.io.Serializable;

import ${packageName}.util.Utilities;

/**
 * <p>
 * This ASO will have session scope (see <a
 * href="http://tapestry.apache.org/tapestry4.1/usersguide/state.html">http://tapestry.apache.org/tapestry4.1/usersguide/state.html</a>)
 * and will be created upon first access (thus creating a new session if one does not already
 * exist).
 * </p>
 * <p>
 * For example one could store a user's credentials here. To store something, simply add an
 * accessor/mutator.
 * </p>
 * <code>
 *
 * private User user;
 * public User getUser(){ return this.user; }
 * public void setUser (User usr ){ this.user=usr;}
 *
 * </code>
 */
public class Visit implements Serializable
{

    private static final long serialVersionUID = 5024704945209999149L;

    private Utilities utilities;

    public Utilities getUtilities()
    {
        return utilities;
    }

    public void setUtilities(Utilities utilities)
    {
        this.utilities = utilities;
    }

}
