package at.fhjoanneum.ippr.persistence.builder;

import org.apache.commons.lang3.StringUtils;

public class BuilderUtils {

	private BuilderUtils() {

	}

	public static void isNotBlank(final String value) {
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Value must be not null");
		}
	}

	public static void isNotBlank(final String value, final String fieldName) {
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(String.format("Value of '%s' must be not null", fieldName));
		}
	}
}
