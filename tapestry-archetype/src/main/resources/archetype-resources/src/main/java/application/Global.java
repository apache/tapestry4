package ${packageName}.application;

import java.io.Serializable;

import ${packageName}.util.Utilities;

/**
 * <p>
 * This ASO will have application scope (see <a
 * href="http://tapestry.apache.org/tapestry4.1/usersguide/state.html">http://tapestry.apache.org/tapestry4.1/usersguide/state.html</a>)
 * and will be created upon first access. Information stored here will be available to all users.
 * </p>
 * <p>
 * You should not store user-specific information here because it will be available to all users.
 * You could however store some utilities object here. To store something, simply add an
 * accessor/mutator.
 * </p>
 */
public class Global implements Serializable
{

    private static final long serialVersionUID = 4566826739855338869L;

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
