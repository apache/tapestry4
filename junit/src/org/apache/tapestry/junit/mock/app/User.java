//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.junit.mock.app;

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
