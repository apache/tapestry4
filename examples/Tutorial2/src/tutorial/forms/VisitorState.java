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
