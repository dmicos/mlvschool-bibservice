package application.model;

public class ModelRules {

	/**
	 * Only one way to compute the user login in the client. By changing this,
	 * we change all Business rules of the client.
	 * 
	 * @param lastName
	 * @param cardID
	 * @return the computed logging.
	 */
	public static String computeUserLogging(String lastName, int cardID) {
		return lastName.toLowerCase() + '.' + cardID;
	}
}
