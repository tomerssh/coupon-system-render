package app.core.controllers;

/**
 * Abstract - All controllers inherit from this.
 */
public abstract class ClientController {

	/**
	 * Use the corresponding service login method
	 */
	public abstract boolean login(String email, String password);

}
