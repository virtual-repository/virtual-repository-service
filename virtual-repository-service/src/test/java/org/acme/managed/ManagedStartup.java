package org.acme.managed;

import org.acme.utils.TestUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ManagedStartup {

	@Deployment(testable = false)
	static WebArchive war() {
		return TestUtils.warWithTests();
	}

	@Test
	public void startsUpInTomcatWithNoErros() {
	}

}
