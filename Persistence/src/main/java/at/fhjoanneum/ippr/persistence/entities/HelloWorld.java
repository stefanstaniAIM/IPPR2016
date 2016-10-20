package at.fhjoanneum.ippr.persistence.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloWorld {

	private static final Logger LOG = LogManager.getLogger(HelloWorld.class);

	public static void main(final String[] args) {
		LOG.debug("test");
	}

	public static String get() {
		LOG.debug("return 'test'");
		return "test";
	}
}
