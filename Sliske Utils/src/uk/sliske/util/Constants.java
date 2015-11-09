package uk.sliske.util;

public class Constants {

	public static final String	USER_PATH;
	public static final String	BANNED_CHARS	= "<>:\"/\\|?*";

	static {
		USER_PATH = System.getProperty("user.home") + "\\sliske\\";
		
	}

}
