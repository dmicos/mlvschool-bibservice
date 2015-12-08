package fr.upem.rmirest.bilmancamp.helpers;

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
	 * Generate a default password. it is a debug method
	 * 
	 * @return The generated password
	 */
	public static String generatePassword() {
		return Long.toHexString(Double.doubleToLongBits(Math.random()));
	}
}
