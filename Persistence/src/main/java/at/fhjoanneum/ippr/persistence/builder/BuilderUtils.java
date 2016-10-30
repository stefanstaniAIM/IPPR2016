package at.fhjoanneum.ippr.persistence.builder;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class BuilderUtils {
	public static void isNotBlank(final String value) {
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Value of string must be not null");
		}
	}

	public static <T> void isNotNull(final T value) {
		if (Objects.isNull(value)) {
			throw new IllegalArgumentException("Value must be not null");
		}
	}
}
