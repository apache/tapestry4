package ${packageName}.application;
import java.io.Serializable;

import java.io.Serializable;
import org.springframework.beans.factory.InitializingBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import ${packageName}.util.Utilities;

  /**
     * You can "store" anything here you would
     * store in the session, and
     * only for things you'd persist
     * for the whole application, per user.
     *
     * A good example: the working credentials of one user logged into a system.
     *
     * To Store something, simply add an accessor/mutator.
     *
     * <code>
     *
     * private User user;
     * public User getUser(){ return this.user; }
     * public void setUser (User usr ){ this.user=usr;}
     *
     * </code>
     *
    */
public class Global implements Serializable
{
     private Utilities utilities ;

    public Utilities getUtilities() {
        return utilities;
    }

    public void setUtilities(Utilities utilities) {
        this.utilities = utilities;
    }




}
