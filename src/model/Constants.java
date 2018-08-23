package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class Constants {
	public static final int MAX_TEXT_LEN = 512;
	public static int NULL_USER_ID = -1;
	public static int NULL_IMAGE_ID = -1;
	public static HashSet<String> ISSUE_STATUS = new HashSet<String>(Arrays.asList(
			new String[] {"open", "solved", "in_progress"}
	));
}
