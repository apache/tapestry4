//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package tutorial.forms;

import java.io.Serializable;

/**
 * Used to capture the state of each user within the web application
 * @author Neil Clayton
 */
public class VisitorState implements Serializable {
	/**
	 * Returns the dateOfBirth.
	 * @return String
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Returns the favoriteColour.
	 * @return String
	 */
	public String getFavoriteColour() {
		return favoriteColour;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the dateOfBirth.
	 * @param dateOfBirth The dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Sets the favoriteColour.
	 * @param favoriteColour The favoriteColour to set
	 */
	public void setFavoriteColour(String favoriteColour) {
		this.favoriteColour = favoriteColour;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setUserName(String name) {
		this.userName = name;
	}

	private String userName;
	private String dateOfBirth;
	private String favoriteColour;
}
