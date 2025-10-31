package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentLoader {
	
	private static Dotenv dotenv = Dotenv.configure()
			.ignoreIfMissing()
			.load();
	
	public static String getEnv(String key) {
		String value = System.getenv(key);
		if (value == null) {
			value = dotenv.get(key);
		}
		return value;
	}

}
