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

package tutorial.forms;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Used to capture the state of each user within the web application
 * @author Neil Clayton
 */
public class VisitorState implements Serializable
{
	/**
	 * Returns the dateOfBirth.
	 * @return String
	 */
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	public String getDateOfBirthAsString()
	{
		return DateFormat.getDateInstance().format(dateOfBirth);
	}

	/**
	 * Returns the favoriteColour.
	 * @return String
	 */
	public String getFavoriteColour()
	{
		return favoriteColour;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * Sets the dateOfBirth.
	 * @param dateOfBirth The dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Sets the favoriteColour.
	 * @param favoriteColour The favoriteColour to set
	 */
	public void setFavoriteColour(String favoriteColour)
	{
		this.favoriteColour = favoriteColour;
	}

	public void setDateOfBirthAsString(String newDOB) throws ParseException
	{
		dateOfBirth = DateFormat.getDateInstance().parse(newDOB);
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setUserName(String name)
	{
		this.userName = name;
	}

	private String userName;
	private Date dateOfBirth = new Date(0);
	private String favoriteColour;
}
