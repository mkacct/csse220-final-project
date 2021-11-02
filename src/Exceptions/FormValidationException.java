package Exceptions;

/**
 * Thrown in form validation to indicate the validation failed
 * message is a human readable reason why it failed
 * @author R_002
 */
public class FormValidationException extends Exception {
	private String message;

	public FormValidationException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {return this.message;}
}