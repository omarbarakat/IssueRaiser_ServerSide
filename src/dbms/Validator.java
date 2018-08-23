package dbms;

import org.apache.commons.validator.routines.EmailValidator;

public class Validator {
	public static boolean isValidUserName(String input) {
		String unameRegex = "^\\w+(?:[ _-]\\w+)*$";
		return input!=null && input!="" && input.matches(unameRegex);
	}
	
	public static boolean isValidEmail(String input) {
		return EmailValidator.getInstance().isValid(input);
	}
	
	public static boolean isValidPhone(String input) {
		String phoneRegex = "^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$";
		return input!=null && input!="" && input.matches(phoneRegex);
	}
	
	public static boolean isValidIssueText(String input) {
		return (input.length() < 512);
	}
	
}
