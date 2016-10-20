package at.fhjoanneum.ippr.persistence.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HelloWorldTest {

	@Test
	public void test() {
		assertThat(HelloWorld.get(), is("test"));
	}
}
