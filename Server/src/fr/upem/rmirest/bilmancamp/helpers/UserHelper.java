package fr.upem.rmirest.bilmancamp.helpers;

import fr.upem.rmirest.bilmancamp.interfaces.User;

public class UserHelper {

	/**
	 * Compute the given user information to generate an ID
	 * 
	 * @param fname
	 * @param lname
	 * @param cardNumber
	 * @return
	 */
	public static String computeId(String fname, String lname, int cardNumber) {
		return fname.substring(0, 1).toLowerCase() + lname.toLowerCase() + cardNumber;
	}

	/**
	 * Compute Id of the given {@link User}
	 * 
	 * @param user
	 *            The user to compute iD
	 * @return the computed ID
	 */
	public static String computeId(User user) {
		return computeId(user.getFirstName(), user.getLastName(), user.getCardNumber());
	}

	/**
	 * Generate a default password. it is a debug method
	 * 
	 * @return The generated password
	 */
	public static String generatePassword() {
		return Long.toHexString(Double.doubleToLongBits(Math.random()));
	}
}
