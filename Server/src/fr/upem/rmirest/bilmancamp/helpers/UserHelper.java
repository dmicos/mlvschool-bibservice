package fr.upem.rmirest.bilmancamp.helpers;

import fr.upem.rmirest.bilmancamp.models.UserPOJO;

public class UserHelper {

	/**
	 * Compute the given user information to generate an ID
	 * 
	 * @param fname
	 * @param lname
	 * @param cardNumber
	 * @return
	 */
	public static String computeId(UserPOJO user) {
		return user.getLastName() + user.getCardNumber();
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
